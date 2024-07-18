import { ApplicationConfig, provideZoneChangeDetection } from '@angular/core';
import {PreloadAllModules, provideRouter, withComponentInputBinding, withPreloading} from '@angular/router';
import { routes } from './app.routes';
import { provideAnimationsAsync } from '@angular/platform-browser/animations/async';
import {environment} from "../environments/environment";
import {provideHttpClient} from "@angular/common/http";


export const config = Object.freeze(environment);



export const appConfig: ApplicationConfig = {
  providers: [
    provideZoneChangeDetection({ eventCoalescing: true }),
    provideRouter(routes, withComponentInputBinding(), withPreloading(PreloadAllModules)),
    provideAnimationsAsync(),
    provideHttpClient()
  ]
};
