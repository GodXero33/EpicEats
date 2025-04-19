import { Component, ElementRef, ViewChild } from '@angular/core';
import { ApiService } from '../../../service/api.service';
import { Employee } from '../../../model/employee/employee.model';
import { EmployeeRole } from '../../../enum/employee-role.enum';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-terminate-employee',
  imports: [CommonModule, FormsModule],
  templateUrl: './terminate-employee.component.html',
  styleUrl: './terminate-employee.component.css'
})
export class TerminateEmployeeComponent {
public id: number = 1;
  public name: string = '';
  public phone: string = '';
  public email: string = '';
  public address: string = '';
  public salary: number = 0;
  public role: string = '';
  public dob: string = '';
  public minDateForDOB: string = '';
  public maxDateForDOB: string = '';
  public searchedEmployee: Employee | null = null;
  
  public idField!: ElementRef;
  @ViewChild('nameField') nameField!: ElementRef;
  @ViewChild('phoneField') phoneField!: ElementRef;
  @ViewChild('emailField') emailField!: ElementRef;
  @ViewChild('addressField') addressField!: ElementRef;
  @ViewChild('salaryField') salaryField!: ElementRef;
  @ViewChild('roleSelect') roleSelect!: ElementRef;
  @ViewChild('dobField') dobField!: ElementRef;

  @ViewChild('idField') set _idField (reference: ElementRef) {
    this.idField = reference;

    this.idField.nativeElement.focus();
  }

  constructor (private apiService: ApiService) {}

  public terminateEmployee (): void {
    if (!this.searchedEmployee || !confirm('Are you sure you want to terminate this employee?')) return;

    this.apiService.patch(`/employee/terminate/${this.searchedEmployee.id}`).subscribe({
      next: () => {
        this.clearInputs();

        this.searchedEmployee = null;
      },
      error: (error) => {
        console.error(error);
      }
    });
  }

  public onIdFieldKeydown (event: KeyboardEvent) {
    if (event.key === 'Enter' && this.id > 0) this.searchEmployee();
  }

  public searchEmployee (): void {
    if (!this.id) return;

    this.apiService.get(`/employee/${this.id}`).subscribe({
      next: (response) => {
        this.searchedEmployee = response as Employee;

        this.id = this.searchedEmployee.id;
        this.name = this.searchedEmployee.name;
        this.phone = this.searchedEmployee.phone;
        this.email = this.searchedEmployee.email;
        this.address = this.searchedEmployee.address;
        this.salary = this.searchedEmployee.salary;
        this.role = this.searchedEmployee.role;
        this.dob = this.searchedEmployee.dob;

        setTimeout(() => {
          this.nameField.nativeElement.focus();
        }, 100);
      },
      error: (error) => {
        console.error(error.message);
        this.clearInputs();

        this.searchedEmployee = null;
      }
    });
  }

  private clearInputs (): void {
    this.id = 1;
    this.name = '';
    this.phone = '';
    this.email = '';
    this.address = '';
    this.salary = 0;
    this.role = '';
    this.dob = '';
  }
}
