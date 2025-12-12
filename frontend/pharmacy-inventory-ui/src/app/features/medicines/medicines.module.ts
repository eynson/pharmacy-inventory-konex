import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormsModule } from '@angular/forms';
import { PrimeNGModule } from '../../shared/modules/primeng.module';

import { MedicinesRoutingModule } from './medicines-routing.module';
import { MedicineListComponent } from './pages/medicine-list/medicine-list.component';
import { MedicineFormComponent } from './pages/medicine-form/medicine-form.component';
import { MedicineDetailComponent } from './pages/medicine-detail/medicine-detail.component';

@NgModule({
  declarations: [
    MedicineListComponent,
    MedicineFormComponent,
    MedicineDetailComponent
  ],
  imports: [
    CommonModule,
    ReactiveFormsModule,
    FormsModule,
    PrimeNGModule,
    MedicinesRoutingModule
  ]
})
export class MedicinesModule { }
