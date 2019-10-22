package com.fish.yuyou.util;

import android.text.TextUtils;

import com.fish.yuyou.R;
import com.fish.yuyou.base.YuApplication;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class DateUtil {
    private static final String FORMAT_0 = "yyyy-MM-dd HH:mm";
    private static final String FORMAT_1 = "yyyy-MM-dd HH:mm:ss";
    private static final String FORMAT_2 = "yyyy-MM-dd";
    private static final String FORMAT_3 = "yyyy-MM-dd-HH-mm-ss-SSS";
    private static final String FORMAT_4 = "yyyyMMddHHmmssSSS";
    private static final String FORMAT_5 = "HH:mm:ss";
    private static final String FORMAT_7 = "MM-dd HH:mm:ss";
    private static Calendar c = Calendar.getInstance();

    /**
     * 获得指定日期的前一天
     *
     * @param specifiedDay
     * @param preDays      指定日期的前几天的集合
     * @return
     * @throws Exception
     */
    public static List<String> getSpecifiedDayBefore(String specifiedDay, int preDays) {
        List<String> dateList = new ArrayList<>();
        Date date = null;
        Calendar c = Calendar.getInstance();
        try {
            date = new SimpleDateFormat("yy-MM-dd").parse(specifiedDay);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        c.setTime(date);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        int day = c.get(Calendar.DATE);
        for (int i = 0; i < preDays; i++) {
            c.set(Calendar.DATE, day - i);
            String days = format.format(c.getTime());
            dateList.add(days);
        }
//        c.set(Calendar.DATE, day - preDays);
//
//
//        String dayBefore = new SimpleDateFormat("yyyy-MM-dd").format(c.getTime());
        return dateList;
    }

    /**
     * @param caculateCount -1代表上一周，1代表下一周,0代表本周
     * @param dateStr       输入当前的日期
     * @return 获取当前日期所在的一周（上一周/下一周)
     * @author:zxy
     */
    public static List<String> getWeekDay(String dateStr, int caculateCount) {
        List<String> dateList = new ArrayList<>();
        try {
            Date date = null;
            SimpleDateFormat sbf = new SimpleDateFormat("yyyy-MM-dd");
            Calendar cal = Calendar.getInstance();
            cal.setTime(sbf.parse(dateStr));
            cal.add(Calendar.WEEK_OF_YEAR, caculateCount);
//            cal.setFirstDayOfWeek(Calendar.SUNDAY);//设置周日为一周的第一天
            for (int ii = 1; ii < 7; ii++) {
                cal.set(Calendar.DAY_OF_WEEK, ii);//设置日期为当前时间所在的星期ii（日、一、二、三、四、五）
                date = cal.getTime();
                dateList.add(sbf.format(date));
            }
            cal.set(Calendar.DAY_OF_WEEK, 0);//周六
            date = cal.getTime();
            dateList.add(sbf.format(date));
        } catch (Exception ee) {
            ee.printStackTrace();
        }
        return dateList;
    }

    /**
     * 功能:获取当天的日期(格式是yyyy-MM-dd)
     *
     * @return "yyyy-MM-dd"
     */
    public static String getCurTime() {
        Date nowDate = new Date(System.currentTimeMillis());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(FORMAT_2);//"yyyy-MM-dd"
        return simpleDateFormat.format(nowDate);
    }

    /**
     * 功能:获取某两天的日期字符串数组(格式是yyyy-MM-dd)
     *
     * @param date    指定日期，为null表示当前日期
     * @param predays 负数表示相对于date几天前，正数表示几天后
     * @return { yesterday, today }
     * @author: Wangbinbin
     * @date:2015-3-12下午2:43:29
     */
    public static String[] getTwoDaysTimeAsFormat2(Date date, int predays) {
        SimpleDateFormat dateFormate = new SimpleDateFormat(FORMAT_2);
        if (date == null) {
            date = new Date();
        }
        String today = dateFormate.format(date);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, predays);
        date = calendar.getTime();
        String yesterday = dateFormate.format(date);
        return new String[]{yesterday, today};
    }

    /**
     * 功能:获取某两天的日期毫秒值(跟上面方法类似)
     *
     * @param time    (单位：毫秒) 指定日期的毫秒值，为-1表示当前日期
     * @param predays 负数表示相对于date几天前，正数表示几天后
     * @return {yesterdayTime,todayTime}
     * @author: yefx
     * @date:2015-6-29上午10:41:02
     */
    public static long[] getTwoDaysTime(long time, int predays) {
        long todayTime = -1, yesterdayTime = -1;
        if (time == -1) {
            time = new Date().getTime();
        }
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(time);
        c.add(Calendar.DAY_OF_MONTH, predays);
        yesterdayTime = c.getTimeInMillis();
        return new long[]{yesterdayTime, todayTime};
    }

    public static long[] getTwoDaysTimeAsSecond(long time, int predays) {
        long[] times = getTwoDaysTime(time, predays);
        times[0] = times[0] / 1000;
        times[1] = times[1] / 1000;
        return times;
    }

    public static Date str2Date(String str) {
        return str2Date(str, null);
    }

    public static Date str2Date(String str, String format) {
        if (str == null || str.length() == 0) {
            return null;
        }
        if (format == null || format.length() == 0) {
            format = FORMAT_1;
        }
        Date date = null;
        try {
            SimpleDateFormat simpleFormat = new SimpleDateFormat(format);
            date = simpleFormat.parse(str);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return date;
    }

    /***
     * 得到时间字符串对应的时间戳
     *
     * @param time 格式为FORMAT_2 = "yyyy-MM-dd";
     * @return 单位:毫秒
     */
    public static long getTimeFromFormat2(String time) {
        if (time == null || "".equals(time)) {
            return System.currentTimeMillis();
        }
        Date date = str2Date(time, FORMAT_2);
        return date.getTime();
    }

    /**
     * 功能:得到时间字符串对应的时间戳
     *
     * @param timeStr 格式为FORMAT_2 = "yyyy-MM-dd";
     * @return 单位:秒
     * @author: yefx
     * @date:2015-6-30下午1:41:27
     */
    public static long getTimeAsSecondFromFormat2(String timeStr) {
        if (timeStr == null || "".equals(timeStr)) {
            return System.currentTimeMillis() / 1000;
        }
        Date date = str2Date(timeStr, FORMAT_2);
        return date.getTime() / 1000;
    }

    /***
     * 得到时间字符串对应的时间戳
     *
     * @param time 格式为FORMAT_1 = "yyyy-MM-dd HH:mm:ss";
     * @return 单位:毫秒
     */
    public static long getTimeFromFormat1(String time) {
        if (time == null || "".equals(time)) {
            return System.currentTimeMillis();
        }
        Date date = str2Date(time);
        return date.getTime();
    }

    /**
     * 功能:得到时间字符串对应的时间戳
     *
     * @param timeStr 格式为FORMAT_1 = "yyyy-MM-dd HH:mm:ss";
     * @return 单位:秒
     * @author: yefx
     * @date:2015-6-30下午1:41:27
     */
    public static long getTimeAsSecondFromFormat1(String timeStr) {
        if (timeStr == null || "".equals(timeStr)) {
            return System.currentTimeMillis() / 1000;
        }
        Date date = str2Date(timeStr);
        return date.getTime() / 1000;
    }

    /**
     * 功能:得到时间字符串对应的时间戳
     *
     * @param time FORMAT_0 = "yyyy-MM-dd HH:mm";
     * @return 单位:毫秒
     * @author: yfx
     * @date:2016年10月21日上午11:37:35
     */
    public static long getTimeFromFormat0(String time) {
        if (time == null || "".equals(time)) {
            return System.currentTimeMillis();
        }
        Date date = str2Date(time, FORMAT_0);
        return date.getTime();
    }

    /**
     * 功能:得到时间字符串对应的时间戳
     *
     * @param timeStr FORMAT_0 = "yyyy-MM-dd HH:mm";
     * @return 单位:秒
     * @author: yfx
     * @date:2016年10月21日上午11:36:39
     */
    public static long getTimeAsSecondFromFormat0(String timeStr) {
        if (timeStr == null || "".equals(timeStr)) {
            return System.currentTimeMillis() / 1000;
        }
        Date date = str2Date(timeStr, FORMAT_0);
        return date.getTime() / 1000;
    }

    /**
     * 功能:获取系统时间戳(按毫秒计算)
     *
     * @return 单位:毫秒
     * @author: yefx
     * @date:2015-6-30下午4:23:30
     */
    public static long getCurrentTimeBySystem() {
//        return System.currentTimeMillis();
        return CommonUtil.getCurrentTimeAsSecond() * 1000;
    }

    /**
     * 功能:获取系统时间戳(按秒计算)
     *
     * @return 单位:秒
     * @author: yefx
     * @date:2015-6-30下午4:24:03
     */
    public static long getCurrentTimeBySystemAsSecond() {
        return getCurrentTimeBySystem() / 1000;
    }

    /**
     * 功能:
     *
     * @param time 类似14252...000 单位:毫秒
     * @return "yyyy-MM-dd HH:mm:ss"
     * @author: yefx
     * @date:2015-6-30下午5:33:15
     */
    public static String getStrAsFormat1(long time) {
        if (time == 0) {
            return "";
        }
        return new SimpleDateFormat(FORMAT_1).format(time);
    }

    /**
     * 功能:
     *
     * @param StrTime 类似14252...000 单位:毫秒
     * @return "yyyy-MM-dd HH:mm:ss"
     * @author: yefx
     * @date:2015-6-30下午5:33:15
     */
    public static String getStrAsFormat(String StrTime) {
        long time = Long.parseLong(StrTime);
        return new SimpleDateFormat(FORMAT_1).format(time * 1000);
    }

    /**
     * 功能:
     *
     * @param timeStr 类似"14252...000" 毫秒单位的时间戳字符串
     * @return "yyyy-MM-dd HH:mm:ss"
     * @author: yefx
     * @date:2015-6-30下午5:37:46
     */
    public static String getStrAsFormat1(String timeStr) {
        return getStrAsFormat1(Long.valueOf(timeStr));
    }

    /**
     * 功能:
     *
     * @param time 14252... 单位:秒
     * @return "yyyy-MM-dd HH:mm:ss"
     * @author: yefx
     * @date:2015-6-30下午5:33:33
     */
    public static String getStrAsFormat1FromSecond(long time) {
        return getStrAsFormat1(time * 1000);
    }

    /**
     * 功能:
     *
     * @param timeStr 秒单位的时间戳字符串(类似"14252...")
     * @return "yyyy-MM-dd HH:mm:ss";
     * @author: yefx
     * @date:2015-6-30下午5:36:51
     */
    public static String getStrAsFormat1FromSecond(String timeStr) {
        return getStrAsFormat1FromSecond(Long.valueOf(timeStr));
    }

    /**
     * 功能:将年月日时分秒格式化为"2015-03-25 02:13"形式的字符串
     *
     * @param year
     * @param month
     * @param day
     * @param hour
     * @param minute
     * @return
     * @author: yefx
     * @date:2015-6-29下午5:08:31
     */
    public static String getFormat1StrAsTwoDigits(int year, int month, int day, int hour, int minute) {
        return year + "-" + getFormatAsTwoDigits(month) + "-" + getFormatAsTwoDigits(day) + " " + getFormatAsTwoDigits(hour) + ":" + getFormatAsTwoDigits(minute);
    }

    /**
     * 功能:将年月日时分秒格式化为"2015-03-25 02:13:05"形式的字符串
     *
     * @param year
     * @param month
     * @param day
     * @param hour
     * @param minute
     * @param second
     * @return
     * @author: yefx
     * @date:2015-11-2下午7:06:07
     */
    public static String getFormat1StrAsTwoDigits(int year, int month, int day, int hour, int minute, int second) {
        return year + "-" + getFormatAsTwoDigits(month) + "-" + getFormatAsTwoDigits(day) + " " + getFormatAsTwoDigits(hour) + ":" + getFormatAsTwoDigits(minute) + ":" + getFormatAsTwoDigits(second);
    }

    /**
     * 功能:
     *
     * @param year
     * @param month
     * @param day
     * @return
     * @author: yefx
     * @date:2015-6-29下午5:08:31
     */
    public static String getAsFormat2AsTwoDigits(int year, int month, int day) {
        return year + "-" + getFormatAsTwoDigits(month) + "-" + getFormatAsTwoDigits(day);
    }

    /**
     * 功能:将数字格式化最最小长度为2的类似"02"的字符串
     *
     * @param value
     * @return
     * @author: yefx
     * @date:2015-6-29下午5:08:37
     */
    public static String getFormatAsTwoDigits(int value) {
        String valueStr = "-1";
        if (value < 10) {
            valueStr = "0" + value;
        } else {
            valueStr = value + "";
        }
        return valueStr;
    }

    /**
     * 功能:由年月日生成时间戳
     *
     * @param year
     * @param month 正常月份减一
     * @param day
     * @return 单位:毫秒
     * @author: yefx
     * @date:2015-7-9下午5:45:40
     */
    public static long getTime(int year, int month, int day) {
        long time = -1;
        Calendar c = Calendar.getInstance();
        c.set(year, month, day, 0, 0, 0);
        return c.getTimeInMillis();
    }

    /**
     * 功能:由年月日生成时间戳(以秒为单位)
     *
     * @param year
     * @param month
     * @param day
     * @return 单位:秒
     * @author: yefx
     * @date:2015-7-10下午2:56:28
     */
    public static long getTimeAsSecond(int year, int month, int day) {
        return getTime(year, month - 1, day) / 1000;
    }

    /**
     * 功能:由年月日时分生成时间戳 单位:毫秒
     *
     * @param year
     * @param month  正常月份减一
     * @param day
     * @param hour
     * @param minute
     * @return 单位:毫秒
     * @author: yefx
     * @date:2015-7-10下午2:23:53
     */
    public static long getTime(int year, int month, int day, int hour, int minute) {
        return getTime(year, month, day, hour, minute, 0);
    }

    public static long getTime(int year, int month, int day, int hour, int minute, int second) {
        if (second <= -1) {
            second = 0;
        }
        Calendar c = Calendar.getInstance();
        c.set(year, month, day, hour, minute, second);
        return c.getTimeInMillis();
    }

    /**
     * 功能:由年月日时分生成时间戳(单位:秒)(因为参数没有秒，秒按0算)
     *
     * @param year
     * @param month  正常月份减一
     * @param day
     * @param hour
     * @param minute
     * @return 单位:秒
     * @author: yefx
     * @date:2015-7-10下午2:57:32
     */
    public static long getTimeAsSecond(int year, int month, int day, int hour, int minute) {
        return getTime(year, month, day, hour, minute) / 1000;
    }

    public static long getTimeAsSecond(int year, int month, int day, int hour, int minute, int second) {
        return getTime(year, month, day, hour, minute, second) / 1000;
    }

    /**
     * 功能:获取时间戳对应的精确到天（即凌晨的零点）的时间戳
     *
     * @param time 单位：毫秒
     * @return 单位：毫秒
     * @author: yefx
     * @date:2015-7-10下午4:39:25
     */
    public static long getTimeFromTime(long time) {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(time);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        return c.getTimeInMillis();
    }

    /**
     * 功能:获取时间戳(单位:秒)对应的精确到天的时间戳(单位:毫秒)
     *
     * @param time 单位：秒
     * @return 单位：毫秒
     * @author: yefx
     * @date:2015-7-10下午4:35:34
     */
    public static long getTimeFromSecond(long time) {
        return getTimeFromTime(time * 1000);
    }

    /**
     * 功能:获取时间戳字符串(单位:秒)对应的精确到天的时间戳(单位:毫秒)
     *
     * @param timeStr 单位：秒
     * @return 单位：毫秒
     * @author: yefx
     * @date:2015-7-10下午4:35:34
     */
    public static long getTimeFromSecond(String timeStr) {
        return getTimeFromSecond(Long.parseLong(timeStr));
    }

    /**
     * 功能:
     *
     * @param time
     * @return HH:mm:ss
     * @author: wangyk
     * @date:2015-9-22下午2:22:57
     */
    public static String getStrAsFormat7(long time) {
        if (time == 0) {
            return "";
        }
        return new SimpleDateFormat(FORMAT_7).format(time);
    }

    /**
     * 功能:
     *
     * @param time 毫秒
     * @return yyyy-MM-dd
     * @author: yefx
     * @date:2015-7-13上午10:48:21
     */
    public static String getAsFormat2(long time) {
        return new SimpleDateFormat(FORMAT_2).format(new Date(time));
    }

    /**
     * 功能:
     *
     * @param time 毫秒
     * @return yyyy-MM-dd
     * @author: yefx
     * @date:2015-7-24下午4:00:16
     */
    public static String getAsFormat2FromSecond(long time) {
        return getAsFormat2(time * 1000);
    }

    /**
     * 功能:
     *
     * @param time 毫秒
     * @return yyyy-MM-dd
     * @author: yefx
     * @date:2015-7-24下午4:00:37
     */
    public static String getAsFormat2FromSecond(String time) {
        return getAsFormat2(Long.parseLong(time) * 1000);
    }

    public static String getAsFormat5(int hour, int minute) {
        return getFormatAsTwoDigits(hour) + ":" + getFormatAsTwoDigits(minute) + ":00";
    }

    public static int getCurrYear() {
        return c.get(Calendar.YEAR);
    }

    // 月
    public static int getCurrMonth() {
        return c.get(Calendar.MONTH) + 1;
    }

    public static String getCurrMonthAsTwoDigits() {
        return getFormatAsTwoDigits(getCurrMonth());
    }

    public static String getCustomMonthAsTwoDigits(int month) {
        return getFormatAsTwoDigits(month);
    }

    // 日
    public static int getCurrDay() {
        return c.get(Calendar.DAY_OF_MONTH);
    }

    public static String getCurrDayAsTwoDigits() {
        return getFormatAsTwoDigits(getCurrDay());
    }

    public static String getCustomDayAsTwoDigits(int day) {
        return getFormatAsTwoDigits(day);
    }

    /**
     * 功能:获取现在的时间字符串
     *
     * @return FORMAT_1 = "yyyy-MM-dd HH:mm:ss";
     * @author: yefx
     * @date:2015-11-2下午3:27:06
     */
    public static String getCurrDayAsFormat1() {
        return getStrAsFormat1(getCurrentTimeBySystem());
    }

    /**
     * 功能:将秒单位的时间戳转换成相对于现在时刻的三天表示形式<br/>
     * 不同年的时间显示　yyyy-MM-dd HH:mm(:ss)<br/>
     * 前天以前的时间显示 MM-dd HH:mm(:ss)<br/>
     * 前天的时间显示 前天 HH:mm(:ss)<br/>
     * 昨天的时间显示 昨天 HH:mm(:ss)<br/>
     * 今天的时间显示 HH:mm(:ss)<br/>
     *
     * @param time      单位:毫秒
     * @param partCount 保留HH:mm:ss几部分,为2则显示HH:mm,为其他则显示HH:mm:ss
     * @return
     * @author: yefx
     * @date:2015-6-17下午4:48:22
     */
    public static String getThreeDayFormatString(long time, int partCount) {
        return getThreeDayFormat(time, partCount);
    }

    /**
     * @param time
     * @param partCount
     * @return
     */
    public static String getThreeDayFormatFromSecond(long time, int partCount) {
        return getThreeDayFormatString(time * 1000, partCount);
    }

    /**
     * 获取现在时间的三天表示形式
     *
     * @param partCount
     * @return
     */
    public static String getThreeDayFormatNow(int partCount) {
        return getThreeDayFormatFromSecond(getCurrentTimeBySystemAsSecond(), partCount);
    }

    /**
     * 功能:将FORMAT_1 = "yyyy-MM-dd HH:mm:ss" 这种形式的日期字符串显示为前天，昨天，今天的时间
     *
     * @param dateStr FORMAT_1 = "yyyy-MM-dd HH:mm:ss" 这种形式的日期字符串 但并不一定是标准的小于10补零的形式
     * @return
     * @author: yefx
     * @date:2015-6-16上午9:54:29
     */
    public static String getThreeDayFormatString(String dateStr, int partCount) {
        dateStr = formatAsFormat1(dateStr);
        return getThreeDayFormatString(getTimeFromFormat1(dateStr), partCount);
    }

    /**
     * 功能:将类似于2015-6-12 1:2:6 这样的日期规范成2015-06-12 01:02:06
     *
     * @param timeStr
     * @return
     * @author: yefx
     * @date:2015-11-2下午7:11:22
     */
    private static String formatAsFormat1(String timeStr) {
        String formatTime = "-1";
        String[] dateTime = timeStr.split(" ");// [0]yyyy-MM-dd [1]HH:mm:ss
        String[] date = dateTime[0].split("-");// [0]yyyy [1]MM [2]dd
        String[] time = dateTime[1].split(":");// [0]HH [1]mm [2]ss
        formatTime = getFormat1StrAsTwoDigits(getRealIntValue(date[0]), getRealIntValue(date[1]), getRealIntValue(date[2]), getRealIntValue(time[0]), getRealIntValue(time[1]), getRealIntValue(time[2]));
        return formatTime;
    }

    /**
     * 功能:将数据arr的前count个元素按拼接符linked拼揍出字符串
     *
     * @param arr
     * @param count
     * @param linked
     * @return
     * @author: yefx
     * @date:2015-6-16下午1:07:15
     */
    private static String getArrayElementToStr(String[] arr, int count, String linked) {
        String str = "";
        for (int i = 0; i < arr.length; i++) {
            if (i < count - 1) {
                str += arr[i] + linked;
            } else if (i == count - 1) {
                str += arr[i];
            } else {
                break;
            }
        }
        return str;
    }

    public static String getBinaryValue(String digitStr) {
        String digit = "";
        if (!TextUtils.isEmpty(digitStr)) {
            if (Integer.parseInt(digitStr) < 10 && digitStr.length() == 1) {
                digit = "0" + digitStr;
            } else {
                digit = digitStr;
            }
        }
        return digit;
    }

    /**
     * 功能:取数字字符串的真实数字值，01取1,12取12,1取1()
     *
     * @param digitStr 首部最多有1个0的数字字符串
     * @return
     * @author: yefx
     * @date:2015-6-16上午10:55:11
     */
    private static int getRealIntValue(String digitStr) {
        if (TextUtils.isEmpty(digitStr)) {
            return 12306;
        }
        String value = "int值";
        if (digitStr.indexOf("0") == 0 && digitStr.length() >= 2) {
            value = digitStr.substring(1);
        } else {
            value = digitStr;
        }
        return Integer.valueOf(value);
    }

    /**
     * 功能:
     *
     * @param t
     * @param partCount
     * @return
     * @author: yfx
     * @date:2015-12-2下午11:58:32
     */
    private static String getThreeDayFormat(long t, int partCount) {
        String dateStr = getStrAsFormat1(t);
        if (t > getCurrentTimeBySystem())// 未来的时间，这里我们就不关心了
        //因为与服务器时间不一致（比服务器时间慢了几秒），所以未来时间不能不算了
        {
//            return dateStr;
        }
        if (partCount != 2) {
            partCount = 3;
        }
        String[] dateTime = dateStr.split(" ");// [0]yyyy-MM-dd [1]HH:mm:ss
        String[] date = dateTime[0].split("-");// [0]yyyy [1]MM [2]dd
        String[] time = dateTime[1].split(":");// [0]HH [1]mm [2]ss
        String timePart = getArrayElementToStr(time, partCount, ":");
        long todayBeginTime = getTimeFromTime(getCurrentTimeBySystem());// 今天凌晨时间
        long delta = (todayBeginTime - t) / 1000;// 秒
        if (delta <= 0)// 今天凌晨到现在之间的时间
        {
//            dateStr = "今天" + timePart;
            dateStr = "" + timePart;
        } else if (delta <= 24 * 60 * 60)// 昨天凌晨到今天凌晨之间
        {
            dateStr = "昨天 " + timePart;
        } else if (delta <= 2 * 24 * 60 * 60)// 前天凌晨到今天凌晨之间
        {
            dateStr = "前天 " + timePart;
        } else {
            String now = getCurrDayAsFormat1();
            String[] nowDateTime = now.split(" ");// [0]yyyy-MM-dd [1]HH:mm:ss
            String[] nowDate = nowDateTime[0].split("-");// [0]yyyy [1]MM [2]dd
            if (date[0].equals(nowDate[0]))// yyyy一样
            {
                String datePart = date[1] + "-" + date[2];
                dateStr = datePart + " " + timePart;
            } else {// yyyy 不一样
            }
        }
        return dateStr;
    }
}
