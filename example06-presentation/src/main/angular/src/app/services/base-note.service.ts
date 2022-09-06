import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Note } from '../models/note';

export abstract class BaseNoteService {
  protected defaultHeaders = new HttpHeaders({
    'Content-Type': 'application/json'
  });

  protected constructor(protected http: HttpClient) {
  }


  abstract getAll(): Observable<Note[]>;

  abstract create(priority: number, content: string, deadline:Date): Observable<Note>;
}
