import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from 'src/app/auth/services/auth.service';
import { ApplicationsListService } from './application-list.service';
import { Application } from './application.model';


@Component({
  selector: 'app-applications-list',
  templateUrl: './applications-list.component.html',
  styleUrls: ['./applications-list.component.css']
})
export class ApplicationsListComponent implements OnInit {
  page = 1;
  count = 0;
  pageSize = 10;
  pageSizes = [10, 20, 40];

  applications: Application[];
  userId: string;
  
  constructor(private appService: ApplicationsListService, private auth: AuthService, private router: Router) { }

  ngOnInit(): void {
    this.userId = this.auth.getUserId();
    if (this.isAdmin()) {
      this.getApplicationsForAdmin();
    } else {
      this.appService.getUserApplications(this.userId).subscribe(res => {
        this.applications = res;
      });
    }
  }

  handlePageChange(event: any) {
    this.page = event;
    this.getApplicationsForAdmin();
  }

  handlePageSizeChange(event: any) {
    this.pageSize = event.target.value;
    this.page = 1;
    this.getApplicationsForAdmin();
  }

  getRequestParams(page: number, pageSize: number) {
    let params = {};
    if (page) {
      params[`page`] = page - 1;
    }
    if (pageSize) {
      params[`size`] = pageSize;
    }
    return params;
  }

  private getApplicationsForAdmin() {
    const params = this.getRequestParams(this.page, this.pageSize);
    this.appService.getApplications(params).subscribe(res => {
      this.applications = res['content'];
      this.count = res['totalElements']
    });
  }

  isAdmin() {
    return this.auth.getUserRole() == 'ADMIN';
  }

  onEdit(application: Application) {
    this.router.navigate(['applications', application.id]);
  }
}
