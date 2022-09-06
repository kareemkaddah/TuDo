import {Component, EventEmitter, Input, OnChanges, OnInit, Output, SimpleChanges} from '@angular/core';
import {NoteService} from "../../services/note.service";
import {NoteWithUser} from "../../models/noteWithUser";
import {BehaviorSubject} from "rxjs";

@Component({
  selector: 'wt2-items',
  templateUrl: './items.component.html',
  styleUrls: ['./items.component.css']
})
export class ItemsComponent implements OnInit, OnChanges {

  @Output()
  public updated = new EventEmitter();
  @Input()
  notes: NoteWithUser[];
  @Input()
  adminMode: boolean;

  selectedNote: NoteWithUser;
  updateRequested: boolean
  priorities: string[] = ['low', 'medium', 'high', 'urgent'];
  public errorMessage: string;

  constructor(private noteService: NoteService) {
    this.updateRequested = false
    this.adminMode = false;
  }

  ngOnChanges(changes: SimpleChanges): void {
        if(changes['notes']){

        }
    }

  ngOnInit(): void {
    this.errorMessage = null;
    this.updateRequested = false
  }


  onSelect(item: NoteWithUser): void{
    this.selectedNote = item;
  }

  removeItem(item: NoteWithUser): void {

    this.noteService.remove(item.note.id).subscribe(
      {next: () =>{
          this.updated.emit();
          this.selectedNote = null;
          this.errorMessage = null;
          this.updateRequested = false;
        },
        error: () =>{this.errorMessage = 'Could not remove note';}
      }
    );

  }

  requestUpdateItem():void{
    this.updateRequested = true;
  }


  noteUpdated() {
    this.selectedNote = null;
    this.updateRequested = false;
    this.errorMessage = null;
    this.updated.emit()
  }


}
