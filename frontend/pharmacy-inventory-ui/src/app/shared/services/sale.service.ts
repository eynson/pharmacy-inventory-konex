import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface Sale {
  id: string;
  medicineId: string;
  medicineName: string;
  quantitySold: number;
  unitValue: number;
  totalValue: number;
  saleDate: string;
}

export interface SaleRequest {
  medicineId: string;
  quantitySold: number;
}

export interface PagedSaleResponse {
  content: Sale[];
  currentPage: number;
  pageSize: number;
  totalPages: number;
  totalElements: number;
}

@Injectable({
  providedIn: 'root'
})
export class SaleService {

  private apiUrl = 'http://localhost:8080/api/v1/sales';

  constructor(private http: HttpClient) { }

  getSalesByDateRange(startDate: string, endDate: string, page: number = 0, size: number = 10): Observable<PagedSaleResponse> {
    const params = new HttpParams()
      .set('startDate', startDate)
      .set('endDate', endDate)
      .set('page', page.toString())
      .set('size', size.toString());

    return this.http.get<PagedSaleResponse>(this.apiUrl, { params });
  }

  getSaleById(id: string): Observable<Sale> {
    return this.http.get<Sale>(`${this.apiUrl}/${id}`);
  }

  createSale(sale: SaleRequest): Observable<Sale> {
    return this.http.post<Sale>(this.apiUrl, sale);
  }
}
