import { Routes } from '@angular/router';

export const routes: Routes = [
  {
    path: 'medicines',
    loadChildren: () => import('./features/medicines/medicines.module').then(m => m.MedicinesModule)
  },
  {
    path: 'sales',
    loadChildren: () => import('./features/sales/sales.module').then(m => m.SalesModule)
  },
  {
    path: '',
    redirectTo: '/medicines',
    pathMatch: 'full'
  },
  {
    path: '**',
    redirectTo: '/medicines'
  }
];
