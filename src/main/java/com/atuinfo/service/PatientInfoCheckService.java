package com.atuinfo.service;

import com.atuinfo.common.StrUtil;
import com.atuinfo.core.ResultCode;
import com.atuinfo.exception.ErrorMassageException;
import com.atuinfo.util.MapToXmlUtile;
import com.atuinfo.util.XMLhelper;
import com.jfinal.aop.Before;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.tx.Tx;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Yz
 * @company Atu
 * @create  2019-08-08 15:46
 */
@Before(Tx.class)
public class PatientInfoCheckService extends XMLhelper {

    /**
     * 患者信息校验（getPatientInfo）
     * @param strRequest
     * @return
     */
    public String getPatientInfo(String strRequest) {
        String strTypeName = "";
        // 解析xml 得到参数
        Map<String, Object> params = FormatValidation(strRequest);
//        final String patientName = BeanUtil.convert(params.get("patientName"),String.class); // 患者姓名
        final String idCard = StrUtil.objToStr(params.get("idCard")); // 证件号
        final String idcardType = StrUtil.objToStr(params.get("idcardType")); // 证件类型
        final String cardNo = StrUtil.objToStr(params.get("cardNo")); // 卡号
        final String cardType = StrUtil.objToStr(params.get("cardType")); //  卡类型：1 身份证、2 住院号、3 就诊卡号等，4.电子健康卡

        String newCardType ="";
        switch (cardType)
        {
            case "1":
                newCardType = "就诊卡号";
                break;
            case "2":
                newCardType = "身份证号";
                break;
            case "3":
                newCardType = "住院号";
                break;
            case "4":
                newCardType = "电子健康卡";
                break;
            default:
                throw new ErrorMassageException("无效的类别");
        }

        Map result = new HashMap();
        //如果就诊卡不为空 就按照就诊卡查询
        if (!StrKit.isBlank(cardNo)) {
            String strSql = "Select B.病人ID As PatientID,B.姓名 As PatName,B.性别 As PatSex,B.年龄 As PatAge,B.家庭电话 As HomePhone,nvl(Sum(C.预交余额),0) As AccBalance From 病人信息 B,病人余额 C Where B.病人ID=C.病人ID And C.类型=2 And B." + newCardType + "=? \n" +
                    "Group by B.病人ID,B.姓名,B.性别,B.年龄,B.家庭电话\n" +
                    "\n";
            Record getUserList = Db.findFirst(strSql, cardNo);
            //接收数据

            if (getUserList == null) {
                throw new ErrorMassageException("抱歉,未能查询到数据");
            }
            Map<String, Object> Records = getUserList.getColumns();
            String patientID = StrUtil.objToStr(Records.get("病人ID"));
            String patientName = StrUtil.objToStr(Records.get("姓名"));
            String patientSex = StrUtil.objToStr(Records.get("性别"));
            String patientIdCard = StrUtil.objToStr(Records.get("身份证号"));
            String patientAge = StrUtil.objToStr(Records.get("年龄"));
            String patientCardNo = StrUtil.objToStr(Records.get("就诊卡号"));
            String patientCall = StrUtil.objToStr(Records.get("联系人电话"));

            result.put("patientID", patientID);
            result.put("patientName", patientName);
            result.put("patientSex", patientSex);
            result.put("patientIdCard", patientIdCard);
            result.put("patientAge", patientAge);
            result.put("patientCardNo", patientCardNo);
            result.put("patientCall", patientCall);
        } else {
            // 否则使用身份证
            String strSql = "Select B.病人ID As PatientID,B.姓名 As PatName,B.性别 As PatSex,B.年龄 As PatAge,B.家庭电话 As HomePhone,nvl(Sum(C.预交余额),0) As AccBalance From 病人信息 B,病人余额 C Where B.病人ID=C.病人ID And C.类型=2 And B.身份证号=? \n" +
                    "Group by B.病人ID,B.姓名,B.性别,B.年龄,B.家庭电话";
            Record getUserList = Db.findFirst(strSql, idCard);
            if (getUserList == null) {
                throw new ErrorMassageException("抱歉,未能查询到数据");
            }
            Map<String, Object> Records = getUserList.getColumns();
            String patientID = StrUtil.objToStr(Records.get("PatientID"));
            String patientName = StrUtil.objToStr(Records.get("PatName"));
            String patientSex = StrUtil.objToStr(Records.get("PatSex"));
            String patientIdCard = StrUtil.objToStr(idCard);
            String patientAge = StrUtil.objToStr(Records.get("PatAge"));
            String patientCardNo = StrUtil.objToStr(cardNo);
            String patientCall = StrUtil.objToStr(Records.get("HomePhone"));

            result.put("patientID", patientID);
            result.put("patientName", patientName);
            result.put("patientSex", patientSex);
            result.put("patientIdCard", patientIdCard);
            result.put("patientAge", patientAge);
            result.put("patientCardNo", patientCardNo);
            result.put("patientCall", patientCall);
        }
        return MapToXmlUtile.mapToXml(ResultCode.SUCCESS, "患者信息效验成功！", result, false);
    }
}