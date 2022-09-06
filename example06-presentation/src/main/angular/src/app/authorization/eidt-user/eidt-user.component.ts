import { Component, OnInit } from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {ActivatedRoute, Router} from "@angular/router";
import {AccountService} from "../../services/account.service";
import {AlertService} from "../../services/alert.service";
import {first} from "rxjs/operators";

@Component({
  selector: 'wt2-eidt-user',
  templateUrl: './eidt-user.component.html',
  styleUrls: ['./eidt-user.component.sass']
})
export class EidtUserComponent implements OnInit {

  form: FormGroup;
  id: string;
  loading = false;
  submitted = false;

  constructor(
    private formBuilder: FormBuilder,
    private route: ActivatedRoute,
    private router: Router,
    private accountService: AccountService,
    private alertService: AlertService
  ) {}

  ngOnInit() {
    /**this.id = this.route.snapshot.params['id'];*/
    const passwordValidators = [Validators.minLength(6)];

      passwordValidators.push(Validators.required);


    this.form = this.formBuilder.group({
      firstName: ['', Validators.required],
      lastName: ['', Validators.required],
      password: ['', passwordValidators]
    });


    this.f['firstName'].setValue( this.accountService.userValue.firstName);
    this.f['lastName'].setValue( this.accountService.userValue.lastName);
    this.f['password'].setValue( this.accountService.loginPasswordSubject.value);

  }

  // convenience getter for easy access to form fields
  get f() { return this.form.controls; }

  onSubmit() {
    this.submitted = true;

    // reset alerts on submit
    this.alertService.clear();

    // stop here if form is invalid
    if (this.form.invalid) {
      return;
    }

    this.loading = true;

      this.updateUser();

  }


  private updateUser() {
    this.accountService.update( this.form.value)
      .pipe(first())
      .subscribe(
        data => {
          this.alertService.success('Update successful', { keepAfterRouteChange: true });
          console.log("success")
          this.loading = false;
        },
        error => {
          this.alertService.error(error);
          this.loading = false;
        });
  }

}
