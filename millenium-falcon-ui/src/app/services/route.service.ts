import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { EmpireIntel, Route, TravelPlan } from '../models/models';
import { environment } from '../../environments/environment'; //
@Injectable({
  providedIn: 'root'
})
export class RouteService {
  private apiUrl = environment.apiUrl;

  constructor(private http: HttpClient) {}

  getRoutes(): Observable<Route[]> {
    return this.http.get<Route[]>(`${this.apiUrl}/api/v1/routes`);
  }

  findTravelPlan(empire_intel: EmpireIntel): Observable<TravelPlan> {
    return this.http.post<TravelPlan>(
      `${this.apiUrl}/api/v1/routes/travel-plan`,
      empire_intel
    );
  }
}
