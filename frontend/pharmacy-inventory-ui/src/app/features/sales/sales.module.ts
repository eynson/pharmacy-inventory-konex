import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormsModule } from '@angular/forms';
import { PrimeNGModule } from '../../shared/modules/primeng.module';

import { SalesRoutingModule } from './sales-routing.module';
import { SaleListComponent } from './pages/sale-list/sale-list.component';
import { SaleFormComponent } from './pages/sale-form/sale-form.component';
import { SaleDetailComponent } from './pages/sale-detail/sale-detail.component';

@NgModule({
  declarations: [
    SaleListComponent,
    SaleFormComponent,
    SaleDetailComponent
  ],
  imports: [
    CommonModule,
    ReactiveFormsModule,
    FormsModule,
    PrimeNGModule,
    SalesRoutingModule
  ]
})
export class SalesModule { }
