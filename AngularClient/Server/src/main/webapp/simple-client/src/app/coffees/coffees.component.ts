import { Component, OnInit } from '@angular/core';
import { Coffee } from '../interfaces/coffee';
import { CoffeeService } from '../services/coffees/coffee-service.service';

@Component({
  selector: 'app-coffees',
  templateUrl: './coffees.component.html',
  styleUrls: ['./coffees.component.css']
})
export class CoffeesComponent implements OnInit {
  coffees : Coffee[] = [];
  selectedCoffee : Coffee;
  cloneSelectedCoffee: Coffee;

  constructor(private coffeeService : CoffeeService) { }

  ngOnInit(): void {
      this.getCoffees();
  }

  select(coffee : Coffee) : void {
      if (coffee !== this.selectedCoffee) {
        this.selectedCoffee = coffee;
        this.cloneSelectedCoffee = {id: this.selectedCoffee.id, name: this.selectedCoffee.name,
            origin: this.selectedCoffee.origin, price: this.selectedCoffee.price, quantity: this.selectedCoffee.quantity} as Coffee;
      } else {
        this.selectedCoffee = null;
        this.cloneSelectedCoffee = null;
      }
  }

  add(cof : Coffee) : void {
    this.reset();
    this.coffeeService.addCoffee(cof)
        .subscribe(_ => this.getCoffees());
  }

  delete(cof : Coffee) : void {
    this.reset();
    this.coffees = this.coffees.filter(coffee => cof !== coffee);
    this.coffeeService.deleteCoffee(cof.id)
        .subscribe();
  }

  update(cof : Coffee) : void {
    this.coffees[this.coffees.indexOf(this.selectedCoffee)] = cof;
    this.selectedCoffee = cof;
    this.coffeeService.updateCoffee(this.selectedCoffee)
        .subscribe();
  }
  
  trivia() : void {
    this.coffeeService.howManyReport(this.selectedCoffee.id)
        .subscribe(no => window.alert(`This coffee has been ordered by ${no} clients`));
  }

  filterCoffeesByName(term: string) : void {
      this.reset();
      this.coffeeService.filterCoffeesByName(term)
          .subscribe(coffees => {
             this.coffees = coffees;
             this.sort();
          });
  }

  filterCoffeesByOrigin(term: string) : void {
      this.reset();
      this.coffeeService.filterCoffeesByOrigin(term)
          .subscribe(coffees => {
              this.coffees = coffees;
              this.sort();
          });
  }

  removeFilters() {
    this.reset();
    this.getCoffees();
  }

  private sort() : void {
    this.coffees.sort((a, b) => a.id - b.id);
  }

  private reset() : void {
    this.selectedCoffee = null;
  }

  private getCoffees() : void {
    this.coffeeService.getAllCoffees()
        .subscribe(coffees => { this.coffees = coffees; this.sort(); });
  }

}
