import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import {NotesComponent} from "./notes/notes.component";
import {Login} from "./authorization/login/login.component";
import {RegisterComponent} from "./authorization/register/register.component";
import {AuthorizationComponent} from "./authorization/authorization.component";
import {AuthGuard} from "./auth.guard";
import {AllNotesComponent} from "./all-notes/all-notes.component";


const routes: Routes = [
  { path: 'account-login', component: Login },
  { path: 'account-register', component: RegisterComponent },
  {path: 'account', component: AuthorizationComponent, canActivate: [AuthGuard]},
  { path: 'notes', component: NotesComponent , canActivate: [AuthGuard]},
  {path: 'all-notes', component: AllNotesComponent, canActivate: [AuthGuard]},
  { path: '**',
    redirectTo: 'account',
    pathMatch: 'full'
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {}
