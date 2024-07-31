import {Component, OnInit} from '@angular/core';
import {MenuItem} from "primeng/api";
import {TabMenuModule} from "primeng/tabmenu";
import {ActivatedRoute, Router} from "@angular/router";
import {CommonModule} from "@angular/common";

@Component({
  selector: 'app-agent-details',
  standalone: true,
  imports: [
    TabMenuModule, CommonModule
  ],
  templateUrl: './agent-details.component.html',
  styleUrl: './agent-details.component.scss'
})
export class AgentDetailsComponent implements OnInit {

  tabs: MenuItem[] | undefined;

  constructor(
    private router: Router,
    private route: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.tabs = [
      {
        label: 'Dashboard',
        icon: 'pi pi-chart-line',
        command: () => {
          this.router.navigate(['dashboard'], {relativeTo: this.route});
        }
      },
      // { label: 'Application',
      //   icon: 'pi pi-link',
      //   command: () => {
      //     this.router.navigate(['application'], {relativeTo: this.route});
      //   }
      // },
      {
        label: 'Agent Info',
        icon: 'pi pi-list',
        command: () => {
          this.router.navigate(['agent-info'], {relativeTo: this.route});
        }
      }
    ]
  }
}
