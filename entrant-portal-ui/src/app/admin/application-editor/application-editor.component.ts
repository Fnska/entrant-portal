import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { apiUrl } from 'src/environments/environment';

@Component({
  selector: 'app-application-editor',
  templateUrl: './application-editor.component.html',
  styleUrls: ['./application-editor.component.css']
})
export class ApplicationEditorComponent implements OnInit {
  applicationId: number;
  form: FormGroup;

  constructor(private route: ActivatedRoute, private http: HttpClient, private fb: FormBuilder, private router: Router) { }

  ngOnInit(): void {
    this.applicationId = parseInt(this.route.snapshot.paramMap.get('id'));

    const user = this.fb.group({
      id: {value: '', disabled: true},
      login: {value: '', disabled: true},
      role: {value: '', disabled: true}
    });
    
    const faculty = this.fb.group({
      id: {value: '', disabled: true},
      name: {value: '', disabled: true}
    });

    const course = this.fb.group({
      id: {value: '', disabled: true},
      faculty: faculty,
      name: {value: '', disabled: true},
      seats: {value: '', disabled: true},
      leftSeats: {value: '', disabled: true}
    });

    this.form = this.fb.group({
      id: {value: '', disabled: true},
      user: user,
      course: course,
      priority: '',
      rating: {value: '', disabled: true},
      status: '',
      examScore: ''
    });

    this.http.get(`${apiUrl}/applications/${this.applicationId}`).subscribe(data => {
      this.form.setValue(data);
    });
  }

  onSubmit() {
    this.http.post(`${apiUrl}/applications`, this.form.getRawValue()).subscribe();
  }

  onGetEntrantInfo(entrantId: number) {
    this.router.navigate(['entrants', entrantId]);
  }

  onCancel() {
    this.router.navigate(['applications']);
  }

  get entrantId() {
    return parseInt(this.form.get('user.id').value);
  }
}
