import { Component, OnDestroy } from '@angular/core';
import { NavigationEnd, Router, RouterOutlet } from '@angular/router';
import { filter, Subject, takeUntil } from 'rxjs';

@Component({
  selector: 'app-search-employee',
  imports: [RouterOutlet],
  templateUrl: './search-employee.component.html',
  styleUrl: './search-employee.component.css'
})
export class SearchEmployeeComponent implements OnDestroy {
  public currentRoute!: string;
  private destroyRouterSub$: Subject<void> = new Subject();

  constructor (private router: Router) {
    this.currentRoute = this.router.url.split('/')[3];

    this.router.events
      .pipe(
        filter(event => event instanceof NavigationEnd),
        takeUntil(this.destroyRouterSub$)
      )
      .subscribe(event => this.currentRoute = event.url.split('/')[3]);
  }

  public ngOnDestroy (): void {
    this.destroyRouterSub$.next();
    this.destroyRouterSub$.complete();
  }

  public navigateSection (path: string): void {
    this.router.navigate([`employee/search/${path}`]);
  }
}
