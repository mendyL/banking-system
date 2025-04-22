import { Injectable, inject, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError, finalize, tap } from 'rxjs/operators';
import { Message } from '../models/message.model';
import { PaginatedResponse } from '../models/paginatedResponse.model';
import { environment } from '../../app.config';

@Injectable({
  providedIn: 'root'
})
export class MessageService {
  private readonly http = inject(HttpClient);
  private readonly messagesApi = environment.messagesApiUrl;

  readonly messages = signal<Message[]>([]);
  readonly selectedMessage = signal<Message | null>(null);
  readonly loading = signal<boolean>(false);
  readonly error = signal<string | null>(null);

  getMessages(params?: Record<string, any>): Observable<PaginatedResponse<Message>> {
    this.loading.set(true);
    this.error.set(null);

    return this.http.get<PaginatedResponse<Message>>(`${this.messagesApi}/messages`, { params }).pipe(
      tap({
        next: (response) => {
          this.messages.set(response.content);
        },
        error: () => {
          this.error.set('Failed to load messages');
        }
      }),
      finalize(() => this.loading.set(false)),
      catchError(err => {
        this.error.set('Failed to load messages');
        return throwError(() => err);
      })
    );
  }

  getMessageById(id: string): Observable<Message> {
    this.loading.set(true);
    this.error.set(null);

    return this.http.get<Message>(`${this.messagesApi}/messages/${id}`).pipe(
      tap({
        next: (message) => {
          this.selectedMessage.set(message);
        },
        error: () => {
          this.error.set(`Failed to load message with ID: ${id}`);
        }
      }),
      finalize(() => this.loading.set(false)),
      catchError(err => {
        this.error.set(`Failed to load message with ID: ${id}`);
        return throwError(() => err);
      })
    );
  }
}
