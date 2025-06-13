import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SearchEmployeeByIdComponent } from './search-employee-by-id.component';

describe('SearchEmployeeByIdComponent', () => {
  let component: SearchEmployeeByIdComponent;
  let fixture: ComponentFixture<SearchEmployeeByIdComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SearchEmployeeByIdComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(SearchEmployeeByIdComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
