package com.atuinfo.service;

import com.alibaba.fastjson.JSONObject;
import com.atuinfo.common.ExecPublic;
import com.atuinfo.common.Initiation;
import com.atuinfo.common.StrUtil;
import com.atuinfo.common.UserInfo;
import com.atuinfo.core.ResultCode;
import com.atuinfo.exception.ErrorMassageException;
import com.atuinfo.util.BeanUtil;
import com.atuinfo.util.MapToXmlUtile;
import com.atuinfo.util.StaxonUtils;
import com.jfinal.aop.Before;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.*;
import com.jfinal.plugin.activerecord.tx.Tx;
import com.sun.org.apache.xpath.internal.operations.And;
import jdk.nashorn.internal.objects.annotations.Where;

import java.math.BigDecimal;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Yz
 * @version 1.0.0
 * @date 2019-08-01 10:25
 */
@Before(Tx.class)
public class BookingService {
    //接收XML参数
    public Map<String, Object> FormatValidation(String strRequest) {
        Map<String, Object> params = (Map<String, Object>) JSONObject.parseObject(StaxonUtils.xml2json(strRequest), Map.class).get("Request");
        if (null == params) {
            throw new ErrorMassageException("格式错误,请参照文档以正确格式传参");
        }
        return params;
    }

    //查询
    public Object execute(final String sql) {
        return Db.execute(new ICallback() {
            @Override
            public Object call(Connection conn) {
                try {
                    CallableStatement proc = conn.prepareCall("{" + sql + "}");
                    proc.execute();
                } catch (SQLException e) {
                    throw new ErrorMassageException(e.getMessage());
                }
                return null;
            }
        });
    }

    /**
     * 取消预约
     *
     * @param strRequest
     * @return
     */
    public String cancelBook(String strRequest) {
        // 解析xml 得到参数
        Map<String, Object> params = FormatValidation(strRequest);
        final String thirdPartyNo = StrUtil.objToStr(params.get("thirdPartyNo"));
        final String bookingOrderId = StrUtil.objToStr(params.get("bookingOrderId"));
        if (StrKit.isBlank(bookingOrderId)) {
            throw new ErrorMassageException("bookingOrderId 不能为空");
        }
        //判断当前号源是否存在
        final List<Record> recordList = Db.find(" Select A.诊室,A.门诊号,A.姓名,A.性别,A.年龄,B.费别,C.编码 As 付款方式,发生时间,号序\n" + "  From 病人挂号记录 A,病人信息 B,医疗付款方式 C\n" +
                "\tWhere A.病人ID=B.病人ID And B.医疗付款方式=C.名称 and A.No=? and A.预约=1 And A.记录性质=2 and A.记录状态=1", bookingOrderId);
        //System.out.println(recordList.size());
        if (recordList.size() == 0) {
            throw new ErrorMassageException("号源不存在");
        }
        List<Map<String, Object>> nursingRecords = new ArrayList<>();
        for (Record record : recordList) {
            Map<String, Object> map = record.getColumns();
            nursingRecords.add(map);
        }
        //调用取消预约存储过程
        Db.execute(new ICallback() {
            @Override
            public Object call(Connection conn) throws SQLException {
                try {
                    CallableStatement proc = conn.prepareCall("{Call Zl_三方机构挂号_Delete (?,?,?,?,?)}");
                    proc.setString(1, bookingOrderId);
                    proc.setString(2, thirdPartyNo);
                    proc.setString(3, "取消预约");
                    proc.setString(4, null);
                    proc.setString(5, null);
                    proc.execute();
                } catch (SQLException e) {
                    throw new ErrorMassageException("执行Call Zl_三方机构挂号_Delete存储过程错误");
                }
                //params = RecordBuilder.me.build(DbKit.getConfig(), rs);
                //代码来到这里就说明你的存储过程已经调用成功，如果有输出参数，接下来就是取输出参数的一个过程
                    /*Record record = new Record();
                    //国税有税源无
                    record.set("GSYSYW",proc.getObject(1));
                    //国税无税源有
                    record.set("GSWSYY",proc.getObject(2));
                    //识别号不同名称相同
                    record.set("SBHBTMCT",proc.getObject(3));
                    //识别号相同名称不同
                    record.set("SBHTMCBT",proc.getObject(4));
                    //识别号名称都相同
                    record.set("SBHMCXT",proc.getObject(5));
                    //setAttr("Count",record);
                    return proc;*/
                return null;
            }
        });
        /**
         * 逻辑代码
         */
        // 返回结果集
        return MapToXmlUtile.mapToXml(ResultCode.SUCCESS, "成功", null, false);
    }

