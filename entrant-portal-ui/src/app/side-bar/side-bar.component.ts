import { HttpClient } from '@angular/common/http';
import { Component, Input, OnInit } from '@angular/core';
import { apiUrl } from 'src/environments/environment';
import { AuthService } from '../auth/services/auth.service';

@Component({
  selector: 'app-side-bar',
  templateUrl: './side-bar.component.html',
  styleUrls: ['./side-bar.component.css']
})
export class SideBarComponent implements OnInit {
  private role: string;
  
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
      this.http.get(`${apiUrl}/applications/reports/create`).subscribe();
    }
  }
}
