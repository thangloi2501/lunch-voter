import { Component, OnInit } from '@angular/core';
import { CrudService } from 'src/app/shared/services/crud/crud.service';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { VisibilityService } from '../../../shared/services/visibility.service';
import { WebsocketService } from 'src/app/shared/services/websocket.service';


@Component({
  selector: 'app-createform',
  templateUrl: './createform.component.html',
  styleUrls: ['./createform.component.sass'],
})
export class CreateFormComponent implements OnInit {
  createForm!: FormGroup;
  endForm!: FormGroup;
  isShowForm: boolean = true;
  isShowEndButton: boolean = true;
  infoName = localStorage.getItem('name');
  infoLink = getLink(localStorage.getItem('code'));
  isCreator = localStorage.getItem('isCreator') == "true";

  constructor(
    private crudService: CrudService,
    private fb: FormBuilder,
    private visibilityService: VisibilityService,
    private websocketService: WebsocketService
  ) {
  }

  ngOnInit() {
    if (localStorage.getItem('isFinal')) this.isShowForm = false;
    else this.isShowForm = localStorage.getItem('code') == null;

    this.isShowEndButton = localStorage.getItem('isFinal') == null;

    this.createForm = this.fb.group({
      name: ['', [Validators.required, Validators.minLength(3)]]
    });

    this.endForm = this.fb.group({});
  }

  createSession(): void {
    if (this.createForm.valid) {
      let name = this.createForm.get('name')?.value;

      this.crudService.post('/votes', {
        'name': name
      })
        .then(res => {
          if (res.message) {
            throw new Error(res.message);
          } else {
            return res.data;
          }
        })
        .then(data => {
          localStorage.setItem('name', name);
          localStorage.setItem('code', data.code);
          localStorage.setItem('userCode', data.userCode);
          localStorage.setItem('isCreator', "true");

          this.isShowForm = false;
          this.infoName = name;
          this.infoLink = getLink(data.code);
          this.isCreator = true;

          this.visibilityService.setLiveboardVisibility(true);
        })
        .catch(error => {
          console.error('Error fetching data:', error);
          alert("Error creating session: " + error.error.message);
        });

    } else {
      alert('Please fill in all the required fields.');
    }
  }

  leaveSession(): void {
    const code = localStorage.getItem('code');
    const userCode = localStorage.getItem('userCode');

    this.crudService.put('/votes/leave', {
      'code': code,
      'userCode': userCode
    })
      .then(res => {
        if (res.message) {
          throw new Error(res.message);
        } else {
          return res.data;
        }
      })
      .then(data => {
        this.websocketService.disconnect();

        localStorage.removeItem('code');
        localStorage.removeItem('name');
        localStorage.removeItem('userCode');
        localStorage.removeItem('isCreator');
        window.location.reload();
      })
      .catch(error => {
        console.error('Error fetching data:', error);
        alert("Error leaving session: " + error.error.message);
      });
  }

  newSession(): void {
    localStorage.removeItem('isFinal');
    localStorage.removeItem('code');
    localStorage.removeItem('name');
    localStorage.removeItem('isCreator');
    window.location.reload();
  }

  endSession(): void {
    const code = localStorage.getItem('code');
    const userCode = localStorage.getItem('userCode');

    this.crudService.post('/votes/end', {
      'code': code,
      'userCode': userCode
    })
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
        alert("Error ending session: " + error.error.message);
      });
  }
}

function getLink(code: any) {
  return location.origin + location.pathname + "/join?code=" + code;
}
