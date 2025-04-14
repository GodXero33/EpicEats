import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, NavigationEnd, Router, RouterOutlet } from '@angular/router';
import { AuthService } from './service/auth.service';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet, CommonModule],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent {
  showNavbar: boolean = true;

  constructor (private authService: AuthService, private router: Router) {
    this.router.events.subscribe(event => {
      const navHidePaths = ['', '/login', '/signup'];

      if (event instanceof NavigationEnd) {
        this.showNavbar = navHidePaths.includes(event.url);

        if (this.router.url !== '/signup' && !this.authService.isAuthenticated())
          this.router.navigate(['/login']);
      }
    });
  }
}
