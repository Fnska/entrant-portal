import { HttpEvent, HttpHandler, HttpInterceptor, HttpRequest } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { Observable } from 'rxjs';

@Injectable()
export class AuthInterceptor implements HttpInterceptor {
  
  constructor(private router: Router) {}

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    const idToken = localStorage.getItem("JWT_TOKEN");
    if (idToken) {
      const clonedReq = req.clone({
        setHeaders: {'Authorization': `Bearer ${idToken}`},
        //withCredentials: true
      });
      return next.handle(clonedReq);
    }
    else {
      if (this.router.url != '/signup') {
        this.router.navigate(['login']);
      }
      return next.handle(req);
    }
  }
}