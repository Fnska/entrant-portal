import { Component, Input, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { AuthService } from 'src/app/auth/services/auth.service';
import { NewEducationService } from './new-education.service';

@Component({
  selector: 'app-new-education',
  templateUrl: './new-education.component.html',
  styleUrls: ['./new-education.component.css']
})
export class NewEducationComponent implements OnInit {
  @Input() fixedPriority: string;
  userId: string;
  form: FormGroup;
  faculties: {};
  courses: {};
  formSent: boolean;

  constructor(private fb: FormBuilder, private auth: AuthService, private edu: NewEducationService) { }

  ngOnInit(): void {
    this.userId = this.auth.getUserId();
    this.formSent = false;
    this.form = this.fb.group({
      faculty: [''],
      course: [''],
      priority: {value: this.fixedPriority, disabled: true}
    });

    this.edu.getFaculties().subscribe(res => {
      this.faculties = res;
    });
  }

  onChangeFaculty(facultyId: number) {
    if (facultyId) {
      this.edu.getCourses(facultyId).subscribe(res => {
        this.courses = res;
      });
    } else {
      this.courses = null;
    }
  }

  onSubmit() {
    this.edu.saveApplication(this.userId, this.course.value, this.priority.value);
    this.formSent = true;
  }

  isFormSent(): boolean {
    return this.formSent;
  }

  get course() {
    return this.form.get('course');
  }

  get priority() {
    return this.form.get('priority');
  }
}
