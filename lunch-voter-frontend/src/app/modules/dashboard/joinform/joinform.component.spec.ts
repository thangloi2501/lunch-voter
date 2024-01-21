import { ComponentFixture, TestBed } from '@angular/core/testing';

import { JoinFormComponent } from './joinform.component';

describe('JoinFormComponent', () => {
  let component: JoinFormComponent;
  let fixture: ComponentFixture<JoinFormComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [JoinFormComponent],
    });
    fixture = TestBed.createComponent(JoinFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
