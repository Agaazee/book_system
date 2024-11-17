import { NgModule } from '@angular/core';
import { BrowserModule, Title } from '@angular/platform-browser';
import { LoginComponent } from './login/login.component';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { LocationStrategy, HashLocationStrategy } from '@angular/common';
import { HttpClientModule } from '@angular/common/http';
import { AppService } from './app.service';
import { DashboardComponent } from './dashboard/dashboard.component';
import { CommonModule } from '@angular/common';  // Import CommonModule
import { MatDialogModule } from '@angular/material/dialog';
@NgModule({
  declarations: [AppComponent, LoginComponent,  DashboardComponent],
  imports: [
    AppRoutingModule,
    BrowserModule,FormsModule,ReactiveFormsModule,HttpClientModule, HttpClientModule, CommonModule,
    MatDialogModule

  ],
  providers: [
    { provide: LocationStrategy, useClass: HashLocationStrategy },
    Title
  ],
  bootstrap: [AppComponent],
})
export class AppModule {}
