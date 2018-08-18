package com.flappygod.lipo.lxlibrary.Tools;

import java.util.Random;

public class RandomTool {

    /**********
     * 获取指定长度的Int数据
     *
     * @param length 长度
     * @return
     */
    public static String getRandomIntString(int length) {
        String base = "0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }

    /***************
     * 获取指定长度的随机字符串
     *
     * @param length 长度
     * @return
     */
    public static String getRandomString(int length) {
        //length表示生成字符串的长度
        String base = "abcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }

}
