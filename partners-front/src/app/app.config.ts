import { ApplicationConfig } from '@angular/core';
import { provideRouter } from '@angular/router';
import { provideHttpClient, withFetch, withInterceptorsFromDi } from '@angular/common/http';
import { provideAnimations } from '@angular/platform-browser/animations';
import { HTTP_INTERCEPTORS } from '@angular/common/http';
import { ErrorInterceptor } from './core/interceptors/error.interceptor';
import { providePrimeNG } from 'primeng/config';
import Aura from '@primeng/themes/aura';

import { routes } from './app.routes';
export const appConfig: ApplicationConfig = {
  providers: [
    provideRouter(routes),
    provideHttpClient(withInterceptorsFromDi(), withFetch()),
    provideAnimations(),
    {
      provide: HTTP_INTERCEPTORS,
      useClass: ErrorInterceptor,
      multi: true
    },
    providePrimeNG({
      theme: {
          preset: Aura,
          options: {
            darkModeSelector: false || 'none'
          }
      }
      
  })
  ]
};


export const environment = {
  production: false,
  partnersApiUrl: 'http://localhost:8080/api',
  messagesApiUrl: 'http://localhost:8081/api'
};

export const environmentProd = {
  production: true,
  partnersApiUrl: 'http://localhost:8080/api',
  messagesApiUrl: 'http://localhost:8081/api'
};


