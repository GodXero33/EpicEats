import { Component } from '@angular/core';
import { NavigationEnd, Router, RouterOutlet } from '@angular/router';
import { filter, Subject, takeUntil } from 'rxjs';

@Component({
  selector: 'app-restaurant',
  imports: [RouterOutlet],
  templateUrl: './restaurant.component.html',
  styleUrl: './restaurant.component.css'
})
export class RestaurantComponent {
public currentRoute!: string;
  public destroyRouterSub$: Subject<void> = new Subject();

  constructor (private router: Router) {
    this.currentRoute = this.router.url.split('/')[2];

    this.router.events
      .pipe(
        filter(event => event instanceof NavigationEnd),
        takeUntil(this.destroyRouterSub$)
      )
      .subscribe(event => this.currentRoute = event.url.split('/')[2]);
  }

  public ngOnDestroy (): void {
    this.destroyRouterSub$.next();
    this.destroyRouterSub$.complete();
  }

  public sideNavBarNavigateTo (route: string): void {
    this.router.navigate([`/restaurant/${route}`]);
  }
}
