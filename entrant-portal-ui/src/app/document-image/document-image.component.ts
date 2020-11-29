import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { AuthService } from '../auth/services/auth.service';
import { PersonalDataService } from '../entrant/personal-data/personal-data.service';

@Component({
  selector: 'app-document-image',
  templateUrl: './document-image.component.html',
  styleUrls: ['./document-image.component.css']
})
export class DocumentImageComponent implements OnInit {
  isImageLoading: boolean;
  imageToShow: any;
  educations: any[];
  entrantId: string;
  constructor(private personalDataService: PersonalDataService, private auth: AuthService, private route: ActivatedRoute) { }

  ngOnInit(): void {
    this.entrantId = this.route.snapshot.paramMap.get('id');
    this.personalDataService.getLinksToUploadedFiles(this.entrantId).subscribe(edu => {
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
