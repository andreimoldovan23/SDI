import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Follower } from '../interfaces/follower';
import { Post } from '../interfaces/post';
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
    let routeString = this.route.toString();
    if (routeString.indexOf('v2') > -1) {
      this.route.params.subscribe(params => this.getUserDetails(parseInt(params['id'])));
    } else {
      this.route.params.subscribe(params => this.getUser(parseInt(params['id'])))
    }
  }

  private getUser(id) : void {
    this.service.getUserFollowersPosts(id)
      .subscribe(result => this.user = result);
  }

  private getUserDetails(id) : void {
    const follower = {
      name: 'f1'
    } as Follower;

    const post = {
      title: 't1'
    } as Post;

    const user = {
      name: 'u1',
      followers: [follower],
      posts: [post]
    } as User;
    
    this.service.getUserDetails(id, user)
      .subscribe(result => this.user = result);
  }

}
