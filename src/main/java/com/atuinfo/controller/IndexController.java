package com.atuinfo.controller;


import com.jfinal.core.Controller;



public class IndexController extends Controller {
    public void index(int age){
        renderText("界面加载成功，兄弟，加油"+age);
    }



}
