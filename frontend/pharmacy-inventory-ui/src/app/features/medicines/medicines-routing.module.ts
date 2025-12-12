import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { MedicineListComponent } from './pages/medicine-list/medicine-list.component';
import { MedicineFormComponent } from './pages/medicine-form/medicine-form.component';
import { MedicineDetailComponent } from './pages/medicine-detail/medicine-detail.component';

const routes: Routes = [
  {
    path: '',
    children: [
      { path: '', component: MedicineListComponent },
      { path: 'new', component: MedicineFormComponent },
      { path: ':id/edit', component: MedicineFormComponent },
      { path: ':id', component: MedicineDetailComponent }
    ]
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class MedicinesRoutingModule { }
