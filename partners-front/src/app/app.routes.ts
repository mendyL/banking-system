
import { Routes } from '@angular/router';
import { MainLayoutComponent } from './layout/main-layout/main-layout.component';

export const routes: Routes = [
  {
    path: '',
    component: MainLayoutComponent,
    children: [
      {
        path: '',
        redirectTo: 'messages',
        pathMatch: 'full'
      },
      {
        path: 'home',
        loadComponent: () => import('./features/home/home.component')
          .then(m => m.HomeComponent),
        title: 'Home'
      },
      {
        path: 'messages',
        loadComponent: () => import('./features/messages/message-list/message-list.component')
          .then(m => m.MessageListComponent),
        title: 'Messages'
      },
      {
        path: 'partners',
        loadComponent: () => import('./features/partners/partner-list/partner-list.component')
          .then(m => m.PartnerListComponent),
        title: 'Partners'
      }
    ]
  },
  {
    path: '**',
    redirectTo: 'messages'
  }
];
