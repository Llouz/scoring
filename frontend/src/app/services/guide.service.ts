import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';

import { ApiResponse } from '../models/api-response.model';
import { Guide } from '../models/guide.model';

@Injectable({
  providedIn: 'root'
})
export class GuideService {
  private readonly baseUrl = 'http://localhost:8080/api';
  private readonly http = inject(HttpClient);

  getAllGuides(): Observable<Guide[]> {
    return this.http.get<Guide[]>(`${this.baseUrl}/guides`);
  }

  searchGuide(nom: string): Observable<ApiResponse<Guide>> {
    const params = new HttpParams().set('nom', nom);
    return this.http.get<ApiResponse<Guide>>(`${this.baseUrl}/guides/search`, { params });
  }

  createGuide(guide: Guide): Observable<ApiResponse<Guide>> {
    return this.http.post<ApiResponse<Guide>>(`${this.baseUrl}/guides`, guide);
  }

  deleteGuide(nom: string): Observable<ApiResponse<null>> {
    const params = new HttpParams().set('nom', nom);
    return this.http.delete<ApiResponse<null>>(`${this.baseUrl}/guides/delete`, { params });
  }
}
