import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { UserFollowersComponent } from './user-followers/user-followers.component';
import { UserHelloComponent } from './user-hello/user-hello.component';
import { UserPostsFollowersComponent } from './user-posts-followers/user-posts-followers.component';

const routes: Routes = [
  {path: "blog/user-hello/:id", component: UserHelloComponent},
  {path: "blog/user-followers/:id", component: UserFollowersComponent},
  {path: "blog/user-posts-followers/:id", component: UserPostsFollowersComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
