import { Component, ElementRef, ViewChild } from '@angular/core';
import { Router, RouterOutlet } from '@angular/router';

@Component({
  selector: 'app-search-employee',
  imports: [RouterOutlet],
  templateUrl: './search-employee.component.html',
  styleUrl: './search-employee.component.css'
})
export class SearchEmployeeComponent {
  public navigatedRoute: string = '';

  constructor (private router: Router) {}

  public navigateSection (path: string): void {
    if (path !== 'all' && path !== 'by-id') return;

    this.navigatedRoute = path;

    this.router.navigate([`employee/search/${path}`]);
  }
}
