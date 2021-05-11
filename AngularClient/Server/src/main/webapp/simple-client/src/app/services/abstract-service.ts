import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, of } from 'rxjs';
import { catchError, tap } from 'rxjs/operators';

import { MessageService } from "./messages/message-service.service";

@Injectable({
    providedIn: "root"
})
export class AbstractService {
    baseUrl : string = "http://localhost:8080/api/";

    options = {
        headers: new HttpHeaders({"Content-Type": "application/json"})
    };
    
    constructor(private messageService : MessageService, private http : HttpClient) {}

    log(message : string) : void {
        this.messageService.addMessage(message);
    }

    handleError<T>(operations="operations", result ?: T) {
        return (error : any) : Observable<T> => {
            window.alert(error.error);
            this.log(`${operations} failed: ${error.error}`);
            return of(result as T);
        }
    }

    getItems<T>(url : string, logMessage : string, operationName : string) : Observable<T[]> {
        return this.http.get<T[]>(url)
            .pipe(
              tap(_ => this.log(logMessage)),
              catchError(this.handleError<T[]>(operationName, []))
            );
    }
    
    getOneItem<T>(url : string, logMessage : string, operationName : string) : Observable<T> {
        return this.http.get<T>(url)
          .pipe(
            tap(_ => this.log(logMessage)),
            catchError(this.handleError<T>(operationName))
          );
    }
    
    updateItem<T>(item: T, url: string, logMessage: string, operationName: string): Observable<any> {
        return this.http.put(url, item, this.options)
          .pipe(
              tap(_ => this.log(logMessage)),
              catchError(this.handleError<T>(operationName))
          );
    }
    
    addItem<T>(item: T, url: string, logMessage: string, operationName: string): Observable<any> {
        return this.http.post<T>(url, item, this.options)
          .pipe(
              tap((newItem: T) => this.log(logMessage)),
              catchError(this.handleError<T>(operationName))
          );
    }
    
    deleteItem<T>(url: string, logMessage: string, operationName: string): Observable<any> {
        return this.http.delete<T>(url, this.options)
          .pipe(
              tap(_ => this.log(logMessage)),
              catchError(this.handleError<T>(operationName))
        );
    }

    howManyReport(url: string, logMessage: string, operationName: string): Observable<number> {
        return this.http.get<number>(url, this.options)
            .pipe(
                tap(_ => this.log(logMessage)),
                catchError(this.handleError<number>(operationName))
        );
    }

    filter<T>(url: string, logMessage: string, operationName: string): Observable<T[]> {
        return this.http.get<T[]>(url, this.options)
            .pipe(
                tap(_ => this.log(logMessage)),
                catchError(this.handleError<T[]>(operationName, []))
        );
    }
    
}