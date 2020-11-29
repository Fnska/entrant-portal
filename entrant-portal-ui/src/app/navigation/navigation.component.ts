import { Component, OnInit } from '@angular/core';
import { AuthService } from '../auth/services/auth.service';

@Component({
  selector: 'app-navigation',
  templateUrl: './navigation.component.html',
  styleUrls: ['./navigation.component.css']
})
export class NavigationComponent implements OnInit {
  role: string;
  login: string;
  entrantId: string;
  constructor(private auth: AuthService) { }

  ngOnInit(): void {
    this.entrantId = localStorage.getItem('USER_ID');
    this.login = this.auth.getLoggedUser();
    this.role = this.auth.getUserRole();
  }
  
  logout(){
    this.auth.doLogout();
  }
}
