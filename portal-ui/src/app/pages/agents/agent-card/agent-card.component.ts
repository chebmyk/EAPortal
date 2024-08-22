import {Component, Input, OnDestroy, OnInit} from '@angular/core';
import {Agent} from "../../../model/agents";
import {CardModule} from "primeng/card";
import {MeterGroupModule} from "primeng/metergroup";
import {TooltipModule} from "primeng/tooltip";
import {AvatarModule} from "primeng/avatar";
import {AgentsService} from "../agents.service";
import {DecimalPipe} from "@angular/common";
import {Router} from "@angular/router";
import {Ripple} from "primeng/ripple";
import {Subscription} from "rxjs";

@Component({
  selector: 'agent-card',
  standalone: true,
  imports: [
    CardModule,
    MeterGroupModule,
    TooltipModule,
    AvatarModule,
    DecimalPipe,
    Ripple,
  ],
  templateUrl: './agent-card.component.html',
  styleUrl: './agent-card.component.scss'
})
export class AgentCardComponent implements OnInit, OnDestroy {

  private sseStream!: Subscription;

  @Input()
  agent!: Agent;
  memoryUsageBoard=  [
    { label: 'Used', color: 'rgb(246,213,0)', value: 0 },
    { label: 'Available', color: 'rgba(15,159,236,0.89)', value: 0 }
  ];
  mem_available: number = 0;
  mem_used: number = 0;
  mem_total: number = 0;
  mem_current: number = 0;
  current_cpu_load=0;


  cpuUsageBoard=  [
    { label: 'Load', color: 'rgb(13,230,13)', value: this.current_cpu_load*100 },
    { label: 'Available', color: 'rgba(246,241,0,0.89)', value: 100-this.current_cpu_load*100 }
  ];


  constructor(
    private agentsService: AgentsService,
    private router: Router
  ) {
  }

  ngOnInit(): void {

    console.log("Agent", this.agent)
    this.mem_total = this.agent.metadata["memory.total"];


    this.sseStream = this.agentsService.getAgentSystemLoad(this.agent.instanceId)
      .subscribe({
        next: (event: any) => {
          console.log(event)
          if (event['lastEventId'] == "memory") {
            this.mem_current = event['data']
            this.mem_available = this.mem_current/this.mem_total*100;
            this.mem_used = 100 - this.mem_available;
            this.memoryUsageBoard = [
              { label: 'Used', color: 'rgb(246,213,0)', value: this.mem_used },
              { label: 'Available', color: 'rgba(15,159,236,0.89)', value: this.mem_available }
            ];
          }
          if (event['lastEventId'] == "cpu") {
            this.current_cpu_load = event['data']
          }
        },
        error: (error: any) => console.log(error)
      })
  }

  ngOnDestroy() {
    if (this.sseStream) {
      this.sseStream.unsubscribe();
    }
  }

  gotoDetails(id: string) {
    this.router.navigate([`/agents/${this.agent.instanceId}`]);
  }

}
