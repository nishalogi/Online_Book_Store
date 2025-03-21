import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { HomeComponent } from './components/home/home.component';
import { BookListComponent } from './components/book-list/book-list.component';
import { BookDetailsComponent } from './components/book-details/book-details.component';
import { CartComponent } from './components/cart/cart.component';
import { OrdersComponent } from './components/orders/orders.component';
import { LoginComponent } from './components/login/login.component';
import { CheckoutComponent } from './components/checkout/checkout.component';
import { OrderDetailsComponent } from './components/order-details/order-details.component';
import { SignupComponent } from './components/signup/signup.component';



const routes: Routes = [
  { path: '', redirectTo: '/home', pathMatch: 'full' }, // Default route
  { path: 'home', component: HomeComponent }, // ✅ Route for home
  { path: 'book-list', component: BookListComponent },
  { path: 'book-details/:id', component: BookDetailsComponent }, // ✅ Dynamic Book Details Route
  { path: 'cart', component: CartComponent },
  { path: 'orders', component: OrdersComponent }, // ✅ Can add authentication guard later
  { path: 'checkout', component: CheckoutComponent }, // ✅ Can add authentication guard later
  { path: 'login', component: LoginComponent },
  { path: 'order-details/:id', component: OrderDetailsComponent },
  { path: '', redirectTo: '/orders', pathMatch: 'full' },
  { path: 'signup', component: SignupComponent },
  { path: 'admin', loadChildren: () => import('./admin/admin.module').then(m => m.AdminModule) },
];
  

  
  

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
