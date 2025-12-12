import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { SaleListComponent } from './pages/sale-list/sale-list.component';
import { SaleFormComponent } from './pages/sale-form/sale-form.component';
import { SaleDetailComponent } from './pages/sale-detail/sale-detail.component';

const routes: Routes = [
  {
    path: '',
    children: [
      { path: '', component: SaleListComponent },
      { path: 'new', component: SaleFormComponent },
      { path: ':id', component: SaleDetailComponent }
    ]
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class SalesRoutingModule { }
