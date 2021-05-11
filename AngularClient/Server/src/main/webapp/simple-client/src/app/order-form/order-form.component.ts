import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { MatDialog, MatDialogConfig } from '@angular/material/dialog';
import { ChooseClientDialogComponent } from '../choose-client-dialog/choose-client-dialog.component';
import { ChooseCoffeeDialogComponent } from '../choose-coffee-dialog/choose-coffee-dialog.component';
import { ShopOrder } from '../interfaces/shoporder';

@Component({
  selector: 'app-order-form',
  templateUrl: './order-form.component.html',
  styleUrls: ['./order-form.component.css']
})
export class OrderFormComponent implements OnInit {
  @Input() selectedOrder : ShopOrder;
  @Output() statusEventEmitter = new EventEmitter<ShopOrder>();
  @Output() deleteDatesEventEmitter = new EventEmitter();
  @Output() buyEventEmitter = new EventEmitter();

  clientId : number;
  coffeeId : number;

  constructor(private dialog: MatDialog) { }

  ngOnInit(): void {
  }

  changeStatus() : void {
    const newOrder = {id: this.selectedOrder.id, status: this.selectedOrder.status} as ShopOrder;
    this.statusEventEmitter.emit(newOrder);
  }

  deleteBetweenDates(date1: string, date2: string) : void {
    if (date1.match("[0-9]{4}-[0-9]{2}-[0-9]{2}//[0-9]{2}::[0-9]{2}::[0-9]{2}")
        && date2.match("[0-9]{4}-[0-9]{2}-[0-9]{2}//[0-9]{2}::[0-9]{2}::[0-9]{2}")) {
        const object = {date1: date1, date2: date2};
        this.deleteDatesEventEmitter.emit(object);
    } else {
      window.alert("Invalid timestamp format. Hover over date to check format");
    }
  }

  buy() : void {
    if (this.clientId != null && this.coffeeId != null) {
      const object = {clientId: this.clientId, coffeeId: this.coffeeId};
      this.buyEventEmitter.emit(object);
      this.clientId = null;
      this.coffeeId = null;
    } else {
      window.alert("You have to select a client and a coffee");
    }
  }

  openClientDialog() : void {
    const dialogConfig = new MatDialogConfig();
    dialogConfig.width = '600px';
    dialogConfig.height = '600px';
    dialogConfig.hasBackdrop = true;
    dialogConfig.disableClose = true;
    dialogConfig.autoFocus = true;

    const dialogRef = this.dialog.open(ChooseClientDialogComponent, dialogConfig);
    dialogRef.afterClosed()
        .subscribe(data => {
          if(data != null) {
            this.clientId = data.id;
          }
        });
  }

  openCoffeeDialog() : void {
    const dialogConfig = new MatDialogConfig();
    dialogConfig.width = '600px';
    dialogConfig.height = '600px';
    dialogConfig.hasBackdrop = true;
    dialogConfig.disableClose = true;
    dialogConfig.autoFocus = true;

    const dialogRef = this.dialog.open(ChooseCoffeeDialogComponent, dialogConfig);
    dialogRef.afterClosed()
        .subscribe(data => {
          if(data != null) {
            this.coffeeId = data.id;
          }
        });
  }

}
