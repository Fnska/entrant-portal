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
  private readonly LAST_APP_PAGE = 'LAST_APP_PAGE';
  private readonly LAST_APP_PAGE_SIZE = 'LAST_APP_PAGE_SIZE';
  page = 1;
  count = 0;
  pageSize = 10;
  pageSizes = [10, 20, 40];

  applications: Application[];
  userId: string;

  constructor(private appService: ApplicationsListService, private auth: AuthService, private router: Router) { }

  ngOnInit(): void {
    this.userId = this.auth.getUserId();
    var lastPage = localStorage.getItem(this.LAST_APP_PAGE);
    var lastPageSize = localStorage.getItem(this.LAST_APP_PAGE_SIZE);
    if (lastPage) {
      this.page = parseInt(lastPage);
    }
    if (lastPageSize) {
      this.pageSize = parseInt(lastPageSize);
    }
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
    localStorage.setItem(this.LAST_APP_PAGE, this.page.toString());
    this.getApplicationsForAdmin();
  }

  handlePageSizeChange(event: any) {
    this.pageSize = event.target.value;
    this.page = 1;
    localStorage.setItem(this.LAST_APP_PAGE, this.page.toString());
    localStorage.setItem(this.LAST_APP_PAGE_SIZE, this.pageSize.toString());
    this.getApplicationsForAdmin();
  }

  compareFn( optionOne, optionTwo ) : boolean {
    return optionOne.id === optionTwo.id;
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
