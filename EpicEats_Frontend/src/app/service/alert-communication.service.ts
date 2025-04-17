import { Injectable } from "@angular/core";
import { Subject } from "rxjs";

@Injectable({
	providedIn: 'root'
})
export class AlertCommunicationService {
	private alertSource = new Subject<{ error: string, info: string }>();
	public alertValue$ = this.alertSource.asObservable();

	public showError (error: string): void {
		this.alertSource.next({ error: error, info: '' });
	}

	public showInfo (info: string): void {
		this.alertSource.next({ error: '', info: info });
	}
}
