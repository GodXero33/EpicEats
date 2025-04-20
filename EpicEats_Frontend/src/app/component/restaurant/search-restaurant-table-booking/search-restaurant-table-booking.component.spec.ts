import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SearchRestaurantTableBookingComponent } from './search-restaurant-table-booking.component';

describe('SearchRestaurantTableBookingComponent', () => {
  let component: SearchRestaurantTableBookingComponent;
  let fixture: ComponentFixture<SearchRestaurantTableBookingComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SearchRestaurantTableBookingComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(SearchRestaurantTableBookingComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
