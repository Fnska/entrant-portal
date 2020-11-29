import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { baseUserUrl } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class PersonalDataService {

  constructor(private http: HttpClient) {}

  savePassportData(userId: string, passportData: any): void {
    this.http.post(`${baseUserUrl}/${userId}/passport`, passportData).subscribe()
  }

  saveUserData(userId: string, userData: any): void {
    this.http.post(`${baseUserUrl}/${userId}/data`, userData).subscribe();
  }

  uploadPhoto(userId: string, uploadData: any, education: string) {
    return this.http.post(`${baseUserUrl}/${userId}/files/upload?grade=`+education, uploadData);
  }

  getUserData(userId: string): Observable<any>  {
    return this.http.get(`${baseUserUrl}/${userId}/data`);
  }

  getPassportData(userId: string): Observable<any>  {
    return this.http.get(`${baseUserUrl}/${userId}/passport`);
  }

  getLinksToUploadedFiles(userId: string): Observable<any> {
    return this.http.get(`${baseUserUrl}/${userId}/files`);
  }

  getImageByUrl(imgUrl: string): Observable<any> {
    return this.http.get(imgUrl, { responseType: 'blob' });
  }
}