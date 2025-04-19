import { Component, OnDestroy } from '@angular/core';
import { NavigationEnd, Router, RouterOutlet } from '@angular/router';
import { filter, Subject, takeUntil } from 'rxjs';

@Component({
  selector: 'app-employee',
  imports: [RouterOutlet],
  templateUrl: './employee.component.html',
  styleUrl: './employee.component.css'
})
export class EmployeeComponent implements OnDestroy {
  public currentRoute!: string;
  public destroyRouterSub$: Subject<void> = new Subject();

  constructor (private router: Router) {
    this.currentRoute = this.router.url.split('/')[2];

    this.router.events
      .pipe(
        filter(event => event instanceof NavigationEnd),
        takeUntil(this.destroyRouterSub$)
      )
      .subscribe((event: NavigationEnd) => this.currentRoute = event.url.split('/')[2]);
  }

  public ngOnDestroy (): void {
    this.destroyRouterSub$.next();
    this.destroyRouterSub$.complete();
  }

  public sideNavBarNavigateTo (route: string): void {
    this.router.navigate([`/employee/${route}`]);
  }
}
