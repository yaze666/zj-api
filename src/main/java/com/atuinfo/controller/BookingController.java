package com.atuinfo.controller;

import com.atuinfo.Interceptors.ErrorExptionInterceptor;
import com.atuinfo.service.BookingService;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.core.Controller;
import com.jfinal.kit.HttpKit;

/**
 * @author dp
 * @version 1.0.0
 * @date 2019-08-01 11:02
 */
@Before(ErrorExptionInterceptor.class)
public class BookingController extends Controller {
    @Inject
    private BookingService bookingService;

    /**
     * 取消预约
     */
    public void index(){
        String strRequest = HttpKit.readData(getRequest());
        // 调用方法直接返回
        renderText(bookingService.cancelBook(strRequest));
    }


    /**
     * 预约取号
     */
    public void bookingGetNo(){
        String strRequest = HttpKit.readData(getRequest());

        renderText(bookingService.bookingGetNo(strRequest));
    }


    /**
     * 预约确认 OutPatBookingConfirm
     */
    public void BookingConfirm(){
        String strXml = HttpKit.readData(getRequest());
        // 调用方法直接返回
        renderText(String.valueOf(bookingService.bookingConfirm(strXml)));
    }


    /**
     * 预约退费成功通知
     */
    public void BookingCancelMoney()throws Exception {
        String strXml = HttpKit.readData(getRequest());
        // 调用方法直接返回
        renderText(String.valueOf(bookingService.bookingCancelMoney(strXml)));
    }

}
