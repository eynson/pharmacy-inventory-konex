import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { MedicineService, Medicine, PagedMedicineResponse, MedicineRequest } from './medicine.service';

describe('MedicineService', () => {
  let service: MedicineService;
  let httpMock: HttpTestingController;
  const apiUrl = 'http://localhost:8080/api/v1/medicines';

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [MedicineService]
    });
    service = TestBed.inject(MedicineService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  describe('getMedicines', () => {
    it('should fetch paginated medicines with default parameters', (done) => {
      const mockResponse: PagedMedicineResponse = {
        content: [
          {
            id: 'med-001',
            name: 'Aspirin',
            factoryLaboratory: 'Bayer',
            manufacturingDate: '2024-01-01',
            expirationDate: '2026-01-01',
            quantityInStock: 100,
            unitValue: 5.50,
            expirationStatus: 'VALID'
          }
        ],
        currentPage: 0,
        pageSize: 10,
        totalPages: 1,
        totalElements: 1
      };

      service.getMedicines().subscribe((response) => {
        expect(response.content.length).toBe(1);
        expect(response.content[0].name).toBe('Aspirin');
        expect(response.totalElements).toBe(1);
        done();
      });

      const req = httpMock.expectOne(
        req => req.url === apiUrl && req.params.get('page') === '0' && req.params.get('size') === '10'
      );
      expect(req.request.method).toBe('GET');
      req.flush(mockResponse);
    });

    it('should fetch medicines with custom page and size', (done) => {
      const mockResponse: PagedMedicineResponse = {
        content: [],
        currentPage: 2,
        pageSize: 20,
        totalPages: 5,
        totalElements: 100
      };

      service.getMedicines(2, 20).subscribe((response) => {
        expect(response.currentPage).toBe(2);
        expect(response.pageSize).toBe(20);
        done();
      });

      const req = httpMock.expectOne(
        req => req.url === apiUrl && req.params.get('page') === '2' && req.params.get('size') === '20'
      );
      expect(req.request.method).toBe('GET');
      req.flush(mockResponse);
    });

    it('should include search parameter when provided', (done) => {
      const mockResponse: PagedMedicineResponse = {
        content: [],
        currentPage: 0,
        pageSize: 10,
        totalPages: 1,
        totalElements: 0
      };

      service.getMedicines(0, 10, 'Aspirin').subscribe((response) => {
        expect(response.content.length).toBe(0);
        done();
      });

      const req = httpMock.expectOne(
        req => req.url === apiUrl && req.params.get('search') === 'Aspirin'
      );
      expect(req.request.method).toBe('GET');
      req.flush(mockResponse);
    });
  });

  describe('getMedicineById', () => {
    it('should fetch a single medicine by ID', (done) => {
      const medicineId = 'med-001';
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

      service.getMedicineById(medicineId).subscribe((medicine) => {
        expect(medicine.id).toBe('med-001');
        expect(medicine.name).toBe('Aspirin');
        done();
      });

      const req = httpMock.expectOne(`${apiUrl}/${medicineId}`);
      expect(req.request.method).toBe('GET');
      req.flush(mockMedicine);
    });

    it('should return error when medicine not found', (done) => {
      const medicineId = 'med-999';

      service.getMedicineById(medicineId).subscribe(
        () => fail('should have failed'),
        (error) => {
          expect(error.status).toBe(404);
          done();
        }
      );

      const req = httpMock.expectOne(`${apiUrl}/${medicineId}`);
      expect(req.request.method).toBe('GET');
      req.flush('Not found', { status: 404, statusText: 'Not Found' });
    });
  });

  describe('createMedicine', () => {
    it('should create a new medicine', (done) => {
      const newMedicine: MedicineRequest = {
        name: 'New Medicine',
        factoryLaboratory: 'Lab1',
        manufacturingDate: '2024-01-01',
        expirationDate: '2026-01-01',
        quantityInStock: 50,
        unitValue: '10.50'
      };

      const mockResponse: Medicine = {
        id: 'med-002',
        name: newMedicine.name,
        factoryLaboratory: newMedicine.factoryLaboratory,
        manufacturingDate: newMedicine.manufacturingDate || '',
        expirationDate: newMedicine.expirationDate || '',
        quantityInStock: newMedicine.quantityInStock,
        unitValue: 10.50,
        expirationStatus: 'VALID'
      };

      service.createMedicine(newMedicine).subscribe((response) => {
        expect(response.id).toBe('med-002');
        expect(response.name).toBe('New Medicine');
        done();
      });

      const req = httpMock.expectOne(apiUrl);
      expect(req.request.method).toBe('POST');
      expect(req.request.body).toEqual(newMedicine);
      req.flush(mockResponse);
    });
  });

  describe('updateMedicine', () => {
    it('should update an existing medicine', (done) => {
      const medicineId = 'med-001';
      const updateData: Partial<MedicineRequest> = {
        name: 'Updated Aspirin',
        quantityInStock: 150
      };

      const mockResponse: Medicine = {
        id: medicineId,
        name: 'Updated Aspirin',
        factoryLaboratory: 'Bayer',
        manufacturingDate: '2024-01-01',
        expirationDate: '2026-01-01',
        quantityInStock: 150,
        unitValue: 5.50,
        expirationStatus: 'VALID'
      };

      service.updateMedicine(medicineId, updateData).subscribe((response) => {
        expect(response.name).toBe('Updated Aspirin');
        expect(response.quantityInStock).toBe(150);
        done();
      });

      const req = httpMock.expectOne(`${apiUrl}/${medicineId}`);
      expect(req.request.method).toBe('PUT');
      expect(req.request.body).toEqual(updateData);
      req.flush(mockResponse);
    });
  });

  describe('deleteMedicine', () => {
    it('should delete a medicine', (done) => {
      const medicineId = 'med-001';

      service.deleteMedicine(medicineId).subscribe(() => {
        expect(true).toBe(true);
        done();
      });

      const req = httpMock.expectOne(`${apiUrl}/${medicineId}`);
      expect(req.request.method).toBe('DELETE');
      req.flush(null);
    });

    it('should handle delete error', (done) => {
      const medicineId = 'med-999';

      service.deleteMedicine(medicineId).subscribe(
        () => fail('should have failed'),
        (error) => {
          expect(error.status).toBe(404);
          done();
        }
      );

      const req = httpMock.expectOne(`${apiUrl}/${medicineId}`);
      expect(req.request.method).toBe('DELETE');
      req.flush('Not found', { status: 404, statusText: 'Not Found' });
    });
  });
});
