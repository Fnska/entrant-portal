import { CommonModule } from '@angular/common';
import { HTTP_INTERCEPTORS } from '@angular/common/http';
import { NgModule } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { LoginComponent } from '../login/login.component';
import { SignupComponent } from '../signup/signup.component';
import { AuthInterceptor } from './auth.interceptor';
import { AuthGuard } from './guards/auth.guard';
import { ResourceGuard } from './guards/resource.guard';
import { AuthService } from './services/auth.service';
import { UnauthorizedInterceptor } from './unauthorized.interceptor';

@NgModule({
  declarations: [LoginComponent, SignupComponent],
  providers: [
    AuthGuard,
    AuthService,
    ResourceGuard,
    {
      provide: HTTP_INTERCEPTORS,
      useClass: AuthInterceptor,
      multi: true
    },
    {
      provide: HTTP_INTERCEPTORS,
      useClass: UnauthorizedInterceptor,
      multi: true,
    }
  ],
  imports: [
    ReactiveFormsModule,
    CommonModule
  ]
})
export class AuthModule { }