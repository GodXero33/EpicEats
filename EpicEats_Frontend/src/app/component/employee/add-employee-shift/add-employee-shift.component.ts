import { Component, ElementRef, ViewChild } from '@angular/core';
import { ApiService } from '../../../service/api.service';
import { Employee } from '../../../model/employee/employee.model';
import { EmployeeRole } from '../../../enum/employee-role.enum';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-add-employee-shift',
  imports: [FormsModule, CommonModule],
  templateUrl: './add-employee-shift.component.html',
  styleUrl: './add-employee-shift.component.css'
})
export class AddEmployeeShiftComponent {
  public id: number = 1;
  public name: string = '';
  public phone: string = '';
  public email: string = '';
  public address: string = '';
  public salary: number = 0;
  public role: string = 'CASHIER';
  public dob: string = '';

  private isInvertSorted: boolean = false;
  private filterStringValue: string | null = null;
  public loadedEmployees: Array<Employee> = [];
  public viewableEmployees: Array<Employee> = [];

  public nameField!: ElementRef;
  @ViewChild('phoneField') phoneField!: ElementRef;
  @ViewChild('emailField') emailField!: ElementRef;
  @ViewChild('addressField') addressField!: ElementRef;
  @ViewChild('salaryField') salaryField!: ElementRef;
  @ViewChild('roleSelect') roleSelect!: ElementRef;
  @ViewChild('dobField') dobField!: ElementRef;

  @ViewChild('nameField') set _nameField (reference: ElementRef) {
    this.nameField = reference;

    this.nameField.nativeElement.focus();
  }

  constructor (private apiService: ApiService) {}

  public searchEmployee (): void {
    this.loadAllEmployees();
  }

  public searchedEmployeeListBackBtnOnClick (): void {
    this.loadedEmployees.length = 0;
  }

  private clearInputs (): void {
    this.name = '';
    this.phone = '';
    this.email = '';
    this.address = '';
    this.salary = 0;
    this.role = 'CASHIER';
    this.dob = '';
  }

  public onKeydown (event: KeyboardEvent): void {
    if (event.key !== 'Enter') return;

    const target = event.target;

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

  public filterAllEmployees (): void {
    if (this.filterStringValue == null || this.filterStringValue.length == 0) {
      this.viewableEmployees = this.loadedEmployees.filter(employee => employee);
      return;
    }

    const value: string = this.filterStringValue.trim().toLowerCase();

    const filterEmployees = (employee: Employee): Employee | undefined => {
      if (
        employee.role.toString().toLowerCase().includes(value) ||
        employee.name.toString().toLowerCase().includes(value) ||
        employee.phone.toString().toLowerCase().includes(value) ||
        employee.email.toString().toLowerCase().includes(value)
      ) return employee;

      return undefined;
    }

    this.viewableEmployees = this.loadedEmployees.filter(filterEmployees);
  }

  private sortEmployees (employeeComparator: (a: Employee, b: Employee) => number): void {
    this.isInvertSorted = !this.isInvertSorted;
    this.loadedEmployees = this.loadedEmployees.sort(employeeComparator);

    this.filterAllEmployees();
  }

  public sortById (): void {
    this.sortEmployees(this.isInvertSorted ?
      (a: Employee, b: Employee) => a.id - b.id :
      (a: Employee, b: Employee) => b.id - a.id);
  }

  public sortByName (): void {
    this.sortEmployees(this.isInvertSorted ?
      (a: Employee, b: Employee) => a.name.localeCompare(b.name) :
      (a: Employee, b: Employee) => b.name.localeCompare(a.name));
  }

  public sortBySalary (): void {
    this.sortEmployees(this.isInvertSorted ?
      (a: Employee, b: Employee) => a.salary - b.salary :
      (a: Employee, b: Employee) => b.salary - a.salary);
  }

  public sortByRole (): void {
    this.sortEmployees(this.isInvertSorted ?
      (a: Employee, b: Employee) => a.role.localeCompare(b.role) :
      (a: Employee, b: Employee) => b.role.localeCompare(a.role));
  }

  public sortByDOB (): void {
    this.sortEmployees(this.isInvertSorted ?
      (a: Employee, b: Employee) => new Date(a.dob).getTime() - new Date(b.dob).getTime() :
      (a: Employee, b: Employee) => new Date(b.dob).getTime() - new Date(a.dob).getTime());
  }

  public loadAllEmployees (): void {
    const filterEmployee = Employee.builder()
      .email(this.email)
      .dob(this.dob)
      .id(this.id)
      .phone(this.phone)
      .build();

    this.apiService.post('/employee/filter', filterEmployee).subscribe({
      next: (response) => {
        if (!Array.isArray(response)) return;

        this.loadedEmployees.length = 0;

        response.forEach((employeeData: Employee) => this.loadedEmployees.push(Employee.builder()
          .id(employeeData.id)
          .name(employeeData.name)
          .phone(employeeData.phone)
          .email(employeeData.email)
          .address(employeeData.address)
          .salary(employeeData.salary)
          .role(employeeData.role)
          .dob(employeeData.dob)
          .build()
        ));

        this.filterAllEmployees();
      },
      error: (error) => {
        console.error(error.message);
      }
    });
  }

  public getFormatterRole (role: EmployeeRole): string {
    const value = role.toString().toLowerCase();

    return value[0].toUpperCase() + value.substring(1);
  }
}
