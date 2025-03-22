import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { FormsModule } from '@angular/forms'; 
import { HttpClientModule, provideHttpClient, withFetch } from '@angular/common/http'; 
import { AppRoutingModule } from './app-routing.module';
import { ReactiveFormsModule } from '@angular/forms';

// Components
import { AppComponent } from './app.component';
import { HomeComponent } from './components/home/home.component';
import { HeaderComponent } from './components/header/header.component';
import { FooterComponent } from './components/footer/footer.component';
import { CartComponent } from './components/cart/cart.component';
import { CheckoutComponent } from './components/checkout/checkout.component';
import { LoginComponent } from './components/login/login.component';
import { OrdersComponent } from './components/orders/orders.component';
import { OrderDetailsComponent } from './components/order-details/order-details.component';
import { SignupComponent } from './components/signup/signup.component'; 


// Modules
import { BookModule } from './modules/book/book.module'; 

// Services
import { CartService } from './services/cart.service';
import { BookService } from './services/book.service';




@NgModule({
  declarations: [
    AppComponent,
    HomeComponent,
    HeaderComponent,
    FooterComponent,
    CartComponent,
    CheckoutComponent,
    LoginComponent,
    OrdersComponent,
    OrderDetailsComponent,
    SignupComponent,
    ],
  imports: [
    BrowserModule,
    FormsModule,
    HttpClientModule,
    AppRoutingModule,
    BookModule,
    ReactiveFormsModule,
  ],
  providers: [
    CartService,
    BookService,
    provideHttpClient(withFetch())
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }


