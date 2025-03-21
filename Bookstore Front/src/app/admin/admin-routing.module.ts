import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AdminPanelComponent } from './admin-panel/admin-panel.component';
import { DashboardComponent } from './dashboard/dashboard.component';
import { ManageBooksComponent } from './manage-books/manage-books.component';
import { ManageOrdersComponent } from './manage-orders/manage-orders.component';
import { ManageUsersComponent } from './manage-users/manage-users.component';

const routes: Routes = [
  { path: '', component: AdminPanelComponent, children: [
      { path: 'dashboard', component: DashboardComponent },
      { path: 'manage-books', component: ManageBooksComponent },
      { path: 'manage-orders', component: ManageOrdersComponent },
      { path: 'manage-users', component: ManageUsersComponent },
      { path: '', redirectTo: 'dashboard', pathMatch: 'full' }
  ]}
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class AdminRoutingModule { }
