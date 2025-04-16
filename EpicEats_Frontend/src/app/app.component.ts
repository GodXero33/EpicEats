import { CommonModule } from '@angular/common';
import { Component, ElementRef, ViewChild } from '@angular/core';
import { NavigationEnd, Router, RouterOutlet } from '@angular/router';
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
  @ViewChild('homeNavBtn') homeNavBtn!: ElementRef;
  @ViewChild('employeeNavBtn') employeeNavBtn!: ElementRef;
  @ViewChild('orderNavBtn') orderNavBtn!: ElementRef;
  @ViewChild('inventoryNavBtn') inventoryNavBtn!: ElementRef;
  @ViewChild('financeNavBtn') financeNavBtn!: ElementRef;
  @ViewChild('merchandiseNavBtn') merchandiseNavBtn!: ElementRef;
  @ViewChild('restaurantNavBtn') restaurantNavBtn!: ElementRef;
  @ViewChild('settingsNavBtn') settingsNavBtn!: ElementRef;

  private navRouteMatchers = [
    { match: '/home', getRef: () => this.homeNavBtn },
    { match: '/employee', getRef: () => this.employeeNavBtn },
    { match: '/order', getRef: () => this.orderNavBtn },
    { match: '/inventory', getRef: () => this.inventoryNavBtn },
    { match: '/finance', getRef: () => this.financeNavBtn },
    { match: '/merchandise', getRef: () => this.merchandiseNavBtn },
    { match: '/restaurant', getRef: () => this.restaurantNavBtn },
    { match: '/settings', getRef: () => this.settingsNavBtn },
  ];

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

        const matched = this.navRouteMatchers.find(m => this.router.url.startsWith(m.match));
        
        if (matched) {
          const newBtn = matched.getRef().nativeElement;

          if (this.currentNavBtn) this.currentNavBtn.classList.remove('active');
          
          newBtn.classList.add('active');

          this.currentNavBtn = newBtn;
        }
      }
    });
  }

  public navBarNavigateTo (route: string): void {
    this.router.navigate([`/${route}`]);
  }
}
