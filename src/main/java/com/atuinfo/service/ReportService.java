package com.atuinfo.service;
import com.atuinfo.util.XMLhelper;
import com.atuinfo.exception.ErrorMassageException;
import com.atuinfo.util.BeanUtil;
import com.jfinal.aop.Before;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.tx.Tx;
import java.util.Map;

/**
 * @author Yz
 * @company Atu
 * @create  2019-08-06 11:17
 */
@Before(Tx.class)
public class ReportService extends XMLhelper {

    /**
     * 获取报告单列表 (getReportList) [根据患者就诊卡号和时间获取报告单列表]
     * @param strRequest
     * @return
     */
    public  String  getReportList(String strRequest){
        // 解析xml 得到参数
        Map<String, Object> params = FormatValidation(strRequest);
        final String cardNo = BeanUtil.convert(params.get("cardNo"),String.class); // 就诊卡号
        final String cardtype = BeanUtil.convert(params.get("cardtype"),String.class); // 1 身份证、2 住院号、3 就诊卡号等4.电子健康卡
        final String startTime = BeanUtil.convert(params.get("startTime"),String.class); // 开始时间（空表示查询全部）格式：yyyy-MM-dd
        // 判断是否空值
        if (StrKit.isBlank(cardNo)) {
            throw new ErrorMassageException("入参XML不存在节点" + cardNo);
        }
        if (StrKit.isBlank(cardtype)) {
            throw new ErrorMassageException("入参XML不存在节点" + cardtype);
        }
        if (StrKit.isBlank(startTime)) {
            throw new ErrorMassageException("入参XML不存在节点" + startTime);
        }

        return  null;
    }


    /**
     * 获取检查报告详情（getInspectpicDetail）  [根据报告单号reportNo查询检查报告详情]
     * @param strRequest
     * @return
     */
    public  String  getInspectpicDetail(String strRequest){
        // 解析xml 得到参数
        Map<String, Object> params = FormatValidation(strRequest);
        final String reportNo = BeanUtil.convert(params.get("reportNo"),String.class); // 报告单号

        // 判断是否空值
        if (StrKit.isBlank(reportNo)) {
            throw new ErrorMassageException("入参XML不存在节点" + reportNo);
        }

        return null;
    }


    /**
     * 获取检验报告详情（getInspectDetail）  [根据报告单号reportNo查询检验报告详情]
     * @param strRequest
     * @return
     */
    public  String  getInspectDetail(String strRequest){
        // 解析xml 得到参数
        Map<String, Object> params = FormatValidation(strRequest);
        final String reportNo = BeanUtil.convert(params.get("reportNo"),String.class); // 报告单号

        // 判断是否空值
        if (StrKit.isBlank(reportNo)) {
            throw new ErrorMassageException("入参XML不存在节点" + reportNo);
        }

        return null;
    }


    /**
     * 获取微生物报告详情（getChemicalDetail）  [根据报告单号reportNo查询微生物报告详情]
     * @param strRequest
     * @return
     */
    public  String  getChemicalDetail(String strRequest){
        // 解析xml 得到参数
        Map<String, Object> params = FormatValidation(strRequest);
        final String reportNo = BeanUtil.convert(params.get("reportNo"),String.class); // 报告单号

        // 判断是否空值
        if (StrKit.isBlank(reportNo)) {
            throw new ErrorMassageException("入参XML不存在节点" + reportNo);
        }

        return null;
    }





}
