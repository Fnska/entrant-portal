import { HttpClient, HttpResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { apiUrl } from 'src/environments/environment';
import { AuthService } from '../auth/services/auth.service';

@Component({
  selector: 'app-side-bar',
  templateUrl: './side-bar.component.html',
  styleUrls: ['./side-bar.component.css']
})
export class SideBarComponent implements OnInit {
  private role: string;
  private blob: Blob;

  constructor(private auth : AuthService, private http: HttpClient) { }

  ngOnInit(): void {
    this.role = this.auth.getUserRole();
  }

  isAdmin() {
    if (this.role == 'ADMIN') {
      return true;
    }
    return false;
  }

  onCompleteAdmissions() {
    if (confirm('Do you want to complete admissions?')) {
      this.http.post(`${apiUrl}/complete`, {}).subscribe();
    }
  }

  onGetReport() {
    if (confirm('Do you want to generate report?')) {
      this.http.get<Blob>(`${apiUrl}/applications/reports/create`, 
      {observe: 'response', responseType: 'blob' as 'json'}).subscribe(
        (resp: HttpResponse<Blob>) => {
          var data = resp.body;
          this.blob = new Blob([data], {type: 'application/pdf'});
          var downloadURL = window.URL.createObjectURL(data);
          var link = document.createElement('a');
          link.href = downloadURL;
          var contentDisposition = resp.headers.get('Content-Disposition');
          var filename = contentDisposition.split(';')[1].split('filename')[1].split('=')[1].trim().replace(/\"/g, '');
          link.download = filename;
          link.click();
      });
    }
  }
}
