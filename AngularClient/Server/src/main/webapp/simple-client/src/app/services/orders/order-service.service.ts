import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpClient } from '@angular/common/http';

import { AbstractService } from '../abstract-service';
import { ShopOrder } from '../../interfaces/shoporder';
import { catchError, tap } from 'rxjs/operators';
import { Coffee } from 'src/app/interfaces/coffee';

@Injectable({
  providedIn: 'root'
})
export class OrderService {
  private baseUrl : string = `${this.utils.baseUrl}orders`;

  constructor(
    private utils : AbstractService,
    private http : HttpClient
  ) {}

  getAllOrders() : Observable<ShopOrder[]> {
    const logMessage = "OrderService: getAll";
    const errorMessage = "getAll";
    return this.utils.getItems<ShopOrder>(this.baseUrl, logMessage, errorMessage);
  }

  getOneOrder(id: number) : Observable<ShopOrder> {
    const url = `${this.baseUrl}/${id}`;
    const logMessage = `OrderService: getOne w/ id=${id}`;
    const errorMessage = `getOne w/ id=${id}`;
    return this.utils.getOneItem<ShopOrder>(url, logMessage, errorMessage);
  }

  buyCoffee(client_id: number, coffee_id: number) : Observable<any> {
    const url = `${this.baseUrl}?clientId=${client_id}&coffeeId=${coffee_id}`;
    const logMessage = `OrderService: buyCoffee w/ client_id=${client_id} and coffee_id=${coffee_id}`;
    const errorMessage = `buyCoffee w/ client_id=${client_id} and coffee_id=${coffee_id}`;
    return this.utils.updateItem<ShopOrder>(null, url, logMessage, errorMessage);
  }

  changeStatus(order: ShopOrder) : Observable<any> {
    const url = `${this.baseUrl}/${order.id}?status=${order.status}`;
    const logMessage = `OrderService: changeStatus w/ id=${order.id}`;
    const errorMessage = `changeStatus w/ id=${order.id}`;
    return this.utils.updateItem<ShopOrder>(order, url, logMessage, errorMessage);
  }

  deleteOrdersBetweenDates(date1: string, date2: string) : Observable<any> {
    const url = `${this.baseUrl}/deleteOrdersBetweenDates?date1=${date1}&date2=${date2}`;
    const logMessage = `OrderService: deleteOrdersBetweenDates w/ date1=${date1} and date2=${date2}`;
    const errorMessage = `deleteOrdersBetweenDates w/ date1=${date1} and date2=${date2}`;
    return this.utils.deleteItem<ShopOrder>(url, logMessage, errorMessage);
  }

  filterClientCoffees(id: number) : Observable<Coffee[]> {
    const url = `${this.baseUrl}/filterClientCoffees?id=${id}`;
    const logMessage = `OrderService: filterClientCoffees w/ id=${id}`;
    const errorMessage = `filterClientCoffees w/ id=${id}`;
    return this.utils.filter<Coffee>(url, logMessage, errorMessage);
  }

  filterClientOrders(id: number) : Observable<ShopOrder[]> {
    const url = `${this.baseUrl}/filterClientOrders?id=${id}`;
    const logMessage = `OrderService: filterClientOrders w/ id=${id}`;
    const errorMessage = `filterClientOrders w/ id=${id}`;
    return this.utils.filter<ShopOrder>(url, logMessage, errorMessage);
  }
  
}
