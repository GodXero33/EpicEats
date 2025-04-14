import { Injectable } from "@angular/core";
import { Router } from "@angular/router";

@Injectable({
	providedIn: 'root'
})
export class AuthService {
	constructor (private router: Router) {}

	public setToken (token: string, role: string): void {
		sessionStorage.setItem('authToken', token);
		sessionStorage.setItem('userRole', role);
	}

	public getToken (): string | null {
		return sessionStorage.getItem('authToken');
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

	public logout (): void {
		sessionStorage.removeItem('authToken');
		this.router.navigate(['/login']);
	}
}
