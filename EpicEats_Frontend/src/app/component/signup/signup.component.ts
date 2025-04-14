import { Component, ElementRef, ViewChild } from '@angular/core';
import { AlertComponent } from '../alert/alert.component';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { LoginUser } from '../../model/login-user.model';
import { HttpClient, HttpErrorResponse, HttpHeaders, HttpResponse } from '@angular/common/http';
import { Router } from '@angular/router';
import { Employee } from '../../model/employee.model';
import { AuthService } from '../../service/auth.service';

@Component({
  selector: 'app-signup',
  imports: [CommonModule, FormsModule, AlertComponent],
  templateUrl: './signup.component.html',
  styleUrl: './signup.component.css'
})
export class SignupComponent {
  public adminUsername: string = '';
  public adminPassword: string = '';
  public errorMessage: string = '';
  public employeeId!: number;
  public loadedEmployee: Employee | null = null;
  @ViewChild('adminUsernameInput') adminUsernameField!: ElementRef;
  @ViewChild('adminPasswordInput') adminPasswordField!: ElementRef;
  @ViewChild('employeeIdInput') employeeIdInput!: ElementRef;
  
  private token: string = '';

  constructor (private http: HttpClient, private router: Router, private authService: AuthService) {}

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

  private showErrorAlert (message: string): void {
    this.errorMessage = message;

    setTimeout(() => {
      this.errorMessage = '';
    }, 5000);
  }

  public onAdminLoginKeydown (event: KeyboardEvent): void {
    if (event.key !== 'Enter') return;

    if (event.target === this.adminUsernameField.nativeElement) {
      this.adminPasswordField.nativeElement.focus();
      return;
    }

    this.adminLogin();
  }

  private handleResponseOkData (data: any): void {
    console.log(data);
    if (!data.user) {
      this.showErrorAlert('Invalid server response');
      return;
    }

    if (data.user.role.toLowerCase() != 'admin') {
      this.adminUsernameField.nativeElement.focus();
      this.adminUsernameField.nativeElement.setCustomValidity('User found. But target user is not an admin');
      this.adminUsernameField.nativeElement.reportValidity();
      return;
    }

    // next menu
    console.log('next menu');
  }

  public adminLogin (): void {
    if (!this.validateAdminLoginInputs()) return;

    const loginAdmin: LoginUser = LoginUser.builder()
      .username(this.adminUsername)
      .password(this.adminPassword)
      .build();

    const headers = new HttpHeaders({
      'Content-Type': 'application/json'
    });

    this.http.post<any>('http://localhost:8080/user/login', loginAdmin, { headers, observe: 'response' }).subscribe({
      next: (response: HttpResponse<any>) => {
        if (response.status === 200) {
          this.handleResponseOkData(response.body.data);
        } else {
          this.showErrorAlert('An unexpected error occurred');
        }
      },
      error: (error: HttpErrorResponse) => {
        const errorMessage: string = error.status === 0 ?
          'Failed to connect server' :
          error.status === 401 ?
            'Invalid username or password' :
            'An unexpected error occurred';

        this.showErrorAlert(errorMessage);

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
      'Authorization': `Bearer ${this.authService.getToken()}`
    });

    this.http.get<any>(`http://localhost:8080/employee/${this.employeeId}`, { headers, observe: 'response' }).subscribe({
      next: (response: HttpResponse<any>) => {
        if (response.status === 200) {
          if (!response.body.data) {
            this.showErrorAlert('Invalid server response');
            return;
          }

          const employeeData: any = response.body.data;

          this.loadedEmployee = Employee.builder()
            .name(employeeData.name)
            .address(employeeData.address)
            .phone(employeeData.phone)
            .email(employeeData.email)
            .build();
        } else {
          this.loadedEmployee = null;

          this.showErrorAlert('An unexpected error occurred');
        }
      },
      error: (error: HttpErrorResponse) => {
        this.loadedEmployee = null;

        const errorMessage: string = error.status === 0 ?
          'Failed to connect server' :
          error.status === 401 ?  
            'Failed to connect server. Try login again.' :
            'An unexpected error occurred';

        this.showErrorAlert(errorMessage);
      }
    });
  }
}
