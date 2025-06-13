import { Injectable } from "@angular/core";
import { Subject, TimeoutConfig } from "rxjs";

@Injectable({
	providedIn: 'root'
})
export class AlertCommunicationService {
	private alertSource: Subject<{ error: string, info: string }> = new Subject();
	public alertValue$ = this.alertSource.asObservable();
	private resetDelay: number = 5000;
	private resetTimeoutId: ReturnType<typeof setTimeout> | null = null;

	private reset (): void {
		this.alertSource.next({ error: '', info: '' });
	}

	private clearExistingTimeout (): void {
		if (this.resetTimeoutId !== null) {
			clearTimeout(this.resetTimeoutId);
			
			this.resetTimeoutId = null;
		}
	}

	public showError (error: string): void {
		this.clearExistingTimeout();
		this.alertSource.next({ error: error, info: '' });
		
		this.resetTimeoutId = setTimeout(this.reset.bind(this), this.resetDelay);
	}

	public showInfo (info: string): void {
		this.clearExistingTimeout();
		this.alertSource.next({ error: '', info: info });
		
		this.resetTimeoutId = setTimeout(this.reset.bind(this), this.resetDelay);
	}
}
