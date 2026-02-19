import { Component } from '@angular/core';

@Component({
  selector: 'app-root',
  standalone: true,
  templateUrl: './app.component.html'
})
export class AppComponent {
  title = 'Spring + Angular';
  description = 'Angular reads nonce from meta tag and sends it in X-CSP-Nonce header for API calls.';
}
