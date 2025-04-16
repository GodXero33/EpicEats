import { Component } from '@angular/core';

@Component({
  selector: 'app-home',
  imports: [],
  templateUrl: './home.component.html',
  styleUrl: './home.component.css'
})
export class HomeComponent {
  public username: string = 'sathish';
  public greet: string = '';

  constructor () {
    this.updateGreet();
    setInterval(this.updateGreet, 1 * 60 * 1000);
  }

  private updateGreet () {
    const hours = new Date().getHours();
    const username = sessionStorage.getItem('username');

    this.greet = hours <= 12 ? 'Morning' : hours <= 15 ? 'Afternoon' : 'evening';
    this.username = username ? username : '';
  }
}
