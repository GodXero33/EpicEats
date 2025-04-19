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
}
