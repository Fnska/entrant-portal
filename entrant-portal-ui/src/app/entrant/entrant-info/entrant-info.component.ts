import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { PersonalDataService } from '../personal-data/personal-data.service';

@Component({
  selector: 'app-entrant-info',
  templateUrl: './entrant-info.component.html',
  styleUrls: ['./entrant-info.component.css']
})
export class EntrantInfoComponent implements OnInit {
  name: string;
  lastname: string;
  surname: string;
  series: string;
  number: string;
  entrantId: number;
  constructor(private personalDataService: PersonalDataService, private route: ActivatedRoute) { }

  ngOnInit(): void {
    this.entrantId = parseInt(this.route.snapshot.paramMap.get('id'));

    this.personalDataService.getUserData(`${this.entrantId}`).subscribe(data => {
      if (data != null) {
        this.name = data.name,
        this.lastname = data.lastname,
        this.surname = data.surname;
      }
    });

    this.personalDataService.getPassportData(`${this.entrantId}`).subscribe(data => {
      if (data != null) {
        this.series = data.series,
        this.number = data.number
      }
    });
  }
}
