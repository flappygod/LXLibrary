package com.flappygod.lipo.lxlibrary.Tools;

import android.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/**********************************
 * Created by lijunlin on 2016/8/23.
 * <p>
 * version  1.0.0
 */
public class AESTool {

    /******************
     * 加密
     *
     * @param sSrc 加密字符串
     * @param sKey 秘钥
     * @return
     * @throws Exception
     */
    public static String Encrypt(String sSrc, String sKey) throws Exception {
        if (sKey == null) {
            throw new RuntimeException("Key为空null");
        }
        if (sKey.length() != 16) {
            throw new RuntimeException("Key长度需要是16位");
        }
        //keybyte
        byte[] raw = sKey.getBytes("utf-8");
        //AES加密
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        //"算法/模式/补码方式"
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        //加密
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
        //加密
        byte[] encrypted = cipher.doFinal(sSrc.getBytes("utf-8"));
        //此处使用BASE64做转码功能，同时能起到2次加密的作用。
        return Base64.encodeToString(encrypted, Base64.DEFAULT);
    }

    /***************
     * 解密
     *
     * @param sSrc 解密字符串
     * @param sKey 秘钥
     * @return
     * @throws Exception
     */
    public static String Decrypt(String sSrc, String sKey) throws Exception {
        if (sKey == null) {
            throw new RuntimeException("Key为空null");
        }
        if (sKey.length() != 16) {
            throw new RuntimeException("Key长度需要是16位");
        }
        //bytes
        byte[] raw = sKey.getBytes("utf-8");
        //AES加密
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        //"算法/模式/补码方式"
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        //解密方式
        cipher.init(Cipher.DECRYPT_MODE, skeySpec);
        //先使用Base64
        byte[] encrypted1 = Base64.decode(sSrc, Base64.DEFAULT);
        //解密
        byte[] original = cipher.doFinal(encrypted1);
        //输出字符串
        String originalString = new String(original, "utf-8");
        //返回字符串
        return originalString;
    }
}
