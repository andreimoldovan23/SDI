import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { FormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { MatDialogModule } from '@angular/material/dialog';
import { MatTableModule } from '@angular/material/table';
import { MatSortModule } from '@angular/material/sort';
import { MatPaginatorModule } from '@angular/material/paginator';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { MatInputModule } from '@angular/material/input';
 
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { AddressesComponent } from './addresses/addresses.component';
import { ClientsComponent } from './clients/clients.component';
import { CoffeesComponent } from './coffees/coffees.component';
import { OrdersComponent } from './orders/orders.component';
import { MessagesComponent } from './messages/messages.component';
import { ClientFormComponent } from './client-form/client-form.component';
import { AddressFormComponent } from './address-form/address-form.component';
import { CoffeeFormComponent } from './coffee-form/coffee-form.component';
import { OrderFormComponent } from './order-form/order-form.component';
import { ClientFilterComponent } from './client-filter/client-filter.component';
import { CoffeeFilterComponent } from './coffee-filter/coffee-filter.component';
import { ChooseAddressDialogComponent } from './choose-address-dialog/choose-address-dialog.component';
import { ChooseClientDialogComponent } from './choose-client-dialog/choose-client-dialog.component';
import { ChooseCoffeeDialogComponent } from './choose-coffee-dialog/choose-coffee-dialog.component';
import { SeeOrdersDialogComponent } from './see-orders-dialog/see-orders-dialog.component';
import { SeeCoffeesDialogComponent } from './see-coffees-dialog/see-coffees-dialog.component';

@NgModule({
  declarations: [
    AppComponent,
    AddressesComponent,
    ClientsComponent,
    CoffeesComponent,
    OrdersComponent,
    MessagesComponent,
    ClientFormComponent,
    AddressFormComponent,
    CoffeeFormComponent,
    OrderFormComponent,
    ClientFilterComponent,
    CoffeeFilterComponent,
    ChooseAddressDialogComponent,
    ChooseClientDialogComponent,
    ChooseCoffeeDialogComponent,
    SeeOrdersDialogComponent,
    SeeCoffeesDialogComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    FormsModule,
    BrowserAnimationsModule,
    MatDialogModule,
    MatTableModule,
    MatSortModule,
    MatPaginatorModule,
    MatCheckboxModule,
    MatInputModule
  ],
  entryComponents: [
    ChooseAddressDialogComponent,
    ChooseClientDialogComponent,
    ChooseCoffeeDialogComponent,
    SeeCoffeesDialogComponent,
    SeeOrdersDialogComponent
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
