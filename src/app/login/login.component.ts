import { AfterViewInit, Component, OnDestroy, OnInit } from '@angular/core';
import { FormGroup, FormControl } from '@angular/forms';  // Use FormGroup and FormControl
import { Router } from '@angular/router';
import { Subject } from 'rxjs';
import { AppService } from '../app.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit, OnDestroy, AfterViewInit {
  public loginForm = new FormGroup({
    username: new FormControl(''),
    password: new FormControl(''),
 
  });
  public loginError: any = '';
  public assetsUrl: any;
  public isPasswordVisible = false;
  public redirectUrl: any;

  private unsubscribe: Subject<void> = new Subject<void>();
  constructor(
    private router: Router,
    private appService: AppService,
  ) {}

  ngOnInit() {
  }
  ngAfterViewInit(): void {
  }

 ngOnDestroy(): void {
    // Clean up subscriptions
    this.unsubscribe.next();
    this.unsubscribe.complete();
  }
  

  
login(): void {
  if (this.loginForm.invalid) {
    // Mark all fields as touched to show validation errors
    this.loginForm.markAllAsTouched();
    return;
  }

  // Get login info from form
  const loginInfo = {
    username: this.loginForm.value.username,
    password: this.loginForm.value.password,
  };

  // Trigger login action
  this.appService.emitEventUpdate(true);
  this.appService.login(loginInfo).subscribe({
    next: (response) => {
      const token = response?.token; // Token is in the "token" field of the response
      if (token) {
        // Store the token in localStorage
        localStorage.setItem('authToken', token);
        console.log('Login success:', response);
        this.router.navigate(['/dashboard']);// Navigate to dashboard
      }
    },
    error: (err) => {
      this.handleLoginError(err);
    },
    complete: () => {
      // Stop loading state after login is completed
      this.appService.emitEventUpdate(false);
    },
  });
}

  handleLoginError(err: any) {
    throw new Error('Method not implemented.');
  }

  get username() {
    return this.loginForm.get('username');
  }

  get password() {
    return this.loginForm.get('password');
  }

  togglePasswordVisibility() {
    this.isPasswordVisible = !this.isPasswordVisible;
  }
}
