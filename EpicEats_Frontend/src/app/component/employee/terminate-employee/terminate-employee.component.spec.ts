import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TerminateEmployeeComponent } from './terminate-employee.component';

describe('TerminateEmployeeComponent', () => {
  let component: TerminateEmployeeComponent;
  let fixture: ComponentFixture<TerminateEmployeeComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TerminateEmployeeComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(TerminateEmployeeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
