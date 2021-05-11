import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { MatDialog, MatDialogConfig } from '@angular/material/dialog';
import { ChooseAddressDialogComponent } from '../choose-address-dialog/choose-address-dialog.component';
import { Client } from '../interfaces/client';
import { SeeCoffeesDialogComponent } from '../see-coffees-dialog/see-coffees-dialog.component';
import { SeeOrdersDialogComponent } from '../see-orders-dialog/see-orders-dialog.component';

@Component({
  selector: 'app-client-form',
  templateUrl: './client-form.component.html',
  styleUrls: ['./client-form.component.css']
})
export class ClientFormComponent implements OnInit {
  @Input() selectedClient : Client;
  @Output() updateEventEmitter = new EventEmitter<Client>();
  @Output() addEventEmitter = new EventEmitter();

  constructor(private dialog: MatDialog) { }

  ngOnInit(): void {
  }

  onSubmit() : void {
    const newClient = {firstName: this.selectedClient.firstName, lastName: this.selectedClient.lastName, age: this.selectedClient.age,
        phoneNumber: this.selectedClient.phoneNumber, address: this.selectedClient.address} as Client;
    this.addEventEmitter.emit(newClient);
  }

  update() : void {
    this.updateEventEmitter.emit(this.selectedClient);
  }

  openDialog() : void {
      const dialogConfig = new MatDialogConfig();
      dialogConfig.autoFocus = true;
      dialogConfig.width = '600px';
      dialogConfig.height = '600px';
      dialogConfig.hasBackdrop = true;
      dialogConfig.disableClose = true;
      
      const dialogRef = this.dialog.open(ChooseAddressDialogComponent, dialogConfig);
      dialogRef.afterClosed()
          .subscribe(data => {
            if (data != null)
                this.selectedClient.address = data;
          });
  }

  checkClientCoffees() : void {
    const dialogConfig = new MatDialogConfig();
    dialogConfig.autoFocus = true;
    dialogConfig.width = '600px';
    dialogConfig.height = '400px';
    dialogConfig.hasBackdrop = true;
    dialogConfig.disableClose = false;
    dialogConfig.data = this.selectedClient.id;

    this.dialog.open(SeeCoffeesDialogComponent, dialogConfig);
  }

  checkClientOrders() : void {
    const dialogConfig = new MatDialogConfig();
    dialogConfig.autoFocus = true;
    dialogConfig.width = '800px';
    dialogConfig.height = '400px';
    dialogConfig.hasBackdrop = true;
    dialogConfig.disableClose = false;
    dialogConfig.data = this.selectedClient.id;

    this.dialog.open(SeeOrdersDialogComponent, dialogConfig);
  }

}
