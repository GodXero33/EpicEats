import { CommonModule } from '@angular/common';
import { HttpClient, HttpErrorResponse, HttpHeaders } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { LoginUser } from '../../model/login-user.type';
import { Router } from '@angular/router';
import { ViewChild, ElementRef } from '@angular/core';
import { AuthService } from '../../service/auth.service';

@Component({
  selector: 'app-login',
  imports: [CommonModule, FormsModule],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent implements OnInit {
  public username: string = '';
  public password: string = '';
  @ViewChild('usernameInput') usernameField!: ElementRef;
  @ViewChild('passwordInput') passwordField!: ElementRef;

  constructor (private authService: AuthService, private http: HttpClient, private router: Router) {}
  
  public ngOnInit (): void {
    if (this.authService.isAuthenticated()) {
      this.router.navigate(['/home']);
    }
  }

  public login () {
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
          console.log('Login successful!');
          sessionStorage.setItem('authToken', response.body.data);

          this.router.navigate(['/home']);
        } else {
          console.error('Unexpected status:', response.status);
        }
      },
      error: (error: HttpErrorResponse) => {
        if (error.status === 404) {
          console.error('Server not found (404)');
        } else if (error.status === 401) {
          console.error('Invalid username or password (401)');
        } else {
          console.error('An unexpected error occurred:', error.message);
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
