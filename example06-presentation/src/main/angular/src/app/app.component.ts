import { Component } from '@angular/core';
import {User} from "./models/user";
import {AccountService} from "./services/account.service";

@Component({
  selector: 'wt2-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.sass']
})
export class AppComponent {
  user:User = null
  constructor(private accountService: AccountService) {
    this.accountService.user.subscribe(x => this.user = x);
  }
}
