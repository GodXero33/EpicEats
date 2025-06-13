import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UpdateEmployeePromotionComponent } from './update-employee-promotion.component';

describe('UpdateEmployeePromotionComponent', () => {
  let component: UpdateEmployeePromotionComponent;
  let fixture: ComponentFixture<UpdateEmployeePromotionComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [UpdateEmployeePromotionComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(UpdateEmployeePromotionComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
