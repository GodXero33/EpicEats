import { Component } from '@angular/core';
import { NavigationEnd, Router, RouterOutlet } from '@angular/router';

@Component({
  selector: 'app-employee',
  imports: [RouterOutlet],
  templateUrl: './employee.component.html',
  styleUrl: './employee.component.css'
})
export class EmployeeComponent {
  public currentRoute!: string;

  constructor (private router: Router) {
    this.router.events.subscribe(event => {
      if (event instanceof NavigationEnd) {
        if (event.url === '/employee') this.router.navigate(['/employee/add']);

        this.currentRoute = event.url.split('/')[2];
      }
    });
  }

  public sideNavBarNavigateTo (route: string): void {
    this.router.navigate([`/employee/${route}`]);
  }
}
