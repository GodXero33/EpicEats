import { CommonModule } from '@angular/common';
import { Component, ElementRef, ViewChild } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Employee } from '../../../model/employee/employee.model';
import { EmployeeRole } from '../../../enum/employee-role.enum';
import { HttpClient, HttpErrorResponse, HttpHeaders, HttpResponse } from '@angular/common/http';
import { AuthService } from '../../../service/auth.service';

@Component({
  selector: 'app-add-employee',
  imports: [CommonModule, FormsModule],
  templateUrl: './add-employee.component.html',
  styleUrl: './add-employee.component.css'
})
export class AddEmployeeComponent {
  public name: string = 'shan';
  public phone: string = '0770110488';
  public email: string = 'shansathish33@gmail.com';
  public address: string = '';
  public salary: number = 0;
  public role: string = 'CASHIER';
  public dob: string = '';
  public minDateForDOB: string = '';
  public maxDateForDOB: string = '';

  @ViewChild('nameField') nameField!: ElementRef;
  @ViewChild('phoneField') phoneField!: ElementRef;
  @ViewChild('emailField') emailField!: ElementRef;
  @ViewChild('addressField') addressField!: ElementRef;
  @ViewChild('salaryField') salaryField!: ElementRef;
  @ViewChild('roleSelect') roleSelect!: ElementRef;
  @ViewChild('dobField') dobField!: ElementRef;

  constructor (private http: HttpClient, private authService: AuthService) {
    const today: Date = new Date();

    const subtractYears = (date: Date, years: number): Date => {
      const d = new Date(date);
      d.setFullYear(d.getFullYear() - years);
      return d;
    };

    const formatDate = (d: Date): string => `${d.getFullYear()}-${(d.getMonth() + 1).toString().padStart(2, '0')}-${d.getDate().toString().padStart(2, '0')}`;

    const minDOB = subtractYears(today, 55);
    const maxDOB = subtractYears(today, 18);

    this.dob = formatDate(maxDOB);
    this.minDateForDOB = formatDate(minDOB);
    this.maxDateForDOB = formatDate(maxDOB);
  }

  private validateInputs (): boolean {
    if (!/^[A-Za-z0-9]+(_[A-Za-z0-9]+)*$/.test(this.name)) {
      this.nameField.nativeElement.setCustomValidity('Employee name must contain only letters, numbers, and underscores (no leading/trailing/multiple underscores)');
      this.nameField.nativeElement.reportValidity();
      return false;
    }

    if (!/^0?7\d{8}$/.test(this.phone)) {
      this.phoneField.nativeElement.setCustomValidity('Phone must match 07xxxxxxxx or 7xxxxxxxx');
      this.phoneField.nativeElement.reportValidity();
      return false;
    }

    if (!/^[\w.-]+@[a-zA-Z\d.-]+\.[a-zA-Z]{2,}$/.test(this.email)) {
      this.emailField.nativeElement.setCustomValidity('Invalid email format');
      this.emailField.nativeElement.reportValidity();
      return false;
    }

    return true;
  }

  public addEmployee (): void {
    if (!this.validateInputs()) return;

    if (this.phone.length == 9) this.phone = '0' + this.phone;

    const newEmployee: Employee = Employee.builder()
      .name(this.name)
      .phone(this.phone)
      .email(this.email)
      .address(this.address)
      .salary(this.salary)
      .role(EmployeeRole[this.role as keyof typeof EmployeeRole])
      .dob(this.dob)
      .build();

      const headers = new HttpHeaders({
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${this.authService.getToken()}`
      });

    this.http.post<any>('http://localhost:8080/employee/', newEmployee, { headers }).subscribe({
      next: (response: HttpResponse<any>) => {
        console.log(response.body);
      },
      error: (error: HttpErrorResponse) => {
        console.log(error.error.message);
      }
    });
  }
}
