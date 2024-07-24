import { Routes } from '@angular/router';
import {AgentsComponent} from "./pages/agents/agents.component";
import {AgentsService} from "./pages/agents/agents.service";
import {AgentDetailsComponent} from "./pages/agents/agent-details/agent-details.component";

export const routes: Routes = [
  { path: 'agents',
    component: AgentsComponent
  },
  { path: 'agents/:id',
    component: AgentDetailsComponent
  },
  // {
  //   path: 'items',
  //   loadChildren: () => import('./items/items.module').then(m => m.ItemsModule)
  // },
  { path: '',
    redirectTo: '/agents', pathMatch: 'full'
  }
];


