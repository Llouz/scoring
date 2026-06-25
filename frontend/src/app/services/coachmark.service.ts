import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';

import { ApiResponse } from '../models/api-response.model';
import { Coachmark } from '../models/coachmark.model';

@Injectable({
  providedIn: 'root'
})
export class CoachmarkService {
  private readonly baseUrl = 'http://localhost:8080/api';
  private readonly http = inject(HttpClient);

  saveOrUpdate(coachmark: Coachmark): Observable<ApiResponse<Coachmark>> {
    return this.http.post<ApiResponse<Coachmark>>(`${this.baseUrl}/coachmarks/save-or-update`, coachmark);
  }

  getUserScores(nom: string, prenom: string): Observable<ApiResponse<Coachmark[]>> {
    const params = new HttpParams().set('nom', nom).set('prenom', prenom);
    return this.http.get<ApiResponse<Coachmark[]>>(`${this.baseUrl}/coachmarks/user`, { params });
  }
}