    /**
     * 预约取号
     *
     * @param strRequest
     * @return
     */
    public String bookingGetNo(String strRequest) {
        double dbl冲预交 = 0;
        String str医保结算方式 = "";
        String strRem = "";

        // 解析xml 得到参数
        Map<String, Object> params = FormatValidation(strRequest);
        final String bookingOrderId = StrUtil.objToStr(params.get("bookingOrderId")); // 预约单编号
        final String thirdPartyNo = StrUtil.objToStr(params.get("thirdPartyNo"));  // 第三方支付流水号
        final String outTradeNo = StrUtil.objToStr(params.get("outTradeNo"));   // 业务订单号
        final String payFee = StrUtil.objToStr(params.get("payFee")); // 挂号：支付费用
        final String tradeType = StrUtil.objToStr(params.get("tradeType")); // 支付方式
        if (StrKit.isBlank(bookingOrderId)) {
        }
        // 判断三方返回的费用与总费用是否相等
        final Record recordList = Db.findFirst("  Select 病人ID,nvl(sum(实收金额),0) as 金额 From 门诊费用记录 Where NO=? and 记录性质=4 group by 病人id", bookingOrderId);
        if (recordList == null) {
            throw new ErrorMassageException("数据不存在");
        }
        // 把查询的sql数据放进集合
        Map<String, Object> nursingRecords = recordList.getColumns();

        // 得到单个数据的值
        String patientId = String.valueOf(nursingRecords.get("病人id")); // 病人ID
        String sum = String.valueOf(nursingRecords.get("金额")); // 获取数据库的金额
        // 将金额转换成金额类型
        BigDecimal MoneySum = new BigDecimal(sum);    //数据库金额
        BigDecimal payFeeSum = new BigDecimal(payFee);//前台传递的金额
        // 如果两次金额不一致
        if (!MoneySum.equals(payFeeSum)) {
            throw new ErrorMassageException("挂号金额合计与该号的价格不一致"); // 提示错误
        }
        // 获取病人预约信息
        final Record PatientInfoList = Db.findFirst(" \n" +
                "Select A.号别,A.诊室,A.门诊号,A.姓名,A.性别,A.年龄,B.费别,nvl(C.编码,'09') As 付款方式,发生时间,号序,D.ID As" +
                " 医生ID,D.姓名 As 医生姓名,A.执行部门ID As 科室ID From 病人挂号记录 A,病人信息 B,医疗付款方式 C,人员表 D " +
                "Where A.病人ID=B.病人ID And B.医疗付款方式=C.名称 And A.No=? And A.预约=1 And A.记录性质=2 And 执行人=D.姓名", bookingOrderId);
        // 接收数据
        // 判断空
        if (PatientInfoList == null) {
            throw new ErrorMassageException("预约挂号单信息丢失或已经取号，请重新获取！");
        }
        Map<String, Object> Records = PatientInfoList.getColumns();

        //取值  后续会调用到
        String hb = Records.get("号别").toString();          // 获取号别
        long doctorId = ((BigDecimal) Records.get("医生ID")).longValue();  // 医生ID
        Timestamp timestamp = PatientInfoList.getTimestamp("发生时间");
        Date date = new Date(timestamp.getTime());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        String startTime = simpleDateFormat.format(date);  // 发生时间
        String doctorName = String.valueOf(Records.get("医生姓名"));  // 医生姓名
        long departmentsId = ((BigDecimal) Records.get("科室ID")).longValue();// 科室ID
        String consultingRoom = String.valueOf(Records.get("诊室")); // 诊室
        String patientNumber = String.valueOf(Records.get("门诊号")); // 门诊号
        String name = String.valueOf(Records.get("姓名")); // 姓名
        String sex = String.valueOf(Records.get("性别")); // 性别
        String age = String.valueOf(Records.get("年龄")); // 年龄
        String payMethod = String.valueOf(Records.get("付款方式"));//付款方式
        String fb = String.valueOf(Records.get("费别")); // 费别
        String hx = String.valueOf(Records.get("号序")); // 号序

        final Record List = Db.findFirst("Select 项目ID From 挂号安排 Where 号码=?", hb);
        // 把查询的sql数据放进集合
        Map<String, Object> rs = List.getColumns();
        if (List == null) {
            throw new ErrorMassageException("预约挂号单收费信息已作废，请重新挂号！");
        }
        String ProjectId = String.valueOf(rs.get("项目ID")); // 项目ID

        ExecPublic.InitCardInfo(ExecPublic.GetHisCardTypeID(tradeType));

        // 调用Zl_预约挂号接收_Insert全部以现金将病人进行接收。
        final Record List1 = Db.findFirst("Select 病人结帐记录_id.nextval From dual");
        Map<String, Object> rs1 = List1.getColumns();

        // 结账ID
        String NEXTVAL = String.valueOf(rs1.get("nextval")); // 病人结帐记录_id
        String strHISVERSION = Initiation.HISVERSION.trim();

        String strSql = "Call Zl_预约挂号接收_Insert(";
        // No
        strSql += "'" + bookingOrderId + "'";
        // 实际票号
        strSql += ",NULL";
        // 领用ID
        strSql += ",NULL";
        // 结帐id
        strSql += "," + NEXTVAL.toString();
        // 诊室
        strSql += ",'" + consultingRoom + "'";
        // 病人ID
        strSql += "," + patientId.toString();
        // 标识号【门诊号】
        strSql += "," + patientNumber;
        // 姓名
        strSql += ",'" + name + "'";
        // 性别
        strSql += ",'" + sex + "'";
        // 年龄
        strSql += ",'" + age + "'";
        // 付款方式
        strSql += ",'" + payMethod + "'";
        // 费别
        strSql += ",'" + fb + "'";
        // 结算方式
        strSql += ",'" + ExecPublic.CardInfo.getY_str结算方式() + "'";
        // 现金支付
        strSql += "," + payFeeSum;
        // 预交支付
        strSql += ",0";
        // 个帐支付
        strSql += ",0";
        // 发生时间
        strSql += ",to_date('" + startTime + "','YYYY-MM-DD HH24:MI:SS')";
        // 序号
        if (strHISVERSION != "35.120")
            strSql += "," + hx;
        // 操作员编号
        strSql += ",'" + UserInfo.编号 + "'";
        // 操作员姓名
        strSql += ",'" + UserInfo.姓名 + "'";
        // 生成队列_In
        if (strHISVERSION != "35.120")
            strSql += ",1";
        // 登记时间_In
        strSql += ",sysdate";
        // 卡类别id_In
        strSql += "," + 12;
        // 结算卡序号_In
        strSql += ",Null";
        // 卡号_In
        strSql += ",'" + thirdPartyNo + "'";
        // 交易流水号_In
        strSql += ",'" + outTradeNo + "'";
        // 交易说明_In
        strSql += ",'" + strRem + "'";
        strSql += ")";
        execute(strSql); // 执行SQL

        //⑥ 调用zl_病人结算记录_update更新医保结算方式。若为三方支付传入三方支付信息。
        if (!str医保结算方式.equals("")) {
            //提交挂号交易[扣预交金]
            if (dbl冲预交 > 0) {
                // 走冲预交模式
                strSql = "Call zl_病人结算记录_update(" + NEXTVAL + ",'" + str医保结算方式 + "',0,Null,1)";
            } else {
                // 走三方模式
                strSql = "Call zl_病人结算记录_update(" + NEXTVAL + "," +
                        "'" + str医保结算方式 + "',0,'" + ExecPublic.CardInfo.getP_intID() + "',0,'" + ExecPublic.CardInfo.getP_intID() + "',Null," +
                        "'" + thirdPartyNo + "','" + outTradeNo + "','" + strRem + "')";
            }
            execute(strSql);
        }

        // 强制保存挂号订单号到病人挂号记录的交易水号
        strSql = "Update 病人挂号记录" +
                "    Set 交易流水号='" + outTradeNo + "',交易说明='" + strRem + "'" +
                "    Where 记录性质 = 1 And 记录状态 = 1 And No='" + bookingOrderId + "'";
        Db.update(strSql);

        // 强制保存挂号订单号到病人预交记录的交易水号
        strSql = "Update 病人预交记录" +
                "    Set 交易流水号='" + outTradeNo + "',交易说明='" + strRem + "'" +
                "    Where 结帐id = " + NEXTVAL + " And 卡类别ID=" + ExecPublic.CardInfo.getP_intID();
        Db.update(strSql);


        //⑦ 调用Zl_病人挂号汇总_Update更新病人挂号汇总。
        strSql = "Call Zl_病人挂号汇总_Update('";
        // 医生姓名
        strSql += doctorName;
        // 医生ID
        strSql += "'," + (doctorId > 0 ? doctorId : "Null");
        // 收费细目ID
        strSql += "," + ProjectId;
        // 执行部门ID
        strSql += "," + departmentsId;
        // 发生时间
        strSql += ",to_date('" + startTime + "','YYYY-MM-DD HH24:MI:SS')";
        // 预约标志【是否为预约接收:0-非预约挂号; 1-预约挂号,2-预约接收】
        strSql += ",2";
        // 号码
        strSql += ",'" + hb + "'";
        strSql += ")";
        Db.find(strSql);

        Map result = new HashMap();
        result.put("bookingOrderId", bookingOrderId);

        return MapToXmlUtile.mapToXml(ResultCode.SUCCESS, "挂号取号（OutPatRegisteGetNo）交易成功！", result, false);
    }

