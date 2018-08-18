package com.flappygod.lipo.lxlibrary.Tools;

import android.util.Base64;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * Created by yang on 2015/10/31.
 */
public class SerializableTool {

    /*****************
     * 序列换为字符串
     * @param object  序列化对象
     */
    public static String serializeToString(Serializable object) {
        // 序列化使用的输出流
        ObjectOutputStream OOS = null;
        // 序列化后数据流给ByteArrayOutputStream 来保存。
        // ByteArrayOutputStream 可转成字符串或字节数组
        ByteArrayOutputStream BAOS = new ByteArrayOutputStream();
        // ByteArrayInputStream 可接收一个字节数组 "byte[] "。供反序列化做参数
        ByteArrayInputStream BAIS = null;
        // 反序列化使用的输入流
        ObjectInputStream OIS = null;
        try {
            // byte[] myb= "s ";
            OOS = new ObjectOutputStream(BAOS);
            OOS.writeObject(object);
            byte[] abc = BAOS.toByteArray();
            String StrMySerializer= Base64.encodeToString(abc, Base64.DEFAULT);
            OOS.close();
            return StrMySerializer;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /********************
     * 获取序列化保存的字符串
     * @param str  字符串
     * @return
     */
    public static Serializable antiStringToObject(String str) {
        // 序列化使用的输出流
        ObjectOutputStream OOS = null;
        // 序列化后数据流给ByteArrayOutputStream 来保存。
        // ByteArrayOutputStream 可转成字符串或字节数组
        ByteArrayOutputStream BAOS = new ByteArrayOutputStream();
        // ByteArrayInputStream 可接收一个字节数组 "byte[] "。供反序列化做参数
        ByteArrayInputStream BAIS = null;
        // 反序列化使用的输入流
        ObjectInputStream OIS = null;
        try {
            //反序列化
            byte[] ddd = Base64.decode(str, Base64.DEFAULT);
            BAIS = new ByteArrayInputStream(ddd);
            OIS = new ObjectInputStream(BAIS);
            Serializable c = null;
            c = (Serializable) (OIS.readObject());
            OIS.close();
            return c;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


}
