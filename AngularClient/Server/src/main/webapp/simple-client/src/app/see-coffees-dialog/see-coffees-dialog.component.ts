import { Component, Inject, OnInit, ViewChild } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import { OrderService } from '../services/orders/order-service.service';

@Component({
  selector: 'app-see-coffees-dialog',
  templateUrl: './see-coffees-dialog.component.html',
  styleUrls: ['./see-coffees-dialog.component.css']
})
export class SeeCoffeesDialogComponent implements OnInit {
  dataSource = new MatTableDataSource();
  displayedColumns : string[] = ['name', 'origin', 'price', 'quantity'];
  clientId: number;

  @ViewChild(MatSort) sort: MatSort;
  @ViewChild(MatPaginator) paginate: MatPaginator;

  constructor(
    private ref: MatDialogRef<SeeCoffeesDialogComponent>,
    @Inject(MAT_DIALOG_DATA) private data: number,
    private orderService: OrderService
  ) { 
    this.clientId = data;
  }

  ngOnInit(): void {
      this.getCoffees();
  }

  ngAfterViewInit() {
    this.dataSource.sort = this.sort;
    this.dataSource.paginator = this.paginate;
  }

  applyFilter(filterValue: string) {
    filterValue = filterValue.trim();
    filterValue = filterValue.toLowerCase();
    this.dataSource.filter = filterValue;
  }

  private getCoffees() : void {
      this.orderService.filterClientCoffees(this.clientId)
          .subscribe(coffees => this.dataSource.data = coffees);
  }
 
}
