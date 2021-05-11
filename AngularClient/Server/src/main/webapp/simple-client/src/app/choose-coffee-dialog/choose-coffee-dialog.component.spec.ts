import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ChooseCoffeeDialogComponent } from './choose-coffee-dialog.component';

describe('ChooseCoffeeDialogComponent', () => {
  let component: ChooseCoffeeDialogComponent;
  let fixture: ComponentFixture<ChooseCoffeeDialogComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ChooseCoffeeDialogComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ChooseCoffeeDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
