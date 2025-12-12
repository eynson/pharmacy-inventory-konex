import { ComponentFixture, TestBed } from '@angular/core/testing';
import { RouterTestingModule } from '@angular/router/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ReactiveFormsModule, FormsModule } from '@angular/forms';
import { MessageService } from 'primeng/api';
import { SaleListComponent } from './sale-list.component';
import { SaleService, Sale, PagedSaleResponse } from '../../../../shared/services/sale.service';
import { of, throwError } from 'rxjs';
import { ToastModule } from 'primeng/toast';
import { ButtonModule } from 'primeng/button';
import { TableModule } from 'primeng/table';
import { CalendarModule } from 'primeng/calendar';
import { CommonModule } from '@angular/common';

describe('SaleListComponent', () => {
  let component: SaleListComponent;
  let fixture: ComponentFixture<SaleListComponent>;
  let saleService: jasmine.SpyObj<SaleService>;
  let messageService: MessageService;

  const mockSales: Sale[] = [
    {
      id: 'sale-001',
      medicineId: 'med-001',
      medicineName: 'Aspirin',
      quantitySold: 50,
      unitValue: 5.50,
      totalValue: 275.00,
      saleDate: '2024-11-15'
    },
    {
      id: 'sale-002',
      medicineId: 'med-002',
      medicineName: 'Ibuprofen',
      quantitySold: 100,
      unitValue: 3.75,
      totalValue: 375.00,
      saleDate: '2024-11-20'
    }
  ];

  const mockPagedResponse: PagedSaleResponse = {
    content: mockSales,
    currentPage: 0,
    pageSize: 10,
    totalPages: 1,
    totalElements: 2
  };

  beforeEach(async () => {
    const saleServiceSpy = jasmine.createSpyObj('SaleService', [
      'getSalesByDateRange',
      'getSaleById',
      'createSale'
    ]);

    await TestBed.configureTestingModule({
      declarations: [SaleListComponent],
      imports: [
        RouterTestingModule,
        HttpClientTestingModule,
        ReactiveFormsModule,
        FormsModule,
        CommonModule,
        ToastModule,
        ButtonModule,
        TableModule,
        CalendarModule
      ],
      providers: [
        { provide: SaleService, useValue: saleServiceSpy },
        MessageService
      ]
    }).compileComponents();

    saleService = TestBed.inject(SaleService) as jasmine.SpyObj<SaleService>;
    messageService = TestBed.inject(MessageService);
    spyOn(messageService, 'add');

    saleService.getSalesByDateRange.and.returnValue(of(mockPagedResponse));

    fixture = TestBed.createComponent(SaleListComponent);
    component = fixture.componentInstance;
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  describe('ngOnInit', () => {
    it('should initialize with default date range', () => {
      fixture.detectChanges();

      expect(component.startDate).toBeDefined();
      expect(component.endDate).toBeDefined();
      expect(component.endDate?.getDate()).toBe(new Date().getDate());
    });

    it('should load sales on init', (done) => {
      fixture.detectChanges();

      setTimeout(() => {
        expect(saleService.getSalesByDateRange).toHaveBeenCalled();
        expect(component.sales.length).toBe(2);
        expect(component.totalRecords).toBe(2);
        done();
      }, 0);
    });
  });

  describe('loadSales', () => {
    beforeEach(() => {
      component.startDate = new Date(2024, 10, 1);
      component.endDate = new Date(2024, 10, 30);
    });

    it('should fetch sales for selected date range', () => {
      component.loadSales();

      expect(saleService.getSalesByDateRange).toHaveBeenCalled();
      expect(component.sales).toEqual(mockSales);
      expect(component.totalRecords).toBe(2);
    });

    it('should show warning when start date is missing', () => {
      component.startDate = null;

      component.loadSales();

      expect(messageService.add).toHaveBeenCalledWith(
        jasmine.objectContaining({
          severity: 'warn',
          summary: 'Validación',
          detail: 'Seleccione rango de fechas'
        })
      );
      expect(saleService.getSalesByDateRange).not.toHaveBeenCalled();
    });

    it('should show warning when end date is missing', () => {
      component.endDate = null;

      component.loadSales();

      expect(messageService.add).toHaveBeenCalledWith(
        jasmine.objectContaining({
          severity: 'warn',
          summary: 'Validación',
          detail: 'Seleccione rango de fechas'
        })
      );
      expect(saleService.getSalesByDateRange).not.toHaveBeenCalled();
    });

    it('should handle error when loading sales fails', (done) => {
      saleService.getSalesByDateRange.and.returnValue(throwError(() => new Error('Load failed')));

      component.loadSales();

      setTimeout(() => {
        expect(messageService.add).toHaveBeenCalledWith(
          jasmine.objectContaining({
            severity: 'error',
            summary: 'Error',
            detail: 'Error al cargar ventas'
          })
        );
        expect(component.loading).toBe(false);
        done();
      }, 0);
    });
  });

  describe('getTotalSales', () => {
    it('should calculate total sales value from all sales', () => {
      component.sales = mockSales;

      const total = component.getTotalSales();

      expect(total).toBe(650.00); // 275 + 375
    });

    it('should return 0 when no sales', () => {
      component.sales = [];

      const total = component.getTotalSales();

      expect(total).toBe(0);
    });
  });

  describe('onPageChange', () => {
    beforeEach(() => {
      component.startDate = new Date(2024, 10, 1);
      component.endDate = new Date(2024, 10, 30);
    });

    it('should update pagination and load sales', () => {
      const event = {
        first: 20,
        rows: 10
      };

      component.onPageChange(event);

      expect(component.currentPage).toBe(2);
      expect(component.pageSize).toBe(10);
      expect(saleService.getSalesByDateRange).toHaveBeenCalled();
    });

    it('should handle different page sizes', () => {
      const event = {
        first: 0,
        rows: 25
      };

      component.onPageChange(event);

      expect(component.pageSize).toBe(25);
    });
  });

  describe('viewSale', () => {
    it('should navigate to sale detail page', () => {
      spyOn(component['router'], 'navigate');

      component.viewSale('sale-001');

      expect(component['router'].navigate).toHaveBeenCalledWith(['/sales', 'sale-001']);
    });
  });

  describe('createSale', () => {
    it('should navigate to sale creation page', () => {
      spyOn(component['router'], 'navigate');

      component.createSale();

      expect(component['router'].navigate).toHaveBeenCalledWith(['/sales', 'new']);
    });
  });
});
