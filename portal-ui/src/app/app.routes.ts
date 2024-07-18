import { Routes } from '@angular/router';
import {AgentsComponent} from "./pages/agents/agents.component";

export const routes: Routes = [
  { path: 'agents',
    component: AgentsComponent
  },
  // {
  //   path: 'items',
  //   loadChildren: () => import('./items/items.module').then(m => m.ItemsModule)
  // },
  { path: '',
    redirectTo: '/agents', pathMatch: 'full'
  }
];


