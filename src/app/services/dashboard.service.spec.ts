import { Component, OnInit } from '@angular/core';
import { DashboardService } from '../services/dashboard.service';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css'],
})
export class DashboardComponent implements OnInit {
  data: any[] = []; // To store the fetched data
  isLoading = true; // To manage loading state
  error: string | null = null; // To manage errors

  constructor(private dashboardService: DashboardService) {}

  ngOnInit(): void {
    this.fetchData();
  }

  fetchData(): void {
    this.dashboardService.getData().subscribe(
      (response) => {
        this.data = response; // Assign the API response to the `data` variable
        this.isLoading = false; // Stop the loading indicator
      },
      (error) => {
        console.error('Error fetching data:', error);
        this.error = 'Failed to load data. Please try again later.';
        this.isLoading = false;
      }
    );
  }
}

