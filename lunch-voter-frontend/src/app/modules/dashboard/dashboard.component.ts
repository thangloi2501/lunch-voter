import { Component, OnInit } from '@angular/core';

import { CrudService } from 'src/app/shared/services/crud/crud.service';

import { FormBuilder, FormGroup, Validators } from '@angular/forms';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.sass'],
})
export class DashboardComponent implements OnInit {
  // createForm!: FormGroup;
  // isShow: boolean = true;

  content = '';

  constructor(
    private crudService: CrudService,
    private fb: FormBuilder
  ) { }

  ngOnInit() {
    // this.createForm = this.fb.group({
    //   name: ['', [Validators.required, Validators.minLength(3)]]
    // });

    // this.isShow = localStorage.getItem('code') != null;

    // console.log('>>' + localStorage.getItem('code'));
    // console.log('>>' + this.isShow);
  }

  // createSession(): void {
  //   if (this.createForm.valid) {
  //     let name = this.createForm.get('name')?.value;

  //     this.crudService.post('/votes', {
  //       'name': name
  //     })
  //       .then(res => {
  //         if (res.message) {
  //           throw new Error(res.message);
  //         } else {
  //           return res.data;
  //         }
  //       })
  //       .then(data => {
  //         this.content = data.code;
  //         localStorage.setItem('code', data.code);
  //         localStorage.setItem('userCode', data.userCode);
  //         console.log(data);
  //       })
  //       .catch(error => {
  //         console.error('Error fetching data:', error);
  //         alert("Error creating session: " + error.message);
  //       });

  //   } else {
  //     alert('Please fill in all the required fields.');
  //   }
  // }
}