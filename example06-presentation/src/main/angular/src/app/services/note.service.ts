import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { BaseNoteService } from './base-note.service';
import { environment as env } from '../../environments/environment';
import {Note} from "../models/note";
import {AccountService} from "./account.service";

@Injectable({
  providedIn: 'root'
})
export class NoteService extends BaseNoteService {



  constructor(http: HttpClient,private accountService:AccountService) {
    super(http);
  }

  getAll(): Observable<Note[]> {
    return this.http.get<Note[]>(`${env.apiUrl}/notes`, {headers: this.accountService.getAuthHeaders()}).pipe(
      map(body => body.map(n => Note.fromObject(n)))
    );
  }

  getMyNotes():Observable<Note[]>{
    return this.http.get<any[]>(`${env.apiUrl}/notes/me`,{headers:this.accountService.getAuthHeaders()}).pipe(
      map(body => body.map(n=>Note.fromObject(n)))
    );
  }

  create(priority: number, content: string, deadline: Date): Observable<Note> {
      return this.http.post<any>(`${env.apiUrl}/notes/add`, {priority, content,deadline},
        {headers:this.accountService.getAuthHeaders()}).pipe(
        map(body => Note.fromObject(body))
      );
  }

  update(id: number,priority: number, content: string, deadline: Date): Observable<Note> {
    return this.http.put<any>(`${env.apiUrl}/notes/update/${id}`, {priority, content,deadline},
      {headers:this.accountService.getAuthHeaders()}).pipe(
      map(body => Note.fromObject(body))
    );
  }

  remove(id: number): Observable<Note> {
    return this.http.delete<any>(`${env.apiUrl}/notes/delete/${id}`,
      {headers:this.accountService.getAuthHeaders()}).pipe(
      map(body => Note.fromObject(body))
    );
  }


}

