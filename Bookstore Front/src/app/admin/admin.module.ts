import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpClientModule } from '@angular/common/http';
import { AdminRoutingModule } from './admin-routing.module';
import { AdminPanelComponent } from './admin-panel/admin-panel.component';
import { DashboardComponent } from './dashboard/dashboard.component';
import { ManageBooksComponent } from './manage-books/manage-books.component';
import { ManageUsersComponent } from './manage-users/manage-users.component';
import { FormsModule } from '@angular/forms';
import { ManageOrdersComponent } from './manage-orders/manage-orders.component';


@NgModule({
  declarations: [
    AdminPanelComponent,
    DashboardComponent,
    ManageUsersComponent,
    ManageBooksComponent,
    ManageOrdersComponent
    
  ],
  imports: [
    CommonModule,
    AdminRoutingModule,
    FormsModule,
    HttpClientModule,
  

  ]
})
export class AdminModule { }
