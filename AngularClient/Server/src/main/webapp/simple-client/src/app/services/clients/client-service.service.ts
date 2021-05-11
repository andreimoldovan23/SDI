import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

import { AbstractService } from '../abstract-service';
import { Client } from '../../interfaces/client';


@Injectable({
  providedIn: 'root'
})
export class ClientService {
  private baseUrl : string = `${this.utils.baseUrl}clients`;

  constructor(
    private utils : AbstractService
  ) {}

  getAllClients() : Observable<Client[]> {
    const logMessage = "ClientService: getAll";
    const errorMessage = "getAll";
    return this.utils.getItems<Client>(this.baseUrl, logMessage, errorMessage);
  }

  getOneClient(id: number) : Observable<Client> {
    const url = `${this.baseUrl}/${id}`;
    const logMessage = `ClientService: getOne w/ id=${id}`;
    const errorMessage = `getOne w/ id=${id}`;
    return this.utils.getOneItem<Client>(url, logMessage, errorMessage);
  }

  addClient(client: Client) : Observable<any> {
    const logMessage = `ClientService: addClient w/ name=${client.firstName}`;
    const errorMessage = `addClient w/ name=${client.firstName}`;
    return this.utils.addItem<Client>(client, this.baseUrl, logMessage, errorMessage);
  }

  updateClient(client: Client) : Observable<any> {
    const url = `${this.baseUrl}/${client.id}`;
    const logMessage = `ClientService: updateClient w/ id=${client.id}`;
    const errorMessage = `updateClient w/ id=${client.id}`;
    return this.utils.updateItem<Client>(client, url, logMessage, errorMessage);
  }

  deleteClient(id: number) : Observable<any> {
    const url = `${this.baseUrl}/${id}`;
    const logMessage = `ClientService: deleteClient w/ id=${id}`;
    const errorMessage = `deleteClient w/ id=${id}`;
    return this.utils.deleteItem<Client>(url, logMessage, errorMessage);
  }

  howManyReport(id: number) : Observable<number> {
    const url = `${this.baseUrl}/report?id=${id}`;
    const logMessage = `ClientService: howManyReport w/ id=${id}`;
    const errorMessage = `howManyReport w/ id=${id}`;
    return this.utils.howManyReport(url, logMessage, errorMessage);
  }

  filterClientsByName(name: string) : Observable<Client[]> {
    const url = `${this.baseUrl}/filterClients?name=${name}`;
    const logMessage = `ClientService: filterClientsByName w/ name=${name}`;
    const errorMessage = `filterClientsByName w/ name=${name}`;
    return this.utils.filter<Client>(url, logMessage, errorMessage);
  }

  filterClientsByAge(age1: number, age2: number) : Observable<Client[]> {
    const url = `${this.baseUrl}/filterClientsByAge?age1=${age1}&age2=${age2}`;
    const logMessage = `ClientService: filterClientsByAge w/ age1=${age1} and age2=${age2}`;
    const errorMessage = `filterClientsByAge w/ age1=${age1} and age2=${age2}`;
    return this.utils.filter<Client>(url, logMessage, errorMessage);
  }

}
