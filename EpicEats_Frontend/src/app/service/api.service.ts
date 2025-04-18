import { Injectable } from "@angular/core";
import { environment } from "../../environments/environment";
import { HttpClient, HttpErrorResponse, HttpHeaders, HttpParams } from "@angular/common/http";
import { AuthService } from "./auth.service";
import { AlertCommunicationService } from "./alert-communication.service";
import { catchError, map, Observable, throwError } from "rxjs";

@Injectable({
	providedIn: 'root'
})
export class ApiService {
	private baseUrl: string = environment.apiUrl;

	constructor (private http: HttpClient, private authService: AuthService, private alertCommunicationService: AlertCommunicationService) {}

	private buildHeaders (): HttpHeaders {
		return new HttpHeaders({
			'Content-Type': 'application/json',
			'Authorization': `Bearer ${this.authService.getToken()}`
		});
	}

	public get<T>(endpoint: string): Observable<T> {
		return this.http.get<T>(`${this.baseUrl}${endpoint}`, { headers: this.buildHeaders() }).pipe(catchError(this.handleError), map((res) => this.handleResponse<T>(res)));
	}

	public post<T>(endpoint: string, body: any): Observable<T> {
		return this.http.post<T>(`${this.baseUrl}${endpoint}`, body, { headers: this.buildHeaders() }).pipe(catchError(this.handleError), map((res) => this.handleResponse<T>(res)));
	}

	public put<T>(endpoint: string, body: any): Observable<T> {
		return this.http.put<T>(`${this.baseUrl}${endpoint}`, body, { headers: this.buildHeaders() }).pipe(catchError(this.handleError), map((res) => this.handleResponse<T>(res)));
	}

	public delete<T>(endpoint: string): Observable<T> {
		return this.http.delete<T>(`${this.baseUrl}${endpoint}`, { headers: this.buildHeaders() }).pipe(catchError(this.handleError), map((res) => this.handleResponse<T>(res)));
	}

	public patch<T>(endpoint: string): Observable<T> {
		return this.http.patch<T>(`${this.baseUrl}${endpoint}`, null, { headers: this.buildHeaders() }).pipe(catchError(this.handleError), map((res) => this.handleResponse<T>(res)));
	}

	private handleError = (error: HttpErrorResponse): Observable<never> => {
		let errorMessage = 'An unknown error occurred!';

		if (error.status === 0) {
			errorMessage = 'Network error: Failed to connect to the server';
		} else if (error.status === 401) {
			errorMessage = 'Unauthorized: Please log in again';
		} else {
			errorMessage = `Error ${error.status}: ${error.error?.message}`;
		}

		this.alertCommunicationService.showError(error.error?.message);
		return throwError(() => new Error(errorMessage));
	}

	private handleResponse = <T>(response: any): T => {
		if (response && typeof response === 'object' && 'message' in response && 'data' in response) {
			this.alertCommunicationService.showInfo(response.message);

			return response.data as T;
		}

		return response as T;
	}
}
