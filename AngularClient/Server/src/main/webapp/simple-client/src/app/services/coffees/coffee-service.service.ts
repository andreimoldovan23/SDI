import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

import { AbstractService } from '../abstract-service';
import { Coffee } from '../../interfaces/coffee';

@Injectable({
  providedIn: 'root'
})
export class CoffeeService {
  private baseUrl : string = `${this.utils.baseUrl}coffees`;

  constructor(
    private utils : AbstractService
  ) {}

  getAllCoffees() : Observable<Coffee[]> {
    const logMessage = "CoffeeService: getAll";
    const errorMessage = "getAll";
    return this.utils.getItems<Coffee>(this.baseUrl, logMessage, errorMessage);
  }

  getOneCoffee(id: number) : Observable<Coffee> {
    const url = `${this.baseUrl}/${id}`;
    const logMessage = `CoffeeService: getOne w/ id=${id}`;
    const errorMessage = `getOne w/ id=${id}`;
    return this.utils.getOneItem<Coffee>(url, logMessage, errorMessage);
  }

  addCoffee(coffee: Coffee) : Observable<any> {
    const logMessage = `CoffeeService: addCoffee w/ name=${coffee.name}`;
    const errorMessage = `addCoffee w/ name=${coffee.name}`;
    return this.utils.addItem<Coffee>(coffee, this.baseUrl, logMessage, errorMessage);
  }

  updateCoffee(coffee: Coffee) : Observable<any> {
    const url = `${this.baseUrl}/${coffee.id}`;
    const logMessage = `CoffeeService: updateCoffee w/ id=${coffee.id}`;
    const errorMessage = `updateCoffee w/ id=${coffee.id}`;
    return this.utils.updateItem<Coffee>(coffee, url, logMessage, errorMessage);
  }

  deleteCoffee(id: number) : Observable<any> {
    const url = `${this.baseUrl}/${id}`;
    const logMessage = `CoffeeService: deleteCoffee w/ id=${id}`;
    const errorMessage = `deleteCoffee w/ id=${id}`;
    return this.utils.deleteItem<Coffee>(url, logMessage, errorMessage);
  }

  howManyReport(id: number) : Observable<number> {
    const url = `${this.baseUrl}/report?id=${id}`;
    const logMessage = `CoffeeService: howManyReport w/ id=${id}`;
    const errorMessage = `howManyReport w/ id=${id}`;
    return this.utils.howManyReport(url, logMessage, errorMessage);
  }

  filterCoffeesByName(name: string) : Observable<Coffee[]> {
    const url = `${this.baseUrl}/filterCoffees?name=${name}`;
    const logMessage = `CoffeeService: filterCoffeesByName w/ name=${name}`;
    const errorMessage = `filterCoffeesByName w/ name=${name}`;
    return this.utils.filter<Coffee>(url, logMessage, errorMessage);
  }

  filterCoffeesByOrigin(origin: string) : Observable<Coffee[]> {
    const url = `${this.baseUrl}/filterCoffeesByOrigin?origin=${origin}`;
    const logMessage = `CoffeeService: filterCoffeesByAge w/ origin=${origin}`;
    const errorMessage = `filterCoffeesByOrigin w/ origin=${origin}`;
    return this.utils.filter<Coffee>(url, logMessage, errorMessage);
  }
  
}
