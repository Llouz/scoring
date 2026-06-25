import { Routes } from '@angular/router';

import { CoachmarkComponent } from './components/coachmarks/coachmarks.component';
import { GuideComponent } from './components/guides/guides.component';
import { UserComponent } from './components/users/users.component';

export const routes: Routes = [
  { path: '', redirectTo: '/users', pathMatch: 'full' },
  { path: 'users', component: UserComponent },
  { path: 'guides', component: GuideComponent },
  { path: 'coachmarks', component: CoachmarkComponent },
  { path: '**', redirectTo: '/users', pathMatch: 'full' }
];
