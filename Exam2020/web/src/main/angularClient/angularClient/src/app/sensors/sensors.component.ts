import { Component, OnDestroy, OnInit } from '@angular/core';
import { Subscription } from 'rxjs';
import { SensorsService } from '../services/sensors.service';
import { interval } from 'rxjs';
import { SensorList } from '../interfaces/SensorList';

@Component({
  selector: 'app-sensors',
  templateUrl: './sensors.component.html',
  styleUrls: ['./sensors.component.css']
})
export class SensorsComponent implements OnInit, OnDestroy {
  show: boolean = false;
  namesList: SensorList;
  sensors = [];
  subscription: Subscription;

  constructor(private service: SensorsService) { }

  ngOnInit(): void {
    this.getNames();
  }

  ngOnDestroy() : void {
    this.subscription && this.subscription.unsubscribe();
  }

  getNames() : void {
    this.service.getSensorNames()
      .subscribe(result => this.namesList = result);
  }

  getSensorDetails(name: string) : void {
    this.service.getSensorDetails(name)
      .subscribe(result => this.sensors[name] = result);
  }

  kill(name: string) : void {
    this.service.killSensor(name)
      .subscribe();
  }

  updateSensors() : void {
    this.namesList.measurements.forEach(m => {
      this.getSensorDetails(m.name);
    });
  }

  getUpdates() : void {
    this.show = !this.show;
    if (this.show) {
      const source = interval(10000);
      this.subscription = source.subscribe(_ => this.updateSensors());
    } else {
      this.subscription && this.subscription.unsubscribe();
    }
  }

}
