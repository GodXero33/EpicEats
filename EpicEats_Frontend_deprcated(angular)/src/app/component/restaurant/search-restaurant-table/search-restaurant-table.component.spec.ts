import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SearchRestaurantTableComponent } from './search-restaurant-table.component';

describe('SearchRestaurantTableComponent', () => {
  let component: SearchRestaurantTableComponent;
  let fixture: ComponentFixture<SearchRestaurantTableComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SearchRestaurantTableComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(SearchRestaurantTableComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
