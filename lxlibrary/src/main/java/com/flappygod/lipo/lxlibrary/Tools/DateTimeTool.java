package com.flappygod.lipo.lxlibrary.Tools;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by yang on 2016/8/12.
 * version  1.0.0
 */
public class DateTimeTool {

    /***************
     * 只有日期格式化
     *
     * @param date 日期
     * @return
     */
    public static String formatNorMalTimeStrOnlyDate(Date date) {
        //定义格式
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        //不为空进行处理
        if (date != null) {
            try {
                return df.format(date);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return df.format(new Date());
    }


    /***************
     * 只有日期的格式化
     *
     * @param str 日期字符串
     * @return
     */
    public static String formatNorMalTimeStrOnlyDate(String str) {
        //定义格式
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        //不为空进行处理
        if (str != null) {
            try {
                String ret = str;
                if (ret.contains(".")) {
                    ret = ret.split("\\.")[0];
                }
                ret = ret.replace("T", " ").replace("/", "-");
                Date date = df.parse(ret);
                return df.format(date);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return df.format(new Date());
    }


    /**************
     * 将时间格式化为字符串
     *
     * @param date 时间
     * @return
     */
    public static String formatNorMalTimeStr(Date date) {
        //定义格式
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //不为空进行处理
        if (date != null) {
            try {
                return df.format(date);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return df.format(new Date());
    }

    /************
     * 将字符串转为 yyyy-MM-dd HH:mm:ss样式
     * 将字符串时间格式化
     *
     * @param str 字符串
     * @return
     */
    public static String formatNorMalTimeStr(String str) {
        //定义格式
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //不为空进行处理
        if (str != null) {
            try {
                String ret = str;
                if (ret.contains(".")) {
                    ret = ret.split("\\.")[0];
                }
                ret = ret.replace("T", " ").replace("/", "-");
                Date date = df.parse(ret);
                return df.format(date);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return df.format(new Date());
    }


    /***************
     * 将字符串转为时间
     *
     * @param str 字符串
     * @return
     */
    public static Date formatNorMalTimeDate(String str) {
        //定义格式
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //不为空进行处理
        if (str != null) {
            try {
                String ret = str;
                //去掉后面的小数点
                if (ret.contains(".")) {
                    ret = ret.split("\\.")[0];
                }
                ret = ret.replace("T", " ").replace("/", "-");
                Date date = df.parse(ret);
                return date;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return new Date();
    }


    /*************
     * 获取两个时间之间的时间差字符串，
     *
     * @param startDate 开始时间
     * @param endDate   结束时间
     * @return
     */
    public static String getTwoDateIntervals(Date startDate, Date endDate, int accuracy) {
        int accuracyCount = accuracy;
        if (startDate == null || endDate == null) {
            return 0 + "分" + 0 + "秒";
        } else {
            long delay = new Date().getTime() - startDate.getTime();
            long day = delay / (1000 * 60 * 60 * 24);
            long shi = delay / (1000 * 60 * 60) % 24;
            long fen = delay / (1000 * 60) % 60;
            long miao = delay / 1000 % 60;

            String str = "";
            if (day != 0) {
                if (day > 2) {
                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                    return df.format(startDate);
                }
                accuracyCount--;
                if (accuracyCount >= 0)
                    str = str + day + "天";
            }
            if (day != 0 || shi != 0) {
                accuracyCount--;
                if (accuracyCount >= 0)
                    str = str + shi + "时";
            }
            if (day != 0 || shi != 0 || fen != 0) {
                accuracyCount--;
                if (accuracyCount >= 0)
                    str = str + fen + "分";
            }
            if (day != 0 || shi != 0 || fen != 0 || miao != 0) {
                accuracyCount--;
                if (accuracyCount >= 0)
                    str = str + miao + "秒";
            }
            return str + "前";
        }
    }


    /************************
     * 获取和当前时间的差值
     *
     * @param date 时间
     * @return
     */
    public static String getDateStringCompareNower(Date date) {
        Date nower = new Date();
        try {
            int between = daysBetween(date, nower);
            if (between == 0) {
                SimpleDateFormat ft = new SimpleDateFormat("今天 HH:mm");
                return ft.format(date);
            } else if (between == 1) {
                SimpleDateFormat ft = new SimpleDateFormat("昨天 HH:mm");
                return ft.format(date);
            } else if (between == 2) {
                SimpleDateFormat ft = new SimpleDateFormat("前天 HH:mm");
                return ft.format(date);
            } else {
                SimpleDateFormat ft = new SimpleDateFormat("MM月dd日 HH:mm");
                return ft.format(date);
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }


    /**
     * * 计算两个日期之间相差的天数
     * * @param smdate 较小的时间
     * * @param bdate  较大的时间
     * * @return 相差天数
     * * @throws ParseException
     */
    public static int daysBetween(Date smdate, Date bdate) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        smdate = sdf.parse(sdf.format(smdate));
        bdate = sdf.parse(sdf.format(bdate));
        Calendar cal = Calendar.getInstance();
        cal.setTime(smdate);
        long time1 = cal.getTimeInMillis();
        cal.setTime(bdate);
        long time2 = cal.getTimeInMillis();
        long between_days = (time2 - time1) / (1000 * 3600 * 24);
        return Integer.parseInt(String.valueOf(between_days));
    }


    /************************
     * 获取多少天之前的数据
     * @param days 天数
     * @return
     */
    public static Date getDateDaysAgo(int days) {
        //获取当前日期
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date today = new Date();
        //当前日期
        String endDate = sdf.format(today);
        //获取三十天前日期
        Calendar theCa = Calendar.getInstance();
        theCa.setTime(today);
        //最后一个数字30可改，30天的意思
        theCa.add(theCa.DATE, -days);
        //时间
        Date start = theCa.getTime();
        //三十天之前日期
        String startDate = sdf.format(start) + "00:00:00";
        //七天之前的时间
        return formatNorMalTimeDate(startDate);
    }

    /************************
     * 获取多少天之前的数据
     * @param days 天数
     * @return
     */
    public static String getDateDaysAgoStr(int days) {
        //获取当前日期
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date today = new Date();
        //当前日期
        String endDate = sdf.format(today);
        //获取三十天前日期
        Calendar theCa = Calendar.getInstance();
        theCa.setTime(today);
        //最后一个数字30可改，30天的意思
        theCa.add(theCa.DATE, -days);
        //时间
        Date start = theCa.getTime();
        //三十天之前日期
        String startDate = sdf.format(start) + "00:00:00";
        //七天之前的时间
        return startDate;
    }

}
