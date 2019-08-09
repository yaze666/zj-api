package com.atuinfo.util;

import com.alibaba.fastjson.JSONObject;
import com.atuinfo.exception.ErrorMassageException;

import java.util.Map;

/**
 * @author Yz
 * @company Atu
 * @create  2019-08-06 11:23
 */
public class XMLhelper {
    /**
     * 入参
     * @param strRequest
     * @return
     */
    public Map<String,Object> FormatValidation(String strRequest){
        Map<String, Object> params = (Map<String, Object>) JSONObject.parseObject(StaxonUtils.xml2json(strRequest), Map.class).get("Request");
        if (null == params) {
            throw new ErrorMassageException("格式错误,请参照文档以正确格式传参");
        }
        return  params;
    }
}
