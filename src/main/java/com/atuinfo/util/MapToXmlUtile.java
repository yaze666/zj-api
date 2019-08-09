package com.atuinfo.util;

import java.util.Map;

/**
 * @author dp
 * @version 1.0.0
 * @date 2019-08-06 09:19
 */
public class MapToXmlUtile {
    public static String toXml(Map<String, Object> params) {
        StringBuilder xml = new StringBuilder();
        // xml.append("<?xml version=\"1.0\" encoding=\"GB2312\" standalone=\"yes\" ?>");
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            String key   = entry.getKey();
            String value = String.valueOf(entry.getValue());
            // 略过空值
            xml.append("<").append(key).append(">");
            xml.append(value);
            xml.append("</").append(key).append(">");
        }
        return xml.toString();
    }

    public static String mapToXml(int code, String message, Map<String, Object> params,boolean ifList) {
        StringBuilder xml = new StringBuilder();
        xml.append("<Response>\n");
        xml.append("<returnCode>"+code+"</returnCode>\n");
        xml.append("<returnInfo>"+message+"</returnInfo>\n");

        if (ifList) {
            xml.append("<list>\n");
        }
        if (null != params) {
            xml.append(toXml(params));
        }
        if (ifList) {
            xml.append("</list>\n");
        }


        xml.append("</Response>");
        return xml.toString();
    }
}
