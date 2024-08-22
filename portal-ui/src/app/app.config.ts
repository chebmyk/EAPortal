import {ApplicationConfig, importProvidersFrom, provideZoneChangeDetection} from '@angular/core';
import {
  PreloadAllModules,
  provideRouter,
  withComponentInputBinding,
  withPreloading,
  withRouterConfig
} from '@angular/router';
import { routes } from './app.routes';
import { provideAnimationsAsync } from '@angular/platform-browser/animations/async';
import {environment} from "../environments/environment";
import {provideHttpClient} from "@angular/common/http";
import {BrowserAnimationsModule} from "@angular/platform-browser/animations";
import {BrowserModule} from "@angular/platform-browser";


export const config = Object.freeze(environment);



export const appConfig: ApplicationConfig = {
  providers: [
    provideZoneChangeDetection({ eventCoalescing: true }),

    provideRouter(routes,
      withComponentInputBinding(),
      withPreloading(PreloadAllModules),
      withRouterConfig({paramsInheritanceStrategy: 'always'})
    ),

    importProvidersFrom(BrowserAnimationsModule),
    importProvidersFrom(BrowserModule),
    provideAnimationsAsync(),
    provideHttpClient()
  ]
};




