import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AddRestaurantTableComponent } from './add-restaurant-table.component';

describe('AddRestaurantTableComponent', () => {
  let component: AddRestaurantTableComponent;
  let fixture: ComponentFixture<AddRestaurantTableComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AddRestaurantTableComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AddRestaurantTableComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
