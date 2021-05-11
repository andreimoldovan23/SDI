import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ChooseClientDialogComponent } from './choose-client-dialog.component';

describe('ChooseClientDialogComponent', () => {
  let component: ChooseClientDialogComponent;
  let fixture: ComponentFixture<ChooseClientDialogComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ChooseClientDialogComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ChooseClientDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
