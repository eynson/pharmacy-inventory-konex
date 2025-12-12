import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { SaleService, Sale, PagedSaleResponse, SaleRequest } from './sale.service';

describe('SaleService', () => {
  let service: SaleService;
  let httpMock: HttpTestingController;
  const apiUrl = 'http://localhost:8080/api/v1/sales';

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [SaleService]
    });
    service = TestBed.inject(SaleService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  describe('getSalesByDateRange', () => {
    it('should fetch sales within date range with default pagination', (done) => {
      const startDate = '2024-11-01';
      const endDate = '2024-11-30';
      const mockResponse: PagedSaleResponse = {
        content: [
          {
            id: 'sale-001',
            medicineId: 'med-001',
            medicineName: 'Aspirin',
            quantitySold: 50,
            unitValue: 5.50,
            totalValue: 275.00,
            saleDate: '2024-11-15'
          }
        ],
        currentPage: 0,
        pageSize: 10,
        totalPages: 1,
        totalElements: 1
      };

      service.getSalesByDateRange(startDate, endDate).subscribe((response) => {
        expect(response.content.length).toBe(1);
        expect(response.content[0].id).toBe('sale-001');
        expect(response.totalElements).toBe(1);
        done();
      });

      const req = httpMock.expectOne(
        req => req.url === apiUrl &&
               req.params.get('startDate') === startDate &&
               req.params.get('endDate') === endDate &&
               req.params.get('page') === '0' &&
               req.params.get('size') === '10'
      );
      expect(req.request.method).toBe('GET');
      req.flush(mockResponse);
    });

    it('should fetch sales with custom page and size', (done) => {
      const startDate = '2024-11-01';
      const endDate = '2024-11-30';
      const mockResponse: PagedSaleResponse = {
        content: [],
        currentPage: 1,
        pageSize: 20,
        totalPages: 3,
        totalElements: 60
      };

      service.getSalesByDateRange(startDate, endDate, 1, 20).subscribe((response) => {
        expect(response.currentPage).toBe(1);
        expect(response.pageSize).toBe(20);
        expect(response.totalPages).toBe(3);
        done();
      });

      const req = httpMock.expectOne(
        req => req.url === apiUrl &&
               req.params.get('page') === '1' &&
               req.params.get('size') === '20'
      );
      expect(req.request.method).toBe('GET');
      req.flush(mockResponse);
    });

    it('should return empty content when no sales in date range', (done) => {
      const startDate = '2024-12-01';
      const endDate = '2024-12-31';
      const mockResponse: PagedSaleResponse = {
        content: [],
        currentPage: 0,
        pageSize: 10,
        totalPages: 0,
        totalElements: 0
      };

      service.getSalesByDateRange(startDate, endDate).subscribe((response) => {
        expect(response.content.length).toBe(0);
        expect(response.totalElements).toBe(0);
        done();
      });

      const req = httpMock.expectOne(req => req.url === apiUrl);
      expect(req.request.method).toBe('GET');
      req.flush(mockResponse);
    });
  });

  describe('getSaleById', () => {
    it('should fetch a single sale by ID', (done) => {
      const saleId = 'sale-001';
      const mockSale: Sale = {
        id: 'sale-001',
        medicineId: 'med-001',
        medicineName: 'Aspirin',
        quantitySold: 50,
        unitValue: 5.50,
        totalValue: 275.00,
        saleDate: '2024-11-15'
      };

      service.getSaleById(saleId).subscribe((sale) => {
        expect(sale.id).toBe('sale-001');
        expect(sale.medicineName).toBe('Aspirin');
        expect(sale.totalValue).toBe(275.00);
        done();
      });

      const req = httpMock.expectOne(`${apiUrl}/${saleId}`);
      expect(req.request.method).toBe('GET');
      req.flush(mockSale);
    });

    it('should handle sale not found error', (done) => {
      const saleId = 'sale-999';

      service.getSaleById(saleId).subscribe(
        () => fail('should have failed'),
        (error) => {
          expect(error.status).toBe(404);
          done();
        }
      );

      const req = httpMock.expectOne(`${apiUrl}/${saleId}`);
      expect(req.request.method).toBe('GET');
      req.flush('Not found', { status: 404, statusText: 'Not Found' });
    });
  });

  describe('createSale', () => {
    it('should create a new sale', (done) => {
      const newSale: SaleRequest = {
        medicineId: 'med-001',
        quantitySold: 50
      };

      const mockResponse: Sale = {
        id: 'sale-002',
        medicineId: 'med-001',
        medicineName: 'Aspirin',
        quantitySold: 50,
        unitValue: 5.50,
        totalValue: 275.00,
        saleDate: '2024-11-15'
      };

      service.createSale(newSale).subscribe((response) => {
        expect(response.id).toBe('sale-002');
        expect(response.quantitySold).toBe(50);
        expect(response.totalValue).toBe(275.00);
        done();
      });

      const req = httpMock.expectOne(apiUrl);
      expect(req.request.method).toBe('POST');
      expect(req.request.body).toEqual(newSale);
      req.flush(mockResponse);
    });

    it('should handle validation error on create', (done) => {
      const invalidSale: SaleRequest = {
        medicineId: 'med-999',
        quantitySold: 1000
      };

      service.createSale(invalidSale).subscribe(
        () => fail('should have failed'),
        (error) => {
          expect(error.status).toBe(400);
          done();
        }
      );

      const req = httpMock.expectOne(apiUrl);
      expect(req.request.method).toBe('POST');
      req.flush('Invalid sale data', { status: 400, statusText: 'Bad Request' });
    });
  });
});
