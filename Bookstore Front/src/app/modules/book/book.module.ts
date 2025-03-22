import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router'; 

import { BookListComponent } from '../../components/book-list/book-list.component';
import { BookDetailsComponent } from '../../components/book-details/book-details.component';
import { ReactiveFormsModule } from '@angular/forms';

@NgModule({
  declarations: [
    BookListComponent,
    BookDetailsComponent
  ],
  imports: [
    CommonModule, 
    RouterModule,
    ReactiveFormsModule   
  ],
  exports: [
    BookListComponent,
    BookDetailsComponent  
  ]
})
export class BookModule { }

