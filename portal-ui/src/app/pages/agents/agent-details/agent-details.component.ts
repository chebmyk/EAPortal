import {Component, OnInit} from '@angular/core';
import {MenuItem} from "primeng/api";
import {TabMenuModule} from "primeng/tabmenu";
import {ActivatedRoute, Router} from "@angular/router";
import {CommonModule} from "@angular/common";
import {AvatarModule} from "primeng/avatar";
import {Agent} from "../../../model/agents";
import {AgentsService} from "../agents.service";

@Component({
  selector: 'app-agent-details',
  standalone: true,
  imports: [
    TabMenuModule, CommonModule, AvatarModule
  ],
  templateUrl: './agent-details.component.html',
  styleUrl: './agent-details.component.scss'
})
export class AgentDetailsComponent implements OnInit {

  agent!: Agent ;
  tabs: MenuItem[] | undefined;


  constructor(
    private router: Router,
    private activatedRoute: ActivatedRoute,
    private agentsService: AgentsService
  ) {}


  ngOnInit(): void {

    console.log("router params", this.activatedRoute.snapshot.params)
    let id = this.activatedRoute.snapshot.params['id'];

    this.agentsService.getAgent(id)
      .subscribe({
        next: res => {
          console.log(res)
          this.agent = res;
        },
        error: error => {
          console.log("Error loading agent details", error)
        }
      })

    this.tabs = [
      {
        label: 'Dashboard',
        icon: 'pi pi-chart-line',
        command: () => {
          this.router.navigate(['dashboard'], {relativeTo: this.activatedRoute});
        }
      },
      {
        label: 'Agent Info',
        icon: 'pi pi-list',
        command: () => {
          this.router.navigate(['agent-info'], {relativeTo: this.activatedRoute});
        }
      }
    ]
  }
}
