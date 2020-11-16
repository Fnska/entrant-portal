import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { ApplicationEditorComponent } from './admin/application-editor/application-editor.component';
import { AuthGuard } from './auth/guards/auth.guard';
import { ResourceGuard } from './auth/guards/resource.guard';
import { ApplicationsListComponent } from './entrant/applications-list/applications-list.component';
import { EntrantInfoComponent } from './entrant/entrant-info/entrant-info.component';
import { NewEducationListComponent } from './entrant/new-education-list/new-education-list.component';
import { PersonalDataComponent } from './entrant/personal-data/personal-data.component';
import { HomeComponent } from './home/home.component';
import { LoginComponent } from './login/login.component';
import { NotFoundComponentComponent } from './not-found-component/not-found-component.component';
import { SignupComponent } from './signup/signup.component';

const routes: Routes = [
  { path: '', pathMatch: 'full', redirectTo: '/login'},
  { path: 'login', component: LoginComponent, canActivate: [AuthGuard]},
  { path: 'signup', component: SignupComponent, canActivate: [AuthGuard]},
  { 
    path: '', 
    component: HomeComponent, 
    canActivate: [ResourceGuard], 
    children: [
      { path: 'profile', component: PersonalDataComponent},
      { path: 'new-edu', component: NewEducationListComponent},
      { path: 'applications', component: ApplicationsListComponent},
      { path: 'entrants/:id', component: EntrantInfoComponent},
      { path: 'applications/:id', component: ApplicationEditorComponent},
    ]
  },
  { path: '**', component: NotFoundComponentComponent},
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
