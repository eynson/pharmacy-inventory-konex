import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { MessageService, ConfirmationService } from 'primeng/api';
import { MedicineService, Medicine, PagedMedicineResponse } from '../../../../shared/services/medicine.service';

@Component({
  selector: 'app-medicine-list',
  templateUrl: './medicine-list.component.html',
  styleUrls: ['./medicine-list.component.scss']
})
export class MedicineListComponent implements OnInit {

  medicines: Medicine[] = [];
  totalRecords: number = 0;
  loading: boolean = false;
  searchValue: string = '';
  currentPage: number = 0;
  pageSize: number = 10;

  constructor(
    private medicineService: MedicineService,
    private router: Router,
    private messageService: MessageService,
    private confirmationService: ConfirmationService
  ) { }

  ngOnInit(): void {
    this.loadMedicines();
  }

  loadMedicines(): void {
    this.loading = true;
    this.medicineService.getMedicines(this.currentPage, this.pageSize, this.searchValue || undefined)
      .subscribe({
        next: (response: PagedMedicineResponse) => {
          this.medicines = response.content;
          this.totalRecords = response.totalElements;
          this.loading = false;
        },
        error: (error) => {
          this.messageService.add({ severity: 'error', summary: 'Error', detail: 'Error al cargar medicamentos' });
          this.loading = false;
        }
      });
  }

  onSearch(): void {
    this.currentPage = 0;
    this.loadMedicines();
  }

  onPageChange(event: any): void {
    this.currentPage = event.first / event.rows;
    this.pageSize = event.rows;
    this.loadMedicines();
  }

  viewMedicine(id: string): void {
    this.router.navigate(['/medicines', id]);
  }

  editMedicine(id: string): void {
    this.router.navigate(['/medicines', id, 'edit']);
  }

  deleteMedicine(medicine: Medicine): void {
    this.confirmationService.confirm({
      message: `¿Está seguro de que desea eliminar ${medicine.name}?`,
      header: 'Confirmar',
      icon: 'pi pi-exclamation-triangle',
      accept: () => {
        this.medicineService.deleteMedicine(medicine.id).subscribe({
          next: () => {
            this.messageService.add({ severity: 'success', summary: 'Éxito', detail: 'Medicamento eliminado' });
            this.confirmationService.close();
            this.loadMedicines();
          },
          error: () => {
            this.messageService.add({ severity: 'error', summary: 'Error', detail: 'Error al eliminar medicamento' });
            this.confirmationService.close();
          }
        });
      }
    });
  }

  sellMedicine(medicine: Medicine): void {
    // Guardar el medicamento completo en sessionStorage para que persista a través de la navegación
    sessionStorage.setItem('medicineToSell', JSON.stringify(medicine));
    this.router.navigate(['/sales', 'new']);
  }

  createMedicine(): void {
    this.router.navigate(['/medicines', 'new']);
  }
}
