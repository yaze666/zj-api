package com.atuinfo.common;
import com.atuinfo.exception.ErrorMassageException;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import java.util.Map;

/**
 * @author Yz
 * @company Atu
 * @create  2019-08-07 10:41
 */

public class License {
    /**
     * 初始化HIS用户相关信息
     */
    public static void getHisUserMessage(){
        final Record recordList = Db.findFirst(" select P.*,D.编码 as 部门编码,D.名称 as 部门名称,M.部门ID,u.用户名 from 上机人员表 U,人员表 P,部门表 D,部门人员 M Where U.人员id = P.id And P.ID=M.人员ID and  M.缺省=1 and M.部门id = D.id and U.用户名=user");
        if (recordList == null) {
            throw new ErrorMassageException("数据不存在");
        }
        //把查询的sql数据放进集合
        Map<String, Object> nursingRecords = recordList.getColumns();

        //得到单个数据的值
        UserInfo.ID=Integer.parseInt(StrUtil.objToStr(nursingRecords.get("ID")));
        UserInfo.编号=StrUtil.objToStr(nursingRecords.get("编号"));
        UserInfo.部门ID=Integer.parseInt(StrUtil.objToStr(nursingRecords.get("部门ID")));
        UserInfo.简码=StrUtil.objToStr(nursingRecords.get("简码"));
        UserInfo.姓名=StrUtil.objToStr(nursingRecords.get("姓名"));
        UserInfo.部门=StrUtil.objToStr(nursingRecords.get("部门名称"));
        UserInfo.用户名=StrUtil.objToStr(nursingRecords.get("用户名"));
        UserInfo.站点=StrUtil.objToStr(nursingRecords.get("站点"));
    }
}
