import { SelectionModel } from '@angular/cdk/collections';
import { Component, OnInit, ViewChild } from '@angular/core';
import { MatDialogRef } from '@angular/material/dialog';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import { Coffee } from '../interfaces/coffee';
import { CoffeeService } from '../services/coffees/coffee-service.service';

@Component({
  selector: 'app-choose-coffee-dialog',
  templateUrl: './choose-coffee-dialog.component.html',
  styleUrls: ['./choose-coffee-dialog.component.css']
})
export class ChooseCoffeeDialogComponent implements OnInit {
  dataSource = new MatTableDataSource();
  displayedColumns: string[] = ['select', 'name', 'origin', 'price', 'quantity'];
  selection = new SelectionModel<Coffee>(false, []);
  selectedCoffee: Coffee;

  @ViewChild(MatSort) sort: MatSort;
  @ViewChild(MatPaginator) paginate: MatPaginator;

  constructor(
      private ref: MatDialogRef<ChooseCoffeeDialogComponent>,
      private coffeeService: CoffeeService) { }

  ngOnInit(): void {
      this.coffeeService.getAllCoffees()
          .subscribe(addresses => this.dataSource.data = addresses);
  }

  ngAfterViewInit() : void {
    this.dataSource.sort = this.sort;
    this.dataSource.paginator = this.paginate;
  }

  onNoClick(): void {
    this.ref.close(null);
  }

  toggleRow(row) : void {
    this.selection.toggle(row);
    this.selectedCoffee = this.selection.selected[0];
  }

  applyFilter(filterValue: string) {
    filterValue = filterValue.trim();
    filterValue = filterValue.toLowerCase();
    this.dataSource.filter = filterValue;
  }

}
