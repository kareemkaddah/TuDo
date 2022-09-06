import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import {HttpClient, HttpHeaders, HttpResponse} from '@angular/common/http';
import { BehaviorSubject, Observable } from 'rxjs';
import { map } from 'rxjs/operators';

import { environment } from '../../environments/environment';
import { User } from '../models/user';

@Injectable({ providedIn: 'root' })
export class AccountService {
  private userSubject: BehaviorSubject<User>;
  public user: Observable<User>;
  public loginPasswordSubject: BehaviorSubject<string>;
  public loginPassword: Observable<string>

  constructor(
    private router: Router,
    private http: HttpClient
  ) {
    this.userSubject = new BehaviorSubject<User>(JSON.parse(localStorage.getItem('user')));
    this.user = this.userSubject.asObservable();
    this.loginPasswordSubject = new BehaviorSubject<string>(localStorage.getItem('password'));
    this.loginPassword = this.loginPasswordSubject.asObservable();
  }

  public get userValue(): User {
    return this.userSubject.value;
  }

  public refresh():void{
    this.http.get<User>(`${environment.apiUrl}/users/me`, {headers: this.getAuthHeaders()}).pipe(
      map(ob => {
        const user = User.fromObject(ob);
        //save token
        user.token = this.userSubject.value.token;
        localStorage.setItem('user',JSON.stringify(user));
        this.userSubject.next(user);
      })
    ).subscribe();

  }



  login(username, password) {
    return this.http.post<User>(`${environment.apiUrl}/authentication/authenticate`, {username, password})
      .pipe(map(user => {
        // store user details and jwt token in local storage to keep user logged in between page refreshes
        localStorage.setItem("password", password);
        this.loginPasswordSubject.next(password);
        localStorage.setItem('user', JSON.stringify(user));
        this.userSubject.next(user);
        return user;
      }));
  }

  logout() {
    let h = this.getAuthHeaders()
    localStorage.removeItem('password');
    this.loginPasswordSubject.next(null);
    localStorage.removeItem('user');
    this.userSubject.next(null);
    this.router.navigate(['/account-login']);
    return this.http.post<User>(`${environment.apiUrl}/authentication/logout`, {}, {headers: h}).pipe(
      map(ob => {})).subscribe();
  }

  register(user: User) {
    return this.http.post<any>(`${environment.apiUrl}/users/add`, user,
      {headers: new HttpHeaders({'Content-Type': 'application/json'})});
  }

  getAllUsers(): Observable<User[]> {
    return this.http.get<any[]>(`${environment.apiUrl}/users/`, {headers: this.getAuthHeaders()})
      .pipe(
        map(body => body.map(n => User.fromObject(n)))
      );
  }


  getAuthHeaders(): HttpHeaders {
    return this.userValue == null ? new HttpHeaders() : new HttpHeaders({
      "Authorization": `Bearer ${this.userValue.token}`
    });
  }

  update(params) {
    return this.http.put<any>(`${environment.apiUrl}/users/update`, params, {headers: this.getAuthHeaders()})
      .pipe(map(x => {
        // update stored user if the logged in user updated their own record
        if (x.id == this.userValue.id) {
          // update local storage
          const user = User.fromObject(x);
          //save token
          user.token = this.userSubject.value.token;
          localStorage.setItem('user', JSON.stringify(user));
          localStorage.setItem('password', params.password);
          // publish updated user to subscribers
          this.loginPasswordSubject.next(params.password);
          this.userSubject.next(user);
        }
        return x;
      }));
  }

  getUserById(id: number):Observable<any>{
    return this.http.get<any>(`${environment.apiUrl}/users/${id}`, {headers: this.getAuthHeaders()}).pipe(
      map(
        x => {User.fromObject(x);}
      )
    );
  }
}
