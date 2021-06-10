import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UserPostsFollowersComponent } from './user-posts-followers.component';

describe('UserPostsFollowersComponent', () => {
  let component: UserPostsFollowersComponent;
  let fixture: ComponentFixture<UserPostsFollowersComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ UserPostsFollowersComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(UserPostsFollowersComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
