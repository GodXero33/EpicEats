import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UpdateRestaurantTableBookingComponent } from './update-restaurant-table-booking.component';

describe('UpdateRestaurantTableBookingComponent', () => {
  let component: UpdateRestaurantTableBookingComponent;
  let fixture: ComponentFixture<UpdateRestaurantTableBookingComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [UpdateRestaurantTableBookingComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(UpdateRestaurantTableBookingComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
