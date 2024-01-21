import { Component, OnInit } from '@angular/core';
import { CrudService } from 'src/app/shared/services/crud/crud.service';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Location } from '@angular/common';
import { VisibilityService } from '../visibility.service';

@Component({
  selector: 'app-createform',
  templateUrl: './createform.component.html',
  styleUrls: ['./createform.component.sass'],
})
export class CreateFormComponent implements OnInit {
  createForm!: FormGroup;
  endForm!: FormGroup;
  isShowForm: boolean = true;
  infoName = localStorage.getItem('name');
  infoLink = getLink(localStorage.getItem('code'));
  isCreator = localStorage.getItem('isCreator') == "true";

  constructor(
    private crudService: CrudService,
    private fb: FormBuilder,
    private location: Location,
    private visibilityService: VisibilityService
  ) { }

  ngOnInit() {
    this.createForm = this.fb.group({
      name: ['', [Validators.required, Validators.minLength(3)]]
    });

    this.endForm = this.fb.group({});

    this.isShowForm = localStorage.getItem('code') == null;

    console.log('>>' + localStorage.getItem('code'));
    console.log('>>' + this.isShowForm);
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

          this.visibilityService.setVisibility(true);

          console.log(data);
        })
        .catch(error => {
          console.error('Error fetching data:', error);
          alert("Error creating session: " + error.error.message);
        });

    } else {
      alert('Please fill in all the required fields.');
    }
  }

  endSession(): void {
    const code = localStorage.getItem('code');
    const userCode = localStorage.getItem('userCode');

    console.log('>>>>>>>>>' + userCode);

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