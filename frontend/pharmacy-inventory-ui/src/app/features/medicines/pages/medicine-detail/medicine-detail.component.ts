import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { MessageService } from 'primeng/api';
import { MedicineService, Medicine } from '../../../../shared/services/medicine.service';

@Component({
  selector: 'app-medicine-detail',
  templateUrl: './medicine-detail.component.html',
  styleUrls: ['./medicine-detail.component.scss']
})
export class MedicineDetailComponent implements OnInit {

  medicine: Medicine | null = null;
  loading: boolean = false;

  constructor(
    private medicineService: MedicineService,
    private messageService: MessageService,
    private router: Router,
    private route: ActivatedRoute
  ) { }

  ngOnInit(): void {
    this.route.params.subscribe((params: any) => {
      if (params['id']) {
        this.loadMedicine(params['id']);
      }
    });
  }

  loadMedicine(id: string): void {
    this.loading = true;
    this.medicineService.getMedicineById(id).subscribe({
      next: (medicine: Medicine) => {
        this.medicine = medicine;
        this.loading = false;
      },
      error: (error) => {
        this.messageService.add({ severity: 'error', summary: 'Error', detail: 'Error al cargar medicamento' });
        this.loading = false;
      }
    });
  }

  goBack(): void {
    this.router.navigate(['/medicines']);
  }

  edit(): void {
    if (this.medicine) {
      this.router.navigate(['/medicines', this.medicine.id, 'edit']);
    }
  }
}
