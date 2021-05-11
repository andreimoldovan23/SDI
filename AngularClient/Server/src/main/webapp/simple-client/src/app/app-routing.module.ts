import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AddressesComponent } from './addresses/addresses.component';
import { ClientsComponent } from './clients/clients.component';
import { CoffeesComponent } from './coffees/coffees.component';
import { MessagesComponent } from './messages/messages.component';
import { OrdersComponent } from './orders/orders.component';

const routes: Routes = [
  {path: "orders", component: OrdersComponent},
  {path: "coffees", component: CoffeesComponent},
  {path: "clients", component: ClientsComponent},
  {path: "addresses", component: AddressesComponent},
  {path: "", component: MessagesComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
