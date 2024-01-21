import { Component, OnInit } from '@angular/core';
import { CrudService } from 'src/app/shared/services/crud/crud.service';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { VisibilityService } from '../visibility.service';


@Component({
  selector: 'app-submitform',
  templateUrl: './submitform.component.html',
  styleUrls: ['./submitform.component.sass'],
})
export class SubmitFormComponent implements OnInit {
  submitForm!: FormGroup;
  isShowForm: boolean = true;

  constructor(
    private crudService: CrudService,
    private fb: FormBuilder,
    private visibilityService: VisibilityService
  ) {
    this.visibilityService.visibility$.subscribe((value) => {
      this.isShowForm = value;
    });
  }

  ngOnInit() {
    this.isShowForm = localStorage.getItem('userCode') != null;

    this.submitForm = this.fb.group({
      vote: ['', [Validators.required, Validators.minLength(3)]]
    });

    console.log('>>' + localStorage.getItem('code'));
    console.log('>>' + this.isShowForm);
  }

  submitVote(): void {
    if (this.submitForm.valid) {
      let voteValue = this.submitForm.get('vote')?.value;

      this.crudService.put('/votes/submit', {
        'code': localStorage.getItem('code'),
        'userCode': localStorage.getItem('userCode'),
        'voteValue': voteValue
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
          alert("Error submitting vote: " + error.error.message);
        });

    } else {
      alert('Please fill in all the required fields.');
    }
  }
}