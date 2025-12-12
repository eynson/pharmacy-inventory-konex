import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { MessageService } from 'primeng/api';
import { SaleService, SaleRequest } from '../../../../shared/services/sale.service';
import { MedicineService, Medicine, PagedMedicineResponse } from '../../../../shared/services/medicine.service';

@Component({
  selector: 'app-sale-form',
  templateUrl: './sale-form.component.html',
  styleUrls: ['./sale-form.component.scss']
})
export class SaleFormComponent implements OnInit {

  saleForm!: FormGroup;
  loading: boolean = false;
  medicines: Medicine[] = [];
  selectedMedicine: Medicine | null = null;
  now: Date = new Date();

  constructor(
    private fb: FormBuilder,
    private saleService: SaleService,
    private medicineService: MedicineService,
    private messageService: MessageService,
    private router: Router,
    private route: ActivatedRoute
  ) {
    this.initForm();
  }

  ngOnInit(): void {
    this.loadMedicines();
  }

  private loadMedicines(): void {
    this.medicineService.getMedicines(0, 1000).subscribe({
      next: (response: PagedMedicineResponse) => {
        this.medicines = response.content.filter(m => m.quantityInStock > 0);
        this.tryPreselectMedicine();
      },
      error: (error) => {
        this.messageService.add({ severity: 'error', summary: 'Error', detail: 'Error al cargar medicamentos' });
      }
    });
  }

  private tryPreselectMedicine(): void {
    const medicineStr = sessionStorage.getItem('medicineToSell');
    
    if (medicineStr) {
      try {
        const medicine = JSON.parse(medicineStr) as Medicine;
        const medicineId = medicine.id;
        
        const medicineInList = this.medicines.find(m => m.id === medicineId);
        if (!medicineInList) {
          this.medicines.push(medicine);
        }
        
        setTimeout(() => {
          this.saleForm.get('medicineId')?.setValue(medicineId);
          this.selectedMedicine = medicine;
        }, 100);
        
        sessionStorage.removeItem('medicineToSell');
      } catch (error) {
      }
    }
  }

  initForm(): void {
    this.saleForm = this.fb.group({
      medicineId: ['', Validators.required],
      quantitySold: [null]
    });
  }

  onMedicineChange(): void {
    const medicineId = this.saleForm.get('medicineId')?.value;
    this.selectedMedicine = this.medicines.find(m => m.id === medicineId) || null;
  }

  validateQuantity(): void {
    const quantity = this.saleForm.get('quantitySold')?.value;
    if (this.selectedMedicine && quantity > this.selectedMedicine.quantityInStock) {
      this.messageService.add({
        severity: 'error',
        summary: 'Cantidad insuficiente',
        detail: `Solo hay ${this.selectedMedicine.quantityInStock} unidades disponibles`
      });
      this.saleForm.get('quantitySold')?.reset();
    }
  }

  onSubmit(): void {
    if (this.saleForm.invalid || !this.selectedMedicine) {
      this.messageService.add({ severity: 'warn', summary: 'Validación', detail: 'Verifique los campos requeridos' });
      return;
    }

    const quantity = this.saleForm.get('quantitySold')?.value;
    if (quantity > this.selectedMedicine.quantityInStock) {
      this.messageService.add({
        severity: 'error',
        summary: 'Cantidad insuficiente',
        detail: `Solo hay ${this.selectedMedicine.quantityInStock} unidades disponibles`
      });
      return;
    }

    this.loading = true;
    const sale: SaleRequest = {
      medicineId: this.saleForm.get('medicineId')?.value,
      quantitySold: quantity
    };

    this.saleService.createSale(sale).subscribe({
      next: () => {
        this.messageService.add({ severity: 'success', summary: 'Éxito', detail: 'Venta registrada correctamente' });
        this.router.navigate(['/sales']);
        this.loading = false;
      },
      error: (error) => {
        this.messageService.add({ severity: 'error', summary: 'Error', detail: 'Error al registrar venta' });
        this.loading = false;
      }
    });
  }

  onCancel(): void {
    this.router.navigate(['/sales']);
  }
}
