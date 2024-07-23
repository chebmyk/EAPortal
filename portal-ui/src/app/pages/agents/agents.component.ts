import {Component, OnInit} from '@angular/core';
import {AgentsService} from "./agents.service";
import {AgentCardComponent} from "./agent-card/agent-card.component";
import {CommonModule, NgFor} from "@angular/common";
import {Agent} from "../../model/agents";

@Component({
  selector: 'agents',
  standalone: true,
  imports: [AgentCardComponent, NgFor],
  templateUrl: './agents.component.html',
  styleUrl: './agents.component.scss'
})
export class AgentsComponent implements OnInit{

  agents!: Agent[];

  constructor(
    private agentService: AgentsService
  ) {
  }

  ngOnInit(): void {

    this.agentService.getAgents()
      .subscribe({
        next: (agents: Agent[]) =>  this.agents = agents ,
        error: (error: any) => console.log(error)
      }
      );
  }

}
