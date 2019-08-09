package com.atuinfo.core;

import java.util.HashMap;
import java.util.Map;

/**
 * 响应结果生成工具
 */
public class ResultGenerator {
    private static final String DEFAULT_SUCCESS_MESSAGE = "SUCCESS";
    private static final Map result = new HashMap();

    public static Map genSuccessResult() {
        result.put("returnCode", ResultCode.SUCCESS);
        result.put("returnInfo", DEFAULT_SUCCESS_MESSAGE);
        return result;
    }
    public static Map genSuccessResult(String message) {
        result.put("returnCode", ResultCode.SUCCESS);
        result.put("returnInfo", message);
        return result;
    }

    public static <T> Result<T> genSuccessResult(T data) {
        return new Result()
                .setCode(ResultCode.SUCCESS)
                .setMessage(DEFAULT_SUCCESS_MESSAGE)
                .setData(data);
    }
    public static Map genFailResult(String message) {
        result.put("returnCode", ResultCode.FAIL);
        result.put("returnInfo", message);
        return result;
    }

}
