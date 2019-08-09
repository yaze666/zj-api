package com.atuinfo.common;

import com.atuinfo.exception.ErrorMassageException;
import com.atuinfo.util.BeanUtil;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Yz
 * @company Atu
 * @create  2019-08-02 11:08
 *
 */
public class ExecPublic {
    /**
     * 获取身份识别卡类别
     * @param BankCardType
     * @return
     */
    public static long GetHisCardTypeID(String BankCardType)
    {
        long HisCardTypeID = 0;
        switch (BankCardType)
        {
            case "1"://1.就诊卡
                HisCardTypeID = 1;
                break;
            case "2"://2.建行卡
                HisCardTypeID = 182;
                break;
            case "4"://4.身份证
                HisCardTypeID = 2;
                break;
            default:
                HisCardTypeID = Long.parseLong(BankCardType);
                break;
             //throw new Exception("未找到卡类别ID【" + BankCardType + "】的信息！请与软件供应商联系！");
        }
        if (HisCardTypeID == -1)
            throw new ErrorMassageException("此类别的卡暂不被支持！请与软件供应商联系！");
        return HisCardTypeID;
    }


    /**
     *根据银行传入支付方式
     * @param intCardTypeID
     */
    public static void InitCardInfo(long intCardTypeID) {
//        btCommand cmd = new btCommand("");
//        btDataReader dr = new btDataReader();
        try {
            final Record List= Db.findFirst("Select * From 医疗卡类别 Where ID=?",intCardTypeID);
            if (List==null){
                throw new ErrorMassageException("未找到卡类别ID信息！请与软件供应商联系！");
            }
            Map<String, Object> result = List.getColumns();
            CardInfo.setP_intID((int) intCardTypeID);
            CardInfo.setY_str编码(BeanUtil.convert(result.get("编码1111"), String.class));
            CardInfo.setY_str名称(BeanUtil.convert(result.get("名称"), String.class));
            CardInfo.setY_str短名(BeanUtil.convert(result.get("短名"), String.class));
            CardInfo.setY_str部件(BeanUtil.convert(result.get("部件"), String.class));
            CardInfo.setY_str备注(BeanUtil.convert(result.get("备注"), String.class));
            CardInfo.setY_str特定项目(BeanUtil.convert(result.get("特定项目"), String.class));
            CardInfo.setY_str结算方式(BeanUtil.convert(result.get("结算方式"), String.class));
            CardInfo.setY_str卡号密文(BeanUtil.convert(result.get("卡号密文"), String.class));
            CardInfo.setY_int卡号长度(((BigDecimal) result.get("卡号长度")).intValue());
            CardInfo.setY_int缺省标志(((BigDecimal) result.get("缺省标志")).intValue());
            CardInfo.setY_int是否固定(((BigDecimal) result.get("是否固定")).intValue());
            CardInfo.setY_int是否严格控制(((BigDecimal) result.get("是否严格控制")).intValue());
            CardInfo.setY_int是否自制(((BigDecimal) result.get("是否自制")).intValue());
            CardInfo.setY_int是否存在帐户(((BigDecimal) result.get("是否存在帐户")).intValue());
            CardInfo.setY_int是否退现(((BigDecimal) result.get("是否退现")).intValue());
            CardInfo.setY_int是否全退(((BigDecimal) result.get("是否全退")).intValue());
            CardInfo.setY_int是否重复使用(((BigDecimal) result.get("是否重复使用")).intValue());
            CardInfo.setY_int是否启用(((BigDecimal) result.get("是否启用")).intValue());
            CardInfo.setY_int密码长度(((BigDecimal) result.get("密码长度")).intValue());
            CardInfo.setY_int密码长度限制(((BigDecimal) result.get("密码长度限制")).intValue());
            CardInfo.setY_int密码规则(((BigDecimal) result.get("密码规则")).intValue());
            CardInfo.setY_int是否模糊查找(((BigDecimal) result.get("是否模糊查找")).intValue());
            CardInfo.setY_int是否缺省密码(((BigDecimal) result.get("是否缺省密码")).intValue());
            CardInfo.setY_int险类(((BigDecimal) result.get("险类")).intValue());

        }catch (Exception e){
            //dr.Close();
            //cmd.Close();
            //将错误抛向上一层
            e.printStackTrace();
        }
    }


    /**
     * 根据银行传入支付方式：
     * @param BankCardType
     * @return
     */
    public static long GetHisCardPayID(String BankCardType)
    {
        long HisCardTypeID = 0;

        switch (BankCardType.toUpperCase())
        {
            case "1"://1.支付宝
                HisCardTypeID = 11;
                break;
            case "2"://2.微信
                HisCardTypeID = 12;
                break;
            case "3"://3.APP支付宝
                HisCardTypeID = 13;
                break;
            case "4"://4.APP微信
                HisCardTypeID = 14;
                break;
            case "5"://5.建行支付
                HisCardTypeID = 15;
                break;
            default:
                throw new ErrorMassageException("未找到卡类别ID【" + BankCardType + "】的信息！请与软件供应商联系！");
        }
        return HisCardTypeID;
    }


    /**
     *
     */
       public static CardTypeItem CardInfo = new CardTypeItem();


}
