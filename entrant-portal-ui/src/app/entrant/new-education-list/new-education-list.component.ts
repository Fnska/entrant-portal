import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-new-education-list',
  templateUrl: './new-education-list.component.html',
  styleUrls: ['./new-education-list.component.css']
})
export class NewEducationListComponent implements OnInit {
  low: string = 'LOW';
  medium: string = 'MEDIUM';
  high: string = 'HIGH';
  
  constructor() { }

  ngOnInit(): void {
  }

}
