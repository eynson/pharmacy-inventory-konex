import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface Medicine {
  id: string;
  name: string;
  factoryLaboratory: string;
  manufacturingDate: string;
  expirationDate: string;
  quantityInStock: number;
  unitValue: number;
  expirationStatus: string;
}

export interface MedicineRequest {
  name: string;
  factoryLaboratory: string;
  manufacturingDate: string | null;
  expirationDate: string | null;
  quantityInStock: number;
  unitValue: string;
}

export interface PagedMedicineResponse {
  content: Medicine[];
  currentPage: number;
  pageSize: number;
  totalPages: number;
  totalElements: number;
}

@Injectable({
  providedIn: 'root'
})
export class MedicineService {

  private apiUrl = 'http://localhost:8080/api/v1/medicines';

  constructor(private http: HttpClient) { }

  getMedicines(page: number = 0, size: number = 10, search?: string): Observable<PagedMedicineResponse> {
    let params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());

    if (search) {
      params = params.set('search', search);
    }

    return this.http.get<PagedMedicineResponse>(this.apiUrl, { params });
  }

  getMedicineById(id: string): Observable<Medicine> {
    return this.http.get<Medicine>(`${this.apiUrl}/${id}`);
  }

  createMedicine(medicine: MedicineRequest): Observable<Medicine> {
    return this.http.post<Medicine>(this.apiUrl, medicine);
  }

  updateMedicine(id: string, medicine: Partial<MedicineRequest>): Observable<Medicine> {
    return this.http.put<Medicine>(`${this.apiUrl}/${id}`, medicine);
  }

  deleteMedicine(id: string): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}
