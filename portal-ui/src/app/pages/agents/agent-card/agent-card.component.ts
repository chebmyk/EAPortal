import {Component, Input} from '@angular/core';
import {Agent} from "../../../model/agents";
import {CardModule} from "primeng/card";

@Component({
  selector: 'agent-card',
  standalone: true,
  imports: [
    CardModule
  ],
  templateUrl: './agent-card.component.html',
  styleUrl: './agent-card.component.css'
})
export class AgentCardComponent {

  @Input()
  agent!: Agent;



}
