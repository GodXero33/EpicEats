import { CommonModule } from '@angular/common';
import { HttpClient, HttpErrorResponse, HttpHeaders } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { LoginUser } from '../../model/login-user.type';
import { Router } from '@angular/router';
import { ViewChild, ElementRef } from '@angular/core';
import { AuthService } from '../../service/auth.service';
import { AlertComponent } from "../alert/alert.component";

@Component({
  selector: 'app-login',
  imports: [CommonModule, FormsModule, AlertComponent],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent implements OnInit {
  public username: string = '';
  public password: string = '';
  @ViewChild('usernameInput') usernameField!: ElementRef;
  @ViewChild('passwordInput') passwordField!: ElementRef;
  public errorMessage: string = '';

  constructor (private authService: AuthService, private http: HttpClient, private router: Router) {}
  
  public ngOnInit (): void {
    if (this.authService.isAuthenticated()) {
      this.router.navigate(['/home']);
    }
  }

  private showErrorAlert (message: string): void {
    this.errorMessage = message;

    setTimeout(() => {
      this.errorMessage = '';
    }, 5000);
  }

  private validateInputs (): boolean {
    const usernameField = this.usernameField.nativeElement;
    const passwordField = this.passwordField.nativeElement;

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

  public login () {
    if (!this.validateInputs()) return;

    const loginUser: LoginUser = {
      username: this.username,
      password: this.password
    };
    const headers = new HttpHeaders({
      'Content-Type': 'application/json'
    });

    this.http.post<any>('http://localhost:8080/user/login', loginUser, { headers, observe: 'response' }).subscribe({
      next: (response) => {
        if (response.status === 200) {
          this.authService.setToken(response.body.data);
          this.router.navigate(['/home']);
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
          const usernameField = this.usernameField.nativeElement;
          const passwordField = this.passwordField.nativeElement;

          usernameField.setCustomValidity('Invalid username or password');
          passwordField.setCustomValidity('Invalid username or password');

          usernameField.reportValidity();
          passwordField.reportValidity();

          usernameField.focus();

          this.username = '';
          this.password = '';

          setTimeout(() => {
            usernameField.setCustomValidity('');
            passwordField.setCustomValidity('');
          }, 1000);
        }
      }
    });
  }

  public onKeydown (event: KeyboardEvent) {
    if (event.key !== 'Enter') return;

    if (event.target === this.usernameField.nativeElement) {
      this.passwordField.nativeElement.focus();
      return;
    }

    if (event.target === this.passwordField.nativeElement) {
      if (this.authService.isAuthenticated()) {
        this.router.navigate(['/home']);
      } else {
        this.login();
      }
    }
  }
}
