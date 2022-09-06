import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {first} from 'rxjs/operators';

import {AlertService} from '../../services/alert.service';
import {AccountService} from "../../services/account.service";


@Component({
  selector: 'wt2-register',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.sass']
})
export class Login implements OnInit {
  loginForm: FormGroup;
  loading = false;
  submitted = false;
  returnUrl: string;

  errorMessage: string = "";


  constructor(
    private formBuilder: FormBuilder,
    private route: ActivatedRoute,
    private router: Router,
    private accountService: AccountService,
    private alertService: AlertService
  ) {
    // redirect to home if already logged in
    if (this.accountService.userValue) {
      this.router.navigate(['../account']);
    }
  }

  ngOnInit() {
    this.loginForm = this.formBuilder.group({
      username: ['', Validators.required],
      password: ['', Validators.required]
    });

    this.returnUrl = '../account';
  }

  // convenience getter for easy access to form fields
  get f() {
    return this.loginForm.controls;
  }

  onSubmit() {
    this.submitted = true;

    // reset alerts on submit
    this.alertService.clear();

    // stop here if form is invalid
    if (this.loginForm.invalid) {
      return;
    }

    this.loading = true;
    this.accountService.login(this.f["username"].value, this.f["password"].value)
      .pipe(first())
      .subscribe(
        data => {
          this.errorMessage ="";
          this.router.navigate([this.returnUrl]);
        },
        error => {
          //this.alertService.error(error);
          this.errorMessage = "failed to login";
          this.loading = false;
        });
  }
}
