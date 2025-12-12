import { ComponentFixture, TestBed } from '@angular/core/testing';
import { RouterTestingModule } from '@angular/router/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { MessageService, ConfirmationService } from 'primeng/api';
import { MedicineDetailComponent } from './medicine-detail.component';
import { MedicineService, Medicine } from '../../../../shared/services/medicine.service';
import { of, throwError } from 'rxjs';
import { ActivatedRoute } from '@angular/router';
import { ButtonModule } from 'primeng/button';
import { CardModule } from 'primeng/card';
import { TagModule } from 'primeng/tag';
import { CommonModule } from '@angular/common';

describe('MedicineDetailComponent', () => {
  let component: MedicineDetailComponent;
  let fixture: ComponentFixture<MedicineDetailComponent>;
  let medicineService: jasmine.SpyObj<MedicineService>;
  let messageService: jasmine.SpyObj<MessageService>;
  let confirmationService: jasmine.SpyObj<ConfirmationService>;

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
      'deleteMedicine'
    ]);
    const messageServiceSpy = jasmine.createSpyObj('MessageService', ['add']);
    const confirmationServiceSpy = jasmine.createSpyObj('ConfirmationService', ['confirm', 'close']);

    await TestBed.configureTestingModule({
      declarations: [MedicineDetailComponent],
      imports: [
        RouterTestingModule,
        HttpClientTestingModule,
        CommonModule,
        ButtonModule,
        CardModule,
        TagModule
      ],
      providers: [
        { provide: MedicineService, useValue: medicineServiceSpy },
        { provide: MessageService, useValue: messageServiceSpy },
        { provide: ConfirmationService, useValue: confirmationServiceSpy },
        {
          provide: ActivatedRoute,
          useValue: {
            params: of({ id: 'med-001' })
          }
        }
      ]
    }).compileComponents();

    medicineService = TestBed.inject(MedicineService) as jasmine.SpyObj<MedicineService>;
    messageService = TestBed.inject(MessageService) as jasmine.SpyObj<MessageService>;
    confirmationService = TestBed.inject(ConfirmationService) as jasmine.SpyObj<ConfirmationService>;

    medicineService.getMedicineById.and.returnValue(of(mockMedicine));

    fixture = TestBed.createComponent(MedicineDetailComponent);
    component = fixture.componentInstance;
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  describe('ngOnInit', () => {
    it('should load medicine by id from route params', (done) => {
      fixture.detectChanges();

      setTimeout(() => {
        expect(medicineService.getMedicineById).toHaveBeenCalledWith('med-001');
        expect(component.medicine).toEqual(mockMedicine);
        expect(component.loading).toBe(false);
        done();
      }, 0);
    });

    it('should handle error when loading medicine', (done) => {
      medicineService.getMedicineById.and.returnValue(throwError(() => new Error('Load failed')));

      fixture.detectChanges();

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

  describe('editMedicine', () => {
    it('should navigate to edit page', () => {
      spyOn(component['router'], 'navigate');
      component.medicine = mockMedicine;

      component.edit();

      expect(component['router'].navigate).toHaveBeenCalledWith(['/medicines', 'med-001', 'edit']);
    });

    it('should not navigate if medicine is null', () => {
      spyOn(component['router'], 'navigate');
      component.medicine = null;

      component.edit();

      expect(component['router'].navigate).not.toHaveBeenCalled();
    });
  });

  describe('goBack', () => {
    it('should navigate back to medicines list', () => {
      spyOn(component['router'], 'navigate');

      component.goBack();

      expect(component['router'].navigate).toHaveBeenCalledWith(['/medicines']);
    });
  });

  describe('loadMedicine', () => {
    it('should load medicine and update component', (done) => {
      medicineService.getMedicineById.and.returnValue(of(mockMedicine));

      component.loadMedicine('med-001');

      setTimeout(() => {
        expect(component.medicine).toEqual(mockMedicine);
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
});
