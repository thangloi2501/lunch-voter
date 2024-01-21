import { Component, OnInit } from '@angular/core';
import { CrudService } from 'src/app/shared/services/crud/crud.service';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-joinform',
  templateUrl: './joinform.component.html',
  styleUrls: ['./joinform.component.sass'],
})
export class JoinFormComponent implements OnInit {
  joinForm!: FormGroup;
  isShow: boolean = true;

  content = '';

  constructor(
    private crudService: CrudService,
    private fb: FormBuilder,
    private route: ActivatedRoute
  ) { }

  ngOnInit(): void {
    this.isShow = localStorage.getItem('userCode') == null;

    this.route.queryParams.subscribe(params => {
      const code = params['code'] || '';

      this.joinForm = this.fb.group({
        code: [code, [Validators.required]]
      });
    });
  }

  joinSession(): void {
    if (this.joinForm.valid) {
      let name = this.joinForm.get('name')?.value;

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
          this.content = data.code;
          localStorage.setItem('code', data.code);
          localStorage.setItem('userCode', data.userCode);
          console.log(data);
        })
        .catch(error => {
          console.error('Error fetching data:', error);
          alert("Error creating session: " + error.message);
        });

    } else {
      alert('Please fill in all the required fields.');
    }
  }
}