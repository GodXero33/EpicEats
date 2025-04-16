import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UpdateEmployeeShiftComponent } from './update-employee-shift.component';

describe('UpdateEmployeeShiftComponent', () => {
  let component: UpdateEmployeeShiftComponent;
  let fixture: ComponentFixture<UpdateEmployeeShiftComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [UpdateEmployeeShiftComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(UpdateEmployeeShiftComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
