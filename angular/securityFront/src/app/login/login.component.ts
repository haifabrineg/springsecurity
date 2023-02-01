import { Component, OnInit } from '@angular/core';
import { gsap } from "gsap";
import { TweenMax } from "gsap/all";
declare const az:any;
  
@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {
  

  constructor() { }

  ngOnInit(): void {
    az();
  }
}