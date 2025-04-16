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
  public currentRoute: string | null = null;
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

  private logout (): void { // remove this after all
    this.authService.logout();
  }

  constructor (private authService: AuthService, private router: Router) {
    (window as any).logout = this.logout.bind(this); // remove this after all

    this.router.events.subscribe(event => {
      const navHidePaths: Array<String> = ['', '/login', '/signup'];

      if (event instanceof NavigationEnd) {
        this.showNavbar = !navHidePaths.includes(event.url);
        this.isAdmin = this.authService.isAdmin();

        if (event.url !== '/signup' && !this.authService.isAuthenticated())
          this.router.navigate(['/login']);

        this.currentRoute = event.url.split('/')[1];
      }
    });
  }

  public navBarNavigateTo (route: string): void {
    this.router.navigate([`/${route}`]);
  }
}
