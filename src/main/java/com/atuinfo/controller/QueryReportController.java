package com.atuinfo.controller;

import com.atuinfo.Interceptors.ErrorExptionInterceptor;
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
public class QueryReportController extends Controller {

    @Inject
    private ReportService reportService;

    /**
     * 获取报告单列表
     */
    public void index(){
        String strRequest = HttpKit.readData(getRequest());
        // 调用方法直接返回
        renderText(reportService.getReportList(strRequest));
    }

    /**
     * 获取检查报告详情（inspectpicDetail）
     */
    public void InspectpicDetail(){
        String strRequest = HttpKit.readData(getRequest());
        // 调用方法直接返回
        renderText(reportService.getInspectpicDetail(strRequest));
    }


    /**
     * 获取检验报告详情（inspectDetail）
     */
    public void InspectDetail(){
        String strRequest = HttpKit.readData(getRequest());
        // 调用方法直接返回
        renderText(reportService.getInspectDetail(strRequest));
    }


    /**
     * 获取微生物报告详情（chemicalDetail）
     */
    public void ChemicalDetail(){
        String strRequest = HttpKit.readData(getRequest());
        // 调用方法直接返回
        renderText(reportService.getChemicalDetail(strRequest));
    }



}
