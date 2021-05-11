import { SelectionModel } from '@angular/cdk/collections';
import { Component, OnInit, ViewChild } from '@angular/core';
import { MatDialogRef } from '@angular/material/dialog';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import { Client } from '../interfaces/client';
import { ClientService } from '../services/clients/client-service.service';

@Component({
  selector: 'app-choose-client-dialog',
  templateUrl: './choose-client-dialog.component.html',
  styleUrls: ['./choose-client-dialog.component.css']
})
export class ChooseClientDialogComponent implements OnInit {
  dataSource = new MatTableDataSource();
  displayedColumns: string[] = ['select', 'firstName', 'lastName', 'age', 'phoneNumber'];
  selection = new SelectionModel<Client>(false, []);
  selectedClient: Client;

  @ViewChild(MatSort) sort: MatSort;
  @ViewChild(MatPaginator) paginate: MatPaginator;

  constructor(
      private ref: MatDialogRef<ChooseClientDialogComponent>,
      private clientService: ClientService) { }

  ngOnInit(): void {
      this.clientService.getAllClients()
          .subscribe(clients => this.dataSource.data = clients);
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
    this.selectedClient = this.selection.selected[0];
  }

  applyFilter(filterValue: string) {
    filterValue = filterValue.trim();
    filterValue = filterValue.toLowerCase();
    this.dataSource.filter = filterValue;
  }

}
