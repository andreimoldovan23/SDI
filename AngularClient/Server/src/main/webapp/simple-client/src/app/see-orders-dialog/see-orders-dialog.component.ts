import { Component, Inject, OnInit, ViewChild } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import { OrderService } from '../services/orders/order-service.service';

@Component({
  selector: 'app-see-orders-dialog',
  templateUrl: './see-orders-dialog.component.html',
  styleUrls: ['./see-orders-dialog.component.css']
})
export class SeeOrdersDialogComponent implements OnInit {
  dataSource = new MatTableDataSource();
  displayedColumns : string[] = ['status', 'time', 'coffeeName', 'coffeeOrigin'];
  clientId: number;

  @ViewChild(MatSort) sort: MatSort;
  @ViewChild(MatPaginator) paginate: MatPaginator;

  constructor(
    private ref: MatDialogRef<SeeOrdersDialogComponent>,
    @Inject(MAT_DIALOG_DATA) private data: number,
    private orderService: OrderService
  ) { 
    this.clientId = data;
  }

  ngOnInit(): void {
      this.getOrders();
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

  private getOrders() : void {
      this.orderService.filterClientOrders(this.clientId)
          .subscribe(orders => this.dataSource.data = orders);
  }

}
