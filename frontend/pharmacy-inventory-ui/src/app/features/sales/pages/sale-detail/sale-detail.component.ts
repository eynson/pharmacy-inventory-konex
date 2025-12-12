import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { MessageService } from 'primeng/api';
import { SaleService, Sale } from '../../../../shared/services/sale.service';

@Component({
  selector: 'app-sale-detail',
  templateUrl: './sale-detail.component.html',
  styleUrls: ['./sale-detail.component.scss']
})
export class SaleDetailComponent implements OnInit {

  sale: Sale | null = null;
  loading: boolean = false;

  constructor(
    private saleService: SaleService,
    private router: Router,
    private route: ActivatedRoute,
    private messageService: MessageService
  ) { }

  ngOnInit(): void {
    this.route.params.subscribe(params => {
      if (params['id']) {
        this.loadSale(params['id']);
      }
    });
  }

  loadSale(id: string): void {
    this.loading = true;
    this.saleService.getSaleById(id).subscribe({
      next: (sale: Sale) => {
        this.sale = sale;
        this.loading = false;
      },
      error: (error) => {
        this.messageService.add({ severity: 'error', summary: 'Error', detail: 'Error al cargar los detalles de la venta' });
        this.loading = false;
      }
    });
  }

  goBack(): void {
    this.router.navigate(['/sales']);
  }
}
