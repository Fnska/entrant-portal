import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { apiUrl, baseUserUrl } from 'src/environments/environment';
import { Entrant } from './entrant.model';

@Injectable({
  providedIn: 'root'
})
export class EntrantsListService {
  constructor(private http: HttpClient) {}

  getEntrants(params) {
    return this.http.get(`${apiUrl}/users`, { params });
  }

  disableEntrant(entrant: Entrant) {
    return this.http.post(`${apiUrl}/users/${entrant.id}/disable`, {});
  }

  activateEntrant(entrant: Entrant) {
    return this.http.post(`${apiUrl}/users/${entrant.id}/activate`, {});
  }
}