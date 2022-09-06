import { Component, OnInit } from '@angular/core';
import {User} from "../models/user";
import {AccountService} from "../services/account.service";
import {ActivatedRoute, Router} from "@angular/router";

@Component({
  selector: 'wt2-authorization',
  templateUrl: './authorization.component.html',
  styleUrls: ['./authorization.component.sass']
})
export class AuthorizationComponent  implements OnInit{


  public user: User;
  users:User[] = [];
  constructor(private accountService: AccountService,private router: Router,
              private route: ActivatedRoute) {
    this.accountService.user.subscribe(x => this.user = x);
  }

  ngOnInit(): void {
    if (this.user == null){
      this.router.navigate(['../account-login']);
    }
    this.accountService.refresh();
  }


  logout() {
    this.accountService.logout();
  }
}
