import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SearchEmployeeShiftComponent } from './search-employee-shift.component';

describe('SearchEmployeeShiftComponent', () => {
  let component: SearchEmployeeShiftComponent;
  let fixture: ComponentFixture<SearchEmployeeShiftComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SearchEmployeeShiftComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(SearchEmployeeShiftComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
