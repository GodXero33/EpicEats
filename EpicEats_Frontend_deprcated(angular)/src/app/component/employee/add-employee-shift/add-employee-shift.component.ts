import { Component, ElementRef, ViewChild } from '@angular/core';
import { ApiService } from '../../../service/api.service';
import { Employee } from '../../../model/employee/employee.model';
import { EmployeeRole } from '../../../enum/employee-role.enum';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { EmployeeShift } from '../../../model/employee/employee.shift.model';

@Component({
  selector: 'app-add-employee-shift',
  imports: [FormsModule, CommonModule],
  templateUrl: './add-employee-shift.component.html',
  styleUrl: './add-employee-shift.component.css'
})
export class AddEmployeeShiftComponent {
  public filterEmployee!: Employee;
  private isInvertSorted: boolean = false;
  public loadedEmployees: Array<Employee> = [];
  public loadedEmployeeShifts: Array<EmployeeShift> = [];
  public selectedEmployee: Employee | null = null;
  public isEmployeesLoaded: boolean = false;

  public searchEmployeeIdField!: ElementRef;

  @ViewChild('searchEmployeeEmailField') searchEmployeeEmailField!: ElementRef;
  @ViewChild('searchEmployeeDobField') searchEmployeeDobField!: ElementRef;
  @ViewChild('searchEmployeePhoneField') searchEmployeePhoneField!: ElementRef;

  @ViewChild('searchEmployeeIdField') set _searchEmployeeIdField (reference: ElementRef) {
    this.searchEmployeeIdField = reference;

    this.searchEmployeeIdField.nativeElement.focus();
  }

  constructor (private apiService: ApiService) {
    this.clearInputs();
  }

  public searchEmployee (): void {
    this.loadAllEmployees();
  }

  public clearLoadedEmployeesList (): void {
    this.loadedEmployees.length = 0;
    this.isEmployeesLoaded = false;
  }

  private clearInputs (): void {
    this.filterEmployee = Employee.builder()
      .role('CASHIER')
      .salary(0)
      .build();
  }

  public onKeydown (event: KeyboardEvent): void {
    if (event.key !== 'Enter') return;

    const target = event.target;

    if (target == this.searchEmployeeIdField.nativeElement) {
      this.searchEmployeeEmailField.nativeElement.focus();
      return;
    }

    if (target == this.searchEmployeeEmailField.nativeElement) {
      this.searchEmployeeDobField.nativeElement.focus();
      return;
    }

    if (target == this.searchEmployeeDobField.nativeElement) {
      this.searchEmployeePhoneField.nativeElement.focus();
      return;
    }

    if (target == this.searchEmployeePhoneField.nativeElement) {
      this.searchEmployee();
    }
  }

  private sortEmployees (employeeComparator: (a: Employee, b: Employee) => number): void {
    this.isInvertSorted = !this.isInvertSorted;
    this.loadedEmployees = this.loadedEmployees.sort(employeeComparator);
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

  public sortByEmployeeSince (): void {
    this.sortEmployees(this.isInvertSorted ?
      (a: Employee, b: Employee) => new Date(a.employeeSince).getTime() - new Date(b.employeeSince).getTime() :
      (a: Employee, b: Employee) => new Date(b.employeeSince).getTime() - new Date(a.employeeSince).getTime());
  }

  public loadAllEmployees (): void {
    this.apiService.post('/employee/filter', this.filterEmployee).subscribe({
      next: (response) => {
        if (!Array.isArray(response)) return;

        this.isEmployeesLoaded = true;
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
          .employeeSince(employeeData.employeeSince)
          .build()
        ));
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

  private loadSelectedEmployeeShift (): void {
    if (!this.selectedEmployee) return;

    this.apiService.get(`/employee/shift/by-employee/${this.selectedEmployee.id}`).subscribe({
      next: (response: any) => {
        if (!Array.isArray(response.shifts)) return;

        this.loadedEmployeeShifts.length = 0;

        response.shifts.forEach((employeeShiftData: EmployeeShift) => this.loadedEmployeeShifts.push(EmployeeShift.builder()
          .id(employeeShiftData.id)
          .shiftDate(employeeShiftData.shiftDate)
          .startTime(employeeShiftData.startTime)
          .endTime(employeeShiftData.endTime)
          .build()
        ));

        this.loadedEmployeeShifts.sort((a, b) => {
          const dateCompare = b.shiftDate.localeCompare(a.shiftDate);

          return dateCompare !== 0 ? dateCompare : b.startTime.localeCompare(a.startTime);
        });
      },
      error: (error) => {
        console.error(error.message);
      }
    });
  }

  public onSearchedEmployeesTableClick (event: MouseEvent): void {
    const target = event.target as HTMLElement;
    const clickedRow = target.closest('tr');

    if (!clickedRow || clickedRow.parentElement?.tagName === 'THEAD') return;

    const tbody = clickedRow.closest('tbody');

    if (!tbody) return;

    const rows = Array.from(tbody.querySelectorAll('tr'));
    const index = rows.indexOf(clickedRow) - 1;

    if (index < 0) return;

    this.selectedEmployee = this.loadedEmployees[index];

    this.loadSelectedEmployeeShift();
  }

  public clearSelectedEmployee (): void {
    this.selectedEmployee = null;
  }
}
