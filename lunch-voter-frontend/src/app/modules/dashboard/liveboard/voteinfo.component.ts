import { Component, Input } from '@angular/core';
import { VoteInfo } from 'src/app/shared/model/voteinfo.model';

@Component({
    selector: 'app-voteinfo',
    templateUrl: './voteinfo.component.html',
    styleUrls: ['./voteinfo.component.sass']
})
export class VoteInfoComponent {
    @Input() voteInfo!: VoteInfo;
}