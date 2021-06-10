import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Follower } from '../interfaces/follower';
import { User } from '../interfaces/user';
import { AppUserService } from '../services/app-user.service';

@Component({
  selector: 'app-user-followers',
  templateUrl: './user-followers.component.html',
  styleUrls: ['./user-followers.component.css']
})
export class UserFollowersComponent implements OnInit {
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
    this.service.getUserFollowers(id)
      .subscribe(result => this.user = result);
  }

  private getUserDetails(id) : void {
    const follower = {
      name: 'f1'
    } as Follower;

    const user = {
      name: 'u1',
      followers: [follower]
    } as User;
    
    this.service.getUserDetails(id, user)
      .subscribe(result => this.user = result);
  }

}
