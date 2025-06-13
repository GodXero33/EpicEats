import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DeleteRestaurantTableBookingComponent } from './delete-restaurant-table-booking.component';

describe('DeleteRestaurantTableBookingComponent', () => {
  let component: DeleteRestaurantTableBookingComponent;
  let fixture: ComponentFixture<DeleteRestaurantTableBookingComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [DeleteRestaurantTableBookingComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(DeleteRestaurantTableBookingComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
