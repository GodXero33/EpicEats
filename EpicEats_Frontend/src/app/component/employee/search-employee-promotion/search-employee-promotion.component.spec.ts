import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SearchEmployeePromotionComponent } from './search-employee-promotion.component';

describe('SearchEmployeePromotionComponent', () => {
  let component: SearchEmployeePromotionComponent;
  let fixture: ComponentFixture<SearchEmployeePromotionComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SearchEmployeePromotionComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(SearchEmployeePromotionComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
