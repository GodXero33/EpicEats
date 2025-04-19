import { Component, ElementRef, ViewChild } from '@angular/core';
import { Employee } from '../../../../model/employee/employee.model';
import { ApiService } from '../../../../service/api.service';

@Component({
  selector: 'app-search-all-employees',
  imports: [],
  templateUrl: './search-all-employees.component.html',
  styleUrl: './search-all-employees.component.css'
})
export class SearchAllEmployeesComponent {
  public loadedEmployees: Array<Employee> = [];
  public viewableEmployees: Array<Employee> = [];
  public selectedEmployee: Employee | null = null;
  public selectedEmployeeIndex: number = -1;
  public isInvertSorted: boolean = false;

  @ViewChild('detailsToggleBtn') detailsToggleBtn!: ElementRef;

  constructor (private apiService: ApiService) {
    this.loadAllEmployees();
  }

  public filterAllEmployees (): void {
    function filterEmployee (employee: Employee) {
      return employee;
    }

    this.viewableEmployees = this.loadedEmployees.filter(filterEmployee);
  }

  public loadAllEmployees (): void {
    this.apiService.get('/employee/all').subscribe({
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

  public onAllEmployeesTableClick (event: MouseEvent): void {
    let targetTR: HTMLTableRowElement | null = null;

    if (event.target instanceof HTMLTableCellElement) targetTR = event.target.parentElement as HTMLTableRowElement;

    if (event.target instanceof HTMLTableRowElement) targetTR = event.target;

    if (!targetTR) return;

    if (targetTR.querySelectorAll('th').length) return;

    const tbody: HTMLElement | null = targetTR.parentElement;

    if (!tbody) return;

    const allRows = Array.from(tbody.querySelectorAll('tr'));

    const index = allRows.indexOf(targetTR);

    if (index === -1 || index > this.viewableEmployees.length) return;

    this.selectedEmployee = this.viewableEmployees[index - 1];
    this.selectedEmployeeIndex = index - 1;

    this.detailsToggleBtn.nativeElement.checked = true;
  }

  public terminateSelectedEmployee () {
    if (this.selectedEmployee == null || !confirm('Do you sure you want to terminate this employee?')) return;

    this.apiService.patch(`/employee/terminate/${this.selectedEmployee.id}`).subscribe({
      next: () => {
        this.viewableEmployees.splice(this.selectedEmployeeIndex, 1);
        this.selectedEmployee = null;
        this.detailsToggleBtn.nativeElement.checked = false;
      },
      error: (error) => {
        console.error(error);
      }
    });
  }

  private sortEmployees (employeeComparator: (a: Employee, b: Employee) => number): void {
    this.isInvertSorted = !this.isInvertSorted;
    this.viewableEmployees = this.loadedEmployees.sort(employeeComparator);
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
}
