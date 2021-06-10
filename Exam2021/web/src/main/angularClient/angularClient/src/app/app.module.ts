import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { HttpClientModule } from '@angular/common/http';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { UserHelloComponent } from './user-hello/user-hello.component';
import { UserFollowersComponent } from './user-followers/user-followers.component';
import { UserPostsFollowersComponent } from './user-posts-followers/user-posts-followers.component';

@NgModule({
  declarations: [
    AppComponent,
    UserHelloComponent,
    UserFollowersComponent,
    UserPostsFollowersComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
