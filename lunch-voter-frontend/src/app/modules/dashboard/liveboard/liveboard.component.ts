import { Component, OnInit } from '@angular/core';
import { CrudService } from 'src/app/shared/services/crud/crud.service';
import { VisibilityService } from '../../../shared/services/visibility.service';
import { WebsocketService } from 'src/app/shared/services/websocket.service';
import { VoteInfo } from 'src/app/shared/model/voteinfo.model';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-liveboard',
  templateUrl: './liveboard.component.html',
  styleUrls: ['./liveboard.component.sass'],
})
export class LiveboardComponent implements OnInit {
  isShowForm: boolean = true;
  voteInfoList: VoteInfo[] = [];

  constructor(
    private crudService: CrudService,
    private visibilityService: VisibilityService,
    private websocketService: WebsocketService,
    private toastr: ToastrService
  ) {
    this.visibilityService.liveboardVisibility$.subscribe((value) => {
      this.isShowForm = value;

      if (this.isShowForm) {
        this.loadVoteInfo();
        this.connect();
      } else {
        this.disconnect();
      }
    });
  }

  ngOnInit() {
    this.isShowForm = localStorage.getItem('code') != null;

    console.log('>>' + localStorage.getItem('code'));
    console.log('>>' + this.isShowForm);
  }

  connect(): void {
    const code = localStorage.getItem('code');
    if (code) {
      this.disconnect();
      this.websocketService.connect('/ws/topic/vote/' + code,
        message => {
          this.handleMessage(message);

          console.log(message);
        });
    }
  }

  disconnect(): void {
    this.websocketService.disconnect();
  }

  handleMessage(message: any) {
    if (message.type === 'VOTE_INFO') {
      this.voteInfoList = message.voteItems.map(
        (item: { name: string; voteValue: string; updatedAt: Date; final: boolean }) => ({
          name: item.name,
          voteValue: item.voteValue,
          updatedAt: item.updatedAt,
          isFinal: item.final
        }));

      if (message.ended) {
        this.websocketService.disconnect();
        localStorage.removeItem('code');
        localStorage.removeItem('name');
        localStorage.removeItem('userCode');

        // this.visibilityService.setFormVisibility(false);
      }
    } else {
      this.toastr.show('User ' + message.name + ' ' + message.action);
    }
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
          this.voteInfoList = data.map(
            (item: { name: any; voteValue: any; updatedAt: any; }) => ({
              name: item.name,
              voteValue: item.voteValue,
              updatedAt: item.updatedAt
            }));

          console.log(data);
        })
        .catch(error => {
          console.error('Error fetching data:', error);
        });
    }
  }
}