/*
 * Copyright 2013 The JA-SIG Collaborative. All rights reserved.
 * distributed with this file and available online at
 * http://www.etong.com/
 */
package com.flappygod.lipo.lxlibrary.Tools;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;

/**
 * @author:李俊霖
 * @version:2015-5-6下午3:49:13
 * @since 1.0
 * version  1.0.0
 */
public class FormatStrTool {

    /***********
     * 转换为int
     *
     * @param str 字符串
     * @return
     */
    public static int parseInt(String str) {
        if (str == null || str.equals("") || str.equals("null")) {
            return 0;
        }
        try {
            int retInt= (int) Float.parseFloat(str);
            return retInt;
        } catch (Exception e) {
            return 0;
        }
    }

    /***********
     * 转换为long
     *
     * @param str 字符串
     * @return
     */
    public static long parseLong(String str) {
        if (str == null || str.equals("") || str.equals("null")) {
            return 0;
        }
        try {
            return Long.parseLong(str);
        } catch (Exception e) {
            return 0;
        }
    }


    /********
     * 字符串转换为 BigDecimal
     *
     * @param str 字符串
     * @return
     */
    public static BigDecimal parseDecimal(String str) {
        try {

            return new BigDecimal(str);
        }catch (Exception ex){
            return new BigDecimal(0);
        }
    }


    /*********
     * 将字符串类型转换为float类型，默认保留两位小数
     *
     * @param str 被转换的字符串
     * @return
     */
    public static float strToFloat(String str) {
        return strToFloat(str, 2);
    }

    /*********
     * 将字符串类型转换为float类型，默认保留bit位小数
     *
     * @param str 被转换的字符串
     * @param bit 小数位
     * @return
     */
    public static float strToFloat(String str, int bit) {
        if (str == null || str.equals("") || str.equals("null")) {
            return 0;
        }
        try {
            float t = Float.parseFloat(str);
            String result = String.format("%." + bit + "f", t);
            return Float.parseFloat(result);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }


    /************
     * float转换为保留两位小数的字符串
     *
     * @param t float
     * @return
     */
    public static String floatToStr(float t) {
        DecimalFormat ft = new DecimalFormat("0.00");
        return ft.format(t);
    }

    /************
     * float转换为保留两位小数的字符串
     *
     * @param t   float
     * @param bit 位数
     * @return
     */
    public static String floatToStr(float t, int bit) {
        String str = "0.";
        for (int s = 0; s < bit; s++) {
            str = str + "0";
        }
        DecimalFormat ft = new DecimalFormat(str);
        return ft.format(t);
    }


    /*************
     * BigDecimal转换为字符串，默认四舍五入
     *
     * @param decimal decimal
     * @return
     */
    public static String decimalToStr(BigDecimal decimal) {
        if(decimal==null){
            return "0.00";
        }
        DecimalFormat myformat = new DecimalFormat("0.00");
        myformat.setRoundingMode(RoundingMode.HALF_UP);
        return myformat.format(decimal);
    }

    /*************
     * BigDecimal转换为字符串，默认四舍五入
     *
     * @param decimal decimal
     * @param bit     位数
     * @return
     */
    public static String decimalToStr(BigDecimal decimal, int bit) {
        String str = "0.";
        for (int s = 0; s < bit; s++) {
            str = str + "0";
        }
        DecimalFormat myformat = new DecimalFormat(str);
        myformat.setRoundingMode(RoundingMode.HALF_UP);
        return myformat.format(decimal);
    }

    /*************
     * 字符串转换为BigDecimal，默认保留两位
     *
     * @param str 字符串
     * @return
     */
    public static BigDecimal strToDecimal(String str) {
        return strToDecimal(str, 2);
    }


    /**************
     * 字符串转换为BigDecimal
     *
     * @param str 字符串
     * @param bit 位数
     * @return
     */
    public static BigDecimal strToDecimal(String str, int bit) {
        String bitStr = "0.";
        for (int s = 0; s < bit; s++) {
            bitStr = bitStr + "0";
        }
        DecimalFormat myformat = new DecimalFormat(bitStr);
        myformat.setRoundingMode(RoundingMode.HALF_UP);
        String ret = myformat.format(new BigDecimal(str));
        return new BigDecimal(ret);
    }


    /************
     * 将字符串转换为保留两位小数的字符串
     *
     * @param str
     * @return
     */
    public static String formatStrTwoBit(String str) {
        return formatStr(str, 2);
    }


    /************
     * 将字符串格式化,四舍五入
     *
     * @param str 字符串
     * @param bit 位数
     * @return
     */
    public static String formatStr(String str, int bit) {
        return formatStr(str, bit, RoundingMode.HALF_UP);
    }

    /*************
     * 将字符串格式化
     *
     * @param str  字符串
     * @param bit  小数位数
     * @param mode mode
     * @return
     */
    public static String formatStr(String str, int bit, RoundingMode mode) {
        try {
            String bitStr = "0.";
            for (int s = 0; s < bit; s++) {
                bitStr = bitStr + "0";
            }
            BigDecimal t = new BigDecimal(str);
            DecimalFormat ft = new DecimalFormat(bitStr);
            ft.setRoundingMode(mode);
            return ft.format(t);
        } catch (Exception e) {
            e.printStackTrace();
            return "0.00";
        }
    }


    /*************
     * 格式化BigDecimal,保留两位小数
     *
     * @param decimal decimal
     * @return
     */
    public static BigDecimal formatBigDecimalTwoBit(BigDecimal decimal) {
        return formatBigDecimal(decimal, 2);
    }

    /*************
     * 格式化BigDecimal
     *
     * @param decimal decimal
     * @param bit     小数位数
     * @return
     */
    public static BigDecimal formatBigDecimal(BigDecimal decimal, int bit) {
        return formatBigDecimal(decimal, bit, RoundingMode.HALF_UP);
    }

    /**************
     * 格式化BigDecimal
     *
     * @param decimal decimal
     * @param bit     小数位数
     * @param mode    模式
     * @return
     */
    public static BigDecimal formatBigDecimal(BigDecimal decimal, int bit, RoundingMode mode) {
        String bitStr = "0.";
        for (int s = 0; s < bit; s++) {
            bitStr = bitStr + "0";
        }
        DecimalFormat myformat = new DecimalFormat(bitStr);
        myformat.setRoundingMode(mode);
        String str = myformat.format(decimal);
        return new BigDecimal(str);
    }

    /**********
     * 将float类型的String转换成为保留两位小数的String
     *
     * @param str
     * @return
     */
    public static String formatTwoBitStr(String str) {
        try {
            float t = Float.parseFloat(str);
            DecimalFormat ft = new DecimalFormat("0.00");
            return ft.format(t);
        } catch (Exception e) {
            e.printStackTrace();
            return "0.00";
        }
    }

}
