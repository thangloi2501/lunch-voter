import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { CommonModule } from '@angular/common';
import { DashboardRoutingModule } from './dashboard-routing.module';
import { CreateFormComponent } from './createform/createform.component';
import { DashboardComponent } from './dashboard.component';
import { ReactiveFormsModule } from '@angular/forms';
import { JoinFormComponent } from './joinform/joinform.component';
import { SubmitFormComponent } from './submitform/submitform.component';
import { LiveboardComponent } from './liveboard/liveboard.component';
import { VoteInfoComponent } from './liveboard/voteinfo.component';
import { ToastrModule } from 'ngx-toastr';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';


@NgModule({
  declarations: [
    DashboardComponent,
    CreateFormComponent,
    JoinFormComponent,
    SubmitFormComponent,
    LiveboardComponent,
    VoteInfoComponent
  ],
  imports: [
    CommonModule,
    DashboardRoutingModule,
    ReactiveFormsModule,
    ToastrModule.forRoot(),
    BrowserAnimationsModule
  ],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class DashboardModule { }
