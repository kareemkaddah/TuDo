import { Component, OnInit } from '@angular/core';
import {NoteService} from "../services/note.service";
import {Router} from "@angular/router";
import {AccountService} from "../services/account.service";
import {User} from "../models/user";
import {NoteWithUser} from "../models/noteWithUser";
import {Note} from "../models/note";

@Component({
  selector: 'wt2-all-notes',
  templateUrl: './all-notes.component.html',
  styleUrls: ['./all-notes.component.sass']
})
export class AllNotesComponent implements OnInit {


  private user:User = null
  private notes: Note[] = [];
  private users: User[] = [];
  public errorMessage: string;


  constructor(protected noteService: NoteService,private router: Router,private accountService: AccountService) {
    this.accountService.user.subscribe(x => this.user = x);
    this.load();

  }

  ngOnInit(): void {
    if (this.user == null || this.user.role != "ADMIN" ){
      this.router.navigate(['../account-login']);
    }
    this.errorMessage = null;
    this.load();
  }

  load(): void {
    this.accountService.getAllUsers().subscribe({
      next: users => {
        this.users = users;
      }
    });
    this.noteService.getAll().subscribe({
      next: notes => {
        this.notes = notes;
        }
      ,
      error: x => this.errorMessage = "Could not Load all Notes"
    });

  }

  get getNoteWithUser() : NoteWithUser []{
    let notesWU: NoteWithUser[] = [];
    for (const note of this.notes) {
      notesWU.push(new NoteWithUser(note,
        this.users.find((u) => u.id == note.owner)
    ));
    }
    return notesWU;
  }
}
