package com.emis.vi.bm.util;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DurationFormatUtils;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import sun.misc.BASE64Encoder;

import java.io.*;
import java.lang.management.ManagementFactory;
import java.math.BigDecimal;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.net.URLEncoder;
import java.nio.channels.FileLock;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class emisUtils {
    private static DecimalFormat sdf_pos0 = new DecimalFormat("#");

    private static DecimalFormat sdf_pos1 = new DecimalFormat("#0.0");

    private static DecimalFormat sdf_pos2 = new DecimalFormat("#0.00");

    private static DecimalFormat sdf_pos3 = new DecimalFormat("#0.000");

    private static DecimalFormat sdf_pos4 = new DecimalFormat("#0.0000");

    public static SimpleDateFormat df_yyyyMMddHHmmssSSS = new SimpleDateFormat(
            "yyyyMMddHHmmssSSS");

    public static SimpleDateFormat df_yyyyMMddHHmmss = new SimpleDateFormat(
            "yyyyMMddHHmmss");

    public static SimpleDateFormat df_yyyyMMddHHmm = new SimpleDateFormat(
            "yyyyMMddHHmm");
    public static SimpleDateFormat df_yyyyMMddHH = new SimpleDateFormat(
            "yyyy/MM/dd HH:mm");

    public static SimpleDateFormat df_yyyy_MM_ddHHmmss = new SimpleDateFormat(
            "yyyy-MM-dd HH:mm:ss");

    public static SimpleDateFormat df_yyyy_MM_ddHHmmssE = new SimpleDateFormat(
            "yyyy-MM-dd HH:mm:ss E");

    public static SimpleDateFormat df_HHmmss = new SimpleDateFormat("HHmmss");

    public static SimpleDateFormat df_HH_mm_ss = new SimpleDateFormat(
            "HH:mm:ss");

    public static SimpleDateFormat df_yyMMddHHmmss = new SimpleDateFormat(
            "yyMMddHHmmss");

    public static SimpleDateFormat df_yyyyMMdd = new SimpleDateFormat(
            "yyyyMMdd");

    public static SimpleDateFormat df_yyyyMMddP = new SimpleDateFormat(
            "yyyMMdd");

    public static SimpleDateFormat df_HHmmssSSS = new SimpleDateFormat(
            "HHmmssSSS");

    public static SimpleDateFormat df_yyyy_MM_dd = new SimpleDateFormat(
            "yyyy-MM-dd");

    public static SimpleDateFormat df_MM_dd = new SimpleDateFormat("MM/dd");

    public static String getSYSMMDD() {
        return df_MM_dd.format(new Date());
    }

    /**
     * 返回boolean
     *
     * @param obj
     * @return
     */
    public static boolean parseBoolean(Object obj) {
        return "true".equalsIgnoreCase(parseString(obj)) || "Y".equalsIgnoreCase(parseString(obj));  //$NON-NLS-2$
    }

    /**
     * 返回boolean
     *
     * @param obj
     * @return
     */
    public static boolean parseBoolean(Object obj, boolean defaultValue) {
        return "true".equalsIgnoreCase(parseString(obj, defaultValue + "")) || "Y".equalsIgnoreCase(parseString(obj, defaultValue + ""));  //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
    }

    /**
     * 把Object转换成String，如Object==null 返回默认值
     *
     * @param obj
     * @param defaultValue
     * @return
     */
    public static String parseString(Object obj, String defaultValue) {
        if (obj == null)
            return defaultValue;
        else
            return String.valueOf(obj);
    }

    /**
     * 把Object转换成String，如Object==null 返回""
     *
     * @param obj
     * @return
     */
    public static String parseString(Object obj) {
        return parseString(obj, "");
    }

    /**
     * 处理特殊的。把Object转换成String，如Object==null 返回"" 并且如果有小数，把多余小数位移除
     *
     * @param obj
     * @return
     */
    public static String parseSpeString(Object obj) {
        String str = parseString(obj, "");
        try {
            // 如果是整数，直接返回
            if (isDecimal(str)) {
                Double d = new Double(str);
                int iValue = d.intValue();
                if (iValue == d.doubleValue()) {
                    d = null;
                    return iValue + "";
                }
                d = null;
            }
        } catch (Exception e) {
        }
        return str;
    }

    public static boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        return pattern.matcher(str).matches();
    }

    /**
     * 判断是否为浮点数
     *
     * @param str
     * @return
     */
    public static boolean isDecimal(String str) {
        Pattern pattern = Pattern.compile("^[-]?[0-9]+(\\.)[0-9]+$");
        Matcher isNum = pattern.matcher(str);
        return isNum.matches();
    }

    /**
     * 把Object转换成float，如Object==null 返回默认值
     *
     * @param obj
     * @param defaultValue
     * @return
     */
    public static float parseFloat(Object obj, float defaultValue) {
        // 如果是整数，直接返回
        try {
            Double d = new Double(parseString(obj, String.valueOf(0)));
            int iValue = d.intValue();
            if (iValue == d.doubleValue()) {
                d = null;
                return iValue;
            }
            d = null;
        } catch (Exception e) {
            // TODO: handle exception
        }
        float value = defaultValue;
        try {
            value = Float.parseFloat(parseString(obj, String
                    .valueOf(defaultValue)));
        } catch (Exception ex) {
            value = defaultValue;
        }
        return value;
    }

    /**
     * 把Object转换成float，如Object==null 返回0
     *
     * @param obj
     * @return
     */
    public static float parseFloat(Object obj) {
        return parseFloat(obj, 0);
    }

    /**
     * 把Object转换成int，如Object==null 返回默认值
     *
     * @param obj
     * @param defaultValue
     * @return
     */
    public static int parseInt(Object obj, int defaultValue) {
        int value = defaultValue;
        try {
            // 转成Double，然后取整数
            Double d = new Double(
                    parseString(obj, String.valueOf(defaultValue)));
            value = d.intValue();
            d = null;
        } catch (Exception ex) {
            value = defaultValue;
        }
        return value;
    }

    /**
     * 把Object转换成int，如Object==null 返回0
     *
     * @param obj
     * @return
     */
    public static int parseInt(Object obj) {
        return parseInt(obj, 0);
    }

    /**
     * 把Object转换成long，如Object==null 返回默认值
     *
     * @param obj
     * @param defaultValue
     * @return
     */
    public static long parseLong(Object obj, int defaultValue) {
        long value = defaultValue;
        try {
            value = Long.parseLong(parseString(obj, String
                    .valueOf(defaultValue)));
        } catch (Exception ex) {
            value = defaultValue;
        }
        return value;
    }

    /**
     * 把Object转换成int，如Object==null 返回0
     *
     * @param obj
     * @return
     */
    public static long parseLong(Object obj) {
        return parseLong(obj, 0);
    }

    /**
     * 把Object转换成double，如Object==null 返回0
     *
     * @param obj
     * @return
     */
    public static double parseDouble(Object obj) {
        return parseDouble(obj, 0);
    }

    /**
     * 判断字符是否为全角英数
     *
     * @param str
     * @return true是、false不是
     */
    public static boolean isFullChar(String str) {
        if (str == null || "".equals(str) || str.length() == 0) {
            return false;
        }
        try {
            if (str.length() != str.getBytes("UTF-8").length) {
                // 通过字节长度判断全半角，半角英数占1byte、全角英数根据编码不同占2/3byte
                return true;
            }
        } catch (UnsupportedEncodingException e) {
            //LogKit.error(e, e);
        }
        //LogKit.error("[ERR_CODE][9001]");
        return false;
    }

    /**
     * 释放空闲内存
     */
    public static void fullGC() {
        /**
         * 正在交易中，和进行转档时，不处理
         */
        fullGCRandom();
    }

    /**
     * 释放空闲内存，计数（每5次执行1次）,出异常后不再继续执行
     */
    public static int FULLGC_CNT = 0;

    /**
     * 释放空闲内存
     */
    public static void fullGCRandom() {
        if (FULLGC_CNT < 0) {
            return;
        }
        try {
            FULLGC_CNT++;
            if (FULLGC_CNT == 3) {
                FULLGC_CNT = 0;
            } else {
                return;
            }
            fullGCNoCheck();
        } catch (Exception e) {
            FULLGC_CNT = 0;
            //LogKit.error("freeMemory Error", e);
        }

    }

    /**
     * 释放空闲内存
     */
    public static void fullGCNoCheck() {
        try {
            if (FULLGC_CNT < 0) {
                return;
            }
            //LogKit.info("fullGCNoCheck>>>>>>>>>>>>>");
            Runtime rt = Runtime.getRuntime();
            long isFree = rt.freeMemory();
            long wasFree;
            do {
                wasFree = isFree;
                rt.runFinalization();
                rt.gc();
                isFree = rt.freeMemory();
            } while (isFree > wasFree);
            FULLGC_CNT = 0;
            //LogKit.info("FREEMEMORY>>>>>>>>>>>>>" + isFree);
        } catch (Exception e) {
            FULLGC_CNT = 0;
            //LogKit.error("freeMemory Error", e);
        }
    }

    /**
     * 按指定格式，格式化当前日期
     *
     * @param formatStr
     * @return
     */
    public static String getDATE(String formatStr) {
        Date date = new Date();
        return new SimpleDateFormat(formatStr).format(date);
    }

    /**
     * 按指定格式，格式化传入的日期
     *
     * @param formatStr
     * @param date
     * @return
     */
    public static String getDATE(String formatStr, Date date) {
        return new SimpleDateFormat(formatStr).format(date);
    }

    /**
     * 取当前的日期返回yyyyMMddHHmmss格式字符串
     *
     * @return
     */
    public static String getSYSDATETIME() {
        Date date = new Date();
        return df_yyyyMMddHHmmss.format(date);
    }

    /**
     * 取当前的日期返回yyyyMMddHHmmssSSS格式字符串
     *
     * @return
     */
    public static String getSYSDATETIMEDETAIL() {
        Date date = new Date();
        return df_yyyyMMddHHmmssSSS.format(date);
    }

    /**
     * 取当前的日期返回yyyy-MM-dd HH:mm:ss格式字符串
     *
     * @return
     */
    public static String getSYSDATETIME2() {
        Date date = new Date();
        return df_yyyy_MM_ddHHmmss.format(date);
    }

    /**
     * 传入yyyyMMddHHmmss格式字符串，返回yyyy-MM-dd HH:mm:ss格式字符串
     *
     * @return
     */
    public static String getSYSDATETIME2(String dateTime) {
        if ("".equals(parseString(dateTime))) {
            return "";
        }
        Date date = null;
        try {
            date = df_yyyyMMddHHmmss.parse(dateTime.replace(" ", "").trim());
            return df_yyyy_MM_ddHHmmss.format(date);
        } catch (ParseException e) {
            //LogKit.error(e);
        }
        return dateTime;
    }

    /**
     * 取当前的日期返回yyyyMMddHHmm格式字符串
     *
     * @return
     */
    public static String getSYSDATETIME3() {
        Date date = new Date();
        return df_yyyyMMddHHmm.format(date);
    }

    /**
     * 取long的日期返回yyyyMMddHHmm格式字符串
     *
     * @param lDate
     * @return
     */
    public static String getSYSDATETIME3(long lDate) {
        Date date = null;
        try {
            date = new Date(lDate);
            return df_yyyyMMddHHmm.format(date);
        } catch (Exception e) {
            //LogKit.error(e);
        }
        return "";
    }

    /**
     * 取long的日期返回yyyy/MM/dd HH:mm格式字符串
     *
     * @param lDate
     * @return
     */
    public static String getSYSDATETIME4(long lDate) {
        Date date = null;
        try {
            date = new Date(lDate);
            return df_yyyyMMddHH.format(date);
        } catch (Exception e) {
            //LogKit.error(e);
        }
        return "";
    }

    /**
     * 传入HHmmss格式字符串，返回HH:mm:ss格式字符串
     *
     * @return
     */
    public static String getSYSTIME2(String dateTime) {
        if (dateTime == null || "".equals(dateTime)) {
            return dateTime;
        }
        Date date = null;
        try {
            date = df_HHmmss.parse(dateTime);
            return df_HH_mm_ss.format(date);
        } catch (ParseException e) {
            //LogKit.error(e);
        }
        return dateTime;
    }

    /**
     * 取当前的时间返回HHmmss格式字符串
     *
     * @return
     */
    public static String getSYSTIME() {
        Date date = new Date();
        return df_HHmmss.format(date);
    }

    /**
     * 取当前的时间返回HHmmssSSS格式字符串
     *
     * @return
     */
    public static String getSYSTIMESSS() {
        Date date = new Date();
        return df_HHmmssSSS.format(date);
    }

    /**
     * 取当前的日期返回yyyyMMdd格式字符串
     *
     * @return
     */
    public static String getSYSDATE() {
        Date date = new Date();
        return df_yyyyMMdd.format(date);
    }

    /**
     * 传入yyyyMMdd格式字符串，返回yyyy-MM-dd格式的字符
     *
     * @return
     */
    public static String getSYSDATE2(String dateString) {
        if ("".equals(parseString(dateString))) {
            return "";
        }
        Date date = null;
        try {
            date = df_yyyyMMdd.parse(dateString);
            return df_yyyy_MM_dd.format(date);
        } catch (ParseException e) {
            //LogKit.error(e);
        }
        return dateString;
    }

    /**
     * 取当前的星期
     *
     * @return
     */
    public static String getSYSWEEK() {
        Calendar c = Calendar.getInstance();
        c.getTime();
        int d = c.get(Calendar.DAY_OF_WEEK) - 1;

        // 因周日返回的是 0, 但我们要的是 7 所以在这里做这个判断
        if (d == 0) {
            d = 7;
        }
        return "" + d;
    }

    /**
     * 取当前的几号
     *
     * @return
     */
    public static String getSYSMONTHDAY() {
        Calendar c = Calendar.getInstance();
        c.getTime();
        int d = c.get(Calendar.DAY_OF_MONTH);
        return "" + d;
    }

    /**
     * 取当前的日期返回yyyy-MM-dd格式字符串
     *
     * @return
     */
    public static String getSYSDATE2() {
        Date date = new Date();
        return df_yyyy_MM_dd.format(date);
    }

    /**
     * 取昨天的日期
     *
     * @return
     */
    public static String getYesterday() {
        // SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
        Calendar c = Calendar.getInstance();
        c.getTime();
        int d = c.get(Calendar.DAY_OF_MONTH);
        --d; // 将“日”减一，即得到前一天
        c.set(Calendar.DAY_OF_MONTH, d);
        return df_yyyyMMdd.format(c.getTime());
    }

    /**
     * 得加月后日期
     */
    public static String getAfterMoth(int AferMoth) {
        Calendar c = Calendar.getInstance();// 获得一个日历的实例
        int d = c.get(Calendar.MONTH);
        d = d + AferMoth;
        c.set(Calendar.MONTH, d);
        return df_yyyyMMdd.format(c.getTime());
    }

    /**
     * 得到几天前的日期
     *
     * @param iBeforeDays
     * @return
     */
    public static String getBeforeDay(int iBeforeDays) {
        Calendar c = Calendar.getInstance();
        int d = c.get(Calendar.DAY_OF_MONTH);
        d = d - iBeforeDays;
        c.set(Calendar.DAY_OF_MONTH, d);
        return df_yyyyMMdd.format(c.getTime());
    }

    /**
     * 获取两个日期之间的所有日期(yyyyMMdd)
     *
     * @param startTime 开始日期
     * @param endTime   结束日期
     * @return
     */
    public static List<String> getDays(String startTime, String endTime) {
        // 返回的日期集合
        List<String> days = new ArrayList<String>();
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        try {
            Date start = dateFormat.parse(startTime);
            Date end = dateFormat.parse(endTime);

            Calendar tempStart = Calendar.getInstance();
            tempStart.setTime(start);

            Calendar tempEnd = Calendar.getInstance();
            tempEnd.setTime(end);
            tempEnd.add(Calendar.DATE, +1);// 日期加1(包含结束)
            while (tempStart.before(tempEnd)) {
                days.add(dateFormat.format(tempStart.getTime()));
                tempStart.add(Calendar.DAY_OF_YEAR, 1);
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return days;
    }

    /**
     * 得到几天前的日期
     *
     * @param SL_DATE     :20120811
     * @param iBeforeDays
     * @return
     */
    public static String getBeforeDay(String SL_DATE, int iBeforeDays) {
        Date cDate = Convert(SL_DATE + "000000");
        Calendar c = Calendar.getInstance();
        c.setTime(cDate);
        int d = c.get(Calendar.DAY_OF_MONTH);
        d = d - iBeforeDays;
        c.set(Calendar.DAY_OF_MONTH, d);
        return df_yyyyMMdd.format(c.getTime());
    }

    /**
     * 得到几天前的日期
     *
     * @param iBeforeDays
     * @return
     */
    public static long getBeforeDayToLong(int iBeforeDays) {
        Calendar c = Calendar.getInstance();
        int d = c.get(Calendar.DAY_OF_MONTH);
        d = d - iBeforeDays;
        c.set(Calendar.DAY_OF_MONTH, d);
        return c.getTimeInMillis();
    }

    /**
     * 得到之前的某一时间
     *
     * @param TIME         :20141111120101
     * @param iBeforeTimes 数
     * @param sType        1(HOUR_OF_DAY 时) 2(MINUTE 分) 3(SECOND 秒)
     * @param sFormat      返回格式 df_HHmmss
     * @return
     */
    public static String getBeforeTime(String TIME, int iBeforeTimes, int sType, SimpleDateFormat sFormat) {
        String sTime = parseString(TIME);
        if (sTime.length() != 14) {
            sTime = getSYSDATETIME();
        }
        Date cDate = Convert(sTime);
        Calendar c = Calendar.getInstance();
        c.setTime(cDate);
        int d;
        switch (sType) {
            case 1:
                d = c.get(Calendar.HOUR_OF_DAY);
                d = d - iBeforeTimes;
                c.set(Calendar.HOUR_OF_DAY, d);
                sTime = sFormat.format(c.getTime());
                break;
            case 2:
                d = c.get(Calendar.MINUTE);
                d = d - iBeforeTimes;
                c.set(Calendar.MINUTE, d);
                sTime = sFormat.format(c.getTime());
                break;
            case 3:
                d = c.get(Calendar.SECOND);
                d = d - iBeforeTimes;
                c.set(Calendar.SECOND, d);
                sTime = sFormat.format(c.getTime());
                break;
        }
        return sTime;
    }

    /***
     * 比较两个日期字符串中 compareDate 是否最小
     *
     * @param compareDate
     * @param sysDate
     * @return
     */
    public static boolean minDate(String compareDate, String sysDate) {
        try {
            Date cDate = Convert(compareDate);
            Date sDate = Convert(sysDate);
            long cTime = cDate.getTime();
            long sTtime = sDate.getTime();

            if (cTime > sTtime)
                return false;
            else
                return true;
        } catch (Exception ex) {
            return false;
        }
    }

    /**
     * 字符串转换成Date类型
     *
     * @param strTime
     * @return
     */
    public static Date Convert(String strTime) {
        // String format = "yyyyMMddHHmmss";
        Date date = null;
        try {
            // SimpleDateFormat tf = new SimpleDateFormat(format);
            date = df_yyyyMMddHHmmss.parse(strTime);
        } catch (Exception e) {

        }
        return date;
    }

    /**
     * 日期合法check
     *
     * @param date(20141117) 需要check的日期
     * @return 日期是否合法
     */
    public static boolean chkDateFormat(String date) {
        try {
            // 如果输入日期不是8位的,判定为false.
            if (null == date || "".equals(date) || !date.matches("[0-9]{8}")) {  //$NON-NLS-2$
                return false;
            }
            int year = Integer.parseInt(date.substring(0, 4));
            int month = Integer.parseInt(date.substring(4, 6)) - 1;
            int day = Integer.parseInt(date.substring(6));
            Calendar calendar = GregorianCalendar.getInstance();
            // 当 Calendar 处于 non-lenient 模式时，如果其日历字段中存在任何不一致性，它都会抛出一个异常。
            calendar.setLenient(false);
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.DATE, day);
            // 如果日期错误,执行该语句,必定抛出异常.
            calendar.get(Calendar.YEAR);
        } catch (IllegalArgumentException e) {
            return false;
        }
        return true;
    }

    /**
     * 将该对象序列化成流,因为写在流里的是对象的一个拷贝，<br>
     * 而原对象仍然存在于JVM里面。所以利用这个特性可以实现对象的深拷贝
     *
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public static Object deepCopy(Object sourceObj) {
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream oos;
            oos = new ObjectOutputStream(bos);
            oos.writeObject(sourceObj);
            // 将流序列化成对象
            ByteArrayInputStream bis = new ByteArrayInputStream(bos
                    .toByteArray());
            ObjectInputStream ois = new ObjectInputStream(bis);
            return ois.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 按照系统参数 <br>
     * iSMALL_CHANGE_PLACE抹零位数：小数位后多少抹成一个虚拟商品的金额<br>
     * 返回double类型的金额(金额是负数)<br>
     * 思路：先无条件舍弃按抹零法则设定小数位得到："整数部分"，然后用总金额减去这个"整数部分"<br>
     *
     * @param obj
     * @param isFlag
     * @return
     */
    public static double getSmallChange(Object obj, boolean isFlag) {
        // iSMALL_CHANGE_PLACE抹零法则：小数位后多少抹成一个虚拟商品
        int iSMALL_CHANGE_PLACE = parseInt(emisKeeper.getInstance()
                .getEmisPropMap().get("SMALL_CHANGE_PLACE"), 1);
        boolean isNeedBreak = false;
        try {
            StringBuffer sbf = new StringBuffer(showAmt(obj));
            String reverseStr = sbf.reverse().toString();
            // 如果小数位＜=金额计算到小数几位,直接返回
            int index = reverseStr.indexOf(".");
            if (index <= iSMALL_CHANGE_PLACE) {
                isNeedBreak = true;
            } else {
                int keepIndex = index - iSMALL_CHANGE_PLACE;
                isNeedBreak = parseInt(reverseStr.substring(
                        keepIndex - 1, keepIndex)) == 0;
            }
        } catch (Exception e) {
            isNeedBreak = true;
        }
        if (isNeedBreak) {
            return 0;
        }
        BigDecimal money = null;
        BigDecimal smallChange = null;
        try {
            money = new BigDecimal(parseString(obj, "0").trim()).abs();
            int iRoundingMode = BigDecimal.ROUND_FLOOR;// 2：无条件舍弃位
            // 设置精度，以及舍入规则
            smallChange = money.setScale(iSMALL_CHANGE_PLACE, iRoundingMode);
            // 减去整数部分
            smallChange = money.subtract(smallChange);
        } catch (Exception ex) {
            //LogKit.error(ex);
        }
        if (smallChange != null) {
            double amt = 0;
            if (isFlag) {
                amt = smallChange.doubleValue() * (-1);
            } else {
                amt = smallChange.doubleValue();
            }
            money = null;
            smallChange = null;
            return parseAmt(amt);
        } else {
            money = null;
            smallChange = null;
            return 0;
        }
    }

    /**
     * 按系统参数DEC_PLACE金额计算到小数几位 <br>
     * 注在显示时使用,里面的obj请用getString
     *
     * @param obj
     * @return
     */
    public static String showAmt(Object obj) {
        int iDEC_PLACE = parseInt(emisKeeper.getInstance()
                .getEmisPropMap().get("DEC_PLACE"));
        String sValue = null;
        double value = parseAmt(obj);
        switch (iDEC_PLACE) {
            case 0:
                sValue = sdf_pos0.format(value);
                break;
            case 1:
                sValue = sdf_pos1.format(value);
                break;
            case 2:
                sValue = sdf_pos2.format(value);
                break;
            case 3:
                sValue = sdf_pos3.format(value);
                break;
            case 4:
                sValue = sdf_pos4.format(value);
                break;
            default:
                sValue = sdf_pos2.format(value);
        }
        if (iDEC_PLACE > 0) {
            try {
                int deciValue = parseInt(sValue.substring(sValue
                        .length()
                        - iDEC_PLACE));
                if (deciValue == 0) {
                    return sValue
                            .substring(0, sValue.length() - iDEC_PLACE - 1);
                }
            } catch (Exception e) {
            }
        }
        return sValue;
    }

    /**
     * 默认显示2位小数，比如显示百分比
     *
     * @param obj
     * @return
     */
    public static String showValue(Object obj) {
        int iDEC_PLACE = 2;
        String sValue = null;
        double value = parseValueDouble(obj);
        sValue = sdf_pos2.format(value);
        try {
            int deciValue = parseInt(sValue.substring(sValue.length()
                    - iDEC_PLACE));
            if (deciValue == 0) {
                return sValue.substring(0, sValue.length() - iDEC_PLACE - 1);
            }
        } catch (Exception e) {
        }
        return sValue;
    }

    /**
     * 按系统参数DEC_PLACE金额计算到小数几位 <br>
     * 注在显示折扣时使用
     *
     * @param obj
     * @return
     */
    public static String showDiscAmt(Object obj) {
        int iDEC_PLACE = parseInt(emisKeeper.getInstance()
                .getEmisPropMap().get("DEC_PLACE"));
        String sValue = null;
        double value = parseDiscAmt(obj);
        switch (iDEC_PLACE) {
            case 0:
                sValue = sdf_pos0.format(value);
                break;
            case 1:
                sValue = sdf_pos1.format(value);
                break;
            case 2:
                sValue = sdf_pos2.format(value);
                break;
            case 3:
                sValue = sdf_pos3.format(value);
                break;
            case 4:
                sValue = sdf_pos4.format(value);
                break;
            default:
                sValue = sdf_pos2.format(value);
        }
        if (iDEC_PLACE > 0) {
            try {
                int deciValue = parseInt(sValue.substring(sValue
                        .length()
                        - iDEC_PLACE));
                if (deciValue == 0) {
                    return sValue
                            .substring(0, sValue.length() - iDEC_PLACE - 1);
                }
            } catch (Exception e) {
            }
        }
        return sValue;
    }

    /**
     * 销售数量按系统参数SL_QTY_PLACE金额计算到小数几位 <br>
     * 注在显示时使用,里面的obj请用getString
     *
     * @param obj
     * @return
     */
    public static String showSlQty(Object obj) {
        // 数量整数时是否显示小数位
        boolean bIS_SL_QTY_SHOW_DEC = parseBoolean(emisKeeper.getInstance().getEmisPropMap()
                .get("IS_SL_QTY_SHOW_DEC"));
        if (!bIS_SL_QTY_SHOW_DEC) {
            // 如果是整数，直接返回
            Double d = null;
            try {
                d = new Double(obj.toString());
                int iValue = d.intValue();
                if (iValue == d.doubleValue()) {
                    return iValue + "";
                }
            } catch (Exception e) {
            } finally {
                d = null;
            }
        }

        int iDEC_PLACE = parseInt(emisKeeper.getInstance()
                .getEmisPropMap().get("SL_QTY_PLACE"));
        String sValue = null;
        double value = parseSlQty(obj);
        switch (iDEC_PLACE) {
            case 0:
                sValue = sdf_pos0.format(value);
                break;
            case 1:
                sValue = sdf_pos1.format(value);
                break;
            case 2:
                sValue = sdf_pos2.format(value);
                break;
            case 3:
                sValue = sdf_pos3.format(value);
                break;
            //2019/04/03 viva modify 需求 #47087 麦香缘——前台要货单数量支持小数点后4位需求
            case 4:
                sValue = sdf_pos4.format(value);
                break;
            default:
                sValue = sdf_pos2.format(value);
        }
        if (iDEC_PLACE > 0 && !bIS_SL_QTY_SHOW_DEC) {
            try {
                int deciValue = parseInt(sValue.substring(sValue
                        .length()
                        - iDEC_PLACE));
                if (deciValue == 0) {
                    return sValue
                            .substring(0, sValue.length() - iDEC_PLACE - 1);
                }
            } catch (Exception e) {
            }
        }
        return sValue;
    }

    public static boolean hadInitRoot = false;

    public static String baseStr = "";

    public static String getRoot() {

        if (!hadInitRoot) {
            File file = new File("emisPath.txt");
            if (!file.exists())
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            baseStr = file.getAbsolutePath().substring(0,
                    file.getAbsolutePath().indexOf("emisPath.txt"));
            System.out.println("project root path:" + baseStr);
            if (baseStr.endsWith("\\")) {
                // 不处理\\
            } else if (!baseStr.endsWith("/"))
                baseStr = baseStr + "/";//  
            hadInitRoot = true;

        }

        return baseStr;

    }

    /**
     * 本方法用于取资料存放主路径 主要包括:上传的资料路径 和 列印文件资料路径
     */
    public static String getFileRoot() {
        String fileRoot = "";
        fileRoot = getRoot() + "data/";
        if (checkDir(fileRoot))
            return fileRoot;
        else
            return "";
    }

    /**
     * 本方法用于取资料存放主路径 主要包括:上传的资料路径 和 列印文件资料路径
     */
    public static String getFileRptRoot() {
        String fileRoot = "";
        fileRoot = "data/sale/";
        if (checkDir(fileRoot))
            return fileRoot;
        else
            return "";
    }

    public static boolean checkDir(String dir) {
        boolean ok = false;
        File dfile = new File(dir);
        if (!dfile.exists())
            if (!dfile.isDirectory()) {
                try {
                    dfile.mkdirs();
                    ok = true;
                } catch (Exception e) {
                    ok = false;
                }
            } else
                ok = true;
        else
            ok = true;
        return ok;
    }

    /**
     * 计算两个时间之间相隔时间 （计算判断）
     *
     * @param timeS   开始时间
     * @param timeE     结束时间
     * @param returnType 0 返回时 1 返回分 2 返回秒
     * @return *
     * <p>
     * 传入时间格式 yyyyMMddHHmmss
     */
    public static long IntervalTime(String timeS, String timeE, int returnType) {
        Date begin;
        Date end;
        try {
            begin = df_yyyyMMddHHmmss.parse(timeS);
            end = df_yyyyMMddHHmmss.parse(timeE);
            long between = (end.getTime() - begin.getTime()) / 1000;// 除以1000是为了转换成秒
            long hour1 = between / 3600;
            long minute1 = between % 3600 / 60;
            if (returnType == 0) {
                return hour1;
            } else if (returnType == 1) {
                return minute1;
            } else if (returnType == 2) {
                return between;
            }
            return between;
        } catch (ParseException e) {
            //LogKit.error(e);
        }
        return 0;
    }

    /**
     * 计算两个时间之间相隔时间
     *
     * @param timeS 开始时间
     * @param timeE   结束时间
     * @return
     */
    public static String getIntervalTime(String timeS, String timeE) {
        return getIntervalTime(timeS, timeE, 3);
    }

    /**
     * 计算两个时间之间相隔时间
     *
     * @param timeS   开始时间
     * @param timeE     结束时间
     * @param returnType 0 返回时 1 返回分 2 返回秒 3 返回 时 分
     * @return *
     * <p>
     * 传入时间格式 yyyyMMddHHmmss
     */
    public static String getIntervalTime(String timeS, String timeE, int returnType) {
        Date begin;
        Date end;
        try {
            begin = df_yyyyMMddHHmmss.parse(timeS);
            end = df_yyyyMMddHHmmss.parse(timeE);
            long between = (end.getTime() - begin.getTime()) / 1000;// 除以1000是为了转换成秒
            long hour1 = between / 3600;
            long minute1 = between % 3600 / 60;
            if (returnType == 0) {
                return hour1 + "";
            } else if (returnType == 1) {
                return minute1 + "";
            } else if (returnType == 2) {
                return between + "";
            } else if (returnType == 3) {
                return (hour1 + "小时" + minute1 + "分");
            }
            return (between + "");
        } catch (ParseException e) {
            //LogKit.error(e);
        }
        return "";
    }

    /**
     * 是否为开发模式
     *
     * @return
     */
    public static boolean isDev() {
        try {
            File file = new File("devVenus");
            if (file.exists()) {
                // 开发模式，不检查加密狗
                return true;
            }
        } catch (Exception e) {
        }
        return false;
    }

    /**
     * 計算两个日期相差的天数
     *
     * @param early 開始日期
     * @param late  結束日期
     * @return
     */
    public static final long daysBetween(String early, String late) {
        SimpleDateFormat df_yyyyMMdd = new SimpleDateFormat("yyyyMMdd");
        Date earlydate = null;
        Date latedate = null;
        long days = 0;
        try {
            earlydate = df_yyyyMMdd.parse(early);
            latedate = df_yyyyMMdd.parse(late);
            Calendar calst = Calendar.getInstance();
            Calendar caled = Calendar.getInstance();
            calst.setTime(earlydate);
            caled.setTime(latedate);
            // 设置时间为0时
            calst.set(Calendar.HOUR_OF_DAY, 0);
            calst.set(Calendar.MINUTE, 0);
            calst.set(Calendar.SECOND, 0);
            caled.set(Calendar.HOUR_OF_DAY, 0);
            caled.set(Calendar.MINUTE, 0);
            caled.set(Calendar.SECOND, 0);
            // 得到两个日期相差的天数
            days = ((long) (caled.getTime().getTime() / 1000) - (long) (calst
                    .getTime().getTime() / 1000)) / 3600 / 24;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return days;
    }

    /**
     * 获取脱网许可天数
     *
     * @return
     */
    public static int getOffLineLimit() {
        return parseInt(emisPropUtil
                .getEmisProp("OFF_LINE_LIMIT", "7"));  //$NON-NLS-2$
    }

    /**
     * 可以利用java.util.prefs.Preferences来对注册表进行操作<br>
     * 保存的参数<br>
     * 你想存入对象的话,可以把对象序列化成byte[],然后再存进来,<br>
     * 然后读也是一样的,我们这样操作就像是在操作一个Map一个,<br>
     * 所有的值都是存在这个映射里面. 可以放String,boolean,int,long,float,double等值<br>
     * <p>
     * 如果选的是systemNode...<br>
     * 则保存在[HKEY_LOCAL_MACHINE\SOFTWARE\JavaSoft\Prefs]<br>
     * <p>
     * 如果选的是userNode.... <br>
     * 则保存在[HKEY_CURRENT_USER\Software\JavaSoft\Prefs] 的子节点下面<br>
     * <p>
     * linux下保存在home目录下的一个文件里<br>
     *
     * @param key
     * @param value
     * @return
     */
    public static void setRegValue(String key, String value) {
        Preferences pre = Preferences.systemNodeForPackage(emisUtils.class);
        try {
            pre.put(key, value);
            pre.flush();
        } catch (BackingStoreException e) {
            //LogKit.error(e);
        } finally {
        }
    }

    /**
     * 可以利用java.util.prefs.Preferences来对注册表进行操作<br>
     * 取得保存的参数
     *
     * @param key
     * @return
     */
    public static String getRegValue(String key) {
        Preferences now = Preferences.systemNodeForPackage(emisUtils.class);
        return now.get(key, "");
    }

    /**
     * 检查是否正在运行
     * <p>
     * <pre>
     * 参数列表:
     *    /S     system           指定连接到的远程系统。
     *    /U     [domain\]user    指定应该在哪个用户上下文执行这个命令。
     *    /P     [password]       为提供的用户上下文指定密码。如果省略，则提示输入。
     *    /M     [module]         列出当前使用所给 exe/dll 名称的所有任务。
     *                            如果没有指定模块名称，显示所有加载的模块。
     *    /SVC                    显示每个进程中主持的服务。
     *    /V                      显示详述任务信息。
     *    /FI    filter           显示一系列符合筛选器指定的标准的任务。
     *    /FO    format           指定输出格式。
     *                            有效值: "TABLE"、"LIST"、"CSV"。
     * 筛选器:
     *     筛选器名        有效操作符                有效值
     *     -----------     ---------------           --------------------------
     *     STATUS          eq, ne                    RUNNING | NOT RESPONDING | UNKNOWN
     *     IMAGENAME       eq, ne                    映像名称
     *     PID             eq, ne, gt, lt, ge, le    PID 值
     *     SESSION         eq, ne, gt, lt, ge, le    会话编号
     *
     * 示例:
     *    TASKLIST /FI "IMAGENAME eq eclipse.exe"
     *    TASKLIST /FI "IMAGENAME eq eclipse.exe" /FI "STATUS eq running"
     * </pre>
     *
     * @param taskListName
     * @return
     */
    public static boolean checkTaskList(String taskListName) {
        BufferedReader br;
        String msg;
        try {
            if (taskListName == null || "".equals(taskListName))
                taskListName = "Venus.exe";
            // 只过滤[映像名称]匹配的进程，减少循环次数
            Process pro = Runtime.getRuntime().exec("cmd /c TASKLIST /FI \"IMAGENAME eq " + taskListName + "\"");
            br = new BufferedReader(new InputStreamReader(pro.getInputStream()));
            msg = null;
            // 修正大小导致判断错误
            taskListName = taskListName.toLowerCase();
            while ((msg = br.readLine()) != null) {
                if (1==2) {
                    //LogKit.info(taskListName + ">>" + msg);
                }
                if (msg.toLowerCase().startsWith(taskListName)) {
                    return true;
                }
            }
        } catch (Exception e) {
            //LogKit.error(e, e);
            return false;
        }
        return false;
    }

    /**
     * 检查是否正在运行,并且统计次数
     *
     * @param taskListName
     * @return
     */
    public static int checkTaskListCNT(String taskListName) {
        BufferedReader br;
        String msg;
        int iCNT = 0;
        try {

            Process pro = Runtime.getRuntime().exec("cmd /c TASKLIST");
            br = new BufferedReader(new InputStreamReader(pro.getInputStream()));
            msg = null;
            if (taskListName != null && !"".equals(taskListName)) {
                taskListName = taskListName.toLowerCase();
                while ((msg = br.readLine()) != null)
                    if (msg.toLowerCase().startsWith(taskListName)) {
                        iCNT++;
                    }
            }
        } catch (Exception e) {
            return iCNT;
        }
        return iCNT;
    }

    /**
     * 检查台湾统一编号
     *
     * @param sNo_
     * @return
     */
    public static boolean validateUID4Taiwan(String sNo_) {
        sNo_ = parseString(sNo_);
        if ("".equals(sNo_))
            return false;
        if (!sNo_.matches("\\d{8}"))
            return false;
        boolean isOK = false;
        long wk1, wk2, wk3, wk4, wk5, wk6, wk7, wk8, wk9, wk10, wk_total, wk_check;
        wk1 = parseInt(sNo_.substring(0, 1))
                + parseInt(sNo_.substring(2, 3))
                + parseInt(sNo_.substring(4, 5))
                + parseInt(sNo_.substring(7));

        int i2 = parseInt(sNo_.substring(1, 2));
        int i4 = parseInt(sNo_.substring(3, 4));
        int i6 = parseInt(sNo_.substring(5, 6));
        int i7 = parseInt(sNo_.substring(6, 7));

        wk2 = i2 * 2 / 10;
        wk3 = i4 * 2 / 10;
        wk4 = i6 * 2 / 10;
        wk5 = i7 * 4 / 10;

        wk6 = i2 * 2 % 10;
        wk7 = i4 * 2 % 10;
        wk8 = i6 * 2 % 10;
        wk9 = i7 * 4 % 10;

        wk10 = (wk5 + wk9) / 10;
        wk_total = wk1 + wk2 + wk3 + wk4 + wk5 + wk6 + wk7 + wk8 + wk9;
        wk_check = wk_total % 10;
        if (wk_check == 0) {
            isOK = true;
        } else if (i7 == 7) {
            wk_total = wk1 + wk2 + wk3 + wk4 + wk6 + wk7 + wk8 + wk10;
            wk_check = wk_total % 10;
            isOK = (wk_check == 0);
        }
        return isOK;
    }

    /**
     * 检测程序。
     *
     * @param processName 线程的名字，请使用准确的名字
     * @return 找到返回true, 没找到返回false
     */
    public static boolean findProcess(String processName) {
        BufferedReader bufferedReader = null;
        try {
            Process proc = Runtime.getRuntime().exec(
                    "tasklist /FI \"IMAGENAME eq " + processName + "\"");  //$NON-NLS-2$
            bufferedReader = new BufferedReader(new InputStreamReader(proc
                    .getInputStream()));
            String line = null;
            while ((line = bufferedReader.readLine()) != null) {
                if (line.contains(processName)) {
                    return true;
                }
            }
            return false;
        } catch (Exception ex) {
            //LogKit.error(ex);
            return false;
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (Exception ex) {
                }
            }
        }
    }

    /**
     * 检查网络状态
     *
     * @param httpUrl
     * @return
     */
    public static boolean checkWebState(String httpUrl) {
        if ("".equals(parseString(httpUrl))) {
            return false;
        }
        Socket sock = null;
        try {
            sock = new Socket(httpUrl, 80);
            // 超时检查，不能为空，否则长时间画面没有提示正确断线后再连接正常的信息
            sock.setSoTimeout(15000);
        } catch (Exception e) {
            return false;
        } finally {
            if (sock != null) {
                try {
                    sock.close();
                } catch (IOException e) {
                }
            }
            sock = null;
        }
        return true;
    }

    /**
     * 开启后台网页
     *
     * @param str
     */
    public static void openWeb(String str, boolean isFindDb) {
        try {
            HashMap emisPropMap = emisKeeper.getInstance().getEmisPropMap();
            String WIN_PATH = "rundll32";
            String WIN_FLAG = "url.dll,FileProtocolHandler";
            // "http://117.135.140.121:8083/smepos_cn/login.jsp?ID=root&PASSWD=turbo&S_NO=020001&KEYS=5P"
            String url = parseString(emisPropMap.get("SME_URL") + "/");// 后台地址  //$NON-NLS-2$
            String userName = parseString(emisPropMap
                    .get("WEB_USERNAME"));// 后台用户名 
            String userPsw = parseString(emisPropMap
                    .get("WEB_PASSWORD"));// 后台密码 
            String key = str;
            if (isFindDb) {
                key = parseString(emisPropMap.get("WEB_BTN_" + str
                        + "_KEY"));// Key 
            }
            String S_NO = parseString(emisPropMap.get("S_NO"));// 门市编号 

            if ("ROOT".equalsIgnoreCase(userName)) {
                url += "login.jsp?ID=" + userName + "&PASSWD=" + userPsw  //$NON-NLS-2$
                        + "&KEYS=" + key;
            } else {
                url += "login.jsp?ID=" + userName + "&PASSWD=" + userPsw  //$NON-NLS-2$
                        + "&S_NO=" + S_NO + "&KEYS=" + key;  //$NON-NLS-2$
            }
            String cmd = WIN_PATH + " " + WIN_FLAG + " " + url;  //$NON-NLS-2$
            Runtime.getRuntime().exec(cmd);
            //LogKit.info("===OPEN WEB:" + url);
            // 先暂停3秒
            Thread.sleep(3000);
        } catch (Exception e) {
            //LogKit.error("open web error:" + e);
        }
    }

    /**
     * 开启外部链接
     *
     * @param url
     */
    public static void openWeb(String url) {
        try {
            if (url == null || "".equals(url)) {
                return;
            }
            //地址包含encode=1才加密
            if (url.indexOf("encode=1") > 0) {
                url = base64String(url);
            }
            String WIN_PATH = "rundll32";
            String WIN_FLAG = "url.dll,FileProtocolHandler";
            String cmd = WIN_PATH + " " + WIN_FLAG + " " + url;  //$NON-NLS-2$
            Runtime.getRuntime().exec(cmd);
            //LogKit.info("===OPEN WEB:" + url);
            // 先暂停3秒
            Thread.sleep(3000);
        } catch (Exception e) {
            //LogKit.error("open web error:" + e);
        }
    }

    /**
     * 加密处理地址
     *
     * @param url
     * @return
     */
    public static String base64String(String url) {
        String newUrl = url.substring(0, url.indexOf("?"));
        String oldParam = url.substring(url.indexOf("?") + 1);
        //加密参数
        String params = "";
        try {
            params = new BASE64Encoder().encode(oldParam.getBytes("UTF-8"));
            //再用URLEncoder编码一次，因为后台request会自动url的参数进行解码
            params = URLEncoder.encode(params, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            //LogKit.error("encode error:" + e);
        }
        //加密后新参数
        String newParams = "encode=1&data=" + params;
        url = newUrl + "?" + newParams;
        return url;
    }

    /**
     * 网络异常标记，如果超过10次，建议重启一下。
     */
    public static int iSMEOnLineErrorCNT = 0;

    /**
     * 错误提示的次数
     */
    public static int iSMEOnLineErrorMsgCnt = 0;

    /**
     * 从后台接口中(check_alive.jsp)取得服务器时间yyyy-MM-dd HH:mm:ss
     * 注意IOUtils.toString好像只能调用一次
     *
     * @param _sContentnt 如果前面有处理IOUtils.toString，请传入_sContentnt
     * @return
     */
    private static Date getSerCheckAliveDate(String _sContentnt) {
        try {
            if (_sContentnt == null) {
                return null;
            }
            if (isJSON(_sContentnt)) {
                JSONObject _oJsonObject = parseJSON(_sContentnt);
                String _sTime = parseString(_oJsonObject
                        .getString("SRV_TIME"));
                if (!"".equals(_sTime)) {
                    //LogKit.info("==获取到的服务器时间：" + _sTime);
                    // 保存服务器时间
                    Date serDate = null;
                    try {
                        serDate = df_yyyy_MM_ddHHmmss.parse(_sTime.trim());
                        return serDate;
                    } catch (ParseException e) {
                        //LogKit.error(e);
                    }
                }
            }
        } catch (Exception e) {
            //LogKit.error(e);
        }
        return null;
    }

    /**
     * HTTP 协议 Header的取时间 yyyy-MM-dd HH:mm:ss
     *
     * @param responseMap
     * @throws ParseException
     */
    private static Date getHTTPHeaderDate(Map<String, Object> responseMap) {
        try {

            // 获取后台时间
            String strSerDate = parseString(responseMap.get("Date"));
            //LogKit.info("==获取到的服务器时间：" + strSerDate);
            if (strSerDate != null && !"".equals(strSerDate)) {
                DateFormat sdf;
                // 格式化服务器时间
                if (strSerDate.length() == strSerDate.getBytes().length) {
                    //LogKit.info("==获取到的服务器时间是英文格式！");
                    // 没有中文的格式
                    sdf = new SimpleDateFormat(
                            "EEE, dd MMM yyyy HH:mm:ss z", Locale.ENGLISH);
                } else {
                    //LogKit.info("==获取到的服务器时间是中文格式！");
                    // 有中文的格式
                    sdf = new SimpleDateFormat(
                            "EEE, dd MMM yyyy HH:mm:ss z", Locale.CHINA);
                }
                // 设置本地时间的时区
                sdf.setTimeZone(TimeZone.getDefault());
                Date serDate = sdf.parse(strSerDate);
                return serDate;
            }
        } catch (Exception e) {
            //LogKit.error(e);
        }
        return null;
    }

    /**
     * 检查IP是否通
     *
     * @param ip
     * @return
     */
    public static boolean checkIPOnline(String ip) {
        if ("".equals(parseString(ip))) {
            return false;
        }
        try {
            InetAddress address = InetAddress.getByName(ip);
            return address.isReachable(3000);
        } catch (Exception e) {
            return false;
        }
    }

    public static Double emisRound(double iValue, int iDecimal) {
        double _iReturn = iValue * Math.pow(10, iDecimal);
        _iReturn = Math.round(_iReturn) / Math.pow(10, iDecimal);
        return _iReturn;
    }

    /**
     * 把Object转换成double，如Object==null 返回默认值
     *
     * @param obj
     * @param defaultValue
     * @return
     */
    public static double parseDouble(Object obj, double defaultValue) {
        // 如果是整数，直接返回
        try {
            Double d = new Double(obj.toString());
            int iValue = d.intValue();
            if (iValue == d.doubleValue()) {
                d = null;
                return iValue;
            }
            d = null;
        } catch (Exception e) {
        }
        double value = parseDefaultDouble(obj, defaultValue);
        // 多做一次保留2位小数
        value = parseSpeDouble(value);
        return value;
    }

    /**
     * 把Object转换成double,只取整数
     *
     * @param obj
     * @param obj
     * @return
     */
    public static int parseDoubleToInt(Object obj) {
        // 如果是整数，直接返回
        try {
            Double d = new Double(obj.toString());
            return d.intValue();
        } catch (Exception e) {
        }
        return 0;
    }

    /**
     * 默认的转double类型的方法
     *
     * @param obj
     * @param defaultValue
     * @return
     */
    public static double parseDefaultDouble(Object obj, double defaultValue) {
        // 如果Obj是NaN, 直接返回0
        if ("NaN".equals(parseString(obj))) {
            return 0;
        }
        double value = defaultValue;
        try {
            value = Double.parseDouble(parseString(obj, String
                    .valueOf(defaultValue)));
        } catch (Exception ex) {
            value = defaultValue;
        }
        return value;
    }

    /**
     * 特殊处理double值
     *
     * @param obj
     * @return
     */
    public static double parseSpeDouble(Object obj) {
        // 如果是整数，直接返回
        try {
            Double d = new Double(obj.toString());
            int iValue = d.intValue();
            if (iValue == d.doubleValue()) {
                d = null;
                return iValue;
            }
            d = null;
        } catch (Exception e) {
        }
        // DEC_PLACE金额计算到小数几位
        int iQTY_PLACE = parseInt(emisKeeper.getInstance()
                .getEmisPropMap().get("QTY_PLACE"), 0);
        int iDEC_PLACE = parseInt(emisKeeper.getInstance()
                .getEmisPropMap().get("DEC_PLACE"), 2);
        int iSL_QTY_PLACE = parseInt(emisKeeper.getInstance()
                .getEmisPropMap().get("SL_QTY_PLACE"), 0);
        iDEC_PLACE = Math.max(iQTY_PLACE, iDEC_PLACE);
        iDEC_PLACE = Math.max(iSL_QTY_PLACE, iDEC_PLACE);
        iDEC_PLACE = Math.max(2, iDEC_PLACE);// 必须改成小数位最大的否则，在entity里的getDouble会把一些小数切断
        try {
            StringBuffer sbf = new StringBuffer(parseString(obj, "0")
                    .trim());
            String reverseStr = sbf.reverse().toString();
            // 如果小数位＜=金额计算到小数几位,直接返回
            int curPlace = reverseStr.indexOf(".");
            if (curPlace <= iDEC_PLACE) {
                return parseDefaultDouble(obj, 0);
            }
            // 0为保留整数
            if (iDEC_PLACE == 0) {
                // 看小数位是否为零，如果是，直接返回
                if (parseInt(reverseStr.substring(0, curPlace)) == 0) {
                    return parseDefaultDouble(obj, 0);
                }
            }
        } catch (Exception e) {
            return parseDefaultDouble(obj, 0);
        }
        BigDecimal money = null;
        try {
            money = new BigDecimal(parseString(obj, "0").trim());
            // AMOUNT_RULE进位法则：0：四舍五入 1：无条件进位 2：无条件舍弃位
            int iRoundingMode = BigDecimal.ROUND_HALF_UP;// 四舍五入 必须选这个
            // 设置精度，以及舍入规则
            money = money.setScale(iDEC_PLACE, iRoundingMode);
        } catch (Exception ex) {
            //LogKit.error(ex);
        }
        if (money != null) {
            double amt = money.doubleValue();
            money = null;
            return amt;
        } else {
            return parseDefaultDouble(obj, 0);
        }

    }

    /**
     * 保留4位的处理double值
     *
     * @param obj
     * @return
     */
    public static double parseValueDouble(Object obj) {
        int iDEC_PLACE = 4;
        try {
            StringBuffer sbf = new StringBuffer(parseString(obj, "0")
                    .trim());
            String reverseStr = sbf.reverse().toString();
            // 如果小数位＜=金额计算到小数几位,直接返回
            int curPlace = reverseStr.indexOf(".");
            if (curPlace <= iDEC_PLACE) {
                return parseDefaultDouble(obj, 0);
            }
            // 0为保留整数
            if (iDEC_PLACE == 0) {
                // 看小数位是否为零，如果是，直接返回
                if (parseInt(reverseStr.substring(0, curPlace)) == 0) {
                    return parseDefaultDouble(obj, 0);
                }
            }
        } catch (Exception e) {
            return parseDefaultDouble(obj, 0);
        }
        BigDecimal money = null;
        try {
            money = new BigDecimal(parseString(obj, "0").trim());
            // AMOUNT_RULE进位法则：0：四舍五入 1：无条件进位 2：无条件舍弃位
            int iRoundingMode = BigDecimal.ROUND_HALF_UP;// 四舍五入 必须选这个
            // 设置精度，以及舍入规则
            money = money.setScale(iDEC_PLACE, iRoundingMode);
        } catch (Exception ex) {
            //LogKit.error(ex);
        }
        if (money != null) {
            double amt = money.doubleValue();
            money = null;
            return amt;
        } else {
            return parseDefaultDouble(obj, 0);
        }

    }

    /**
     * 保留几位小数的处理double值
     *
     * @param obj
     * @param iDEC_PLACE 保留几位小数
     * @return
     */
    public static double parseToDouble(Object obj, int iDEC_PLACE) {
        try {
            StringBuffer sbf = new StringBuffer(parseString(obj, "0")
                    .trim());
            String reverseStr = sbf.reverse().toString();
            // 如果小数位＜=金额计算到小数几位,直接返回
            int curPlace = reverseStr.indexOf(".");
            if (curPlace <= iDEC_PLACE) {
                return parseDefaultDouble(obj, 0);
            }
            // 0为保留整数
            if (iDEC_PLACE == 0) {
                // 看小数位是否为零，如果是，直接返回
                if (parseInt(reverseStr.substring(0, curPlace)) == 0) {
                    return parseDefaultDouble(obj, 0);
                }
            }
        } catch (Exception e) {
            return parseDefaultDouble(obj, 0);
        }
        BigDecimal money = null;
        try {
            money = new BigDecimal(parseString(obj, "0").trim());
            // AMOUNT_RULE进位法则：0：四舍五入 1：无条件进位 2：无条件舍弃位
            int iRoundingMode = BigDecimal.ROUND_HALF_UP;// 四舍五入 必须选这个
            // 设置精度，以及舍入规则
            money = money.setScale(iDEC_PLACE, iRoundingMode);
        } catch (Exception ex) {
            //LogKit.error(ex);
        }
        if (money != null) {
            double amt = money.doubleValue();
            money = null;
            return amt;
        } else {
            return parseDefaultDouble(obj, 0);
        }

    }

    /**
     * 按照系统参数 <br>
     * DEC_PLACE金额计算到小数几位 <br>
     * AMOUNT_RULE进位法则：0：四舍五入 1：无条件进位 2：无条件舍弃位 <br>
     * 返回double类型的金额<br>
     * 注在计算时使用,在计算的最后一步使用
     *
     * @param obj
     * @return
     */
    public static double parseAmt(Object obj) {
        // DEC_PLACE金额计算到小数几位
        int iDEC_PLACE = parseInt(emisKeeper.getInstance()
                .getEmisPropMap().get("DEC_PLACE"), 2);
        try {
            StringBuffer sbf = new StringBuffer(parseString(obj, "0")
                    .trim());
            String reverseStr = sbf.reverse().toString();
            // 如果小数位＜=金额计算到小数几位,直接返回
            if (reverseStr.indexOf(".") <= iDEC_PLACE) {
                return parseDefaultDouble(obj, 0);
            }
        } catch (Exception e) {
            return parseDefaultDouble(obj, 0);
        }
        BigDecimal money = null;
        try {
            money = new BigDecimal(parseString(obj, "0").trim());
            int iRoundingMode = BigDecimal.ROUND_HALF_UP;// 四舍五入 必须选这个
            // 设置精度，以及舍入规则
            money = money.setScale(iDEC_PLACE, iRoundingMode);
        } catch (Exception ex) {
            //LogKit.error(ex);
        }
        if (money != null) {
            double amt = money.doubleValue();
            money = null;
            return amt;
        } else {
            return parseDefaultDouble(obj, 0);
        }

    }

    /**
     * 结账最终的小数位 按照系统参数 <br>
     * SMALL_CHANGE_PLACE金额计算到小数几位 <br>
     * 返回double类型的金额<br>
     *
     * @param obj
     * @return
     */
    public static double parseReckonAmt(Object obj) {
        // SMALL_CHANGE_PLACE金额计算到小数几位
        int iDEC_PLACE = parseInt(emisKeeper.getInstance()
                .getEmisPropMap().get("SMALL_CHANGE_PLACE"), 2);
        try {
            StringBuffer sbf = new StringBuffer(parseString(obj, "0")
                    .trim());
            String reverseStr = sbf.reverse().toString();
            // 如果小数位＜=金额计算到小数几位,直接返回
            if (reverseStr.indexOf(".") <= iDEC_PLACE) {
                return parseDefaultDouble(obj, 0);
            }
        } catch (Exception e) {
            return parseDefaultDouble(obj, 0);
        }
        BigDecimal money = null;
        try {
            money = new BigDecimal(parseString(obj, "0").trim());
            int iRoundingMode = BigDecimal.ROUND_DOWN;// ROUND_DOWN 直接删除多余的小数位
            // 设置精度，以及舍入规则
            money = money.setScale(iDEC_PLACE, iRoundingMode);
        } catch (Exception ex) {
            //LogKit.error(ex);
        }
        if (money != null) {
            double amt = money.doubleValue();
            money = null;
            return amt;
        } else {
            return parseDefaultDouble(obj, 0);
        }

    }

    /**
     * 按照系统参数 <br>
     * SL_QTY_PLACE金额计算到小数几位 <br>
     * AMOUNT_RULE进位法则：0：四舍五入 1：无条件进位 2：无条件舍弃位 <br>
     * 返回double类型的数量<br>
     * 注在计算时使用,在计算的最后一步使用
     *
     * @param obj
     * @return
     */
    public static double parseSlQty(Object obj) {
        // 如果是整数，直接返回
        try {
            Double d = new Double(obj.toString());
            int iValue = d.intValue();
            if (iValue == d.doubleValue()) {
                d = null;
                return iValue;
            }
            d = null;
        } catch (Exception e) {
        }
        // DEC_PLACE金额计算到小数几位
        int iDEC_PLACE = parseInt(emisKeeper.getInstance()
                .getEmisPropMap().get("SL_QTY_PLACE"), 0);
        try {
            StringBuffer sbf = new StringBuffer(parseString(obj, "0")
                    .trim());
            String reverseStr = sbf.reverse().toString();
            // 如果小数位＜=金额计算到小数几位,直接返回
            if (reverseStr.indexOf(".") <= iDEC_PLACE) {
                return parseDefaultDouble(obj, 0);
            }
        } catch (Exception e) {
            return parseDefaultDouble(obj, 0);
        }
        BigDecimal money = null;
        try {
            money = new BigDecimal(parseString(obj, "0").trim());
            int iRoundingMode = BigDecimal.ROUND_HALF_UP;// 四舍五入 必须选这个
            // 设置精度，以及舍入规则
            money = money.setScale(iDEC_PLACE, iRoundingMode);
        } catch (Exception ex) {
            //LogKit.error(ex);
        }
        if (money != null) {
            double amt = money.doubleValue();
            money = null;
            return amt;
        } else {
            return parseDefaultDouble(obj, 0);
        }

    }

    /**
     * 折扣计算 按照系统参数 <br>
     * DEC_PLACE金额计算到小数几位 <br>
     * AMOUNT_RULE进位法则：0：四舍五入 1：五舍六入 2：无条件进位 3：无条件舍弃位<br>
     * 返回double类型的金额<br>
     * 注在计算折扣时使用,在计算的最后一步使用
     *
     * @param obj
     * @return
     */
    public static double parseDiscAmt(Object obj) {
        // DEC_PLACE金额计算到小数几位
        int iDEC_PLACE = parseInt(emisKeeper.getInstance()
                .getEmisPropMap().get("DEC_PLACE"), 2);
        int index = -1;
        String str = parseString(obj, "0").trim();
        try {
            StringBuffer sbf = new StringBuffer(str);
            String reverseStr = sbf.reverse().toString();
            index = reverseStr.indexOf(".");
            // 如果小数位＜=金额计算到小数几位,直接返回
            if (index <= iDEC_PLACE) {
                return parseDefaultDouble(obj, 0);
            }
            index = str.indexOf(".");
        } catch (Exception e) {
            return parseDefaultDouble(obj, 0);
        }
        // AMOUNT_RULE折扣进位法则：0：四舍五入 1：五舍六入 2：无条件进位 3：无条件舍弃位
        int iAMOUNT_RULE = parseInt(emisKeeper.getInstance()
                .getEmisPropMap().get("AMOUNT_RULE"), 0);
        BigDecimal money = null;
        try {
            money = new BigDecimal(parseString(obj, "0").trim());
            int iRoundingMode = BigDecimal.ROUND_HALF_UP;// 四舍五入
            switch (iAMOUNT_RULE) {
                case 1:
                    iRoundingMode = BigDecimal.ROUND_HALF_DOWN;// 1：五舍六入
                    break;
                case 2:
                    iRoundingMode = BigDecimal.ROUND_UP;// 2：无条件进位
                    break;
                case 3:
                    iRoundingMode = BigDecimal.ROUND_DOWN;// 3：无条件舍弃位
                    break;
            }
            // 设置精度，以及舍入规则
            money = money.setScale(iDEC_PLACE, iRoundingMode);
        } catch (Exception ex) {
            //LogKit.error(ex);
        }
        if (money != null) {
            double amt = money.doubleValue();
            money = null;
            return amt;
        } else {
            return parseDefaultDouble(obj, 0);
        }
    }

    /**
     * 折扣计算抹零 按照系统参数 <br>
     * SMALL_CHANGE_PLACE金额计算到小数几位 <br>
     * AMOUNT_RULE进位法则：0：四舍五入 1：五舍六入 2：无条件进位 3：无条件舍弃位<br>
     * 返回double类型的金额<br>
     * 注在计算折扣时使用,在计算的最后一步使用
     *
     * @param obj
     * @return
     */
    public static double parseDiscSmallAmt(Object obj) {
        // SMALL_CHANGE_PLACE金额计算到小数几位
        int iDEC_PLACE = parseInt(
                emisKeeper.getInstance().getEmisPropMap().get("SMALL_CHANGE_PLACE"), 2);
        int index = -1;
        String str = parseString(obj, "0").trim();
        try {
            StringBuffer sbf = new StringBuffer(str);
            String reverseStr = sbf.reverse().toString();
            index = reverseStr.indexOf(".");
            // 如果小数位＜=金额计算到小数几位,直接返回
            if (index <= iDEC_PLACE) {
                return parseDefaultDouble(obj, 0);
            }
            index = str.indexOf(".");
        } catch (Exception e) {
            return parseDefaultDouble(obj, 0);
        }
        // AMOUNT_RULE折扣进位法则：0：四舍五入 1：五舍六入 2：无条件进位 3：无条件舍弃位
        int iAMOUNT_RULE = parseInt(emisKeeper.getInstance()
                .getEmisPropMap().get("AMOUNT_RULE"), 0);
        BigDecimal money = null;
        try {
            money = new BigDecimal(parseString(obj, "0").trim());
            int iRoundingMode = BigDecimal.ROUND_HALF_UP;// 四舍五入
            switch (iAMOUNT_RULE) {
                case 1:
                    iRoundingMode = BigDecimal.ROUND_HALF_DOWN;// 1：五舍六入
                    break;
                case 2:
                    iRoundingMode = BigDecimal.ROUND_UP;// 2：无条件进位
                    break;
                case 3:
                    iRoundingMode = BigDecimal.ROUND_DOWN;// 3：无条件舍弃位
                    break;
            }
            // 设置精度，以及舍入规则
            money = money.setScale(iDEC_PLACE, iRoundingMode);
        } catch (Exception ex) {
            //LogKit.error(ex);
        }
        if (money != null) {
            double amt = money.doubleValue();
            money = null;
            return amt;
        } else {
            return parseDefaultDouble(obj, 0);
        }
    }

    /**
     * 按照系统参数 <br>
     * QTY_PLACE单据数量计算到小数几位 <br>
     * AMOUNT_RULE进位法则：0：四舍五入 <br>
     * 返回double类型的数量<br>
     *
     * @param obj
     * @return
     */
    public static double parseQty(Object obj) {
        // 如果是整数，直接返回
        try {
            Double d = new Double(obj.toString());
            int iValue = d.intValue();
            if (iValue == d.doubleValue()) {
                d = null;
                return iValue;
            }
            d = null;
        } catch (Exception e) {
        }
        // DEC_PLACE金额计算到小数几位
        int iDEC_PLACE = parseInt(emisKeeper.getInstance()
                .getEmisPropMap().get("QTY_PLACE"), 0);
        try {
            StringBuffer sbf = new StringBuffer(parseString(obj, "0")
                    .trim());
            String reverseStr = sbf.reverse().toString();
            // 如果小数位＜=金额计算到小数几位,直接返回
            int curPlace = reverseStr.indexOf(".");
            if (curPlace <= iDEC_PLACE) {
                return parseDefaultDouble(obj, 0);
            }
            // 0为保留整数
            if (iDEC_PLACE == 0) {
                // 看小数位是否为零，如果是，直接返回
                if (parseInt(reverseStr.substring(0, curPlace)) == 0) {
                    return parseDefaultDouble(obj, 0);
                }
            }
        } catch (Exception e) {
            return parseDefaultDouble(obj, 0);
        }
        BigDecimal money = null;
        try {
            money = new BigDecimal(parseString(obj, "0").trim());
            int iRoundingMode = BigDecimal.ROUND_HALF_UP;// 四舍五入 必须选这个
            // 设置精度，以及舍入规则
            money = money.setScale(iDEC_PLACE, iRoundingMode);
        } catch (Exception ex) {
            //LogKit.error(ex);
        }
        if (money != null) {
            double amt = money.doubleValue();
            money = null;
            return amt;
        } else {
            return parseDefaultDouble(obj, 0);
        }

    }

    /**
     * 把obj 元角分类型的字串转成double类型的字串 如100转成1.00，以分为单位
     *
     * @param obj
     * @param defaultValue
     * @return
     */
    public static double parseObjectDouble(Object obj, double defaultValue) {
        // 如果Obj是NaN, 直接返回0
        if ("NaN".equals(parseString(obj))) {
            return 0;
        }
        double value = defaultValue;
        try {
            StringBuffer sbf = new StringBuffer(parseString(obj, String.valueOf(defaultValue)));
            if (sbf.toString().indexOf(".") < 0) {
                if (sbf.length() == 1) {
                    sbf.insert(0, "0.0");
                } else if (sbf.length() == 2) {
                    sbf.insert(0, "0.");
                } else {
                    sbf.insert(sbf.length() - 2, ".");
                }
            }
            value = Double.parseDouble(sbf.toString());
        } catch (Exception ex) {
            value = defaultValue;
        }
        return value;
    }

    /***
     * 把金额转成元角分类型的字串，以分为单位 如100.12转成10012
     *
     * @param amt
     * @return
     */
    public static String parseDoubleToString(double amt) {
        if (amt == 0) {
            return "0";
        }
        DecimalFormat df = new DecimalFormat("#0.000");
        StringBuffer sbf = new StringBuffer();
        String sAmt = parseString(df.format(Math.abs(amt)), "0").trim();
        int iNum = -1; // 统计金额小数位
        for (int i = 0; i < sAmt.length(); i++) {
            if (iNum == 2) {
                // 2位小数后的值舍掉
                break;
            }
            if ('.' == sAmt.charAt(i)) {
                iNum++;
            } else {
                if (iNum > -1) {
                    iNum++;
                }
                if (sbf.length() <= 0 && '0' == sAmt.charAt(i)) {
                    // 如0.01舍掉0.0
                    continue;
                }
                sbf.append(sAmt.charAt(i));
            }
        }
        if (iNum == -1 || iNum == 0) {
            sbf.append("00");
        } else if (iNum == 1) {
            sbf.append("0");
        }
        return sbf.toString();
    }

    /**
     * 版本比较 -3--版本号错误，提示无法升级 -2--旧版本，提示是否升级 -1--跳版本，不能升级 0--降版本，提示是否升级 1--可以升级
     * 2--更新前版本为旧版本，不处理
     *
     * @param version        更新前版本号
     * @param versionUpgrade 更新版本号 I2.1.0.0.13090910-20130909100000-UPGRADE
     */
    public static int compareVersion(String version, String versionUpgrade) {
        if (versionUpgrade.indexOf("All") > 0
                || versionUpgrade.indexOf("BETA") > 0
                || version.indexOf("All") > 0 || version.indexOf("BETA") > 0) {  //$NON-NLS-2$
            return 1;
        }
        int[] ver = getVersionInfo(version);
        int[] verUpgrade = getVersionInfo(versionUpgrade);
        // 版本号错误
        if (ver[0] == -2 || verUpgrade[0] == -2) {
            return -3;
        }
        // 更新前版本为旧版本，不处理
        if (ver[0] == -1) {
            return 2;
        }
        // 旧的版本编码，提示是否升级
        if (verUpgrade[0] == -1) {
            return -2;
        }
        // 验证大版本号第一位
        if (verUpgrade[0] < ver[0]) {
            return 0;
        }
        if (verUpgrade[0] - ver[0] == 1) {
            if (ver[1] == 9 && ver[2] == verUpgrade[3]) {
                return 1;
            } else {
                return -1;
            }
        }
        if (verUpgrade[0] - ver[0] > 1) {
            return -1;
        }
        // 验证大版本号第二位
        if (verUpgrade[1] < ver[1]) {
            return 0;
        }
        if (verUpgrade[1] - ver[1] == 1) {
            if (ver[2] == verUpgrade[3]) {
                return 1;
            } else {
                return -1;
            }
        }
        if (verUpgrade[1] - ver[1] > 1) {
            return -1;
        }
        // 验证阶段性版本号
        if (verUpgrade[2] < ver[2]) {
            return 0;
        }
        return 1;
    }

    /**
     * 获取版本号信息 vNumInt[0]、vNumInt[1]--大版本号 vNumInt[2]--阶段性版本号
     * vNumInt[3]--大版本号最后阶段性版本号 return -2 版本号错误，-1 旧编码版本 I2.1.0.0.13090910
     */
    private static int[] getVersionInfo(String version) {
        // if(!version.matches("^{1}(V|I)(\\d*\\.){3}(B|\\d*)\\.$")){
        // return new int[]{-2};
        // }
        int[] vNumInt = new int[4];
        try {
            int endIndex = version.lastIndexOf(".");
            String versionNum = version.substring(1, endIndex);
            ////VBATE2.4.1.0.18020602 ，V2.4.1.0.19040800
            if (versionNum.startsWith("BATE")) {
                versionNum = versionNum.replaceAll("BATE", "");
            }
            if ("B".equals(String.valueOf(versionNum
                    .charAt(versionNum.length() - 1)))) {
                return new int[]{-1};
            }
            String[] vNum = versionNum.split("\\.");
            for (int i = 0; i < vNum.length; i++) {
                vNumInt[i] = parseInt(vNum[i]);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return vNumInt;
    }

    /**
     * 查询门店资料
     *
     * @return 后台返回结果, 非JSON格式返回空
     */
    public static String doStoreOnline(String sSNO, String sType) {
        List<NameValuePair> oPm = new ArrayList<NameValuePair>();
        oPm.add(new BasicNameValuePair("sSNO", sSNO));
        oPm.add(new BasicNameValuePair("sType", sType));
        String _sContentnt = doPostUrl("/jsp/ccr/qryStore.jsp", oPm);
        return isJSON(_sContentnt) ? _sContentnt : "";
    }

    /**
     * 验证是否JSON格式数据
     */
    public static boolean isJSON(String sContentnt) {

        if (sContentnt == null || "".equals(sContentnt)
                || !sContentnt.startsWith("{")) {
            return false;
        }

        return true;
    }

    /**
     * 将String转换为JSON格式
     *
     * @return 内容为空或格式不正确返回null
     */
    public static JSONObject parseJSON(String sContentnt) {
        if (!isJSON(sContentnt)) {
            return null;
        }
        JSONObject _oJSON = null;
        if (!"".equals(sContentnt)) {
            try {
                sContentnt = StringUtils.replace(sContentnt, ":\"null\"",
                        ":\"\"");
                sContentnt = StringUtils.replace(sContentnt, ":\"NULL\"",
                        ":\"\"");
            } catch (Exception e) {
                //LogKit.error(e);
            }
        }
        try {
            _oJSON = JSONObject.fromObject(sContentnt);
        } catch (Exception e) {
            //LogKit.error("===parseJSON error:" + e);
        }
        return _oJSON;
    }

    /**
     * 将String转换为JSON格式
     *
     * @return 内容为空或格式不正确返回null
     */
    public static JSONArray parseJSONArray(String sContentnt) {
        JSONArray _oJSON = null;
        if (!"".equals(sContentnt)) {
            try {
                sContentnt = StringUtils.replace(sContentnt, ":\"null\"",
                        ":\"\"");
                sContentnt = StringUtils.replace(sContentnt, ":\"NULL\"",
                        ":\"\"");
            } catch (Exception e) {
                //LogKit.error(e);
            }
        }
        try {
            _oJSON = JSONArray.fromObject(sContentnt);
        } catch (Exception e) {
            //LogKit.error("===parseJSONArray error:" + e);
        }
        return _oJSON;
    }

    /**
     * 将字符串转为XML
     *
     * @param xml
     * @return
     * @throws Exception
     */
    public static Document getXML(String xml) throws Exception {
        Document doc = null;
        if (null != xml && !"".equals(xml)) {
            try {
                // DocumentHelper 是生成 XML 文档节点的 dom4j API 工厂类
                // 将xml解析为一个XML文档并返回一个新创建的Document
                doc = DocumentHelper.parseText(xml); // 将字符串转为XML
            } catch (DocumentException e) {
                //LogKit.error("===getXML error:" + e);
            } catch (Exception e) {
                //LogKit.error("===getXML error:" + e);
            }
        }
        return doc;
    }

    /**
     * GET方式连线后台
     */
    public static String doGetUrl(String sUrl, String sParameter) {
        // 2018/02/09 viva modify 超时时间由3秒改成10秒
        // 2018/04/17 viva modify 需求 #42289 超时时间由10秒改成30秒
        int iTimeOut = 30000;
        String _sSmeUrl = parseString(emisKeeper.getInstance().getEmisPropMap().get("SME_URL"));
        return doGetYourUrl(_sSmeUrl + sUrl, sParameter, iTimeOut);
    }

    /**
     * GET方式连线（需要输入自己完整的URL地址）
     */
    public static String doGetYourUrl(String sUrl, String sParameter, int iTimeOut) {
        String _sContent = "";
        try {
            HttpClientService _oHttpClient = new HttpClientImpl();
            _oHttpClient.setiTimeOut(iTimeOut);
            _sContent = _oHttpClient.doGet(sUrl, sParameter);
        } catch (Exception e) {
            //LogKit.error(sUrl, e);
        }
        return _sContent;
    }

    /**
     * POST方式连线后台
     */
    public static String doPostUrl(String sUrl, List<NameValuePair> oPm) {
        //2018/04/17 viva modify 需求 #42289 超时时间由10秒改成30秒
        return doPostUrl(sUrl, oPm, 30000);// 8秒-->30秒
    }

    /**
     * 连接后台接口
     *
     * @param sUrl
     * @param oPm
     * @param iTimeOut 毫秒
     * @return
     */
    public static String doPostUrl(String sUrl, List<NameValuePair> oPm,
                                   int iTimeOut) {
        String _sContentnt = "";
        if ("".equals(sUrl)) {
            return _sContentnt;
        }
        String _sSrc = parseString(emisKeeper.getInstance()
                .getEmisPropMap().get("SME_URL"));
        return doPostYourUrl(_sSrc + sUrl, oPm, iTimeOut);
    }

    /**
     * POST方式连线（需要输入自己完整的URL地址）
     *
     * @param sUrl
     * @param oPm
     * @param iTimeOut
     * @return
     */
    public static String doPostYourUrl(String sUrl, List<NameValuePair> oPm, int iTimeOut) {
        String _sContentnt = "";
        if ("".equals(sUrl)) {
            return _sContentnt;
        }
        try {
            HttpClientService _oHttpClient = new HttpClientImpl();
            _oHttpClient.setiTimeOut(iTimeOut);
            _sContentnt = _oHttpClient.doPost(sUrl, oPm);
        } catch (Exception e) {
            //LogKit.error(sUrl, e);
        }
        return _sContentnt;
    }

    /**
     * POST方式连线（需要输入自己完整的URL地址）
     *
     * @param sUrl
     * @param oPm
     * @param iTimeOut
     * @return
     */
    public static String doPostSSLYourUrl(String sUrl, List<NameValuePair> oPm, int iTimeOut) {
        String _sContentnt = "";
        if ("".equals(sUrl)) {
            return _sContentnt;
        }
        try {
            HttpClientService _oHttpClient = new HttpClientImpl();
            _oHttpClient.setiTimeOut(iTimeOut);
            _oHttpClient.setSSL(true);
            _sContentnt = _oHttpClient.doPost(sUrl, oPm);
        } catch (Exception e) {
            //LogKit.error(sUrl, e);
        }
        return _sContentnt;
    }

    /**
     * POST方式连线后台，不需要返回
     */
    public static void doPostUrlNoResponse(String sUrl, List<NameValuePair> oPm, int iTimeOut) {
        if ("".equals(sUrl)) {
            return;
        }
        String _sSrc = parseString(emisKeeper.getInstance().getEmisPropMap().get("SME_URL"));
        boolean isNeedLog = true;
        try {
            if (sUrl.indexOf("download_after") > 0) {
                // 内置sas的下传不显示log
                isNeedLog = false;
            }
            HttpClientService _oHttpClient = new HttpClientImpl();
            _oHttpClient.setiTimeOut(iTimeOut);
            _oHttpClient.setNeedLog(isNeedLog);
            _oHttpClient.setNeedResponse(false);
            _oHttpClient.doPost(_sSrc + sUrl, oPm);
        } catch (Exception e) {
            //LogKit.error("[ERR_CODE][1001]");
            //LogKit.error(sUrl, e);
        }
    }

    /**
     * 支付宝POST方式连线后台
     *
     * @param sUrl 连线支付宝方法名
     * @param oPm  参数
     * @return
     */
    public static String doAlipayPostUrl(String sUrl, List<NameValuePair> oPm) {
        return doPostUrl(sUrl, oPm, 80000); // 80秒
    }

    /**
     * 获取当前操作系统名称. return 操作系统名称 例如:windows xp,linux 等.
     */
    public static String getOSName() {
        return System.getProperty("os.name").toLowerCase();
    }

    public static void main(String[] args) {
        System.out.println("==");

        System.out.println(createEasyCode("A0012"));
        System.out.println(unLockEasyCode("2999"));
    }

    /**
     * 查询礼券信息qryIcGift.jsp
     *
     * @param valMap   IC_NO 卡号<br>
     * @param iTimeOut
     * @return 后台返回结果, 非JSON格式返回空
     * @author viva 2013/04/11
     */
    public static String doQryIcGift(Map valMap, int iTimeOut) {
        String IC_NO = parseString(valMap.get("IC_NO"));
        List<NameValuePair> oPm = new ArrayList<NameValuePair>();
        oPm.add(new BasicNameValuePair("IC_NO", IC_NO));
        String sUrl = "/jsp/ccr/qryIcGift.jsp";
        String _sContentnt = doPostUrl(sUrl, oPm, iTimeOut);
        return isJSON(_sContentnt) ? _sContentnt : "";
    }

    /**
     * 当前发票信息
     *
     * @return
     */
    public static String getCurInvoiceInfo() {
        emisKeeper keeper = emisKeeper.getInstance();
        return "INVOICE_CNT_R:" +
                parseString(keeper.getEmisPropMap().get("INVOICE_CNT_R"))
                + " INVOICE_NO:" +
                parseString(keeper.getEmisPropMap().get("INVOICE_NO"))
                + " INVOICE_CNT:" +
                parseString(keeper.getEmisPropMap().get("INVOICE_CNT"));
    }

    /**
     * 用于锁定文件，实现程序单开（即不允许多个实例）
     *
     * @param lockFile
     * @return
     */
    public static boolean lockInstance(final String lockFile) {
        try {
            final File file = new File(lockFile);
            final RandomAccessFile randomAccessFile = new RandomAccessFile(
                    file, "rw");
            final FileLock fileLock = randomAccessFile.getChannel().tryLock();
            if (fileLock != null) {
                Runtime.getRuntime().addShutdownHook(new Thread() {
                    public void run() {
                        try {
                            fileLock.release();
                            randomAccessFile.close();
                            file.delete();
                        } catch (Exception e) {
                            //LogKit.error("Unable to remove lock file: " + lockFile, e);
                        }
                    }
                });
                return true;
            }
        } catch (Exception e) {
            //LogKit.error("Unable to create and/or lock file: " + lockFile, e);
        }
        return false;
    }

    /**
     * 检查是否为触控模式
     *
     * @return
     */
    public static boolean isTouchMode() {
        return "0".equals(parseString(
                emisPropUtil.getEmisProp("IS_USE_ORDERMODEL"), "0"));  //$NON-NLS-2$
    }

    /**
     * 检查是否为触控模式及显示图
     *
     * @return
     */
    public static boolean isTouchPicMode() {
        return "1".equals(parseString(
                emisPropUtil.getEmisProp("IS_USE_ORDERMODEL"), "0"));  //$NON-NLS-2$
    }

    /**
     * 检查是否为零售模式
     *
     * @return
     */
    public static boolean isRetailMode() {
        return "2".equals(parseString(
                emisPropUtil.getEmisProp("IS_USE_ORDERMODEL"), "0"));  //$NON-NLS-2$
    }


    /**
     * 获取Json的参数
     *
     * @param json
     * @param key
     * @return
     */
    public static String getJsonString(JSONObject json, String key) {
        if (json != null && json.has(key)) {
            return json.getString(key);
        }
        return "";
    }

    /**
     * 转换处理网卡集
     *
     * @param macs
     * @return
     */
    public static String parseMacAddress(List<String> macs) {
        if (macs != null) {
            return macs.toString().replaceAll("\\[", "").replaceAll("\\]", "")  //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
                    .replaceAll(", ", "@");  //$NON-NLS-2$
        } else {
            return "";
        }
    }

    /**
     * 检查是否为美食街服务台模式
     *
     * @return
     */
    public static boolean isCardServiceMode() {
        return "CS".equalsIgnoreCase(System.getProperty("emis.platform"));  //$NON-NLS-2$
    }

    /**
     * 检查是否为美食街财务工具模式
     *
     * @return
     */
    public static boolean isCardFinacialMode() {
        return "CF".equalsIgnoreCase(System.getProperty("emis.platform"));  //$NON-NLS-2$
    }

    /**
     * 检查是否为美食街档口模式
     *
     * @return
     */
    public static boolean isFoodCourtMode() {
        return "FC".equalsIgnoreCase(System.getProperty("emis.platform"));  //$NON-NLS-2$
    }

    /**
     * 判断是否美食街系统，是则使用Asscii编码，否则维持原编码方式
     *
     * @return
     */
    public static boolean isFoodCourtSys() {
        return isFoodCourtMode() || isCardFinacialMode()
                || isCardServiceMode();
    }

    /**
     * 检查是否为Venus财务工具模式
     *
     * @return
     */
    public static boolean isVenusCardFinacialMode() {
        return "IC_GIFT".equalsIgnoreCase(System.getProperty("emis.platform"));  //$NON-NLS-2$
    }

    /**
     * 获取门店信息
     *
     * @param sS_NO  门店编号
     * @param sID_NO 机台编号
     * @return
     */
    public static String getStoreInfoOnline(String sUrl, String sS_NO, String sID_NO) {
        List<NameValuePair> oPm = new ArrayList<NameValuePair>();
        oPm.add(new BasicNameValuePair("S_NO", sS_NO));
        oPm.add(new BasicNameValuePair("ID_NO", sID_NO));
        //2018/04/17 viva modify 需求 #42289 超时时间由10秒改成30秒
        String _sContentnt = doPostYourUrl(sUrl + "/jsp/ccr/getStoreInfo.jsp", oPm, 30000);

        return isJSON(_sContentnt) ? _sContentnt : "";
    }

    /**
     * 必须高于JDK1.5
     *
     * @return
     */
    public static boolean isAtLeastJava15() {
        String javaVersion = System.getProperty("java.version");
        // 1.5 会黑屏暂不支持
        if (javaVersion.contains("1.5.")) {
            return false;
        }
        return true;
    }

    /**
     * 处理空值字串(空格即视为空值)，当为空值时返回defaultValue的值
     *
     * @param obj
     * @param defaultValue
     * @return
     */
    public static String parseBlankString(Object obj, String defaultValue) {
        if (null == obj) {
            return defaultValue;
        } else {
            String val = String.valueOf(obj).trim();
            return "".equals(val) ? defaultValue : val;
        }
    }


    /**
     * 与当前日期比，d'天'HH'小时'mm'分'ss'秒'
     *
     * @param syyyyMMddHHmmss
     * @param formatType      : 1 d'天'HH'小时'mm'分'ss'秒' 2 HH'小时'mm'分'ss'秒' 3 mm'分'ss'秒' 4
     *                        mm'分' 5 HH'小时'mm'分' 6 d'天'HH'小时'mm'分'
     * @return
     */
    public static String getDiffTime(String syyyyMMddHHmmss, int formatType) {
        if ("".equals(syyyyMMddHHmmss)) {
            return "";
        }
        Date date1 = null;
        try {
            date1 = df_yyyyMMddHHmmss.parse(syyyyMMddHHmmss);
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
        if (date1 == null) {
            return "";
        }
        Calendar c = Calendar.getInstance();
        long lTime1 = date1.getTime();
        long lTime2 = c.getTime().getTime();
        String sTitle = "已过";
        if (lTime1 > lTime2) {
            long lTime = lTime1;
            lTime1 = lTime2;
            lTime2 = lTime;
            sTitle = "剩余";
        }
        String sFormat = "HH'小时'mm'分'";
        switch (formatType) {
            case 1:
                sFormat = "d'天'HH'小时'mm'分'ss'秒'";
                break;
            case 2:
                sFormat = "HH'小时'mm'分'ss'秒'";
                break;
            case 3:
                sFormat = "mm'分'ss'秒'";
                break;
            case 4:
                sFormat = "mm'分'";
                break;
            case 5:
                sFormat = "HH'小时'mm'分'";
                break;
            case 6:
                sFormat = "d'天'HH'小时'mm'分'";
                break;
            default:
                break;
        }
        return sTitle
                + DurationFormatUtils.formatPeriod(lTime1, lTime2, sFormat);
    }

    /**
     * 两个日期时间比较，d'天'HH'小时'mm'分'ss'秒'
     *
     * @param syyyyMMddHHmmss
     * @param syyyyMMddHHmmss2
     * @param formatType       : 1 'd'天'HH'小时'mm'分'ss'秒' 2 'HH'小时'mm'分'ss'秒' 3 'mm'分'ss'秒' 4
     *                         'mm'分' 5 'HH'小时'mm'分' 6 'd'天'HH'小时'mm'分'
     * @return
     */
    public static String getDiffTime(String syyyyMMddHHmmss,
                                     String syyyyMMddHHmmss2, int formatType) {
        Date date1 = null;
        Date date2 = null;
        try {
            date1 = df_yyyyMMddHHmmss.parse(syyyyMMddHHmmss);
            date2 = df_yyyyMMddHHmmss.parse(syyyyMMddHHmmss2);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "";
        }
        if (date1 == null || date2 == null) {
            return "";
        }
        long lTime1 = date1.getTime();
        long lTime2 = date2.getTime();
        String sTitle = "已过";
        if (lTime1 > lTime2) {
            long lTime = lTime1;
            lTime1 = lTime2;
            lTime2 = lTime;
            sTitle = "剩余";
        }
        String sFormat = "HH'小时'mm'分'";
        switch (formatType) {
            case 1:
                sFormat = "d'天'HH'小时'mm'分'ss'秒'";
                break;
            case 2:
                sFormat = "HH'小时'mm'分'ss'秒'";
                break;
            case 3:
                sFormat = "mm'分'ss'秒'";
                break;
            case 4:
                sFormat = "mm'分'";
                break;
            case 5:
                sFormat = "HH'小时'mm'分'";
                break;
            case 6:
                sFormat = "d'天'HH'小时'mm'分'";
                break;
            default:
                break;
        }
        //long3_3.2以上要JDK1.6
        return sTitle
                + DurationFormatUtils.formatPeriod(lTime1, lTime2, sFormat);
    }

    /**
     * 是否开启kds
     *
     * @return
     */
    public static boolean isUseKDS() {
        /**
         * 开启kds
         */
        boolean IS_USE_KDS = parseBoolean(emisKeeper.getInstance()
                .getEmisPropMap().get("IS_USE_KDS"));
        /**
         * kds
         */
        if (IS_USE_KDS) {
            // 检查KDS DB有没有设定
            String ip = parseString(emisKeeper.getInstance()
                    .getEmisPropMap().get("KDS_DB_IP"));
            IS_USE_KDS = !("".equals(ip));
        }
        return IS_USE_KDS;
    }

    /**
     * 获取小票随机文章文件名
     *
     * @return
     */
    public static String getArtice() {
        try {
            //先删除空文件
            File fileOld = new File(getRoot() + "Artice");
            String[] articeOld = fileOld.list();
            for (int i = 0; i < articeOld.length; i++) {
                File _artice = new File(getRoot() + "Artice\\" + articeOld[i]);
                if (!(_artice.exists() && _artice.length() > 0)) {
                    _artice.delete();
                }
            }
            //重新获取
            File file = new File(getRoot() + "Artice");
            String[] artice = file.list();
            int random = artice.length;
            if (file != null && file.exists() && random > 0) {
                //返回随机文件名称
                Random r = new Random();
                String fileName = getRoot() + "Artice\\" + artice[r.nextInt(random)];
                return fileName;
            } else {
                return "";
            }
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * 得到系统信息
     * 2018/11/21 viva modify
     */
    public static String getSystemInfo() {
        try {
            StringBuffer strBfr = new StringBuffer("\r\n系统参数相关信息");
            String EXE_DIR = parseString(System
                    .getProperty("user.dir"));
            if (!"".equals(EXE_DIR)) {
                strBfr.append("\r\n[程序目录]:" + EXE_DIR);
            }
            String osName = System.getProperty("os.name");
            String osVer = System.getProperty("os.version");
            if (!"".equals(osName)) {
                strBfr.append("\r\n[操作系统]:" + osName + osVer);
            }
            String sMacAddress = "";
            strBfr.append("\r\n[网卡地址]:" + sMacAddress);
            String JVM = parseString(System
                    .getProperty("java.version"));
            if (!"".equals(JVM)) {
                strBfr.append("\r\n[JAVA版本]:" + JVM);
            }
            String JVM_HOME = parseString(System
                    .getProperty("java.home"));
            if (!"".equals(JVM_HOME)) {
                strBfr.append("\r\n[JAVA目录]:" + JVM_HOME);
            }
            List<String> javaParaList = ManagementFactory.getRuntimeMXBean().getInputArguments();
            if (javaParaList != null && javaParaList.size() > 0) {
                for (Iterator<String> iterator = javaParaList
                        .iterator(); iterator.hasNext(); ) {
                    String str = parseString(iterator
                            .next());
                    if (!"".equals(str)) {
                        strBfr.append("\r\n[JAVA参数]:" + str);
                    }
                }
            }
            //2019/09/03 viva modify 系统参数相关信息添加显示本机IP在前台log中
            try {
                strBfr.append("\r\n[本机的IP]：" + InetAddress.getLocalHost());
            } catch (Exception e) {
                e.printStackTrace();
            }
            List<String> ipList = getLocalIP();
            if (ipList != null && ipList.size() > 1) {
                int i = 1;
                for (Iterator iterator = ipList.iterator(); iterator.hasNext(); ) {
                    strBfr.append("\r\n[多个IP_" + i + "]："
                            + parseString(iterator.next()));
                    i++;
                }
            }
            return strBfr.toString();
        } catch (Exception e) {
            //LogKit.error("", e);
        }
        return "";
    }

    /**
     * 获取机器所有网卡的IP（ipv4）
     *
     * @return
     */
    public static List<String> getLocalIP() {
        List<String> ipList = new ArrayList<String>();
        InetAddress ip = null;
        try {
            Enumeration<NetworkInterface> netInterfaces = (Enumeration<NetworkInterface>) NetworkInterface.getNetworkInterfaces();
            while (netInterfaces.hasMoreElements()) {
                NetworkInterface ni = (NetworkInterface) netInterfaces.nextElement();
                // 遍历所有ip
                Enumeration<InetAddress> ips = ni.getInetAddresses();
                while (ips.hasMoreElements()) {
                    ip = (InetAddress) ips.nextElement();
                    if (null == ip || "".equals(ip)) {
                        continue;
                    }
                    String sIP = ip.getHostAddress();
                    if (sIP == null || sIP.indexOf(":") > -1) {
                        continue;
                    }
                    ipList.add(sIP);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ipList;
    }

    /**
     * 将机台代码转换成数字，去掉需要左边的0,A-J 1-0
     *
     * @param SL_NO 传入的机台代码+流水号
     * @return 转换的机台代码+流水（去0）
     */
    public static String createEasyCode(String SL_NO) {
        if ("".equals(SL_NO)) {
            return SL_NO;
        }
        //先判断SL_NO长度,长度小于2或者大于五返回原字符串,默认2-5位
        if (SL_NO.length() > 5 || SL_NO.length() < 2) {
            return SL_NO;
        }
        String str[] = new String[]{"A", "B", "C", "D", "E", "F", "G", "H", "I", "J"};
        String id_no_flag = SL_NO.substring(0, 1);
        String _sl_no = SL_NO.substring(1, SL_NO.length());
        for (int i = 0; i < str.length; i++) {
            if (str[i].equals(id_no_flag)) {
                if (i == str.length - 1) {
                    id_no_flag = parseString(0);
                } else {
                    id_no_flag = parseString(i + 1);
                }
            }
        }
        _sl_no = _sl_no.replaceFirst("^0*", "");
        return id_no_flag + _sl_no;
    }

    /**
     * 将数字转换成机台代号,A-J 1-0
     *
     * @param SL_NO 传入的机台数字+流水号
     * @return 转换的机台代码+流水（去0）
     */
    public static String unLockEasyCode(String SL_NO) {
        if ("".equals(SL_NO)) {
            return SL_NO;
        }
        //先判断SL_NO长度,大于5返回原字符串
        if (SL_NO.length() > 5) {
            return SL_NO;
        }

        String str[] = new String[]{"A", "B", "C", "D", "E", "F", "G", "H", "I", "J"};
        String id_no_flag = SL_NO.substring(0, 1);
        String _sl_no = SL_NO.substring(1, SL_NO.length());
        for (int i = 0; i < str.length; i++) {
            String s = parseString(i + 1);
            if (s.equals(id_no_flag)) {
                if (i == str.length - 1) {
                    id_no_flag = "J";
                } else {
                    id_no_flag = str[i];
                }
            }
        }

        return id_no_flag + _sl_no;
    }

}
