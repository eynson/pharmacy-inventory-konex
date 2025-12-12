import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { MessageService } from 'primeng/api';
import { MedicineService, Medicine, MedicineRequest } from '../../../../shared/services/medicine.service';

@Component({
  selector: 'app-medicine-form',
  templateUrl: './medicine-form.component.html',
  styleUrls: ['./medicine-form.component.scss']
})
export class MedicineFormComponent implements OnInit {

  medicineForm!: FormGroup;
  loading: boolean = false;
  isEditing: boolean = false;
  medicineId: string | null = null;

  constructor(
    private fb: FormBuilder,
    private medicineService: MedicineService,
    private messageService: MessageService,
    private router: Router,
    private route: ActivatedRoute
  ) {
    this.initForm();
  }

  ngOnInit(): void {
    console.log('MedicineFormComponent inicializado');
    this.route.params.subscribe((params: any) => {
      if (params['id'] && params['id'] !== 'new') {
        this.isEditing = true;
        this.medicineId = params['id'];
        if (this.medicineId) {
          this.loadMedicine(this.medicineId);
        }
      }
    });
  }

  initForm(): void {
    this.medicineForm = this.fb.group({
      name: ['', [Validators.required, Validators.minLength(3)]],
      factoryLaboratory: ['', [Validators.required, Validators.minLength(3)]],
      manufacturingDate: [null],
      expirationDate: [null],
      quantityInStock: [null],
      unitValue: [null]
    });
  }

  loadMedicine(id: string): void {
    this.loading = true;
    this.medicineService.getMedicineById(id).subscribe({
      next: (medicine: Medicine) => {
        this.medicineForm.patchValue({
          name: medicine.name,
          factoryLaboratory: medicine.factoryLaboratory,
          manufacturingDate: new Date(medicine.manufacturingDate),
          expirationDate: new Date(medicine.expirationDate),
          quantityInStock: medicine.quantityInStock,
          unitValue: medicine.unitValue
        });
        this.loading = false;
      },
      error: (error) => {
        this.messageService.add({ severity: 'error', summary: 'Error', detail: 'Error al cargar medicamento' });
        this.loading = false;
      }
    });
  }

  onSubmit(): void {
    if (this.medicineForm.invalid) {
      console.log('Formulario inválido:', this.medicineForm.errors);
      this.messageService.add({ severity: 'warn', summary: 'Validación', detail: 'Verifique los campos requeridos' });
      return;
    }

    this.loading = true;
    const formValue = this.medicineForm.value;
    const medicine: MedicineRequest = {
      name: formValue.name,
      factoryLaboratory: formValue.factoryLaboratory,
      manufacturingDate: this.formatDate(formValue.manufacturingDate),
      expirationDate: this.formatDate(formValue.expirationDate),
      quantityInStock: formValue.quantityInStock,
      unitValue: formValue.unitValue
    };

    console.log('Enviando medicina:', medicine);

    if (this.isEditing && this.medicineId) {
      this.medicineService.updateMedicine(this.medicineId, medicine).subscribe({
        next: () => {
          this.messageService.add({ severity: 'success', summary: 'Éxito', detail: 'Medicamento actualizado' });
          this.router.navigate(['/medicines']);
          this.loading = false;
        },
        error: (error) => {
          console.error('Error al actualizar:', error);
          this.messageService.add({ severity: 'error', summary: 'Error', detail: error.error?.message || 'Error al actualizar medicamento' });
          this.loading = false;
        }
      });
    } else {
      this.medicineService.createMedicine(medicine).subscribe({
        next: () => {
          console.log('Medicamento creado exitosamente');
          this.messageService.add({ severity: 'success', summary: 'Éxito', detail: 'Medicamento creado' });
          this.router.navigate(['/medicines']);
          this.loading = false;
        },
        error: (error) => {
          console.error('Error al crear:', error);
          this.messageService.add({ severity: 'error', summary: 'Error', detail: error.error?.message || 'Error al crear medicamento' });
          this.loading = false;
        }
      });
    }
  }

  onCancel(): void {
    this.router.navigate(['/medicines']);
  }

  private formatDate(date: Date | null): string | null {
    if (!date) return null;
    const year = date.getFullYear();
    const month = String(date.getMonth() + 1).padStart(2, '0');
    const day = String(date.getDate()).padStart(2, '0');
    return `${year}-${month}-${day}`;
  }
}
