import { Component, OnInit } from '@angular/core';

import { AddressService } from '../services/addresses/address-service.service';
import { Address } from '../interfaces/address';

@Component({
  selector: 'app-addresses',
  templateUrl: './addresses.component.html',
  styleUrls: ['./addresses.component.css']
})
export class AddressesComponent implements OnInit {
  addresses : Address[] = [];
  selectedAddress : Address;
  cloneSelectedAddress: Address;

  constructor(private addressService : AddressService) { }

  ngOnInit(): void {
      this.getAddresses();
  }

  getAddresses() : void {
    this.addressService.getAllAddresses()
        .subscribe(addresses => { this.addresses = addresses; this.sort(); });
  }

  select(address : Address) : void {
      if (address !== this.selectedAddress) {
        this.selectedAddress = address;
        this.cloneSelectedAddress = {id: this.selectedAddress.id, city: this.selectedAddress.city,
            street: this.selectedAddress.street, number: this.selectedAddress.number} as Address;
      } else {
        this.selectedAddress = null;
        this.cloneSelectedAddress = null;
      }
  }

  private reset() : void {
    this.selectedAddress = null;
  }

  add(addr : Address) : void {
    this.reset();
    this.addressService.addAddress(addr)
        .subscribe(_ => this.getAddresses());
  }

  delete(address : Address) : void {
    this.reset();
    this.addresses = this.addresses.filter(addr => addr !== address);
    this.addressService.deleteAddress(address.id)
        .subscribe();
  }

  update(addr : Address) : void {
    this.addresses[this.addresses.indexOf(this.selectedAddress)] = addr;
    this.selectedAddress = addr;
    this.addressService.updateAddress(this.selectedAddress)
        .subscribe();
  }

  deleteByNumber(no : number) : void {
    this.reset();
    this.addresses = this.addresses.filter(addr => addr.number !== no);
    this.addressService.deleteAddressesWithNumber(no)
        .subscribe();
  }

  trivia() : void {
    this.addressService.howManyReport(this.selectedAddress.id)
        .subscribe(no => window.alert(`${no} clients live here`));
  }

  private sort() : void {
    this.addresses.sort((a, b) => a.id - b.id);
  }

}
