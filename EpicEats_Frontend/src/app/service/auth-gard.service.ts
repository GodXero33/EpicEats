import { Injectable } from "@angular/core";
import { AuthService } from "./auth.service";
import { ActivatedRouteSnapshot, CanActivate, GuardResult, MaybeAsync, Router, RouterStateSnapshot } from "@angular/router";

@Injectable({
	providedIn: 'root'
})
export class AuthGuard implements CanActivate {
	constructor (private authService: AuthService, private router: Router) {}

	public canActivate (route: ActivatedRouteSnapshot, state: RouterStateSnapshot): boolean {
		if (this.authService.isAuthenticated()) {
			return true;
		}

		this.router.navigate(['/login']);
		return false;
	}
}
