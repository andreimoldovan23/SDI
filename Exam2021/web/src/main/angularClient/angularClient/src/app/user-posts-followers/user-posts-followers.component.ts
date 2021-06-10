import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { User } from '../interfaces/user';
import { AppUserService } from '../services/app-user.service';

@Component({
  selector: 'app-user-posts-followers',
  templateUrl: './user-posts-followers.component.html',
  styleUrls: ['./user-posts-followers.component.css']
})
export class UserPostsFollowersComponent implements OnInit {
  user: User;

  constructor(private service: AppUserService, private route: ActivatedRoute) { }

  ngOnInit(): void {
    this.route.params.subscribe(params => this.getUser(parseInt(params['id'])))
  }

  private getUser(id) : void {
    this.service.getUserFollowersPosts(id)
      .subscribe(result => this.user = result);
  }

}
