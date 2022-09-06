export class Note {
  createdOn: Date;
  deadline: Date;
  owner: number;
  priority: number;
  content: string;
  id: number;

  static fromObject(object: any): Note {
    const n = new Note();
    n.owner = object.owner;
    n.id = object.id;
    n.content = object.content;
    n.createdOn = new Date(object.createdOn);
    n.priority = object.priority;
    n.deadline = object.deadline;
    return n;
  }
}
