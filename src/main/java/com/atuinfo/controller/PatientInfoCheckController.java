package com.atuinfo.controller;

import com.atuinfo.Interceptors.ErrorExptionInterceptor;
import com.atuinfo.service.PatientInfoCheckService;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.core.Controller;
import com.jfinal.kit.HttpKit;

/**
 * @author Yz
 * @company Atu
 * @create  2019-08-08 15:45
 */
@Before(ErrorExptionInterceptor.class)
public class PatientInfoCheckController extends Controller {
    @Inject
    private PatientInfoCheckService patientInfoCheckService;

    /**
     * 患者信息校验（patientInfoCheck）
     */
    public void index(){
        String strRequest = HttpKit.readData(getRequest());
        // 调用方法直接返回getPatientInfo
        renderText(patientInfoCheckService.getPatientInfo(strRequest));
    }

}
