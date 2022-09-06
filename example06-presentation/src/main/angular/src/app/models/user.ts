import {Note} from "./note";

export class User{
  username:string;
  firstName:string;
  lastName:string;
  role: string;
  id: number;
  password: string;
  token: string;
  notes: Note[]


  static fromObject(object: any): User {
    const n = new User();

    n.password = object.password;
    n.role = object.role;
    n.username = object.username;
    n.firstName = object.firstName;
    n.lastName = object.lastName;
    n.id = object.id;
    n.token = object.token;
    n.notes = object.notes;
    return n;
  }
}
