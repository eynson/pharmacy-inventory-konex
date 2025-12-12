import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { MessageService } from 'primeng/api';
import { SaleService, Sale, PagedSaleResponse } from '../../../../shared/services/sale.service';

@Component({
  selector: 'app-sale-list',
  templateUrl: './sale-list.component.html',
  styleUrls: ['./sale-list.component.scss']
})
export class SaleListComponent implements OnInit {

  sales: Sale[] = [];
  totalRecords: number = 0;
  loading: boolean = false;
  currentPage: number = 0;
  pageSize: number = 10;
  startDate: Date | null = null;
  endDate: Date | null = null;

  constructor(
    private saleService: SaleService,
    private router: Router,
    private messageService: MessageService
  ) { }

  ngOnInit(): void {
    const today = new Date();
    this.endDate = today;
    this.startDate = new Date(today.getFullYear(), today.getMonth(), 1);
    this.loadSales();
  }

  loadSales(): void {
    if (!this.startDate || !this.endDate) {
      this.messageService.add({ severity: 'warn', summary: 'Validación', detail: 'Seleccione rango de fechas' });
      return;
    }

    this.loading = true;
    const startDateStr = this.formatDate(this.startDate);
    const endDateStr = this.formatDate(this.endDate);

    this.saleService.getSalesByDateRange(startDateStr, endDateStr, this.currentPage, this.pageSize)
      .subscribe({
        next: (response: PagedSaleResponse) => {
          this.sales = response.content;
          this.totalRecords = response.totalElements;
          this.loading = false;
        },
        error: (error) => {
          this.messageService.add({ severity: 'error', summary: 'Error', detail: 'Error al cargar ventas' });
          this.loading = false;
        }
      });
  }

  onPageChange(event: any): void {
    this.currentPage = event.first / event.rows;
    this.pageSize = event.rows;
    this.loadSales();
  }

  viewSale(id: string): void {
    this.router.navigate(['/sales', id]);
  }

  createSale(): void {
    this.router.navigate(['/sales', 'new']);
  }

  getTotalSales(): number {
    return this.sales.reduce((total, sale) => total + sale.totalValue, 0);
  }

  private formatDate(date: Date): string {
    const year = date.getFullYear();
    const month = String(date.getMonth() + 1).padStart(2, '0');
    const day = String(date.getDate()).padStart(2, '0');
    return `${year}-${month}-${day}`;
  }
}