    /**
     * 预约挂号确认
     *
     * @param strRequest
     * @return
     */
    public String bookingConfirm(String strRequest) {
        String SessionType = ""; // 医生类别
        String TimeValue = "";

        boolean blnTime = false;

        String SchedulingID = "121313414"; // 模拟的排班ID
        // 模拟的AsRowID
        String[] AsRowID = SchedulingID.split("|");
        String endAsRowID = AsRowID[1];
        System.out.println(endAsRowID);

        // 模拟的strDate
        String[] strDate = SchedulingID.split("|");
        String endStrDates = strDate[0];
        System.out.println(endStrDates);

        //解析入参
        Map<String, Object> params = FormatValidation(strRequest);

        final String visitTimeId = BeanUtil.convert(params.get("visitTimeId"), String.class); // 号源
        final String amPm = BeanUtil.convert(params.get("amPm"), String.class); // 上下午:A:上午 P:下午 F:白天 N:夜间 W：全天
        final String queueNo = BeanUtil.convert(params.get("queueNo"), String.class); // 号序（第几号），数字：1，2，3...
        final String payFee = BeanUtil.convert(params.get("payFee"), String.class); // 挂号费,单位：元
        final String idCard = BeanUtil.convert(params.get("idCard"), String.class); // 身份证
        final String patientName = BeanUtil.convert(params.get("patientName"), String.class); // 病人姓名
        final String phoneNumber = BeanUtil.convert(params.get("phoneNumber"), String.class); // 手机号码
        // 判断是否空值
        if (StrKit.isBlank(visitTimeId)) {
            throw new ErrorMassageException("入参XML不存在节点" + visitTimeId);
        }
        if (StrKit.isBlank(amPm)) {
            throw new ErrorMassageException("入参XML不存在节点" + amPm);
        }
        if (StrKit.isBlank(queueNo)) {
            throw new ErrorMassageException("入参XML不存在节点" + queueNo);
        }
        if (StrKit.isBlank(payFee)) {
            throw new ErrorMassageException("入参XML不存在节点" + payFee);
        }
        if (StrKit.isBlank(idCard)) {
            throw new ErrorMassageException("入参XML不存在节点" + idCard);
        }
        if (StrKit.isBlank(patientName)) {
            throw new ErrorMassageException("入参XML不存在节点" + patientName);
        }
        if (StrKit.isBlank(phoneNumber)) {
            throw new ErrorMassageException("入参XML不存在节点" + phoneNumber);
        }

//        long lng病人ID = Convert.ToInt64(OutPatientID);
        String 病人id = ""; // 病人ID

        //获取病人基本信息  根据门诊号查询到病人的姓名，费别，以及门诊号
        /**
         * 注意!!!!!   这个SQL病人ID为门诊号  入参没有门诊号参数
         */
        final List<Record> getUserList = Db.find("Select A.病人ID,A.姓名,A.费别,A.门诊号 From 病人信息 A Where A.身份证号=?", idCard);
        //接收数据
        List<Map<String, Object>> Records = new ArrayList<>();
        for (Record record : getUserList) {
            Map<String, Object> map = record.getColumns();
            Records.add(map);
        }
        //如果可以查出记录则表示启用了时间段
        if (getUserList.size() == 0) {
            throw new ErrorMassageException("病人信息未找到！");
        }
        // 取值
        String patienID = Records.get(0).get("病人ID").toString();
        String name = Records.get(0).get("姓名").toString();
        String fb = Records.get(0).get("费别").toString();
        String patientNumber = Records.get(0).get("门诊号").toString();

        //取得就诊位置
        final List<Record> getClinicAddrList = Db.find("select A.ID As 就诊科室ID,A.名称 As 就诊科室,A.位置 As 就诊位置,B.号类,C.ID As 医生ID,B.医生姓名,Nvl(C.专业技术职务,'*' || D.名称) As 医生职务\\n\" +\n" +
                "                    \"from 部门表 A,挂号安排 B,人员表 C,收费项目目录 D\\n\" +\n" +
                "                    \"Where A.ID=B.科室ID And B.医生姓名 = C.姓名(+) And B.项目ID=D.ID And B.号码=?", endAsRowID);
        if (getClinicAddrList.size() == 0) {
            throw new ErrorMassageException("挂号安排不完善");
        }
        // 取值
        String str就诊科室ID = Records.get(0).get("就诊科室ID").toString();
        String str就诊科室 = Records.get(0).get("就诊科室").toString();
        String str就诊位置 = Records.get(0).get("就诊位置").toString();
        String str号类 = Records.get(0).get("号类").toString();
        String str医生ID = Records.get(0).get("医生ID").toString();
        String str医生姓名 = Records.get(0).get("医生姓名").toString();
        String str医生职务 = Records.get(0).get("医生职务").toString();

        // 判断医生职务
        if (str医生职务.substring(1, 1).equals("*"))
            SessionType = "*" + str医生职务;
        else if (str医生职务.indexOf("副主任") > 0)
            SessionType = "*副主任";
        else if (str医生职务.indexOf("主任") > 0)
            SessionType = "*主任";
        else if (str医生职务.indexOf("急诊") > 0)
            SessionType = "*急诊";
        else
            SessionType = str医生职务;
        // 判断当前号别是否启用时间段
        final List<Record> isHbStartTime = Db.find("Select 1 As Tmp  From 挂号安排 A, 挂号安排时段 B Where a.Id = b.安排id And a.号码 = ?\n" +
                "And b.星期 = Decode(To_Char(decode('?','',trunc(sysdate),to_date('2019-05-12','yyyy-mm-dd')), 'D'), '1', '周日', '2', '周一', '3', '周二', 4, '周三', '5', '周四', '6', '周五', '7', '周六', Null)\n" +
                "And Not Exists (Select 1 From 挂号安排计划 Where 安排id = a.Id And 审核时间 Is Not Null And sysdate Between 生效时间 And 失效时间)\n" +
                "And Not Exists (Select 1 From 挂号安排停用状态 Where 安排id = a.Id And decode('?','',trunc(sysdate),to_date('?','yyyy-mm-dd')) Between 开始停止时间 And 结束停止时间) Union All \n" +
                "\n" +
                "Select 1 As Tmp From 挂号安排计划 X, 挂号计划时段 Y, 挂号安排 Z Where x.Id = y.计划id And x.安排id = z.Id And z.号码 = ?\n" +
                "And y.星期 = Decode(To_Char(decode('?','',trunc(sysdate),to_date('?','yyyy-mm-dd')), 'D'), '1', '周日', '2', '周一', '3', '周二', 4, '周三', '5', '周四', '6', '周五', '7', '周六', Null)\n" +
                "And Not Exists (Select 1 From 挂号安排停用状态 Where 安排id = x.安排id And decode('2','',trunc(sysdate),to_date('?','yyyy-mm-dd')) \n" +
                "Between 开始停止时间 And 结束停止时间)", endAsRowID, endStrDates);
        // 如果可以查出记录则表示启用了时间段
        if (isHbStartTime != null) {
            blnTime = true;
        }
        // 未启用时间段，获取时间段
        if (!blnTime) {
            final List<Record> getTimeBucket = Db.find(" select to_char(开始时间,'HH24:MI') || '-' || to_char(终止时间,'HH24:MI') As TimeValue\n" +
                    "            from (  Select T.ID As 安排ID,Null As 计划ID, to_date('?','YYYY-MM-DD') As RegisterDate,\n" +
                    "                    Decode(To_Char(to_date('?','YYYY-MM-DD'), 'D'), '1', T.周日, '2', T.周一, '3', T.周二, '4', T.周三, '5', T.周四, '6', T.周五,'7', T.周六, Null)\n" +
                    "                    As RegisterTime, T.号码,decode(I.项目特性,'1','急诊',T.号类)\n" +
                    "                    As 号类,T.科室ID,T.项目ID,T.医生ID,S.限号数,S.限约数\n" +
                    "                    From 挂号安排 T,挂号安排限制 S,收费项目目录 I\n" +
                    "                    Where T.ID = S.安排ID(+) And T.项目ID=I.ID\n" +
                    "                    And S.限制项目=Decode(To_Char(to_date('?','YYYY-MM-DD'), 'D'), '1', '周日', '2', '周一', '3', '周二', '4', '周三', '5', '周四', '6', '周五','7', '周六', Null)\n" +
                    "                    And nvl(T.停用日期,to_date('3000-01-01','yyyy-MM-dd')) > to_date('?','YYYY-MM-DD')  And T.号码='?'\n" +
                    "                    And Not Exists (Select 1 From 挂号安排计划 Where 安排id = T.id And 审核时间 Is Not Null\n" +
                    "                            And to_date('?','YYYY-MM-DD') Between 生效时间 And 失效时间) And Not Exists\n" +
                    "                            (Select 1 From 挂号安排停用状态 Where 安排id = T.id And to_date('?','YYYY-MM-DD') Between 开始停止时间 And 结束停止时间)\n" +
                    "                    And Exists (Select 1 From 部门表 Where ID = T.科室ID And to_date('?','YYYY-MM-DD') Between 建档时间 And 撤档时间) Union All\n" +
                    "\n" +
                    "                    select T.ID As 安排ID,Z.ID As 计划ID, to_date('?','YYYY-MM-DD') As RegisterDate,\n" +
                    "                    Decode(To_Char(to_date('?','YYYY-MM-DD'), 'D'), '1', Z.周日, '2', Z.周一, '3', Z.周二, '4', Z.周三, '5', Z.周四, '6', Z.周五,'7', Z.周六, Null)\n" +
                    "                    As RegisterTime, T.号码,T.号类,T.科室ID,T.项目ID,T.医生ID,S.限号数,S.限约数\n" +
                    "                    from 挂号安排 T, 挂号安排计划 Z,挂号计划限制 S ,收费项目目录 I,\n" +
                    "                    ((Select ID From 挂号安排计划 Where (安排ID,生效时间 ) in (select 安排ID,Max(生效时间) As 生效时间 from 挂号安排计划 where 审核时间 is not null\n" +
                    "            And case when to_date('?','yyyy-mm-dd')>sysdate then to_date('?','yyyy-mm-dd') else sysdate end between 生效时间 And 失效时间 group by 安排ID))) U\n" +
                    "                where T.项目ID=I.Id And T.ID =Z.安排ID And Z.Id = S.计划ID(+) And Z.ID=U.ID And S.限制项目=Decode(To_Char(to_date('?','YYYY-MM-DD'), 'D'), '1', '周日', '2', '周一', '3', '周二', '4', '周三', '5', '周四', '6', '周五','7', '周六', Null) " +
                    " And 审核时间 is not null And to_date('?','YYYY-MM-DD') between 生效时间 And 失效时间  And nvl(T.停用日期,to_date('3000-01-01','yyyy-MM-dd')) > to_date('?','YYYY-MM-DD') And T.号码='?' " +
                    " And Not Exists (Select 1 From 挂号安排停用状态 Where 安排id = T.id And to_date('?','YYYY-MM-DD') Between 开始停止时间 And 结束停止时间)And Exists (Select 1 From 部门表" +
                    " Where ID = T.科室ID And to_date('?','YYYY-MM-DD') Between 建档时间 And 撤档时间)) X, 时间段 Y Where X.RegisterTime=Y.时间段", endStrDates, endAsRowID);
           // 还有两个占位符 没有使用
        }
        else{
                //大串SQL
        }
//        if(){
////                throw new Exception(SchedulingID + "|" + intRankID + "已被其它用户占用！");
//            throw new ErrorMassageException(SchedulingID +"|"+ "已被其他用户占用");
//        }
//        TimeValue =  StrUtil.objToStr(getTimeBucket.get(0).get("TimeValue"));   //这里的list有误
        return null;
    }


