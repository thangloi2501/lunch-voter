import { Component, OnInit } from '@angular/core';
import { CrudService } from 'src/app/shared/services/crud/crud.service';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { VisibilityService } from '../../../shared/services/visibility.service';
import { WebsocketService } from 'src/app/shared/services/websocket.service';


@Component({
  selector: 'app-liveboard',
  templateUrl: './liveboard.component.html',
  styleUrls: ['./liveboard.component.sass'],
})
export class LiveboardComponent implements OnInit {
  // submitForm!: FormGroup;
  isShowForm: boolean = true;

  constructor(
    private crudService: CrudService,
    private fb: FormBuilder,
    private visibilityService: VisibilityService,
    private websocketService: WebsocketService
  ) {
    this.visibilityService.visibility$.subscribe((value) => {
      this.isShowForm = value;

      if (this.isShowForm) {
        this.loadVoteInfo();
        this.connect();
      }
    });
  }

  ngOnInit() {
    this.isShowForm = localStorage.getItem('code') != null;

    if (this.isShowForm) {
      this.loadVoteInfo();
      this.connect();
    }

    console.log('>>' + localStorage.getItem('code'));
    console.log('>>' + this.isShowForm);
  }

  connect(): void {
    const code = localStorage.getItem('code');
    if (code) {
      this.disconnect();

    }
  }

  disconnect(): void {
    this.websocketService.disconnect();
  }

  loadVoteInfo(): void {
    const code = localStorage.getItem('code');

    if (code) {
      this.crudService.get('/votes?code=' + code)
        .then(res => {
          if (res.message) {
            throw new Error(res.message);
          } else {
            return res.data;
          }
        })
        .then(data => {
          console.log(data);
        })
        .catch(error => {
          console.error('Error fetching data:', error);
          alert("Error submitting vote: " + error.error.message);
        });

    } else {
      alert('Please fill in all the required fields.');
    }
  }
}