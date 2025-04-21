import { Component, ElementRef, QueryList, ViewChild, ViewChildren } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { LoginUser } from '../../model/security/login-user.model';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Router } from '@angular/router';
import { Employee } from '../../model/employee/employee.model';
import { AuthService } from '../../service/auth.service';
import { User } from '../../model/security/user.model';
import { UserRole } from '../../enum/user-role.enum';
import { AlertCommunicationService } from '../../service/alert-communication.service';

@Component({
  selector: 'app-signup',
  imports: [CommonModule, FormsModule],
  templateUrl: './signup.component.html',
  styleUrl: './signup.component.css'
})
export class SignupComponent {
  public adminUsername: string = '';
  public adminPassword: string = '';
  public employeeId!: number;
  public loadedEmployee: Employee | null = null;
  public signupUserUsername: string = '';
  public signupUserPassword: string = '';
  public signupUserConfirmPassword: string = '';
  public signupUserRole: string = 'EMPLOYEE';
  public signupOk: boolean = false;
  private signupStepContainers!: Array<HTMLElement>;
  private token: string = '';
  private currentSignupStepIndex: number = 0;
  private employeeConfirmBtn!: HTMLElement;

  @ViewChild('adminUsernameInput') adminUsernameField!: ElementRef;
  @ViewChild('adminPasswordInput') adminPasswordField!: ElementRef;
  @ViewChild('employeeIdInput') employeeIdInput!: ElementRef;
  @ViewChild('signupUserUsernameField') signupUserUsernameField!: ElementRef;
  @ViewChild('signupUserPasswordField') signupUserPasswordField!: ElementRef;
  @ViewChild('signupUserConfirmPasswordField') signupUserConfirmPasswordField!: ElementRef;
  @ViewChild('signupUserRoleSelect') signupUserRoleSelect!: ElementRef;
  
  @ViewChild('employeeConfirmBtn') set _employeeConfirmBtn (reference: ElementRef) {
    if (reference) {
      this.employeeConfirmBtn = reference.nativeElement;

      this.scrollEmployeeConfirmButtonIntoView();
    }
  }

  @ViewChildren('signupStep', { read: ElementRef }) set _signupStepContainers (query: QueryList<ElementRef>) {
    this.signupStepContainers = query.toArray().map(elementRef => elementRef.nativeElement);
  }

  constructor (private http: HttpClient, private router: Router, private authService: AuthService, private alertCommunicationService: AlertCommunicationService) {
    window.addEventListener('keydown', (event: KeyboardEvent) => {
      if (event.key === 'Tab')
        event.preventDefault();
    });
  }

  private validateAdminLoginInputs (): boolean {
    const usernameField = this.adminUsernameField.nativeElement;
    const passwordField = this.adminPasswordField.nativeElement;

    usernameField.setCustomValidity('');
    passwordField.setCustomValidity('');

    if (!usernameField.value.trim()) {
      usernameField.setCustomValidity('Username can\'t be empty');
      usernameField.reportValidity();
      return false;
    }

    if (!passwordField.value.trim()) {
      passwordField.setCustomValidity('Password can\'t be empty');
      passwordField.reportValidity();
      return false;
    }

    return true;
  }

  private scrollEmployeeConfirmButtonIntoView (): void {
    if (this.employeeConfirmBtn)
      this.employeeConfirmBtn.scrollIntoView({ behavior: 'smooth', block: "end" });
  }

  private showSignupStep (step: number): void {
    this.signupStepContainers[this.currentSignupStepIndex].classList.add('hide');
    this.signupStepContainers[step].classList.remove('hide');

    this.currentSignupStepIndex = step;
  }

  public signupStepBack (): void {
    if (this.currentSignupStepIndex == 0) {
      this.router.navigate(['/login']);
      return;
    }

    this.showSignupStep(this.currentSignupStepIndex - 1);
  }

  public firstSignupStepProceed (): void {
    this.showSignupStep(1);
    this.adminUsernameField.nativeElement.focus();
  }

  public onAdminLoginKeydown (event: KeyboardEvent): void {
    if (event.key !== 'Enter') return;

    if (event.target === this.adminUsernameField.nativeElement) {
      this.adminPasswordField.nativeElement.focus();
      return;
    }

    this.adminLogin();
  }

  private handleAdminLoginResponseOkData (data: any): void {
    if (!data.user) {
      this.alertCommunicationService.showError('Invalid server response');
      return;
    }

    if (data.user.role.toLowerCase() != 'admin') {
      this.adminUsernameField.nativeElement.focus();
      this.adminUsernameField.nativeElement.setCustomValidity('User found. But target user is not an admin');
      this.adminUsernameField.nativeElement.reportValidity();
      return;
    }

    this.token = data.token;
    this.showSignupStep(2);
    this.employeeIdInput.nativeElement.focus();
  }

  public adminLogin (): void {
    if (!this.validateAdminLoginInputs()) return;

    const loginAdmin = LoginUser.builder()
      .username(this.adminUsername)
      .password(this.adminPassword)
      .build();

    const headers = new HttpHeaders({
      'Content-Type': 'application/json'
    });

    this.http.post<any>('http://localhost:8080/user/login', loginAdmin, { headers, observe: 'response' }).subscribe({
      next: (response) => {
        if (response.status === 200) {
          this.handleAdminLoginResponseOkData(response.body.data);
        } else {
          this.alertCommunicationService.showError('An unexpected error occurred');
        }
      },
      error: (error) => {
        const errorMessage = error.status === 0 ?
          'Failed to connect server' :
          error.status === 401 ?
            'Invalid username or password' :
            'An unexpected error occurred';

        this.alertCommunicationService.showError(errorMessage);

        if (error.status === 401) {
          const usernameField = this.adminUsernameField.nativeElement;
          const passwordField = this.adminPasswordField.nativeElement;

          usernameField.setCustomValidity('Invalid username or password');
          passwordField.setCustomValidity('Invalid username or password');

          usernameField.reportValidity();
          passwordField.reportValidity();

          usernameField.focus();

          this.adminUsername = '';
          this.adminPassword = '';

          setTimeout(() => {
            usernameField.setCustomValidity('');
            passwordField.setCustomValidity('');
          }, 1000);
        }
      }
    });
  }

