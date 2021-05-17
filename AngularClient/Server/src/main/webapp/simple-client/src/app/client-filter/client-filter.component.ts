import { Component, OnInit, Output, EventEmitter } from '@angular/core';

@Component({
  selector: 'app-client-filter',
  templateUrl: './client-filter.component.html',
  styleUrls: ['./client-filter.component.css']
})
export class ClientFilterComponent implements OnInit {
  @Output() nameEventEmitter = new EventEmitter<string>();
  @Output() ageEventEmitter = new EventEmitter();
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

  searchAge(age1, age2) : void {
    if (age1 != null && age2 != null && age1 !== "" && age2 !== "") {
      const object = {
        age1: age1,
        age2: age2
      };
      this.ageEventEmitter.emit(object);
    } else {
      window.alert("You need to input the age limits");
    }
  }

  reset() : void {
    this.resetEventEmitter.emit();
  }

}
