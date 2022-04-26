import { useState } from "react";
import api from "./baseApi";

let accessToken = null;
let current_user = null;

export function login(form){
    return api.post('/auth/login',form).then((res)=>{
        console.log('Login success');
        accessToken=res.data.accessToken;
        console.log(accessToken);
        localStorage.setItem("jwt", accessToken);
        current_user = res.data.user;
    });
}

export function logout(){
    accessToken=null;
    current_user = null;
    localStorage.clear();
}

export function getCurrentUser(){
    return current_user;
}

export function tokenIsPresent() {
    return accessToken != undefined && accessToken != null;
}

export function getToken() {
    console.log("--------------");
    console.log(accessToken);
    console.log("--------------");
    return accessToken;
}