import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DeleteEmployeePromotionComponent } from './delete-employee-promotion.component';

describe('DeleteEmployeePromotionComponent', () => {
  let component: DeleteEmployeePromotionComponent;
  let fixture: ComponentFixture<DeleteEmployeePromotionComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [DeleteEmployeePromotionComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(DeleteEmployeePromotionComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
