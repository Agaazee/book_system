import { Component, OnInit } from '@angular/core';
import { AppService } from '../app.service';
import { FormGroup, FormControl } from '@angular/forms';  // Use FormGroup and FormControl


@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss']
})
export class DashboardComponent implements OnInit {
  [x: string]: any;
  data: any[] = [];
  searchCriteria: string[] = [];
  searchForm: FormGroup;

  constructor(private appService: AppService) {
    this.searchForm = new FormGroup({
      searchKey: new FormControl(''),
      searchValue: new FormControl(''),
    });
  }

  ngOnInit(): void {
    console.log('ngOnInit is called');
    this.getAllUsers();
    this.getSearchCriteria();
  }

  // Fetch all users
  getAllUsers(): void {
    this.appService.getData().subscribe({
      next: (response: any) => {
        console.log('Data Response:', response);
        this.data = response;
      },
      error: (err: any) => {
        console.error('Failed to fetch data:', err);
      },
    });
  }

  // Fetch search criteria
  getSearchCriteria(): void {
    this.appService.getSearchCriteria().subscribe({
      next: (response: any) => {
        this.searchCriteria = response['Searching Criteria'] || [];
      },
      error: (err: any) => {
        console.error('Failed to fetch search criteria:', err);
      },
    });
  }

  // Handle search form submission
  onSearch(event: Event): void {
    event.preventDefault(); // Prevent the default form submission
    const searchKey = this.searchForm.value.searchKey;
    const searchValue = this.searchForm.value.searchValue;

    if (searchKey && searchValue) {
      const requestBody = {
        criteria: searchKey,
        value: searchValue,
      };

      this.appService.searchBooks(requestBody).subscribe({
        next: (response: any) => {
          console.log('Search Results:', response);
          // You can handle the search results here (e.g., display them on the page)
        },
        error: (err: any) => {
          console.error('Search API Error:', err);
        },
      });
    } else {
      console.warn('Both search key and value are required');
    }
  }

}
