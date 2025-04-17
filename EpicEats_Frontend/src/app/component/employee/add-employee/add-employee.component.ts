import { CommonModule } from '@angular/common';
import { Component, ElementRef, ViewChild } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Employee } from '../../../model/employee/employee.model';
import { EmployeeRole } from '../../../enum/employee-role.enum';
import { ApiService } from '../../../service/api.service';

@Component({
  selector: 'app-add-employee',
  imports: [CommonModule, FormsModule],
  templateUrl: './add-employee.component.html',
  styleUrl: './add-employee.component.css'
})
export class AddEmployeeComponent {
  public name: string = '';
  public phone: string = '';
  public email: string = '';
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

  constructor (private apiService: ApiService) {
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

    this.apiService.post('/employee', newEmployee).subscribe({
      next: () => {
        this.clearInputs();
      },
      error: (error) => {
        console.error(error.message);
      }
    });
  }

  private clearInputs (): void {
    this.name = '';
    this.phone = '';
    this.email = '';
    this.address = '';
    this.salary = 0;
    this.role = 'CASHIER';
    this.dob = this.maxDateForDOB;
  }

  public onKeydown (event: KeyboardEvent): void {
    if (event.key !== 'Enter') return;

    const target: EventTarget | null = event.target;

    if (target == this.nameField.nativeElement) {
      this.phoneField.nativeElement.focus();
      return;
    }

    if (target == this.phoneField.nativeElement) {
      this.emailField.nativeElement.focus();
      return;
    }

    if (target == this.emailField.nativeElement) {
      this.addressField.nativeElement.focus();
      return;
    }

    if (target == this.addressField.nativeElement) {
      this.salaryField.nativeElement.focus();
    }
  }
}
