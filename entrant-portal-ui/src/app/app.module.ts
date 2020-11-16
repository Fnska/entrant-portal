import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { AppRoutingModule } from './app-routing.module';
import { HttpClientModule } from '@angular/common/http'
import { AppComponent } from './app.component';
import { NavigationComponent } from './navigation/navigation.component';
import { NotFoundComponentComponent } from './not-found-component/not-found-component.component';
import { ReactiveFormsModule } from '@angular/forms';
import { AuthModule } from './auth/auth.module';
import { HomeComponent } from './home/home.component';
import { ApplicationsListComponent } from './entrant/applications-list/applications-list.component';
import { NewEducationComponent } from './entrant/new-education/new-education.component';
import { PersonalDataComponent } from './entrant/personal-data/personal-data.component';
import { SideBarComponent } from './side-bar/side-bar.component';
import { ApplicationEditorComponent } from './admin/application-editor/application-editor.component';
import { EntrantInfoComponent } from './entrant/entrant-info/entrant-info.component';
import { NgxPaginationModule } from 'ngx-pagination';
import { NewEducationListComponent } from './entrant/new-education-list/new-education-list.component';


@NgModule({
  declarations: [
    AppComponent,
    NavigationComponent,
    PersonalDataComponent,
    NotFoundComponentComponent,
    SideBarComponent,
    NewEducationComponent,
    ApplicationsListComponent,
    HomeComponent,
    ApplicationEditorComponent,
    EntrantInfoComponent,
    NewEducationListComponent,
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    ReactiveFormsModule,
    AuthModule,
    NgxPaginationModule
  ],
  providers: [
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
