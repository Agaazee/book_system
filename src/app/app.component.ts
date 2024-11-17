import { Component, OnDestroy, OnInit, SimpleChanges } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { AppService } from './app.service';
import { BehaviorSubject, Observable, Subject, Subscription, takeUntil } from 'rxjs';
import { HttpHeaders } from '@angular/common/http';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss'],
})
export class AppComponent implements OnInit, OnDestroy {
  title = 'book-ui';
  private eventSubscription?: Subscription;
  public loadImage: boolean = false;
  constructor(
    public router: Router,
    public appService: AppService,
    private activatedRoute: ActivatedRoute
  ) {

  }
  ngOnInit() {
    this.appService.getEventUpdate().subscribe((res) => {
      console.log('Event received:', res);
      if (res) {
        this.loadImage = res;
      } else {
        this.loadImage = false;
      }
    });
    // this.router.navigate(["login"]);

  }
  ngOnChanges(changes: SimpleChanges) {
  }
  ngOnDestroy() {
    this.eventSubscription?.unsubscribe();
  }

}
