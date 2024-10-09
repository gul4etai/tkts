import { NgModule } from '@angular/core';
import { provideRouter, RouterModule, Routes } from '@angular/router';
import { HomePageComponent } from './app/home-page/home-page.component';
import { AppComponent } from './app/app.component';
import { MoviesComponent } from './app/movies/movies.component';
import { MovieDetailsComponent } from './app/movies/movie-details/movie-details.component';
import { AuthComponent } from './app/auth/auth.component';
import { AuthGuard } from './app/shared/auth.guard';
import { UserPanelComponent } from './app/user-panel/user-panel.component';
import { AdminGuard } from './app/shared/admin.guard';
import { ReportsComponent } from './app/reports/reports.component';
import { CheckoutComponent } from './app/checkout/checkout.component';
import { AdminPanelComponent } from './app/admin-panel/admin-panel.component';
import { EditMovieComponent } from './app/admin-panel/widgets/edit-movie/edit-movie.component';
import { RegisterComponent } from './app/register/register.component';
import { UserManagementComponent } from './app/user-management/user-management.component';
import { TheaterComponent } from './app/theaters/theaters.component';

const routes: Routes = [
  { path: '', component: HomePageComponent },
  { path: 'movies', component: MoviesComponent}, 
  { path: 'movie/:id', component: MovieDetailsComponent },
  { path: 'login', component: AuthComponent, pathMatch: 'full' },
  { path: 'user/:name', component: UserPanelComponent, canActivate: [AuthGuard] },
  { path: 'admin', component: AdminPanelComponent, canActivate: [AuthGuard, AdminGuard]},
  { path: 'admin/edit-movie/:id', component: EditMovieComponent, canActivate: [AuthGuard, AdminGuard]},
  { path: 'admin/add-movie', component: EditMovieComponent, canActivate: [AuthGuard, AdminGuard]},
  { path: 'admin/reports', component: ReportsComponent, pathMatch: 'full', canActivate: [AuthGuard, AdminGuard]}, 
  { path: 'admin/user-management', component: UserManagementComponent, pathMatch: 'full', canActivate: [AuthGuard, AdminGuard]}, 
  { path: 'admin/theaters', component: TheaterComponent, pathMatch: 'full', canActivate: [AuthGuard, AdminGuard]}, 
  { path: 'checkout', component: CheckoutComponent, canActivate: [AuthGuard] },
  { path: 'register', component: RegisterComponent },
  { path: '**', redirectTo: '/' } 
]

export const appRoutingProviders = [
  provideRouter(routes),
];
