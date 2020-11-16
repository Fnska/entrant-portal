import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { apiUrl } from 'src/environments/environment';

@Component({
  selector: 'app-signup',
  templateUrl: './signup.component.html',
  styleUrls: ['./signup.component.css']
})
export class SignupComponent implements OnInit {
  form: FormGroup;
  failedSignUp = false;

  constructor(private fb: FormBuilder, private http: HttpClient, private router: Router) { }

  ngOnInit(): void {
    this.form = this.fb.group({
      login : ['', [Validators.required, Validators.pattern("^[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,4}$")]],
      password : ['', Validators.required]
    });
  }

  onSubmit() {
    this.http.post(`${apiUrl}/auth/signup`, this.form.value)
      .pipe(
        catchError((err: HttpErrorResponse) => {
          if (err.status === 400) {
            this.failedSignUp = true;
            return throwError('bad request to signup'); 
          }
        })
      )
      .subscribe(data => {
        this.router.navigate(['login']);
      });
      
  }

  get email() {
    return this.form.get('login');
  }

}
