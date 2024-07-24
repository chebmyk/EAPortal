import {Component, Input, OnInit} from '@angular/core';
import {Agent} from "../../../model/agents";
import {CardModule} from "primeng/card";
import {MeterGroupModule} from "primeng/metergroup";
import {TooltipModule} from "primeng/tooltip";
import {AvatarModule} from "primeng/avatar";
import {AgentsService} from "../agents.service";
import {DecimalPipe} from "@angular/common";
import {ActivatedRoute, Router} from "@angular/router";
import {Ripple} from "primeng/ripple";

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
export class AgentCardComponent implements OnInit {

  @Input()
  agent!: Agent;
  memoryUsageBoard: any;
  mem_available: number = 0;
  mem_used: number = 0;
  mem_total: number = 0;
  mem_current: number = 0;



  constructor(
    private agentsService: AgentsService,
    private route: ActivatedRoute,
    private router: Router

  ) {
  }

  ngOnInit(): void {

    this.mem_total = this.agent.payload.metadata["memory.total"];

    this.agentsService.getMemoryStream("http://localhost:8083/zookeeper/stream/memory")
      .subscribe({
        next: (event: any) => {
          this.mem_current = event['data']
          this.mem_available = this.mem_current/this.mem_total*100;
          this.mem_used = 100 - this.mem_available;

          this.memoryUsageBoard = [
            { label: 'Used', color: 'rgb(246,213,0)', value: this.mem_used },
            { label: 'Available', color: 'rgba(15,159,236,0.89)', value: this.mem_available }
          ];

        },
        error: (error: any) => console.log(error)
      })
  }


  gotoDetails(id: string) {
    this.router.navigate([`/agents/${this.agent.id}`]);
  }
}
