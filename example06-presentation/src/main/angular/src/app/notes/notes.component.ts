import { Component, OnInit } from '@angular/core';
import { NoteService } from '../services/note.service';
import {Note} from "../models/note";
import {Router} from "@angular/router";
import {AccountService} from "../services/account.service";
import {NoteWithUser} from "../models/noteWithUser";

@Component({
  selector: 'wt2-notes',
  templateUrl: './notes.component.html',
  styleUrls: ['./notes.component.sass'],
  providers: [NoteService]

})
export class NotesComponent implements OnInit {

  private notes: Note[] = [];

  private user = null
  constructor(protected noteService: NoteService,private router: Router,private accountService: AccountService) {
    this.accountService.user.subscribe(x => this.user = x);
  }

  ngOnInit(): void {
    if (this.user == null){
      this.router.navigate(['../account-login']);
    }
    this.load();
  }
  get getNoteWithUser() : NoteWithUser []{
    let notesWU: NoteWithUser[] = [];
    for (const note of this.notes) {
      notesWU.push(new NoteWithUser(note,this.user));
    }
    return notesWU;
  }
  load(): void {
    this.noteService.getMyNotes().subscribe({
      next: note => this.notes = note ,
    });
  }

}
