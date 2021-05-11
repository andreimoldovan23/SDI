import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

import { AbstractService } from '../abstract-service';
import { Address } from '../../interfaces/address';

@Injectable({
  providedIn: 'root'
})
export class AddressService {
  private baseUrl : string = `${this.utils.baseUrl}addresses`;

  constructor(
    private utils : AbstractService
  ) {}

  getAllAddresses() : Observable<Address[]> {
    const logMessage = "AddressService: getAll";
    const errorMessage = "getAll";
    return this.utils.getItems<Address>(this.baseUrl, logMessage, errorMessage);
  }

  getOneAddress(id: number) : Observable<Address> {
    const url = `${this.baseUrl}/${id}`;
    const logMessage = `AddressService: getOne w/ id=${id}`;
    const errorMessage = `getOne w/ id=${id}`;
    return this.utils.getOneItem<Address>(url, logMessage, errorMessage);
  }

  addAddress(address: Address) : Observable<any> {
    const logMessage = `AddressService: addAddress w/ city=${address.city}`;
    const errorMessage = `addAddress w/ city=${address.city}`;
    return this.utils.addItem<Address>(address, this.baseUrl, logMessage, errorMessage);
  }

  updateAddress(address: Address) : Observable<any> {
    const url = `${this.baseUrl}/${address.id}`;
    const logMessage = `AddressService: updateAddress w/ id=${address.id}`;
    const errorMessage = `updateAddress w/ id=${address.id}`;
    return this.utils.updateItem<Address>(address, url, logMessage, errorMessage);
  }

  deleteAddress(id: number) : Observable<any> {
    const url = `${this.baseUrl}/${id}`;
    const logMessage = `AddressService: deleteAddress w/ id=${id}`;
    const errorMessage = `deleteAddress w/ id=${id}`;
    return this.utils.deleteItem<Address>(url, logMessage, errorMessage);
  }

  deleteAddressesWithNumber(no: number) : Observable<any> {
    const url = `${this.baseUrl}/deleteAddressesByNumber?number=${no}`;
    const logMessage = `AddressService: deleteAddressesWithNumber w/ no=${no}`;
    const errorMessage = `deleteAddressesWithNumber w/ no=${no}`;
    return this.utils.deleteItem<Address>(url, logMessage, errorMessage);
  }

  howManyReport(id: number) : Observable<number> {
    const url = `${this.baseUrl}/report?id=${id}`;
    const logMessage = `AddressService: howManyReport w/ id=${id}`;
    const errorMessage = `howManyReport w/ id=${id}`;
    return this.utils.howManyReport(url, logMessage, errorMessage);
  }

}