  public onEmployeeSelectKeydown (event: KeyboardEvent): void {
    if (event.key !== 'Enter') return;

    this.employeeSearch();
  }

  public employeeSearch (): void {
    if (!this.employeeId) {
      this.employeeIdInput.nativeElement.setCustomValidity('Employee id is reqiured');
      this.employeeIdInput.nativeElement.reportValidity();
      return;
    }

    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${this.token}`
    });

    this.http.get<any>(`http://localhost:8080/employee/${this.employeeId}`, { headers, observe: 'response' }).subscribe({
      next: (response) => {
        if (response.status === 200) {
          if (!response.body.data) {
            this.alertCommunicationService.showError('Invalid server response');
            return;
          }

          const employeeData = response.body.data;

          this.loadedEmployee = Employee.builder()
            .name(employeeData.name)
            .address(employeeData.address)
            .phone(employeeData.phone)
            .email(employeeData.email)
            .build();

          this.alertCommunicationService.showInfo('Employee found');
          this.scrollEmployeeConfirmButtonIntoView();
        } else {
          this.loadedEmployee = null;

          this.alertCommunicationService.showError('An unexpected error occurred');
        }
      },
      error: (error) => {
        this.loadedEmployee = null;

        const errorMessage: string = error.status === 0 ?
          'Failed to connect server' :
          error.status === 401 ?  
            'Failed to connect server. Try login again.' :
            'An unexpected error occurred';

        this.alertCommunicationService.showError(errorMessage);
      }
    });
  }

  public employeeConfirm (): void {
    if (!this.loadedEmployee) return;

    this.showSignupStep(3);
    this.signupUserUsernameField.nativeElement.focus();
  }

  public validateSignupUserInputs (): boolean {
    const usernameField = this.signupUserUsernameField.nativeElement;
    const passwordField = this.signupUserPasswordField.nativeElement;
    const confirmPasswordField = this.signupUserConfirmPasswordField.nativeElement;

    usernameField.setCustomValidity('');
    passwordField.setCustomValidity('');
    confirmPasswordField.setCustomValidity('');

    if (!usernameField.value.trim()) {
      usernameField.setCustomValidity('Username can\'t be empty');
      usernameField.reportValidity();
      return false;
    }

    if (!/^[A-Za-z0-9]+(_[A-Za-z0-9]+)*$/.test(usernameField.value)) {
      usernameField.setCustomValidity('Username must contain only letters, numbers, and underscores (no leading/trailing/multiple underscores)');
      usernameField.reportValidity();
      return false;
    }

    if (usernameField.value.length < 4 || usernameField.value.length > 15) {
      usernameField.setCustomValidity('Username must 4 characters length and can\'t longer that 15 characters');
      usernameField.reportValidity();
      return false;
    }

    if (!passwordField.value.trim()) {
      passwordField.setCustomValidity('Password can\'t be empty');
      passwordField.reportValidity();
      return false;
    }

    if (!confirmPasswordField.value.trim()) {
      confirmPasswordField.setCustomValidity('Confirm password can\'t be empty');
      confirmPasswordField.reportValidity();
      return false;
    }

    if (passwordField.value !== confirmPasswordField.value) {
      confirmPasswordField.setCustomValidity('Confirm password is not macth with password');
      confirmPasswordField.reportValidity();

      return false;
    }

    return true;
  }

  public onUserSignupKeyDown (event: KeyboardEvent): void {
    if (event.key !== 'Enter') return;

    if (event.target === this.signupUserUsernameField.nativeElement) {
      this.signupUserPasswordField.nativeElement.focus();
      return;
    }

    if (event.target === this.signupUserPasswordField.nativeElement) {
      this.signupUserConfirmPasswordField.nativeElement.focus();
      return;
    }

    if (event.target === this.signupUserConfirmPasswordField.nativeElement) {
      this.signupUserRoleSelect.nativeElement.focus();
      return;
    }

    this.newUserSignup();
  }

  public newUserSignup (): void {
    if (!this.validateSignupUserInputs()) return;

    const newUser = User.builder()
      .username(this.signupUserUsername)
      .password(this.signupUserPassword)
      .employeeId(this.employeeId)
      .role(this.signupUserRole === 'ADMIN' ? UserRole.ADMIN : UserRole.EMPLOYEE)
      .build();

    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${this.token}`
    }); 

    this.http.post<any>(`http://localhost:8080/user/register`, newUser, { headers, observe: 'response' }).subscribe({
      next: (response) => {
        if (response.status === 200) {
          if (!response.body.data || !response.body.data.username) {
            this.alertCommunicationService.showError('Invalid server response');
            return;
          }

          this.signupOk = true;

          this.showSignupStep(4);
        } else {
          this.loadedEmployee = null;

          this.alertCommunicationService.showError('An unexpected error occurred');
        }
      },
      error: (error) => {
        this.loadedEmployee = null;

        this.alertCommunicationService.showError(error.error.message);
      }
    });
  }

  public backToLogin (): void {
    this.authService.logout();
  }
}
