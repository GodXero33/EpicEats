import { Injectable } from "@angular/core";
import { NavigationEnd, Router } from "@angular/router";
import { filter } from "rxjs";

@Injectable({
	providedIn: 'root'
})
export class AuthService {
	private expTimeout: any = null;

	constructor (private router: Router) {
		this.startAutoLogoutCheck();
	}

	private startAutoLogoutCheck(): void {
		this.router.events.pipe(
			filter(event => event instanceof NavigationEnd)
		).subscribe(() => {
			const token = this.getToken();

			if (token) this.setToken(token);
		});
	}

	public setToken (token: string): void {
		sessionStorage.setItem('authToken', token);

		const payload = token.split('.')[1];
		const decodedPayload = JSON.parse(atob(payload));

		const ONE_MINUTE = 60000;
		const expMillis = decodedPayload.exp * 1000;
		const nowMillis = Date.now();
		const deltaMillis = expMillis - nowMillis;
		let remainingTime = deltaMillis > ONE_MINUTE ? deltaMillis - ONE_MINUTE : deltaMillis;

		console.log(`Token will expire in: ${remainingTime} ms`);

		if (this.expTimeout !== null) clearTimeout(this.expTimeout);

		this.expTimeout = setTimeout(() => {
			sessionStorage.setItem('last-route', this.router.url);
			sessionStorage.setItem('last-user', decodedPayload.sub);
			console.log("Token expired. Auto logging out.");
			this.logout(true);
		}, remainingTime);
	}

	public isAdmin () {
		const role: string | null = this.getRole();
		return role != null && role.toLowerCase() === 'admin';
	}

	public getToken (): string | null {
		return sessionStorage.getItem('authToken');
	}

	public getUsername (): string | null {
		const token: string | null = this.getToken();

		return !token ? null : this.decodeToken(token).sub;
	}

	public getRole (): string | null {
		const token: string | null = this.getToken();

		return !token ? null : this.decodeToken(token).role;
	}

	private isTokenExpired (): boolean {
		const token: string | null = this.getToken();

		return !token ? true : this.decodeToken(token).exp * 1000 < new Date().getTime();
	}

	private decodeToken (token: string): any {
		const payload = token.split('.')[1];
		const decodedPayload = atob(payload);
		
		return JSON.parse(decodedPayload);
	}

	public isAuthenticated (): boolean {
		return !this.isTokenExpired(); 
	}

	public logout (isAutoLogout: boolean = false): void {
		sessionStorage.removeItem('authToken');

		if (!isAutoLogout) {
			sessionStorage.removeItem('last-route');
			sessionStorage.removeItem('last-user');
		}
		if (this.expTimeout !== null) clearTimeout(this.expTimeout);

		this.router.navigate(['/login']);
	}
}
