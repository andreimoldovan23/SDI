import { SelectionModel } from '@angular/cdk/collections';
import { Component, OnInit, ViewChild } from '@angular/core';
import { MatDialogRef } from '@angular/material/dialog';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import { Address } from '../interfaces/address';
import { AddressService } from '../services/addresses/address-service.service';

@Component({
  selector: 'app-choose-address-dialog',
  templateUrl: './choose-address-dialog.component.html',
  styleUrls: ['./choose-address-dialog.component.css']
})
export class ChooseAddressDialogComponent implements OnInit {
  dataSource = new MatTableDataSource();
  displayedColumns: string[] = ['select', 'city', 'street', 'number'];
  selection = new SelectionModel<Address>(false, []);
  selectedAddress: Address;

  @ViewChild(MatSort) sort: MatSort;
  @ViewChild(MatPaginator) paginate: MatPaginator;

  constructor(
      private ref: MatDialogRef<ChooseAddressDialogComponent>,
      private addrService: AddressService) { }

  ngOnInit(): void {
      this.addrService.getAllAddresses()
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
    this.selectedAddress = this.selection.selected[0];
  }

  applyFilter(filterValue: string) {
    filterValue = filterValue.trim();
    filterValue = filterValue.toLowerCase();
    this.dataSource.filter = filterValue;
  }

}
