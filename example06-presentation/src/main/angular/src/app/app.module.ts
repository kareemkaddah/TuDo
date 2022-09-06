import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';

import { NotesComponent } from './notes/notes.component';

import { CreateNoteComponent } from './notes/create-note/create-note.component';

import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import {MAT_DATE_LOCALE, MatOptionModule} from "@angular/material/core";
import {MatSelectModule} from "@angular/material/select";
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import {MatDatepickerModule} from "@angular/material/datepicker";
import {MatNativeDateModule} from '@angular/material/core';
import { RegisterComponent } from './authorization/register/register.component';
import { AuthorizationComponent } from './authorization/authorization.component';
import {Login} from "./authorization/login/login.component";
import { ItemsComponent } from './notes/items/items.component';
import { EidtUserComponent } from './authorization/eidt-user/eidt-user.component';
import { AllNotesComponent } from './all-notes/all-notes.component';


@NgModule({
  declarations: [
    AppComponent,
    NotesComponent,
    CreateNoteComponent,
    RegisterComponent,
    AuthorizationComponent,
    Login,
    ItemsComponent,
    EidtUserComponent,
    AllNotesComponent



  ],
    imports: [
        BrowserModule,
        AppRoutingModule,
        NgbModule,
        FormsModule,
        HttpClientModule,

        MatInputModule,
        MatFormFieldModule,
        MatOptionModule,
        MatSelectModule,
        BrowserAnimationsModule,
        MatDatepickerModule,

        MatNativeDateModule,

      ReactiveFormsModule,

    ],
  providers: [{ provide: MAT_DATE_LOCALE, useValue: 'en-GB' }],
  bootstrap: [AppComponent]
})
export class AppModule { }
