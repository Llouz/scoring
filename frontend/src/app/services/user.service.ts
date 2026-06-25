import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';

import { ApiResponse } from '../models/api-response.model';
import { User } from '../models/user.model';

@Injectable({
  providedIn: 'root'
})
export class UserService {
  private readonly baseUrl = 'http://localhost:8080/api';
  private readonly http = inject(HttpClient);

  getAllUsers(): Observable<ApiResponse<User[]>> {
    return this.http.get<ApiResponse<User[]>>(`${this.baseUrl}/users`);
  }

  searchUser(nom: string, prenom: string): Observable<ApiResponse<User>> {
    const params = new HttpParams().set('nom', nom).set('prenom', prenom);
    return this.http.get<ApiResponse<User>>(`${this.baseUrl}/users/search`, { params });
  }

  createUser(user: User): Observable<ApiResponse<User>> {
    return this.http.post<ApiResponse<User>>(`${this.baseUrl}/users`, user);
  }

  deleteUser(nom: string, prenom: string): Observable<ApiResponse<null>> {
    const params = new HttpParams().set('nom', nom).set('prenom', prenom);
    return this.http.delete<ApiResponse<null>>(`${this.baseUrl}/users/delete`, { params });
  }
}
