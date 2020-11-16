import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { apiUrl, baseUserUrl } from 'src/environments/environment';
import { Application } from './application.model';

@Injectable({
  providedIn: 'root'
})
export class ApplicationsListService {
  constructor(private http: HttpClient) {}

  getApplications(params) {
    return this.http.get(`${apiUrl}/applications`, { params });
  }

  getAdminApplications() {
    return this.http.get<Application[]>(`${apiUrl}/applications`);
  }

  getUserApplications(userId: string) {
    return this.http.get<Application[]>(`${baseUserUrl}/${userId}/applications`);
  }
}