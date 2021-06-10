import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, of } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { User } from '../interfaces/user';

@Injectable({
  providedIn: 'root'
})
export class AppUserService {
  baseUrl : string = "http://localhost:8080/blog/";

  options = {
        headers: new HttpHeaders({"Content-Type": "application/json"})
  };

  constructor(private http : HttpClient) { }

  handleError<T>(operations="operations", result ?: T) {
    return (error : any) : Observable<T> => {
        window.alert(`${operations} failed: ${error.error}`);
        return of(result as T);
      } 
  }

  getUser(id: number) : Observable<User> {
    const url = `${this.baseUrl}/user-hello/${id}`;
    const logMessage = `searching user with details w/ id=${id}`;
    return this.http.post<User>(url, this.options)
      .pipe(
        catchError(this.handleError<User>(logMessage))
      );
  }

  getUserFollowers(id: number) : Observable<User> {
    const url = `${this.baseUrl}/user-followers/${id}`;
    const logMessage = `searching user with followers w/ id=${id}`;
    return this.http.get<User>(url, this.options)
      .pipe(
        catchError(this.handleError<User>(logMessage))
      );
  }

  getUserFollowersPosts(id: number) : Observable<User> {
    const url = `${this.baseUrl}/user-posts-followers/${id}`;
    const logMessage = `searching user with posts and followers w/ id=${id}`;
    return this.http.get<User>(url, this.options)
      .pipe(
        catchError(this.handleError<User>(logMessage))
      );
  }

}
