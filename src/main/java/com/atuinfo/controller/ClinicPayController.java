package com.atuinfo.controller;

import com.atuinfo.Interceptors.ErrorExptionInterceptor;
import com.atuinfo.service.ClinicPayService;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.core.Controller;
import com.jfinal.kit.HttpKit;

/**
 * @author Yz
 * @company Atu
 * @create  2019-08-08 9:11
 */

@Before(ErrorExptionInterceptor.class)
public class ClinicPayController extends Controller {
    @Inject
    private ClinicPayService clinicPayService;


    /**
     * 诊间支付（列表查询clinicPayList）
     */
    public void index(){
        String strRequest = HttpKit.readData(getRequest());
        // 调用方法直接返回
        renderText(clinicPayService.getClinicPayList(strRequest));
    }



    /**
     * 诊间支付（详情clinicPayDetail）
     */
    public void ClinicPayDetail(){
        String strRequest = HttpKit.readData(getRequest());
        // 调用方法直接返回
        renderText(clinicPayService.getClinicPayDetail(strRequest));
    }


    /**
     * 诊间支付（支付接口clinicPay）
     */
    public void ClinicPay(){
        String strRequest = HttpKit.readData(getRequest());
        // 调用方法直接返回
        renderText(clinicPayService.getClinicPay(strRequest));
    }

}
