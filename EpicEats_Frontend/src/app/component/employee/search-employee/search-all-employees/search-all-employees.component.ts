import { Component } from '@angular/core';
import { Employee } from '../../../../model/employee/employee.model';
import { ApiService } from '../../../../service/api.service';

@Component({
  selector: 'app-search-all-employees',
  imports: [],
  templateUrl: './search-all-employees.component.html',
  styleUrl: './search-all-employees.component.css'
})
export class SearchAllEmployeesComponent {
  public allEmployeesLoadedEmployees: Array<Employee> = [];
  public allEmployeesViewableEmployees: Array<Employee> = [];

  constructor (private apiService: ApiService) {}

  public filterAllEmployees (): void {
    function filterEmployee (employee: Employee) {
      return employee;
    }

    this.allEmployeesViewableEmployees = this.allEmployeesLoadedEmployees.filter(filterEmployee);
  }

  public loadAllEmployees (): void {
    this.apiService.get('/employee/all').subscribe({
      next: (response) => {
        if (!Array.isArray(response)) return;

        this.allEmployeesLoadedEmployees.length = 0;

        response.forEach((employeeData: Employee) => this.allEmployeesLoadedEmployees.push(Employee.builder()
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

    const tbody: HTMLElement | null = targetTR.parentElement;
    
    if (!tbody) return;

    const allRows = Array.from(tbody.querySelectorAll('tr'));

    const index = allRows.indexOf(targetTR);

    if (index === -1 || index > this.allEmployeesViewableEmployees.length) return;

    console.log(this.allEmployeesViewableEmployees[index - 1]);
  }
}
