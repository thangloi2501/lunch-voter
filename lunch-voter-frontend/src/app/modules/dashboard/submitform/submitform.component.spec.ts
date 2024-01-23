import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SubmitFormComponent } from './submitform.component';

describe('CreateFormComponent', () => {
  let component: SubmitFormComponent;
  let fixture: ComponentFixture<SubmitFormComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [SubmitFormComponent],
    });
    fixture = TestBed.createComponent(SubmitFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