    /**
     * 退费
     *
     * @param strRequest
     * @return
     */
    public String bookingCancelMoney(String strRequest) throws Exception {
        // 解析入参
        Map<String, Object> params = FormatValidation(strRequest);
        final String bookingOrderId = BeanUtil.convert(params.get("bookingOrderId"), String.class); // 预约单编号
        final String thirdPartyNo = BeanUtil.convert(params.get("thirdPartyNo"), String.class); // 第三方支付流水号
        final String tradeType = BeanUtil.convert(params.get("tradeType"), String.class); // 交易类型1：支付宝2：微信3：银行
        final String payFee = BeanUtil.convert(params.get("payFee"), String.class); // 挂号支付费用 单位：元
        // 判断是否空值
        if (StrKit.isBlank(bookingOrderId)) {
            throw new ErrorMassageException("入参XML不存在节点" + bookingOrderId);
        }
        if (StrKit.isBlank(thirdPartyNo)) {
            throw new ErrorMassageException("入参XML不存在节点" + thirdPartyNo);
        }
        if (StrKit.isBlank(tradeType)) {
            throw new ErrorMassageException("入参XML不存在节点" + tradeType);
        }
        if (StrKit.isBlank(payFee)) {
            throw new ErrorMassageException("入参XML不存在节点" + payFee);
        }
        // 判断当前号源是否存在
        final List<Record> recordList = Db.find(" " +
                "Select Round((Sysdate-发生时间)*24*60) As 分钟,A.诊室,A.门诊号,A.姓名,A.性别,A.年龄,B.费别,C.编码 As 付款方式,A.执行状态,发生时间,号序 " +
                "From 病人挂号记录 A,病人信息 B,医疗付款方式 C " +
                "Where A.病人ID=B.病人ID And B.医疗付款方式=C.名称  And A.No=? And A.记录性质=1 and A.记录状态=1", bookingOrderId);
        if (recordList.size() == 0) {
            throw new ErrorMassageException("预约单据号信息已丢失或已取消预约！");
        }
        // 取值
        int min = Integer.parseInt(StrUtil.objToStr(recordList.get(0).get("分钟")));
        if (min < 60) {
            throw new Exception("预约单据号离就诊时间只有【" + min + "】分钟，不能退号！");
        }
        // 执行状态判断
        String exeState = StrUtil.objToStr(recordList.get(0).get("执行状态"));
        switch (exeState) {
            //0 - 等待接诊,1 - 完成就诊,2 - 正在就诊,-1标记为不就诊
            case "0":
                //0 - 等待接诊,可以退号
                break;
            case "1":
                //完成就诊 不能退号
                throw new Exception("预约单据号【" + bookingOrderId + "】已完成就诊，不能退号！");
            case "2":
                throw new Exception("预约单据号【" + bookingOrderId + "】正在就诊，不能退号！");
            case "-1":
                throw new Exception("预约单据号【" + bookingOrderId + "】标记为不就诊，不能退号！");
        }
        // 查病人ID以及金额
        final List<Record> List1 = Db.find("Select 病人ID,nvl(sum(实收金额),0) as 金额 " +
                " From 门诊费用记录 Where NO=? and 记录性质=4 group by 病人id", bookingOrderId);
        // 读取数据
        if (List1 == null || List1.size() == 0) {
            throw new Exception("预约挂号单信息丢失，请重新获取!");
        }
        // 病人ID
        long patientID = Long.parseLong(List1.get(0).get("病人ID").toString());
        if (patientID == 0) {
            throw new Exception("无法找到病人ID!");
        }
        double money = Double.parseDouble(List1.get(0).get("金额").toString());
        double doublePayFee = Double.parseDouble(payFee);
        if (doublePayFee != money) {
            throw new Exception("本次退号的金额合计与该号的价格不一致！");
        }
        // 调用取消预约存储过程
        Db.execute(new ICallback() {
            @Override
            public Object call(Connection conn) throws SQLException {
                try {
                    CallableStatement proc = conn.prepareCall("{Call Zl_三方机构挂号_Delete (?,?,?,?,?)}");
                    proc.setString(1, bookingOrderId);
                    proc.setString(2, null);
                    proc.setString(3, "取消预约");
                    proc.setString(4, null);
                    proc.setString(5, null);
                    proc.execute();
                } catch (SQLException e) {
                    throw new ErrorMassageException("执行Call Zl_三方机构挂号_Delete存储过程错误");
                }
                return null;
            }
        });

        // 强制保存挂号订单号到病人挂号记录的交易水号
        String strRem = payFee + "|" + thirdPartyNo;
        String strSql = "Update 病人挂号记录" +
                "    Set 交易流水号='" + thirdPartyNo + "',交易说明='" + strRem + "'" +
                "    Where 记录性质 = 1 And 记录状态 = 2 And No='" + bookingOrderId + "'";
        Db.update(strSql);

        ExecPublic.InitCardInfo(ExecPublic.GetHisCardPayID(tradeType)); // 支付类型
        // 强制保存挂号订单号到病人预交记录的交易水号
        strSql = "Update 病人预交记录" +
                "    Set 交易流水号='" + thirdPartyNo + "',交易说明='" + strRem + "',卡类别ID='" + ExecPublic.CardInfo.getP_intID() + "'" +
                "    Where  No='" + bookingOrderId + "' And 记录状态=2 And 结算性质=4 And 记录性质=4";
        Db.update(strSql);
        // XML出参
        Map result = new HashMap();
        result.put("bookingOrderId", bookingOrderId);
        return MapToXmlUtile.mapToXml(ResultCode.SUCCESS, "退费交易成功！", result, false);
    }
}
