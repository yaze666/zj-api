package com.atuinfo.common;

/**
 * @author Yz
 * @company Atu
 * @create  2019-08-02 11:49
 */
public class Func {


    /**
     * 将字符串标准化为数字类型
     * @param strNum
     * @param intNum
     * @return
     */
    public static String GetNum(String strNum, int intNum)
    {
        String strReturn = "0";
        String strFormat = "";
        try
        {
            for (int i = 1; i <= intNum; i++)
            {
                //你要重复的字符串
                strFormat += "0";
            }

            double db =Double.parseDouble(strNum);
            if (strFormat.equals(""))
            {
                strReturn = String.valueOf(db);
            }
            else
            {
                strReturn = String.valueOf("0."+ strFormat);
            }
            return strReturn;
        }
        catch (Exception e)
        {
            return strReturn;
        }
    }
}
