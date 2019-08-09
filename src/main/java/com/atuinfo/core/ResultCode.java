package com.atuinfo.core;

/**
 * 响应码枚举，参考HTTP状态码的语义
 */
public class ResultCode {
    public final static int SUCCESS = 0;//成功
    public final static int FAIL = 1;//失败
    public final static int UNAUTHORIZED = 401;//未认证（签名错误）
    public final static int NOT_FOUND = 404;//接口不存在
    public final static int INTERNAL_SERVER_ERROR = 500;//服务器内部错误
}
