import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SearchAllEmployeesComponent } from './search-all-employees.component';

describe('SearchAllEmployeesComponent', () => {
  let component: SearchAllEmployeesComponent;
  let fixture: ComponentFixture<SearchAllEmployeesComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SearchAllEmployeesComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(SearchAllEmployeesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
