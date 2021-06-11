import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, of } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { SensorList } from '../interfaces/SensorList';

@Injectable({
  providedIn: 'root'
})
export class SensorsService {
  baseUrl : string = "http://localhost:8080/api/";

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

  getSensorNames() : Observable<SensorList> {
    const url = `${this.baseUrl}sensors`;
    const logMessage = "getting all sensor names";
    return this.http.get<SensorList>(url, this.options)
      .pipe(
        catchError(this.handleError<SensorList>(logMessage))
      );
  }

  getSensorDetails(name: string) : Observable<SensorList> {
    const url = `${this.baseUrl}sensors/${name}`;
    const logMessage = `getting latest measurements for ${name}`;
    return this.http.get<SensorList>(url, this.options)
      .pipe(
        catchError(this.handleError<SensorList>(logMessage))
      );
  }

  killSensor(name: string) : Observable<any> {
    const url = `${this.baseUrl}sensors/${name}/kill`;
    const logMessage = `killing sensor ${name}`;
    return this.http.post<any>(url, null, this.options)
      .pipe(
        catchError(this.handleError<any>(logMessage))
      );
  }

}
