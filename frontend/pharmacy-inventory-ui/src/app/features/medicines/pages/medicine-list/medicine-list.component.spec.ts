import { ComponentFixture, TestBed } from '@angular/core/testing';
import { RouterTestingModule } from '@angular/router/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { MessageService, ConfirmationService } from 'primeng/api';
import { MedicineListComponent } from './medicine-list.component';
import { MedicineService, Medicine, PagedMedicineResponse } from '../../../../shared/services/medicine.service';
import { of, throwError } from 'rxjs';
import { FormsModule } from '@angular/forms';
import { ToastModule } from 'primeng/toast';
import { ConfirmDialogModule } from 'primeng/confirmdialog';
import { ButtonModule } from 'primeng/button';
import { TableModule } from 'primeng/table';
import { InputTextModule } from 'primeng/inputtext';
import { TagModule } from 'primeng/tag';

describe('MedicineListComponent', () => {
  let component: MedicineListComponent;
  let fixture: ComponentFixture<MedicineListComponent>;
  let medicineService: jasmine.SpyObj<MedicineService>;
  let messageService: MessageService;
  let confirmationService: ConfirmationService;

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

  const mockPagedResponse: PagedMedicineResponse = {
    content: mockMedicines,
    currentPage: 0,
    pageSize: 10,
    totalPages: 1,
    totalElements: 2
  };

  beforeEach(async () => {
    const medicineServiceSpy = jasmine.createSpyObj('MedicineService', [
      'getMedicines',
      'getMedicineById',
      'createMedicine',
      'updateMedicine',
      'deleteMedicine'
    ]);

    await TestBed.configureTestingModule({
      declarations: [MedicineListComponent],
      imports: [
        RouterTestingModule,
        HttpClientTestingModule,
        FormsModule,
        ToastModule,
        ConfirmDialogModule,
        ButtonModule,
        TableModule,
        InputTextModule,
        TagModule
      ],
      providers: [
        { provide: MedicineService, useValue: medicineServiceSpy },
        MessageService,
        ConfirmationService
      ]
    }).compileComponents();

    medicineService = TestBed.inject(MedicineService) as jasmine.SpyObj<MedicineService>;
    messageService = TestBed.inject(MessageService);
    confirmationService = TestBed.inject(ConfirmationService);
    spyOn(messageService, 'add');
    spyOn(confirmationService, 'confirm');
    spyOn(confirmationService, 'close');

    medicineService.getMedicines.and.returnValue(of(mockPagedResponse));

    fixture = TestBed.createComponent(MedicineListComponent);
    component = fixture.componentInstance;
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  describe('ngOnInit', () => {
    it('should load medicines on init', () => {
      fixture.detectChanges();

      expect(medicineService.getMedicines).toHaveBeenCalledWith(0, 10, undefined);
      expect(component.medicines.length).toBe(2);
      expect(component.totalRecords).toBe(2);
      expect(component.loading).toBe(false);
    });

    it('should set loading flag during fetch', (done) => {
      medicineService.getMedicines.and.returnValue(
        of(mockPagedResponse)
      );

      fixture.detectChanges();

      expect(component.loading).toBe(false);
      done();
    });
  });

  describe('loadMedicines', () => {
    it('should fetch medicines with current pagination settings', () => {
      component.currentPage = 1;
      component.pageSize = 20;

      component.loadMedicines();

      expect(medicineService.getMedicines).toHaveBeenCalledWith(1, 20, undefined);
      expect(component.medicines).toEqual(mockMedicines);
      expect(component.totalRecords).toBe(2);
    });

    it('should include search parameter when searchValue is set', () => {
      component.searchValue = 'Aspirin';

      component.loadMedicines();

      expect(medicineService.getMedicines).toHaveBeenCalledWith(0, 10, 'Aspirin');
    });

    it('should handle error when loading medicines fails', (done) => {
      medicineService.getMedicines.and.returnValue(throwError(() => new Error('Load failed')));

      component.loadMedicines();

      setTimeout(() => {
        expect(messageService.add).toHaveBeenCalledWith(
          jasmine.objectContaining({
            severity: 'error',
            summary: 'Error',
            detail: 'Error al cargar medicamentos'
          })
        );
        expect(component.loading).toBe(false);
        done();
      }, 0);
    });
  });

  describe('onSearch', () => {
    it('should reset page and load medicines', () => {
      component.currentPage = 5;
      component.searchValue = 'Aspirin';

      component.onSearch();

      expect(component.currentPage).toBe(0);
      expect(medicineService.getMedicines).toHaveBeenCalledWith(0, 10, 'Aspirin');
    });
  });

  describe('onPageChange', () => {
    it('should update pagination and load medicines', () => {
      const event = {
        first: 20,
        rows: 10
      };

      component.onPageChange(event);

      expect(component.currentPage).toBe(2);
      expect(component.pageSize).toBe(10);
      expect(medicineService.getMedicines).toHaveBeenCalledWith(2, 10, undefined);
    });

    it('should handle different page sizes', () => {
      const event = {
        first: 0,
        rows: 25
      };

      component.onPageChange(event);

      expect(component.currentPage).toBe(0);
      expect(component.pageSize).toBe(25);
    });
  });

  describe('viewMedicine', () => {
    it('should navigate to medicine detail page', () => {
      spyOn(component['router'], 'navigate');

      component.viewMedicine('med-001');

      expect(component['router'].navigate).toHaveBeenCalledWith(['/medicines', 'med-001']);
    });
  });

  describe('editMedicine', () => {
    it('should navigate to medicine edit page', () => {
      spyOn(component['router'], 'navigate');

      component.editMedicine('med-001');

      expect(component['router'].navigate).toHaveBeenCalledWith(['/medicines', 'med-001', 'edit']);
    });
  });

  describe('deleteMedicine', () => {
    it('should confirm and delete medicine', (done) => {
      medicineService.deleteMedicine.and.returnValue(of(void 0));
      let confirmCallback: (() => void) | undefined;
      (confirmationService.confirm as jasmine.Spy).and.callFake((config: any) => {
        confirmCallback = config.accept;
        return confirmationService;
      });

      const medicine = mockMedicines[0];
      component.deleteMedicine(medicine);

      expect(confirmationService.confirm).toHaveBeenCalled();
      confirmCallback?.();

      setTimeout(() => {
        expect(medicineService.deleteMedicine).toHaveBeenCalledWith('med-001');
        done();
      }, 0);
    });

    it('should show success message after deletion', (done) => {
      medicineService.deleteMedicine.and.returnValue(of(void 0));
      let confirmCallback: (() => void) | undefined;
      (confirmationService.confirm as jasmine.Spy).and.callFake((config: any) => {
        confirmCallback = config.accept;
        return confirmationService;
      });

      const medicine = mockMedicines[0];
      component.deleteMedicine(medicine);
      confirmCallback?.();

      setTimeout(() => {
        expect(messageService.add).toHaveBeenCalledWith(
          jasmine.objectContaining({
            severity: 'success',
            summary: 'Éxito',
            detail: 'Medicamento eliminado'
          })
        );
        done();
      }, 0);
    });

    it('should handle deletion error', (done) => {
      medicineService.deleteMedicine.and.returnValue(throwError(() => new Error('Delete failed')));
      let confirmCallback: (() => void) | undefined;
      (confirmationService.confirm as jasmine.Spy).and.callFake((config: any) => {
        confirmCallback = config.accept;
        return confirmationService;
      });

      const medicine = mockMedicines[0];
      component.deleteMedicine(medicine);
      confirmCallback?.();

      setTimeout(() => {
        expect(messageService.add).toHaveBeenCalledWith(
          jasmine.objectContaining({
            severity: 'error',
            summary: 'Error',
            detail: 'Error al eliminar medicamento'
          })
        );
        done();
      }, 0);
    });
  });
});
