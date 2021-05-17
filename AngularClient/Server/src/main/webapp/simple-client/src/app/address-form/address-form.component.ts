import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { Address } from '../interfaces/address';

@Component({
  selector: 'app-address-form',
  templateUrl: './address-form.component.html',
  styleUrls: ['./address-form.component.css']
})
export class AddressFormComponent implements OnInit {
  @Input() selectedAddress : Address;
  initialAddress : Address;

  @Output() updateEventEmitter = new EventEmitter<Address>();
  @Output() deleteByNumberEventEmitter = new EventEmitter<number>();
  @Output() addEventEmitter = new EventEmitter();

  constructor() { }

  ngOnInit(): void {
  }

  ngOnChanges() : void {
    this.initialAddress = JSON.parse(JSON.stringify(this.selectedAddress));
  }

  onSubmit() : void {
    const newAddr = {city: this.selectedAddress.city, 
      street: this.selectedAddress.street, number: this.selectedAddress.number} as Address;
    this.addEventEmitter.emit(newAddr);
  }

  update() : void {
    this.updateEventEmitter.emit(this.selectedAddress);
  }

  deleteByNumber() : void {
    this.deleteByNumberEventEmitter.emit(this.selectedAddress.number);
  }

  isPristine() : boolean {
    return JSON.stringify(this.initialAddress) === JSON.stringify(this.selectedAddress);
  }

}
