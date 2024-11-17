import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Subject, Observable } from 'rxjs';
import { map } from 'rxjs/operators';

@Injectable({
  providedIn: 'root',
})
export class AppService {
  [x: string]: any;
  private eventUpdate = new Subject<any>();

  constructor(private httpClient: HttpClient) {}

  // Emit an event update
  emitEventUpdate(value: any) {
    this.eventUpdate.next(value);
  }

  // Get event update as an observable
  getEventUpdate() {
    return this.eventUpdate.asObservable();
  }

  // Login method
  public login(data: any) {
    return this.httpClient
      .post(`http://localhost:9001/api/admin/login`, data)
      .pipe(map((response: any) => {
        // Assuming the token is in response.token
        if (response && response.token) {
          localStorage.setItem('authToken', response.token);  // Store the token
        }
        return response;
      })
    );
  }

  private getAuthToken(): string | null {
    return localStorage.getItem('authToken');
  }

  getData(): Observable<any> {
    const token = this.getAuthToken();  // Retrieve token from localStorage

    // If token is available, add it to the headers
    const headers = new HttpHeaders({
      'Authorization': token ? `Bearer ${token}` : '',  // Add token in header if available
    });

    return this.httpClient.get(`http://localhost:9001/api/admin/allUsers`, { headers });
  }

  // Example for getting search criteria (keys)
  getSearchCriteria(): Observable<any> {
     const token = this.getAuthToken(); 
      const headers = new HttpHeaders({
      'Authorization': token ? `Bearer ${token}` : '',  // Add token in header if available
    });
    return this.httpClient.get<any>('http://localhost:9001/getSearchingCriteria',  { headers });
  }

  // Example for getting values based on selected key
  getSearchValues(criteria: string, value: string): Observable<any> {
    const token = this.getAuthToken(); 
    const headers = new HttpHeaders({
       'Authorization': token ? `Bearer ${token}` : '',
      'Content-Type': 'application/json',
    });

    const body = { criteria, value };

    return this.httpClient.post<any>('http://localhost:9001/searchBookByCriteria?page=0&size=10', body, { headers });
  }

    searchBooks(requestBody: { criteria: string; value: string }): Observable<any[]> {
        const token = this.getAuthToken(); 
  const url = 'http://localhost:9001/searchBookByCriteria?page=0&size=10';
  const headers = new HttpHeaders({
    'Authorization': token ? `Bearer ${token}` : '',
    'Content-Type': 'application/json',
  });

  return this.httpClient.post<any[]>(url, requestBody, { headers });
}
}

