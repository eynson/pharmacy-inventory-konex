import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule, FormsModule } from '@angular/forms';
import { RouterTestingModule } from '@angular/router/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { MessageService } from 'primeng/api';
import { SaleFormComponent } from './sale-form.component';
import { SaleService, SaleRequest, Sale } from '../../../../shared/services/sale.service';
import { MedicineService, Medicine, PagedMedicineResponse } from '../../../../shared/services/medicine.service';
import { of, throwError } from 'rxjs';
import { ActivatedRoute } from '@angular/router';
import { ToastModule } from 'primeng/toast';
import { CardModule } from 'primeng/card';
import { ButtonModule } from 'primeng/button';
import { DropdownModule } from 'primeng/dropdown';
import { InputNumberModule } from 'primeng/inputnumber';
import { CommonModule } from '@angular/common';

describe('SaleFormComponent', () => {
  let component: SaleFormComponent;
  let fixture: ComponentFixture<SaleFormComponent>;
  let saleService: jasmine.SpyObj<SaleService>;
  let medicineService: jasmine.SpyObj<MedicineService>;
  let messageService: MessageService;

  const mockMedicines: Medicine[] = [
    {
      id: 'med-001',
      name: 'Aspirin',
      factoryLaboratory: 'Bayer',
      manufacturingDate: '2024-01-01',
      expirationDate: '2026-01-01',
      quantityInStock: 100,
      unitValue: 5.50,
      expirationStatus: 'VALID'
    },
    {
      id: 'med-002',
      name: 'Ibuprofen',
      factoryLaboratory: 'Generic',
      manufacturingDate: '2024-01-01',
      expirationDate: '2026-01-01',
      quantityInStock: 200,
      unitValue: 3.75,
      expirationStatus: 'VALID'
    }
  ];

  const mockPagedMedicineResponse: PagedMedicineResponse = {
    content: mockMedicines,
    currentPage: 0,
    pageSize: 1000,
    totalPages: 1,
    totalElements: 2
  };

  beforeEach(async () => {
    const saleServiceSpy = jasmine.createSpyObj('SaleService', ['createSale']);
    const medicineServiceSpy = jasmine.createSpyObj('MedicineService', ['getMedicines']);

    await TestBed.configureTestingModule({
      declarations: [SaleFormComponent],
      imports: [
        ReactiveFormsModule,
        FormsModule,
        RouterTestingModule,
        HttpClientTestingModule,
        CommonModule,
        ToastModule,
        CardModule,
        ButtonModule,
        DropdownModule,
        InputNumberModule
      ],
      providers: [
        { provide: SaleService, useValue: saleServiceSpy },
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

    saleService = TestBed.inject(SaleService) as jasmine.SpyObj<SaleService>;
    medicineService = TestBed.inject(MedicineService) as jasmine.SpyObj<MedicineService>;
    messageService = TestBed.inject(MessageService);
    spyOn(messageService, 'add');

    medicineService.getMedicines.and.returnValue(of(mockPagedMedicineResponse));

    fixture = TestBed.createComponent(SaleFormComponent);
    component = fixture.componentInstance;
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  describe('initForm', () => {
    it('should initialize form with required fields', () => {
      component.initForm();

      expect(component.saleForm).toBeDefined();
      expect(component.saleForm.get('medicineId')?.value).toBe('');
      expect(component.saleForm.get('quantitySold')?.value).toBe(null);
    });

    it('should set medicineId as required', () => {
      component.initForm();

      const medicineIdControl = component.saleForm.get('medicineId');
      medicineIdControl?.setValue('');
      expect(medicineIdControl?.hasError('required')).toBe(true);
    });

    it('should set quantitySold as required', () => {
      component.initForm();

      const medicineIdControl = component.saleForm.get('medicineId');
      medicineIdControl?.setValue('');
      medicineIdControl?.markAsTouched();
      expect(medicineIdControl?.hasError('required')).toBe(true);
      
      medicineIdControl?.setValue('med-001');
      expect(medicineIdControl?.hasError('required')).toBe(false);
    });
  });

  describe('ngOnInit', () => {
    it('should load medicines on init', (done) => {
      component.ngOnInit();

      setTimeout(() => {
        expect(medicineService.getMedicines).toHaveBeenCalledWith(0, 1000);
        expect(component.medicines.length).toBe(2);
        done();
      }, 0);
    });

    it('should filter medicines with inventory available', (done) => {
      const medicinesWithoutStock = [
        ...mockMedicines,
        {
          id: 'med-003',
          name: 'Out of Stock',
          factoryLaboratory: 'Lab',
          manufacturingDate: '2024-01-01',
          expirationDate: '2026-01-01',
          quantityInStock: 0,
          unitValue: 10.00,
          expirationStatus: 'VALID'
        }
      ];

      medicineService.getMedicines.and.returnValue(of({
        ...mockPagedMedicineResponse,
        content: medicinesWithoutStock,
        totalElements: 3
      }));

      component.ngOnInit();

      setTimeout(() => {
        expect(component.medicines.length).toBe(2);
        expect(component.medicines.every(m => m.quantityInStock > 0)).toBe(true);
        done();
      }, 0);
    });

    it('should handle error when loading medicines fails', (done) => {
      medicineService.getMedicines.and.returnValue(throwError(() => new Error('Load failed')));

      component.ngOnInit();

      setTimeout(() => {
        expect(messageService.add).toHaveBeenCalledWith(
          jasmine.objectContaining({
            severity: 'error',
            summary: 'Error',
            detail: 'Error al cargar medicamentos'
          })
        );
        done();
      }, 0);
    });
  });

  describe('onSubmit', () => {
    beforeEach(() => {
      component.initForm();
    });

    it('should not submit invalid form', () => {
      component.saleForm.patchValue({
        medicineId: '', // Empty - invalid
        quantitySold: null
      });

      component.onSubmit();

      expect(messageService.add).toHaveBeenCalledWith(
        jasmine.objectContaining({
          severity: 'warn',
          summary: 'Validación',
          detail: 'Verifique los campos requeridos'
        })
      );
      expect(saleService.createSale).not.toHaveBeenCalled();
    });

    it('should create a new sale with valid data', (done) => {
      const mockSale: Sale = {
        id: 'sale-001',
        medicineId: 'med-001',
        medicineName: 'Aspirin',
        quantitySold: 50,
        unitValue: 5.50,
        totalValue: 275.00,
        saleDate: '2024-11-15'
      };

      saleService.createSale.and.returnValue(of(mockSale));

      component.saleForm.patchValue({
        medicineId: 'med-001',
        quantitySold: 50
      });
      component.selectedMedicine = mockMedicines[0];

      component.onSubmit();

      setTimeout(() => {
        expect(saleService.createSale).toHaveBeenCalledWith({
          medicineId: 'med-001',
          quantitySold: 50
        });
        expect(messageService.add).toHaveBeenCalledWith(
          jasmine.objectContaining({
            severity: 'success',
            summary: 'Éxito',
            detail: 'Venta registrada correctamente'
          })
        );
        done();
      }, 0);
    });

    it('should validate quantity does not exceed available stock', () => {
      component.saleForm.patchValue({
        medicineId: 'med-001',
        quantitySold: 500 // More than available (100)
      });

      component.selectedMedicine = mockMedicines[0];
      component.onSubmit();

      expect(messageService.add).toHaveBeenCalledWith(
        jasmine.objectContaining({
          severity: 'error',
          summary: 'Cantidad insuficiente',
          detail: 'Solo hay 100 unidades disponibles'
        })
      );
      expect(saleService.createSale).not.toHaveBeenCalled();
    });

    it('should handle submission error', (done) => {
      saleService.createSale.and.returnValue(throwError(() => new Error('Create failed')));

      component.saleForm.patchValue({
        medicineId: 'med-001',
        quantitySold: 50
      });      component.selectedMedicine = mockMedicines[0];
      component.onSubmit();

      setTimeout(() => {
        expect(messageService.add).toHaveBeenCalledWith(
          jasmine.objectContaining({
            severity: 'error',
            summary: 'Error',
            detail: 'Error al registrar venta'
          })
        );
        done();
      }, 0);
    });
  });

  describe('onCancel', () => {
    it('should navigate back to sales list', () => {
      spyOn(component['router'], 'navigate');

      component.onCancel();

      expect(component['router'].navigate).toHaveBeenCalledWith(['/sales']);
    });
  });
});
