import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { apiUrl } from 'src/environments/environment';
import { RequestUser } from '../models/request-user.model';
import { ResponseUser } from '../models/response-user.model';
import jwt_decode from '../../../../node_modules/jwt-decode';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  
  private readonly JWT_TOKEN = 'JWT_TOKEN';
  private readonly USER_ID = 'USER_ID';
  private loggedUser: string;
  private userRole: string;

  constructor(private http: HttpClient, private router: Router) { }

  login(user: RequestUser) {
    return this.http.post<ResponseUser>(`${apiUrl}/auth/login`, user).subscribe(response => {
      this.doLoginUser(response);
    });
  }

  doLoginUser(respUser: ResponseUser): void{
    this.loggedUser = respUser.login;
    this.storeJwtToken(respUser.token);
    this.storeUserId(respUser.id);
    this.userRole = respUser.role;
    this.router.navigate(['profile']);
  }

  doLogout(): void {
    this.loggedUser = null;
    this.removeJwtToken();
    this.removeUserId();
    this.removeAppPagination();
    this.removeEntPagination();
    this.router.navigate(['login']);
  }

  isLoggedIn(): boolean {
    if (this.getJwtToken()) {
      return true;
    }
    return false;
  }

  getLoggedUser(): string {
    return jwt_decode(this.getJwtToken()).sub;
  }

  getUserRole(): string {
    return jwt_decode(this.getJwtToken()).role;
  }

  getJwtToken(): string {
    return localStorage.getItem(this.JWT_TOKEN);
  }

  getUserId(): string {
    return localStorage.getItem(this.USER_ID);
  }

  private storeUserId(id: string): void {
    localStorage.setItem(this.USER_ID, id);
  }
  
  private removeUserId(): void {
    localStorage.removeItem(this.USER_ID);
  }

  private storeJwtToken(jwt: string): void {
    localStorage.setItem(this.JWT_TOKEN, jwt);
  }

  private removeJwtToken(): void {
    localStorage.removeItem(this.JWT_TOKEN);
  }

  private removeAppPagination(): void {
    localStorage.removeItem('LAST_APP_PAGE');
    localStorage.removeItem('LAST_APP_PAGE_SIZE');
  }

  private removeEntPagination(): void {
    localStorage.removeItem('LAST_ENT_PAGE');
    localStorage.removeItem('LAST_ENT_PAGE_SIZE');
  }
} 
