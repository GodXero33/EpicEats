import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UpdateRestaurantTableComponent } from './update-restaurant-table.component';

describe('UpdateRestaurantTableComponent', () => {
  let component: UpdateRestaurantTableComponent;
  let fixture: ComponentFixture<UpdateRestaurantTableComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [UpdateRestaurantTableComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(UpdateRestaurantTableComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
