import { Injectable, inject, signal } from '@angular/core';
import { Partner, PartnerResponse } from '../models/partner.model';
import { Observable, throwError, tap, catchError } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../app.config';


@Injectable({ providedIn: 'root' })
export class PartnerService {
  private readonly http = inject(HttpClient);
  private readonly partnersApi = environment.partnersApiUrl;

  readonly partners = signal<Partner[]>([]);
  readonly isLoading = signal(false);
  readonly error = signal<string | null>(null);

  loadPartners(): Observable<PartnerResponse> {
    this.isLoading.set(true);
    this.error.set(null);

    return this.http.get<PartnerResponse>(`${this.partnersApi}/partners`).pipe(
      tap(response => {
        this.partners.set(response.partners);
        this.isLoading.set(false);
      }),
      catchError(err => {
        this.error.set('Failed to load partners');
        this.isLoading.set(false);
        return throwError(() => err);
      })
    );
  }

  addPartner(partner: Partner): Observable<Partner> {
    this.isLoading.set(true);
    this.error.set(null);

    
    return this.http.post<Partner>(`${this.partnersApi}/partners`, partner).pipe(
      tap(newPartner => {
        this.partners.set([...this.partners(), newPartner]);
        this.isLoading.set(false);
      }),
      catchError(err => {
        this.error.set('Failed to add partner');
        this.isLoading.set(false);
        return throwError(() => err);
      })
    );
  }

  deletePartner(id: string): Observable<void> {
    this.isLoading.set(true);
    this.error.set(null);

    return this.http.delete<void>(`${this.partnersApi}/partners/${id}`).pipe(
      tap(() => {
        this.partners.set(this.partners().filter(p => p.id !== id));
        this.isLoading.set(false);
      }),
      catchError(err => {
        this.error.set(`Failed to delete partner with ID: ${id}`);
        this.isLoading.set(false);
        return throwError(() => err);
      })
    );
  }

  refresh(): Observable<PartnerResponse> {
    return this.loadPartners();
  }
}
