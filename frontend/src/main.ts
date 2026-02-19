import { provideHttpClient, withInterceptors } from '@angular/common/http';
import { bootstrapApplication } from '@angular/platform-browser';
import { AppComponent } from './app/app.component';
import { cspNonceInterceptor } from './app/csp-nonce.interceptor';

bootstrapApplication(AppComponent, {
  providers: [
    provideHttpClient(withInterceptors([cspNonceInterceptor]))
  ]
}).catch((err) => console.error(err));
