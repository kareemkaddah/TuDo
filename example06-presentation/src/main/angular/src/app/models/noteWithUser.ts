import {Note} from "./note";
import {User} from "./user";

export class NoteWithUser {
  note:Note;
  user: User;
  constructor(n: Note, u:User) {
    this.user = u;
    this.note = n;
  }
}
