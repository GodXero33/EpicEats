import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DeleteRestaurantTableComponent } from './delete-restaurant-table.component';

describe('DeleteRestaurantTableComponent', () => {
  let component: DeleteRestaurantTableComponent;
  let fixture: ComponentFixture<DeleteRestaurantTableComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [DeleteRestaurantTableComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(DeleteRestaurantTableComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
