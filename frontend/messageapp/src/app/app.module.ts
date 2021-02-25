import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import {NgxDatatableModule} from '@swimlane/ngx-datatable';
import { HttpClientModule } from '@angular/common/http';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { SweetAlert2Module } from '@sweetalert2/ngx-sweetalert2';
import { AppComponent } from './app.component';

@NgModule({
  declarations: [
    AppComponent
  ],
  imports: [
    BrowserModule,
    NgxDatatableModule,
    HttpClientModule,
    FormsModule,
    ReactiveFormsModule,
    SweetAlert2Module.forRoot(),    
  ],
  providers: [],
  bootstrap: [AppComponent],
  exports: [SweetAlert2Module]
})
export class AppModule { }
