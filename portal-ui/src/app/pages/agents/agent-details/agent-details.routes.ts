import { Routes } from '@angular/router';
import {AgentDetailsComponent} from "./agent-details.component";
import {AgentDashboardComponent} from "./agent-dashboard/agent-dashboard.component";
import {AgentInfoComponent} from "./agent-info/agent-info.component";

export const AGENT_DETAILS_ROUTE: Routes = [
  { path: '',
    component: AgentDetailsComponent,
    children: [
      // { path: '',redirectTo: 'dashboard',pathMatch: 'full'},
      {
        path: 'dashboard',
        component: AgentDashboardComponent
      },
      {
        path: 'agent-info',
        component: AgentInfoComponent
      },
    ]
  },


  // {
  //   path: 'items',
  //   loadChildren: () => import('./items/items.module').then(m => m.ItemsModule)
  // },

];


