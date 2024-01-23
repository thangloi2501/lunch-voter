import { Component, OnInit } from '@angular/core';
import { CrudService } from 'src/app/shared/services/crud/crud.service';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { VisibilityService } from '../../../shared/services/visibility.service';
import { WebsocketService } from 'src/app/shared/services/websocket.service';

@Component({
  selector: 'app-joinform',
  templateUrl: './joinform.component.html',
  styleUrls: ['./joinform.component.sass'],
})
export class JoinFormComponent implements OnInit {
  joinForm!: FormGroup;
  isShowForm: boolean = true;
  isShowLeaveButton: boolean = true;
  infoName = localStorage.getItem('name');
  infoLink = getLink(localStorage.getItem('code'));

  constructor(
    private crudService: CrudService,
    private fb: FormBuilder,
    private route: ActivatedRoute,
    private visibilityService: VisibilityService,
    private websocketService: WebsocketService
  ) {
  }

  ngOnInit(): void {
    if (localStorage.getItem('isFinal')) this.isShowForm = false;
    else this.isShowForm = localStorage.getItem('userCode') == null;

    this.isShowLeaveButton = localStorage.getItem('isFinal') == null;

    this.route.queryParams.subscribe(params => {
      const code = params['code'] || '';

      this.joinForm = this.fb.group({
        name: ['', [Validators.required, Validators.minLength(3)]],
        code: [code, [Validators.required]]
      });
    });
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
    window.location.reload();
  }

  joinSession(): void {
    if (this.joinForm.valid) {
      const name = this.joinForm.get('name')?.value;
      const code = this.joinForm.get('code')?.value;

      this.crudService.post('/votes/join', {
        'name': name,
        'code': code
      })
        .then(res => {

          console.log(res);

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

          this.isShowForm = false;
          this.infoName = name;
          this.infoLink = getLink(data.code);

          this.visibilityService.setLiveboardVisibility(true);

          console.log(data);
        })
        .catch(error => {
          console.error('Error fetching data:', error);
          alert("Error joining session: " + error.error.message);
        });

    } else {
      alert('Please fill in all the required fields.');
    }
  }
}

function getLink(code: any) {
  return location.origin + location.pathname + "?code=" + code;
}