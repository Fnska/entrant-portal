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
  isImageLoading: boolean;
  entrantId: number;
  educations: any[];
  imageToShow: any;
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

    this.personalDataService.getLinksToUploadedFiles(this.entrantId.toString()).subscribe(edu => {
      this.educations = edu;
    });
  }

  getImageFromService(imgUrl) {
    this.isImageLoading = true;
    this.getImage(imgUrl).subscribe(data => {
      this.createImageFromBlob(data);
      this.isImageLoading = false;
    }, error => {
      this.isImageLoading = false;
      console.log(error);
    });
  }

  getImage(imageUrl: string): Observable<Blob> {
    return this.personalDataService.getImageByUrl(imageUrl);
  }
  
  createImageFromBlob(image: Blob) {
    let reader = new FileReader();
    reader.addEventListener("load", () => {
       this.imageToShow = reader.result;
    }, false);
 
    if (image) {
       reader.readAsDataURL(image);
    }
  }
}
