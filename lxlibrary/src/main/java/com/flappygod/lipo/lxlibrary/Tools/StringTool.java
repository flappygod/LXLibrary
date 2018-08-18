package com.flappygod.lipo.lxlibrary.Tools;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by yang on 2015/10/29.
 */
public class StringTool {

    /**********
     * 判断字符串是否为空的
     *
     * @param str 字符串
     * @return
     */
    public static boolean isEmpty(String str) {
        if (str == null || str.equals("null") || str.equals(""))
            return true;
        return false;
    }

    /**********
     * 判断字符串是否为空的或者为0
     *
     * @param str 字符串
     * @return
     */
    public static boolean isEmptyOrZero(String str) {
        if (str == null || str.equals("null") || str.equals("") || str.equals("0"))
            return true;
        return false;
    }


    /*****************
     * 转换为不为null的字符串
     *
     * @param str 字符串
     * @return
     */
    public static String ToNotNullStr(String str) {
        if (str == null || str.equals("null"))
            return "";
        return str;
    }

    /*****************
     * 去掉可能为空的情况，并用零代替空
     *
     * @param str 字符串
     * @return
     */
    public static String ToNotEmptyZeroStr(String str) {
        if (str == null || str.equals("null") || str.equals(""))
            return "0";
        return str;
    }


    /**************
     * 转换为不为空的字符串，如果为空，用defaultStr代替
     *
     * @param str        需要转换的字符串
     * @param defaultStr 默认的String
     * @return
     */
    public static String ToNotNullStrWithDefault(String str, String defaultStr) {
        if (str == null || str.equals("null") || str.equals(""))
            return defaultStr;
        return str;
    }

    /**************
     * 用于设置用户名不能为纯数字
     *
     * @return
     */

    public static boolean isNumeric(String str) {
        if (isEmpty(str)) {
            return false;
        }
        final int sz = str.length();
        for (int i = 0; i < sz; i++) {
            if (Character.isDigit(str.charAt(i)) == false) {
                return false;
            }
        }
        return true;
    }

    public static boolean isMobileNumber(String mobiles) {
        Pattern p = Pattern.compile("^((13[0-9])|(14[0-9])|(15[0-9])|(17[0-9])|(18[0-9])|(19[0-9]))\\d{8}$");
        Matcher m = p.matcher(mobiles);
        return m.matches();
    }

    /**
     * 判断邮箱是否合法
     * @param email
     * @return
     */
    public static boolean isEmail(String email) {
        String str = "\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*";
        Pattern p = Pattern.compile(str);
        Matcher m = p.matcher(email);
        return m.matches();
    }


    /*******
     * 得到一个字符串的长度,显示的长度,一个汉字或日韩文长度为2,英文字符长度为1
     * @param  s 需要得到长度的字符串
     * @return int 得到的字符串长度
     */
    public static int charLength(String s) {
        if (s == null)
            return 0;
        char[] c = s.toCharArray();
        int len = 0;
        for (int i = 0; i < c.length; i++) {
            len++;
            if (!isLetter(c[i])) {
                len++;
            }
        }
        return len;
    }

    public static boolean isLetter(char c) {
        int k = 0x80;
        return c / k == 0 ? true : false;
    }

    public static String getLimitString(String str,int length){
        if (str == null)
            return str;
        char[] c = str.toCharArray();
        int len = 0;
        for (int i = 0; i < c.length; i++) {
            len++;
            if (!isLetter(c[i])) {
                len++;
            }
            if(len>length&&i>1){
                String ret=str.substring(0, i);
                return ret;
            }
        }
        return str;
    }


}
