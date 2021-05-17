import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { MatDialog, MatDialogConfig } from '@angular/material/dialog';
import * as moment from 'moment';
import { range } from 'rxjs';
import { ChooseClientDialogComponent } from '../choose-client-dialog/choose-client-dialog.component';
import { ChooseCoffeeDialogComponent } from '../choose-coffee-dialog/choose-coffee-dialog.component';
import { DateRange } from '../interfaces/dateRange';
import { ShopOrder } from '../interfaces/shoporder';

@Component({
  selector: 'app-order-form',
  templateUrl: './order-form.component.html',
  styleUrls: ['./order-form.component.css']
})
export class OrderFormComponent implements OnInit {
  @Input() selectedOrder : ShopOrder;
  initialStatus: string;

  @Output() statusEventEmitter = new EventEmitter<ShopOrder>();
  @Output() deleteDatesEventEmitter = new EventEmitter();
  @Output() buyEventEmitter = new EventEmitter();

  clientId : number;
  coffeeId : number;

  range : DateRange = {date1: '', date2: ''};

  constructor(private dialog: MatDialog) {
  }

  ngOnInit(): void {
  }

  ngOnChanges() : void {
    this.initialStatus = this.selectedOrder.status;
  }

  changeStatus() : void {
    const newOrder = {id: this.selectedOrder.id, status: this.selectedOrder.status} as ShopOrder;
    this.statusEventEmitter.emit(newOrder);
  }

  deleteBetweenDates() : void {
      this.deleteDatesEventEmitter.emit(this.range);
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

  datesMatch() : boolean {
    return this.range.date1 === this.range.date2 && !(this.range.date1 === '');
  }

  checkDates() : boolean {
    if (this.range.date1 === '' || this.range.date2 === '') return true;
    var newDate1 = moment(this.range.date1, "yyyy-MM-dd//HH::mm::ss").toDate();
    var newDate2 = moment(this.range.date2, "yyyy-MM-dd//HH::mm::ss").toDate();
    return newDate1 <= newDate2;
  }

  statusNotChanged() : boolean {
    return this.initialStatus === this.selectedOrder.status;
  }

}
