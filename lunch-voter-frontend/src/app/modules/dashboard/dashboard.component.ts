import { Component, OnInit } from '@angular/core';
import { CrudService } from 'src/app/shared/services/crud/crud.service';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.sass'],
})
export class DashboardComponent implements OnInit {

  showJoin: boolean = false;

  constructor(
    private crudService: CrudService,
    private route: ActivatedRoute
  ) { }

  ngOnInit(): void {
    this.route.url.subscribe(urlSegments => {
      this.showJoin = urlSegments.some(segment => segment.path === 'join');
    });
  }
}