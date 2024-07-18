import {Component, Input} from '@angular/core';
import {Agent} from "../../../model/agents";

@Component({
  selector: 'agent-card',
  standalone: true,
  imports: [],
  templateUrl: './agent-card.component.html',
  styleUrl: './agent-card.component.css'
})
export class AgentCardComponent {

  @Input()
  agent!: Agent;



}
