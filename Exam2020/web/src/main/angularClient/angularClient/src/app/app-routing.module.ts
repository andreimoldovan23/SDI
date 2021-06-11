import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { SensorsComponent } from './sensors/sensors.component';

const routes: Routes = [
  {path: "", component: SensorsComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
