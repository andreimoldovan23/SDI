import { Component, OnInit } from '@angular/core';
import { ShopOrder } from '../interfaces/shoporder';
import { OrderService } from '../services/orders/order-service.service';

@Component({
  selector: 'app-orders',
  templateUrl: './orders.component.html',
  styleUrls: ['./orders.component.css']
})
export class OrdersComponent implements OnInit {
  orders : ShopOrder[] = [];
  selectedOrder : ShopOrder;
  cloneSelectedOrder : ShopOrder;

  constructor(private orderService : OrderService) { }

  ngOnInit(): void {
      this.getOrders();
  }

  select(order : ShopOrder) : void {
      if (order !== this.selectedOrder) {
        this.selectedOrder = order;
        this.cloneSelectedOrder = {id: this.selectedOrder.id, status: this.selectedOrder.status,
            time: this.selectedOrder.time, client: this.selectedOrder.client, coffee: this.selectedOrder.coffee} as ShopOrder;
      } else {
        this.selectedOrder = null;
        this.cloneSelectedOrder = null;
      }
  }

  changeStatus(order: ShopOrder) : void {
    this.selectedOrder.status = order.status;
    this.orderService.changeStatus(order)
        .subscribe();
  }

  buy(event) : void {
    this.reset();
    this.orderService.buyCoffee(event.clientId, event.coffeeId)
        .subscribe(_ => this.getOrders());
  }

  deleteBetweenDates(event) : void {
    this.reset();
    this.orderService.deleteOrdersBetweenDates(event.date1, event.date2)
        .subscribe(_ => this.getOrders());
  }
 
  private sort() : void {
    this.orders.sort((a, b) => a.id - b.id);
  }

  private reset() : void {
    this.selectedOrder = null;
  }

  private getOrders() : void {
    this.orderService.getAllOrders()
        .subscribe(orders => { this.orders = orders; this.sort(); });
  }
}
