import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { CommonModule } from '@angular/common';

import { DashboardRoutingModule } from './dashboard-routing.module';
import { CreateFormComponent } from './createform/createform.component';
import { DashboardComponent } from './dashboard.component';

import { ReactiveFormsModule } from '@angular/forms';

import { JoinFormComponent } from './joinform/joinform.component';

@NgModule({
  declarations: [DashboardComponent, CreateFormComponent, JoinFormComponent],
  imports: [
    CommonModule,
    DashboardRoutingModule,
    ReactiveFormsModule
  ],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class DashboardModule { }