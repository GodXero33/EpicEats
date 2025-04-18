import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ApiService } from '../../../service/api.service';
import { Employee } from '../../../model/employee/employee.model';

@Component({
  selector: 'app-search-employee',
  imports: [CommonModule, FormsModule],
  templateUrl: './search-employee.component.html',
  styleUrl: './search-employee.component.css'
})
export class SearchEmployeeComponent {
  public searchByIdId: number = 1;
  public searchByIdName!: string;
  public searchByIdPhone!: string;
  public searchByIdEmail!: string;
  public searchByIdAddress!: string;
  public searchByIdSalary!: number | undefined;
  public searchByIdRole!: string;
  public searchByIdDob!: string;

  constructor (private apiService: ApiService) {}

  public searchByIdIdFieldOnKeydown (event: KeyboardEvent): void {
    if (event.key !== 'Enter' || !this.searchByIdId) return;

    this.searchByIdSearch();
  }

  public searchByIdSearch ():void {
    this.apiService.get(`/employee/${this.searchByIdId}`).subscribe({
      next: (response) => {
        const searchedEmployee = response as Employee;

        this.searchByIdId = searchedEmployee.id;
        this.searchByIdName = searchedEmployee.name;
        this.searchByIdPhone = searchedEmployee.phone;
        this.searchByIdEmail = searchedEmployee.email;
        this.searchByIdAddress = searchedEmployee.address;
        this.searchByIdSalary = searchedEmployee.salary;
        this.searchByIdRole = searchedEmployee.role;
        this.searchByIdDob = searchedEmployee.dob;
      },
      error: (error) => {
        this.clearSearchByIdDetails();
        console.error(error.message);
      }
    });
  }

  private clearSearchByIdDetails () {
    this.searchByIdId = 1;
    this.searchByIdName = '';
    this.searchByIdPhone = '';
    this.searchByIdEmail = '';
    this.searchByIdAddress = '';
    this.searchByIdSalary = undefined;
    this.searchByIdRole = '';
    this.searchByIdDob = '';
  }
}
