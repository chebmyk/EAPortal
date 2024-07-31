import { Routes } from '@angular/router';
import {AgentsComponent} from "./pages/agents/agents.component";
import {AgentsService} from "./pages/agents/agents.service";
import {AgentDetailsComponent} from "./pages/agents/agent-details/agent-details.component";

export const routes: Routes = [
  { path: 'agents',
    component: AgentsComponent
  },
  {
    path: 'agents/:id',
    loadChildren: () =>
      import('./pages/agents/agent-details/agent-details.routes')
        .then(m => m.AGENT_DETAILS_ROUTE)
  },

  { path: '',
    redirectTo: '/agents', pathMatch: 'full'
  }
];


