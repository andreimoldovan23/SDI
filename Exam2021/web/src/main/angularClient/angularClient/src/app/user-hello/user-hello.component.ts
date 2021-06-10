import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { User } from '../interfaces/user';
import { AppUserService } from '../services/app-user.service';

@Component({
  selector: 'app-user-hello',
  templateUrl: './user-hello.component.html',
  styleUrls: ['./user-hello.component.css']
})
export class UserHelloComponent implements OnInit {
  user: User;

  constructor(private service: AppUserService, private route: ActivatedRoute) { }

  ngOnInit(): void {
    let routeString = this.route.toString();
    if (routeString.indexOf('v2') > -1) {
      this.route.params.subscribe(params => this.getUserDetails(parseInt(params['id'])));
    } else {
      this.route.params.subscribe(params => this.getUser(parseInt(params['id'])))
    }
  }

  private getUser(id) : void {
    this.service.getUser(id)
      .subscribe(result => this.user = result);
  }

  private getUserDetails(id) : void {
    const user = {
      name: 'u1'
    } as User;
    
    this.service.getUserDetails(id, user)
      .subscribe(result => this.user = result);
  }

}
