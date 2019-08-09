package com.atuinfo.controller;

import com.atuinfo.Interceptors.ErrorExptionInterceptor;
import com.atuinfo.service.BeInHospitalService;
import com.atuinfo.service.ReportService;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.core.Controller;
import com.jfinal.kit.HttpKit;

/**
 * @author Yz
 * @company Atu
 * @create  2019-08-06 11:14
 */

@Before(ErrorExptionInterceptor.class)
public class BeInHospitalController extends Controller {

    @Inject
    private BeInHospitalService beInHospitalService;

    /**
     *住院预缴信息查询
     */
    public void index(){
        String strRequest = HttpKit.readData(getRequest());
        // 调用方法直接返回
        renderText(beInHospitalService.getBeInHospitalInfo(strRequest));
    }

    /**
     *
     */
    public void BeInHospitalBills(){
        String strRequest = HttpKit.readData(getRequest());
        // 调用方法直接返回
        renderText(beInHospitalService.getBeInHospitalBills(strRequest));
    }


    /**
     *
     */
    public void BeInHospitalPay(){
        String strRequest = HttpKit.readData(getRequest());
        // 调用方法直接返回
        renderText(beInHospitalService.getBeInHospitalPay(strRequest));
    }




}
