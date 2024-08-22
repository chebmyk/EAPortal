import { Component } from '@angular/core';
import {AvatarModule} from "primeng/avatar";
import {CardModule} from "primeng/card";
import {DecimalPipe} from "@angular/common";
import {MeterGroupModule} from "primeng/metergroup";

@Component({
  selector: 'app-agent-dashboard',
  standalone: true,
    imports: [
        AvatarModule,
        CardModule,
        DecimalPipe,
        MeterGroupModule
    ],
  templateUrl: './agent-dashboard.component.html',
  styleUrl: './agent-dashboard.component.scss'
})
export class AgentDashboardComponent {

}
