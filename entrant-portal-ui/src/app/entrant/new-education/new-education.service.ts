import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { apiUrl, baseUserUrl } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class NewEducationService {
  constructor(private http: HttpClient) {}

  getFaculties() {
    return this.http.get(`${apiUrl}/faculties`);
  }

  getCourses(facultyId: number) {
    return this.http.get(`${apiUrl}/faculties/${facultyId}`);
  }

  saveApplication(userId: string, courseId: number, priority: string) {
    let body = {"courseId": courseId, "priority": priority};
    this.http.post(`${baseUserUrl}/${userId}/applications`, body).subscribe();
  }
}