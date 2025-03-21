import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router'; // ✅ Import RouterModule

import { BookListComponent } from '../../components/book-list/book-list.component';
import { BookDetailsComponent } from '../../components/book-details/book-details.component';
import { ReactiveFormsModule } from '@angular/forms';

@NgModule({
  declarations: [
    BookListComponent,
    BookDetailsComponent
  ],
  imports: [
    CommonModule,  // ✅ Required for ngIf, ngFor, etc.
    RouterModule,
    ReactiveFormsModule     // ✅ Required for routing
  ],
  exports: [
    BookListComponent,
    BookDetailsComponent  // ✅ Export components if used outside this module
  ]
})
export class BookModule { }

