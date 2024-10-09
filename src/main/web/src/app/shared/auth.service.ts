import { Injectable, Injector } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Router } from '@angular/router';
import { JwtHelperService } from '@auth0/angular-jwt';
import { BehaviorSubject, Observable, of } from 'rxjs';
import { catchError, map, tap } from 'rxjs/operators';
import { User } from './helper/user.class';
import { Booking } from './helper/booking.class';

@Injectable({
  providedIn: 'root' 
})
export class AuthService {
  private apiUrl = 'http://localhost:8080/tkts/auth/login';
  isLoggedInStatus: boolean = false;
  currentUser: User | null | undefined;
  private currentUserSubject: BehaviorSubject<User | null> = new BehaviorSubject<User | null>(null);
  currentUser$: Observable<User | null> = this.currentUserSubject.asObservable();  // user info for other components


  constructor(private http: HttpClient, private router: Router, private injector: Injector ) {  // logout if the token is expired
    const token = this.getToken();  
    let jwtHelper = this.injector.get<JwtHelperService>(JwtHelperService);
    if (token && jwtHelper.isTokenExpired(token)) {
      this.logout();  
    } else if (token) {
      this.updateUserInfoFromLocalStorage();  
    }
  }

  ngOnInit() {
    this.currentUser = this.getCurrentUserFromLocalStorage();
    this.currentUserSubject.next(this.currentUser);
  }

  register(user: User): Observable<any> {
    return this.http.post(`http://localhost:8080/tkts/users`, user).pipe(
      catchError((error) => {
        console.error('Registration error:', error);
        return of(error);  
      })
    );
  }

  login(email: string, password: string): Observable<User> {
    return this.http.post<any>(`${this.apiUrl}`, { email, password }).pipe(
      tap(response => {
        const token = response.jwt;
        if (token) {
          localStorage.setItem('jwtToken', token);
          this.isLoggedInStatus = true;
          this.updateUserInfo(email)
        }
      }),
      catchError(error => {
        console.error('Login failed:', error);
        return of(null as any);  
      })
    );
  }

  updateUserInfo(email: string):void {
    this.getUserInfo(email).subscribe(user => {
      this.currentUser = user;
      localStorage.setItem('currentUser', JSON.stringify(user));
      this.currentUserSubject.next(user);
    });
  }

   updateUserInfoFromLocalStorage(): void {
    const userData = localStorage.getItem('currentUser');
    if (userData) {
      this.currentUser = JSON.parse(userData);
      if(this.currentUser) {
        this.currentUserSubject.next(this.currentUser);
      }
    }
  }

  getUserInfo(email: string): Observable<User> {
    const token = localStorage.getItem('jwtToken');
    if (!token) {
      return of(null as any);  
    }

    const headers = new HttpHeaders({
      'Authorization': `Bearer ${token}`
    });

    return this.http.get<User>(`http://localhost:8080/tkts/users/email/` + email, { headers }).pipe(
      catchError(error => {
        console.error('Failed to fetch user info:', error);
        return of(null as any);  
      })
    );
  }

  logout(): void {
    localStorage.removeItem('jwtToken');
    localStorage.removeItem('currentUser');
    this.currentUser = null;
    this.currentUserSubject.next(null);
    this.router.navigate(['/']);
  }

  getToken(): string | null {
    return localStorage.getItem('jwtToken');
  }

  isLoggedIn(): boolean {
    return this.currentUser ? true : false;
  }

  getCurrentUserFromLocalStorage(): User | null {
    const userData = localStorage.getItem('currentUser');
    return userData ? JSON.parse(userData) : null;
  }

  getCurrentUser(): User | null {
    const userData = this.currentUser;
    return userData ? userData : null;
  }

  getUserRole(): string | null {
    const user = this.getCurrentUser();
    return user && user.admin ? 'admin' : 'user';
  }

  isAdmin(): boolean {
    const user = this.getCurrentUser();
    return user ? user.admin : false;
  }

  hasRole(role: string): boolean {
    const userRole = this.getUserRole();
    return userRole === role;
  }

  updateUser(updatedUserData: Partial<User>): Observable<User> {
    const currentUser = this.currentUserSubject.value;
    if (currentUser) {
      const updatedUser = { ...currentUser, ...updatedUserData };
      this.currentUserSubject.next(updatedUser);  
      localStorage.setItem('currentUser', JSON.stringify(updatedUser));  
      return of(updatedUser);  
    }
    throw new Error('User is not logged in');
  }

  updateUser1(updatedUserData: Partial<User>): Observable<User> {
    const currentUser = this.currentUserSubject.value;
    if (currentUser) {
      return this.http.put<User>(`http://localhost:8080/tkts/users/${currentUser.id}`, updatedUserData).pipe(
        tap((updatedUser: User) => {
          // put update in BehaviorSubject 
          this.currentUserSubject.next(updatedUser);
          localStorage.setItem('currentUser', JSON.stringify(updatedUser));
        })
      );
    }
    throw new Error('User is not logged in');
  }


}
