import { Component } from '@angular/core';
import { AlertComponent } from '../alert/alert.component';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-signup',
  imports: [CommonModule, FormsModule, AlertComponent],
  templateUrl: './signup.component.html',
  styleUrl: './signup.component.css'
})
export class SignupComponent {
  public username: string = '';
  public password: string = '';
  public errorMessage: string = '';

  public onKeydown (event: KeyboardEvent): void {}

  public login (): void {}
}
