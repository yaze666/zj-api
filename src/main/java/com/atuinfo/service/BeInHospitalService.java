package com.atuinfo.service;

import com.atuinfo.exception.ErrorMassageException;
import com.atuinfo.util.BeanUtil;
import com.atuinfo.util.XMLhelper;
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
public class BeInHospitalService extends XMLhelper {

    /**
     * 住院预缴信息查询（getBeInHospitalInfo）
     * @param strRequest
     * @return
     */
    public  String  getBeInHospitalInfo(String strRequest){
        // 解析xml 得到参数
        Map<String, Object> params = FormatValidation(strRequest);
        String aa="";
        final String cardNo = BeanUtil.convert(params.get("cardNo"),String.class); // 就诊卡号
        final String cardtype = BeanUtil.convert(params.get("cardtype"),String.class); // 1 身份证、2 住院号、3 就诊卡号等4.电子健康卡
        // 判断是否空值
        if (StrKit.isBlank(cardNo)) {
            throw new ErrorMassageException("入参XML不存在节点" + cardNo);
        }
        if (StrKit.isBlank(cardtype)) {
            throw new ErrorMassageException("入参XML不存在节点" + cardtype);
        }

        return  null;
    }


    /**
     * 住院每日清单查询（beInHospitalBills）
     * @param strRequest
     * @return
     */
    public  String  getBeInHospitalBills(String strRequest){
        // 解析xml 得到参数
        Map<String, Object> params = FormatValidation(strRequest);
        final String inHospitalId = BeanUtil.convert(params.get("inHospitalId"),String.class); // 住院号
        final String startTime = BeanUtil.convert(params.get("startTime"),String.class); // 清单开始日期 yyyy-MM-dd
        final String endTime = BeanUtil.convert(params.get("endTime"),String.class); // 清单结束日期 yyyy-MM-dd

        // 判断是否空值
        if (StrKit.isBlank(inHospitalId)) {
            throw new ErrorMassageException("入参XML不存在节点" + inHospitalId);
        }
        if (StrKit.isBlank(startTime)) {
            throw new ErrorMassageException("入参XML不存在节点" + startTime);
        }
        if (StrKit.isBlank(endTime)) {
            throw new ErrorMassageException("入参XML不存在节点" + endTime);
        }
        return null;
    }


    /**
     * 住院费用预缴（getBeInHospitalPay）
     * @param strRequest
     * @return
     */
    public  String  getBeInHospitalPay(String strRequest){
        // 解析xml 得到参数
        Map<String, Object> params = FormatValidation(strRequest);
        final String tradeType = BeanUtil.convert(params.get("tradeType"),String.class); // 交易类型1：支付宝2：微信3：银行
        final String thirdPartyNo = BeanUtil.convert(params.get("thirdPartyNo"),String.class); // 第三方支付流水号
        final String outTradeNo = BeanUtil.convert(params.get("outTradeNo"),String.class); // 业务订单号
        final String cardNo = BeanUtil.convert(params.get("cardNo"),String.class); // 卡号

        final String cardType = BeanUtil.convert(params.get("cardType"),String.class); // 1 身份证、2 住院号、3 就诊卡号等 4.电子健康卡
        final String inHospitalId = BeanUtil.convert(params.get("inHospitalId"),String.class); // 住院号
//        final String payFee = BeanUtil.convert(params.get("payFee"),String.class); // 缴费金额 单位：元
        final String source = BeanUtil.convert(params.get("source"),String.class); // 第三方服务商Id

        // 判断是否空值
        if (StrKit.isBlank(tradeType)) {
            throw new ErrorMassageException("入参XML不存在节点" + tradeType);
        }
        if (StrKit.isBlank(thirdPartyNo)) {
            throw new ErrorMassageException("入参XML不存在节点" + thirdPartyNo);
        }
        if (StrKit.isBlank(outTradeNo)) {
            throw new ErrorMassageException("入参XML不存在节点" + outTradeNo);
        }
        if (StrKit.isBlank(cardNo)) {
            throw new ErrorMassageException("入参XML不存在节点" + cardNo);
        }
        if (StrKit.isBlank(cardType)) {
            throw new ErrorMassageException("入参XML不存在节点" + cardType);
        }
        if (StrKit.isBlank(inHospitalId)) {
            throw new ErrorMassageException("入参XML不存在节点" + inHospitalId);
        }
//        if (StrKit.isBlank(payFee)) {
//            throw new ErrorMassageException("入参XML不存在节点" + payFee);
//        }
        if (StrKit.isBlank(source)) {
            throw new ErrorMassageException("入参XML不存在节点" + source);
        }
        return null;
    }




}
