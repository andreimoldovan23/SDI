import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
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
    this.route.params.subscribe(params => this.getUser(parseInt(params['id'])))
  }

  private getUser(id) : void {
    this.service.getUserFollowers(id)
      .subscribe(result => this.user = result);
  }

}
