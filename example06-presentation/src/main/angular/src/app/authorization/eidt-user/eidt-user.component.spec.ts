import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EidtUserComponent } from './eidt-user.component';

describe('EidtUserComponent', () => {
  let component: EidtUserComponent;
  let fixture: ComponentFixture<EidtUserComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ EidtUserComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(EidtUserComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
