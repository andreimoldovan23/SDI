import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SeeCoffeesDialogComponent } from './see-coffees-dialog.component';

describe('SeeCoffeesDialogComponent', () => {
  let component: SeeCoffeesDialogComponent;
  let fixture: ComponentFixture<SeeCoffeesDialogComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ SeeCoffeesDialogComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(SeeCoffeesDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
