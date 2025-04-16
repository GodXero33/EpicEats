import { CommonModule } from '@angular/common';
import { Component, ElementRef, OnInit, QueryList, ViewChild, ViewChildren } from '@angular/core';
import { ActivatedRoute, NavigationEnd, Router, RouterOutlet } from '@angular/router';
import { AuthService } from './service/auth.service';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet, CommonModule],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent {
  public showNavbar: boolean = true;
  public isAdmin: boolean = false;
  private currentNavBtn!: HTMLElement;

  @ViewChild('homeNavBtn') set _currentNavBtn (reference: ElementRef) {
    if (reference) this.currentNavBtn = reference.nativeElement;
  }

  constructor (private authService: AuthService, private router: Router) {
    this.router.events.subscribe(event => {
      const navHidePaths = ['', '/login', '/signup'];

      if (event instanceof NavigationEnd) {
        this.showNavbar = !navHidePaths.includes(this.router.url);
        this.isAdmin = this.authService.isAdmin();

        if (this.router.url !== '/signup' && !this.authService.isAuthenticated())
          this.router.navigate(['/login']);
      }
    });
  }

  public navBarNavigateTo (route: string, target: HTMLElement): void {
    this.currentNavBtn.classList.remove('active');
    target.classList.add('active');
    this.currentNavBtn = target;
    
    this.router.navigate([`/${route}`]);
  }
}
