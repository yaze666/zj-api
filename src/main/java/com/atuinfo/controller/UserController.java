package com.atuinfo.controller;

import com.jfinal.core.Controller;

public class UserController extends Controller {
    public void index(){
        renderText("这是user的controller");
    }


    public static void main(String[] args) {
        String 排班Id="121313414";
        String[] ids = 排班Id.split("|");
        String idd=ids[1];
        System.out.println(ids[1]);
    }















}
