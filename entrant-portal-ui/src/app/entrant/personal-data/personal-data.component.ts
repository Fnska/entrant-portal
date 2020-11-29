import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormGroup, Validators, FormBuilder } from '@angular/forms';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { AuthService } from 'src/app/auth/services/auth.service';

import { PersonalDataService } from './personal-data.service';

@Component({
  selector: 'app-personal-data',
  templateUrl: './personal-data.component.html',
  styleUrls: ['./personal-data.component.css']
})
export class PersonalDataComponent implements OnInit {
  userDataForm: FormGroup;
  passportDataFrom: FormGroup;
  lastEducationForm: FormGroup;
  imageUrls: any[];
  imageToShow: any;
  isImageLoading: boolean;
  selectedFile: File = null;
  userId: string;
  formSent: boolean;
  fileExist: boolean;
  
  constructor(private fb: FormBuilder, private personalDataService: PersonalDataService, private auth: AuthService) { }

  ngOnInit(): void {
    this.userId = this.auth.getUserId();
    this.formSent = false;
    this.fileExist = false;
    this.passportDataFrom = this.fb.group({
      series: ['', [Validators.required, Validators.pattern('\\d{4}')]],
      number: ['', [Validators.required, Validators.pattern('\\d{6}')]]
    });

    this.lastEducationForm = this.fb.group({
      grade: 'SCHOOL',
    });

    this.userDataForm = this.fb.group({
      name: ['', Validators.required],
      lastname: ['', Validators.required],
      surname: ['', Validators.required],
    });

    this.personalDataService.getUserData(this.userId).subscribe(data => {
      if (data != null) {
        this.userDataForm.setValue({
          name: data.name,
          lastname: data.lastname,
          surname: data.surname
        });
      }
    });

    this.personalDataService.getPassportData(this.userId).subscribe(data => {
      if (data != null) {
        this.passportDataFrom.setValue({
          series: data.series,
          number: data.number
        });
      }
    });
  }

  onSubmit() {
    const uploadData = new FormData();
    if (this.selectedFile != null) {
      uploadData.append('image', this.selectedFile, this.selectedFile.name);
      this.personalDataService.uploadPhoto(this.userId, uploadData, this.grade.value).pipe(
        catchError((err: HttpErrorResponse) => {
          if (err.status === 409) {
            this.fileExist = true;
            return throwError('file already exists'); 
          }
        })
      ).subscribe();;
    }
    
    this.personalDataService.saveUserData(this.userId, this.userDataForm.value);
    this.personalDataService.savePassportData(this.userId, this.passportDataFrom.value);
    this.formSent = true;
  }

  isFormSent(): boolean {
    return this.formSent;
  }

  isFileExist(): boolean {
    return this.fileExist;
  }

  onFileSelected(event) {
    this.selectedFile = event.target.files[0];
  }

  get name() {
    return this.userDataForm.get('name');
  }
  get surname() {
    return this.userDataForm.get('surname');
  }
  get lastname() {
    return this.userDataForm.get('lastname');
  }
  get series() {
    return this.passportDataFrom.get('series');
  }
  get passNumber() {
    return this.passportDataFrom.get('number');
  }
  get grade() {
    return this.lastEducationForm.get('grade');
  }
}
