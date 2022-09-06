import {Component, EventEmitter, Input, NgModule, OnInit, Output} from '@angular/core';
import {NoteService} from "../../services/note.service";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {Note} from "../../models/note";


export interface Priority {
  value: number;
  viewValue: string;
}

@Component({
  selector: 'wt2-create-note',
  templateUrl: './create-note.component.html',
  styleUrls: ['./create-note.component.sass']
})
export class CreateNoteComponent implements OnInit  {


  @Output()
  public created = new EventEmitter();
  @Output()
  public canceled = new EventEmitter;
  @Input()
  public updateMode:boolean;
  @Input()
  public selectedNote: Note;

  priorities: Priority[] = [
    {value: 1, viewValue: 'low'},
    {value: 2, viewValue: 'medium'},
    {value: 3, viewValue: 'high'},
    {value: 4, viewValue: 'urgent'}
  ];

  public selectedPriority: number = 0;
  public content: string = "";
  public deadline: Date;
  public errorMessage: string;


  constructor(private noteService: NoteService) {

  }

  ngOnInit(): void {
    if (this.selectedNote && this.updateMode){
      this.selectedPriority = this.selectedNote.priority;
      this.content = this.selectedNote.content;
      this.deadline = null;
    }
  }



  public createNote(e: Event): void {
    e.preventDefault();
    this.errorMessage = null;
      if (this.selectedPriority <= 4 && this.selectedPriority>0 && this.content.trim() != null) {
        if (this.updateMode){
          this.noteService.update(this.selectedNote.id,this.selectedPriority,this.content,this.deadline)
            .subscribe(
              {next: () =>{
                  this.created.emit();
                  this.selectedPriority = 0
                  this.content = '';
                  this.deadline = null;
                },
                error: () =>{this.errorMessage = 'Could not update note';}
              }
        );
        }else {
          this.noteService.create(this.selectedPriority, this.content, this.deadline).subscribe({
            next: () => {
              this.created.emit();
              this.selectedPriority = 0;
              this.content = '';
              this.deadline = null;
            },
            error: () => this.errorMessage = 'Could not create note'
          });
        }
    }else {
      setTimeout(()=> {this.errorMessage = "Invalid note";},0);
    }
  }

  getCharsLeft(): number {
    return 255 - this.content.length;
  }

  get canCreate(): boolean {
    return this.getCharsLeft() > 0 && this.getCharsLeft() < 255 && this.selectedPriority > 0 && this.selectedPriority<5 && this.deadline != null;
  }

}
