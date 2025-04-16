import { Injectable } from "@angular/core";
import { AuthService } from "./auth.service";
import { ActivatedRouteSnapshot, CanActivate, GuardResult, MaybeAsync, Router, RouterStateSnapshot } from "@angular/router";

@Injectable({
	providedIn: 'root'
})
export class AuthGuard implements CanActivate {
	constructor (private authService: AuthService, private router: Router) {}

	public canActivate (route: ActivatedRouteSnapshot, state: RouterStateSnapshot): boolean {
		if (!this.authService.isAuthenticated()) {
			this.router.navigate(['/login']);
			return false;
		}

		if (route.data['adminOnly'] === true && !this.authService.isAdmin()) {
			this.router.navigate(['/home']);
			return false;
		}

		return true;
	}
}
