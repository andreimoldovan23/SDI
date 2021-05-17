import { Component, EventEmitter, OnInit, Output } from '@angular/core';

@Component({
  selector: 'app-coffee-filter',
  templateUrl: './coffee-filter.component.html',
  styleUrls: ['./coffee-filter.component.css']
})
export class CoffeeFilterComponent implements OnInit {
  @Output() nameEventEmitter = new EventEmitter<string>();
  @Output() originEventEmitter = new EventEmitter<string>();
  @Output() resetEventEmitter = new EventEmitter();

  constructor() { }

  ngOnInit(): void {
  }

  searchName(term: string) : void {
    // if (term != "")
      this.nameEventEmitter.emit(term);
    // else 
    //   window.alert("You need to input a name");
  }

  searchOrigin(term: string) : void {
    // if (term != "")
      this.originEventEmitter.emit(term);
    // else
    //   window.alert("You need to input an origin");
  }

  reset() : void {
    this.resetEventEmitter.emit();
  }

}
