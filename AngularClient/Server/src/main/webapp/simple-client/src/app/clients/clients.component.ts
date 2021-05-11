import { Component, OnInit } from '@angular/core';
import { Client } from '../interfaces/client';
import { ClientService } from '../services/clients/client-service.service';

@Component({
  selector: 'app-clients',
  templateUrl: './clients.component.html',
  styleUrls: ['./clients.component.css']
})
export class ClientsComponent implements OnInit {
  clients : Client[] = [];
  selectedClient : Client;
  cloneSelectedClient: Client;

  constructor(private clientService : ClientService) { }

  ngOnInit(): void {
      this.getClients();
  }

  select(client : Client) : void {
      if (client !== this.selectedClient) {
        this.selectedClient = client;
        this.cloneSelectedClient = {id: this.selectedClient.id, firstName: this.selectedClient.firstName,
            lastName: this.selectedClient.lastName, age: this.selectedClient.age, phoneNumber: this.selectedClient.phoneNumber,
            address: this.selectedClient.address} as Client;
      } else {
        this.selectedClient = null;
        this.cloneSelectedClient = null;
      }
  }

  add(clie : Client) : void {
    this.reset();
    if (clie.phoneNumber == "") clie.phoneNumber = null;
    this.clientService.addClient(clie)
        .subscribe(_ => this.getClients());
  }

  delete(client : Client) : void {
    this.reset();
    this.clients = this.clients.filter(clie => clie !== client);
    this.clientService.deleteClient(client.id)
        .subscribe();
  }

  update(clie : Client) : void {
    if (clie.phoneNumber == "") clie.phoneNumber = null;
    this.clients[this.clients.indexOf(this.selectedClient)] = clie;
    this.selectedClient = clie;
    this.clientService.updateClient(this.selectedClient)
        .subscribe();
  }
  
  trivia() : void {
    this.clientService.howManyReport(this.selectedClient.id)
        .subscribe(no => window.alert(`This client has ordered ${no} coffees`));
  }

  filterClientsByName(term: string) : void {
      this.reset();
      this.clientService.filterClientsByName(term)
          .subscribe(clients => {
             this.clients = clients;
             this.sort();
          });
  }

  filterClientsByAge(ageTuple) : void {
      this.reset();
      this.clientService.filterClientsByAge(ageTuple.age1, ageTuple.age2)
          .subscribe(clients => {
              this.clients = clients;
              this.sort();
          });
  }

  removeFilters() {
    this.reset();
    this.getClients();
  }

  private sort() : void {
    this.clients.sort((a, b) => a.id - b.id);
  }

  private reset() : void {
    this.selectedClient = null;
  }

  private getClients() : void {
    this.clientService.getAllClients()
        .subscribe(clients => { this.clients = clients; this.sort(); });
  }

}
