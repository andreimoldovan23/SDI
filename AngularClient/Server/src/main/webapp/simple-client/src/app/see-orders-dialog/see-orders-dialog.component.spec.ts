import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SeeOrdersDialogComponent } from './see-orders-dialog.component';

describe('SeeOrdersDialogComponent', () => {
  let component: SeeOrdersDialogComponent;
  let fixture: ComponentFixture<SeeOrdersDialogComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ SeeOrdersDialogComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(SeeOrdersDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
