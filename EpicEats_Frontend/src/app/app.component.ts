import { CommonModule } from '@angular/common';
import { Component, ElementRef, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { NavigationEnd, Router, RouterOutlet } from '@angular/router';
import { AuthService } from './service/auth.service';
import { AlertCommunicationService } from './service/alert-communication.service';
import { Subscription } from 'rxjs';
import { AlertComponent } from './component/alert/alert.component';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet, CommonModule, AlertComponent],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent implements OnInit, OnDestroy {
  public showNavbar: boolean = true;
  public isAdmin: boolean = false;
  public currentRoute: string | null = null;
  public errorAlert: string = '';
  public infoAlert: string = '';
  private alertSubscription!: Subscription;
  @ViewChild('homeNavBtn') homeNavBtn!: ElementRef;
  @ViewChild('employeeNavBtn') employeeNavBtn!: ElementRef;
  @ViewChild('orderNavBtn') orderNavBtn!: ElementRef;
  @ViewChild('inventoryNavBtn') inventoryNavBtn!: ElementRef;
  @ViewChild('financeNavBtn') financeNavBtn!: ElementRef;
  @ViewChild('merchandiseNavBtn') merchandiseNavBtn!: ElementRef;
  @ViewChild('restaurantNavBtn') restaurantNavBtn!: ElementRef;
  @ViewChild('settingsNavBtn') settingsNavBtn!: ElementRef;

  private logout (): void { // remove this after all
    this.authService.logout();
  }

  constructor (private authService: AuthService, private router: Router, private alertCommunicationService: AlertCommunicationService) {
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

  ngOnDestroy (): void {
    if (this.alertSubscription) this.alertSubscription.unsubscribe();
  }

  ngOnInit (): void {
    this.alertSubscription = this.alertCommunicationService.alertValue$.subscribe((alert: { error: string, info: string }) => {
      this.errorAlert = alert.error;
      this.infoAlert = alert.info;
    });
  }

  public navBarNavigateTo (route: string): void {
    this.router.navigate([`/${route}`]);
  }
}
