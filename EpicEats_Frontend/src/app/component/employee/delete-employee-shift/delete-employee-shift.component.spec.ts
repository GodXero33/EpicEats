import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DeleteEmployeeShiftComponent } from './delete-employee-shift.component';

describe('DeleteEmployeeShiftComponent', () => {
  let component: DeleteEmployeeShiftComponent;
  let fixture: ComponentFixture<DeleteEmployeeShiftComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [DeleteEmployeeShiftComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(DeleteEmployeeShiftComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
