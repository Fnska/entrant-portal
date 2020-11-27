import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from 'src/app/auth/services/auth.service';
import { Entrant } from './entrant.model';
import { EntrantsListService } from './entrants-list.service';

@Component({
  selector: 'app-entrants-list',
  templateUrl: './entrants-list.component.html',
  styleUrls: ['./entrants-list.component.css']
})
export class EntrantsListComponent implements OnInit {
  private readonly LAST_ENT_PAGE = 'LAST_ENT_PAGE';
  private readonly LAST_ENT_PAGE_SIZE = 'LAST_ENT_PAGE_SIZE';
  page = 1;
  count = 0;
  pageSize = 10;
  pageSizes = [10, 20, 40];

  entrants: Entrant[];

  constructor(private entService: EntrantsListService) { }

  ngOnInit(): void {
    var lastPage = localStorage.getItem(this.LAST_ENT_PAGE);
    var lastPageSize = localStorage.getItem(this.LAST_ENT_PAGE_SIZE);
    if (lastPage) {
      this.page = parseInt(lastPage);
    }
    if (lastPageSize) {
      this.pageSize = parseInt(lastPageSize);
    }
    this.getEntrants();
  }

  onDisable(entrant: Entrant) {
    this.entService.disableEntrant(entrant).subscribe(_ =>
      this.getEntrants()
    );
  }

  onActivate(entrant: Entrant) {
    this.entService.activateEntrant(entrant).subscribe(_ =>
        this.getEntrants()
    );
  }

  handlePageChange(event: any) {
    this.page = event;
    localStorage.setItem(this.LAST_ENT_PAGE, this.page.toString());
    this.getEntrants();
  }

  handlePageSizeChange(event: any) {
    this.pageSize = event.target.value;
    this.page = 1;
    localStorage.setItem(this.LAST_ENT_PAGE, this.page.toString());
    localStorage.setItem(this.LAST_ENT_PAGE_SIZE, this.pageSize.toString());
    this.getEntrants();
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

  private getEntrants() {
    const params = this.getRequestParams(this.page, this.pageSize);
    this.entService.getEntrants(params).subscribe(res => {
      this.entrants = res['content'];
      this.count = res['totalElements']
    });
  }
}
