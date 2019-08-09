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
 * @create  2019-08-08 9:12
 */

@Before(Tx.class)
public class ClinicPayService extends XMLhelper {

    /**
     * 诊间支付（列表查询getClinicPayList）
     * @param strRequest
     * @return
     */
    public  String  getClinicPayList(String strRequest){
        // 解析xml 得到参数
        Map<String, Object> params = FormatValidation(strRequest);
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
     * 诊间支付（详情clinicPayDetail）
     * @param strRequest
     * @return
     */
    public  String   getClinicPayDetail(String strRequest){
        // 解析xml 得到参数
        Map<String, Object> params = FormatValidation(strRequest);
        final String projectId = BeanUtil.convert(params.get("projectId"),String.class); // 就诊卡号
        // 判断是否空值
        if (StrKit.isBlank(projectId)) {
            throw new ErrorMassageException("入参XML不存在节点" + projectId);
        }

        return  null;
    }




    /**
     * 诊间支付（支付接口clinicPay）
     * @param strRequest
     * @return
     */
    public  String   getClinicPay(String strRequest){
        // 解析xml 得到参数
        Map<String, Object> params = FormatValidation(strRequest);
        final String tradeType = BeanUtil.convert(params.get("tradeType"),String.class); // 交易类型1：支付宝2：微信3：银行
        final String thirdPartyNo = BeanUtil.convert(params.get("thirdPartyNo"),String.class); // 第三方支付流水号
        final String outTradeNo = BeanUtil.convert(params.get("outTradeNo"),String.class); // 业务订单号
        final String cardNo = BeanUtil.convert(params.get("cardNo"),String.class); // 卡号
        final String cardType = BeanUtil.convert(params.get("cardType"),String.class); // 卡类型 :1 身份证、2 住院号、3 就诊卡号等
        final String projectId = BeanUtil.convert(params.get("projectId"),String.class); // 诊间支付单Id
        final String payFee = BeanUtil.convert(params.get("payFee"),String.class); // 缴费金额 元
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
        if (StrKit.isBlank(projectId)) {
            throw new ErrorMassageException("入参XML不存在节点" + projectId);
        }
        if (StrKit.isBlank(payFee)) {
            throw new ErrorMassageException("入参XML不存在节点" + payFee);
        }
        if (StrKit.isBlank(source)) {
            throw new ErrorMassageException("入参XML不存在节点" + source);
        }
        return  null;
    }





}
