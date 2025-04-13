import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { NavigationEnd, Router, RouterOutlet } from '@angular/router';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet, CommonModule],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent {
  showNavbar: boolean = true;

  constructor (private router: Router) {
    this.router.events.subscribe(event => {
      const navHidePaths = ['', '/login', '/signup'];

      if (event instanceof NavigationEnd) this.showNavbar = navHidePaths.includes(event.url);
    });
  }
}
