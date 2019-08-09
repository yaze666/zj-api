package com.atuinfo.Interceptors;

import com.atuinfo.core.Result;
import com.atuinfo.core.ResultCode;
import com.atuinfo.util.MapToXmlUtile;
import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import org.eclipse.jetty.http.HttpStatus;

public class ExceptionInterceptor implements Interceptor {

    @Override
    public void intercept(Invocation invocation) {
        try {
            invocation.invoke();
        } catch (Exception e) {

            e.printStackTrace();
            invocation.getController().renderText(MapToXmlUtile.mapToXml(ResultCode.FAIL, e.getMessage(), null, false));
            //Result result = new Result(HttpStatus.INTERNAL_SERVER_ERROR_500,e.getMessage(),null);

            //invocation.getController().renderJson(result);
        } /*catch (SQLException e) { // 后期再做细节处理
            System.out.println("sql语句错误");
        }*/
    }
}
