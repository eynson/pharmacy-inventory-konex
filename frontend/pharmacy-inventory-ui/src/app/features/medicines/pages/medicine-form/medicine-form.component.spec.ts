import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { RouterTestingModule } from '@angular/router/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { MessageService } from 'primeng/api';
import { MedicineFormComponent } from './medicine-form.component';
import { MedicineService, Medicine, MedicineRequest } from '../../../../shared/services/medicine.service';
import { of, throwError } from 'rxjs';
import { ActivatedRoute } from '@angular/router';
import { ToastModule } from 'primeng/toast';
import { CardModule } from 'primeng/card';
import { ButtonModule } from 'primeng/button';
import { InputTextModule } from 'primeng/inputtext';
import { CalendarModule } from 'primeng/calendar';
import { InputNumberModule } from 'primeng/inputnumber';
import { DividerModule } from 'primeng/divider';
import { CommonModule } from '@angular/common';

describe('MedicineFormComponent', () => {
  let component: MedicineFormComponent;
  let fixture: ComponentFixture<MedicineFormComponent>;
  let medicineService: jasmine.SpyObj<MedicineService>;
  let messageService: MessageService;

  const mockMedicine: Medicine = {
    id: 'med-001',
    name: 'Aspirin',
    factoryLaboratory: 'Bayer',
    manufacturingDate: '2024-01-01',
    expirationDate: '2026-01-01',
    quantityInStock: 100,
    unitValue: 5.50,
    expirationStatus: 'VALID'
  };

  beforeEach(async () => {
    const medicineServiceSpy = jasmine.createSpyObj('MedicineService', [
      'getMedicineById',
      'createMedicine',
      'updateMedicine'
    ]);

    await TestBed.configureTestingModule({
      declarations: [MedicineFormComponent],
      imports: [
        ReactiveFormsModule,
        RouterTestingModule,
        HttpClientTestingModule,
        CommonModule,
        ToastModule,
        CardModule,
        ButtonModule,
        InputTextModule,
        CalendarModule,
        InputNumberModule,
        DividerModule
      ],
      providers: [
        { provide: MedicineService, useValue: medicineServiceSpy },
        MessageService,
        {
          provide: ActivatedRoute,
          useValue: {
            params: of({})
          }
        }
      ]
    }).compileComponents();

    medicineService = TestBed.inject(MedicineService) as jasmine.SpyObj<MedicineService>;
    messageService = TestBed.inject(MessageService);
    spyOn(messageService, 'add');

    fixture = TestBed.createComponent(MedicineFormComponent);
    component = fixture.componentInstance;
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  describe('initForm', () => {
    it('should initialize form with empty values', () => {
      component.initForm();

      expect(component.medicineForm).toBeDefined();
      expect(component.medicineForm.get('name')?.value).toBe('');
      expect(component.medicineForm.get('factoryLaboratory')?.value).toBe('');
    });

    it('should set validators on form fields', () => {
      component.initForm();

      const nameControl = component.medicineForm.get('name');
      nameControl?.setValue('');
      nameControl?.markAsTouched();
      expect(nameControl?.hasError('required')).toBe(true);
      
      nameControl?.setValue('ab');
      expect(nameControl?.hasError('minlength')).toBe(true);
    });
  });

  describe('loadMedicine', () => {
    it('should load medicine and populate form', (done) => {
      medicineService.getMedicineById.and.returnValue(of(mockMedicine));

      component.loadMedicine('med-001');

      setTimeout(() => {
        expect(medicineService.getMedicineById).toHaveBeenCalledWith('med-001');
        expect(component.medicineForm.get('name')?.value).toBe('Aspirin');
        expect(component.medicineForm.get('quantityInStock')?.value).toBe(100);
        expect(component.loading).toBe(false);
        done();
      }, 0);
    });

    it('should handle error when loading medicine fails', (done) => {
      medicineService.getMedicineById.and.returnValue(throwError(() => new Error('Load failed')));

      component.loadMedicine('med-999');

      setTimeout(() => {
        expect(messageService.add).toHaveBeenCalledWith(
          jasmine.objectContaining({
            severity: 'error',
            summary: 'Error',
            detail: 'Error al cargar medicamento'
          })
        );
        expect(component.loading).toBe(false);
        done();
      }, 0);
    });
  });

  describe('onSubmit', () => {
    it('should not submit invalid form', () => {
      component.initForm();
      component.medicineForm.patchValue({
        name: '', // Empty - invalid
        factoryLaboratory: 'Bayer'
      });

      component.onSubmit();

      expect(messageService.add).toHaveBeenCalledWith(
        jasmine.objectContaining({
          severity: 'warn',
          summary: 'Validación',
          detail: 'Verifique los campos requeridos'
        })
      );
      expect(medicineService.createMedicine).not.toHaveBeenCalled();
      expect(medicineService.updateMedicine).not.toHaveBeenCalled();
    });

    it('should create new medicine when isEditing is false', (done) => {
      medicineService.createMedicine.and.returnValue(of(mockMedicine));
      component.initForm();
      component.isEditing = false;
      component.medicineForm.patchValue({
        name: 'New Medicine',
        factoryLaboratory: 'Lab1',
        manufacturingDate: new Date('2024-01-01'),
        expirationDate: new Date('2026-01-01'),
        quantityInStock: 50,
        unitValue: 10.50
      });

      component.onSubmit();

      setTimeout(() => {
        expect(medicineService.createMedicine).toHaveBeenCalled();
        expect(messageService.add).toHaveBeenCalledWith(
          jasmine.objectContaining({
            severity: 'success',
            summary: 'Éxito',
            detail: 'Medicamento creado'
          })
        );
        done();
      }, 0);
    });

    it('should update medicine when isEditing is true', (done) => {
      medicineService.updateMedicine.and.returnValue(of(mockMedicine));
      component.initForm();
      component.isEditing = true;
      component.medicineId = 'med-001';
      component.medicineForm.patchValue({
        name: 'Updated Aspirin',
        factoryLaboratory: 'Bayer',
        quantityInStock: 150
      });

      component.onSubmit();

      setTimeout(() => {
        expect(medicineService.updateMedicine).toHaveBeenCalledWith('med-001', jasmine.any(Object));
        expect(messageService.add).toHaveBeenCalledWith(
          jasmine.objectContaining({
            severity: 'success',
            summary: 'Éxito',
            detail: 'Medicamento actualizado'
          })
        );
        done();
      }, 0);
    });

    it('should handle submission error', (done) => {
      medicineService.createMedicine.and.returnValue(throwError(() => new Error('Create failed')));
      component.initForm();
      component.isEditing = false;
      component.medicineForm.patchValue({
        name: 'New Medicine',
        factoryLaboratory: 'Lab1'
      });

      component.onSubmit();

      setTimeout(() => {
        expect(messageService.add).toHaveBeenCalledWith(
          jasmine.objectContaining({
            severity: 'error',
            summary: 'Error',
            detail: 'Error al crear medicamento'
          })
        );
        done();
      }, 0);
    });
  });
});
