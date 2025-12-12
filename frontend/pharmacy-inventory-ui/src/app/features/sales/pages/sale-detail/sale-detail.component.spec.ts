import { ComponentFixture, TestBed } from '@angular/core/testing';
import { RouterTestingModule } from '@angular/router/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { MessageService } from 'primeng/api';
import { SaleDetailComponent } from './sale-detail.component';
import { SaleService, Sale } from '../../../../shared/services/sale.service';
import { of, throwError } from 'rxjs';
import { ActivatedRoute } from '@angular/router';
import { ButtonModule } from 'primeng/button';
import { CardModule } from 'primeng/card';
import { ToastModule } from 'primeng/toast';
import { CommonModule } from '@angular/common';

describe('SaleDetailComponent', () => {
  let component: SaleDetailComponent;
  let fixture: ComponentFixture<SaleDetailComponent>;
  let saleService: jasmine.SpyObj<SaleService>;
  let messageService: MessageService;

  const mockSale: Sale = {
    id: 'sale-001',
    medicineId: 'med-001',
    medicineName: 'Aspirin',
    quantitySold: 50,
    unitValue: 5.50,
    totalValue: 275.00,
    saleDate: '2024-11-15'
  };

  beforeEach(async () => {
    const saleServiceSpy = jasmine.createSpyObj('SaleService', ['getSaleById']);

    await TestBed.configureTestingModule({
      declarations: [SaleDetailComponent],
      imports: [
        RouterTestingModule,
        HttpClientTestingModule,
        CommonModule,
        ButtonModule,
        CardModule,
        ToastModule
      ],
      providers: [
        { provide: SaleService, useValue: saleServiceSpy },
        MessageService,
        {
          provide: ActivatedRoute,
          useValue: {
            params: of({ id: 'sale-001' })
          }
        }
      ]
    }).compileComponents();

    saleService = TestBed.inject(SaleService) as jasmine.SpyObj<SaleService>;
    messageService = TestBed.inject(MessageService);
    spyOn(messageService, 'add');

    saleService.getSaleById.and.returnValue(of(mockSale));

    fixture = TestBed.createComponent(SaleDetailComponent);
    component = fixture.componentInstance;
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  describe('ngOnInit', () => {
    it('should load sale by id from route params', (done) => {
      fixture.detectChanges();

      setTimeout(() => {
        expect(saleService.getSaleById).toHaveBeenCalledWith('sale-001');
        expect(component.sale).toEqual(mockSale);
        expect(component.loading).toBe(false);
        done();
      }, 0);
    });

    it('should handle error when loading sale', (done) => {
      saleService.getSaleById.and.returnValue(throwError(() => new Error('Load failed')));

      fixture.detectChanges();

      setTimeout(() => {
        expect(messageService.add).toHaveBeenCalledWith(
          jasmine.objectContaining({
            severity: 'error',
            summary: 'Error',
            detail: 'Error al cargar los detalles de la venta'
          })
        );
        expect(component.loading).toBe(false);
        done();
      }, 0);
    });
  });

  describe('goBack', () => {
    it('should navigate back to sales list', () => {
      spyOn(component['router'], 'navigate');

      component.goBack();

      expect(component['router'].navigate).toHaveBeenCalledWith(['/sales']);
    });
  });

  describe('loadSale', () => {
    it('should load sale and update component', (done) => {
      saleService.getSaleById.and.returnValue(of(mockSale));

      component.loadSale('sale-001');

      setTimeout(() => {
        expect(component.sale).toEqual(mockSale);
        expect(component.loading).toBe(false);
        done();
      }, 0);
    });

    it('should handle error when loading sale fails', (done) => {
      saleService.getSaleById.and.returnValue(throwError(() => new Error('Load failed')));

      component.loadSale('sale-999');

      setTimeout(() => {
        expect(messageService.add).toHaveBeenCalledWith(
          jasmine.objectContaining({
            severity: 'error',
            summary: 'Error'
          })
        );
        done();
      }, 0);
    });
  });
});
