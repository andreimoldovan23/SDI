import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { Coffee } from '../interfaces/coffee';

@Component({
  selector: 'app-coffee-form',
  templateUrl: './coffee-form.component.html',
  styleUrls: ['./coffee-form.component.css']
})
export class CoffeeFormComponent implements OnInit {
  @Input() selectedCoffee : Coffee;
  initialCoffee : Coffee;

  @Output() updateEventEmitter = new EventEmitter<Coffee>();
  @Output() addEventEmitter = new EventEmitter();

  constructor() { }

  ngOnInit(): void {
  }

  ngOnChanges() : void {
    this.initialCoffee = JSON.parse(JSON.stringify(this.selectedCoffee));
  }

  onSubmit() : void {
    const newCoffee = {name: this.selectedCoffee.name, origin: this.selectedCoffee.origin, price: this.selectedCoffee.price, quantity: this.selectedCoffee.quantity} as Coffee;
    this.addEventEmitter.emit(newCoffee);
  }

  update() : void {
    this.updateEventEmitter.emit(this.selectedCoffee);
  }

  isPristine() : boolean {
    return this.initialCoffee.name === this.selectedCoffee.name && this.initialCoffee.origin === this.selectedCoffee.origin;
  }

}
