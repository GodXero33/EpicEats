import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AddRestaurantTableBookingComponent } from './add-restaurant-table-booking.component';

describe('AddRestaurantTableBookingComponent', () => {
  let component: AddRestaurantTableBookingComponent;
  let fixture: ComponentFixture<AddRestaurantTableBookingComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AddRestaurantTableBookingComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AddRestaurantTableBookingComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
