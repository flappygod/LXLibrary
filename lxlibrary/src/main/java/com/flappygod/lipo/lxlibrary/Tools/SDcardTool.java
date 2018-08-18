/*
 * Copyright 2013 The JA-SIG Collaborative. All rights reserved.
 * distributed with this file and available online at
 * http://www.etong.com/
 */
package com.flappygod.lipo.lxlibrary.Tools;

import android.content.Context;
import android.os.storage.StorageManager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;


/**
 * <p>Title: WriteSDcard</p>
 * <p>Package: com.etong.mall.utils</p>
 * <p>Description: TODO(这里用一句话描述这个类的作用)</p>
 *
 * @author:邵天元
 * @version:2015-5-19下午2:03:32
 * @since 1.0
 */
public class SDcardTool {


    /***************
     * 将String写入到文件中
     *
     * @param dirPath  文件的路径 以/结尾
     * @param fileName 文件名称
     * @param message  写入文件的内容
     */
    public static void writeFileSdcard(String dirPath, String fileName, String message) {

        try {
            CreateDirTool.createDir(dirPath);
            File file = new File(dirPath + fileName);
            if (!file.exists()) {
                file.createNewFile();
            }
            FileOutputStream fout = new FileOutputStream(file);
            byte[] bytes = message.getBytes();
            fout.write(bytes);
            fout.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /***************
     * 读取文件路径的文件字符串
     *
     * @param dirPath  文件的路径 以/结尾
     * @param fileName 文件名称
     */
    public static String readFileSdcard(String dirPath, String fileName) {
        try {
            File file = new File(dirPath + fileName);
            if (!file.exists()) {
                return null;
            }
            FileInputStream fin = new FileInputStream(file);
            //新建一个字节数组
            byte[] b = new byte[fin.available()];
            //将文件中的内容读取到字节数组中
            fin.read(b);
            fin.close();
            //再将字节数组中的内容转化成字符串形式输出
            String ret = new String(b);
            return ret;
        } catch (Exception e) {
            return null;
        }
    }


    public static class MyVolumeInfo {
        public String id;
        public String path;
        public String internalPath;
        public int type;
        public int state;

    }

    public static List<MyVolumeInfo> getMotedSdcard(Context context) {
        List<MyVolumeInfo> rets = new ArrayList<MyVolumeInfo>();

        try {
            StorageManager mStorageManager = (StorageManager) context.getSystemService(Context.STORAGE_SERVICE);
            //manager类
            Class clsStorageManager = Class.forName("android.os.storage.StorageManager");
            //反射getVolumes方法
            Method method = clsStorageManager.getDeclaredMethod("getVolumes");


            //VolumeInfo类
            Class clsVolumeInfo = Class.forName("android.os.storage.VolumeInfo");
            //获取disk方法
            Method methodGetDisk = clsVolumeInfo.getDeclaredMethod("getDisk");


            //DiskInfo
            Class clsDiskInfo = Class.forName("android.os.storage.DiskInfo");
            //获取disk方法
            Method methodIsSd = clsDiskInfo.getDeclaredMethod("isSd");

            //getVolumes
            Object array = method.invoke(mStorageManager);

            //取得
            Object sdcardVolume = null;

            List listOb = (List) array;
            for (int s = 0; s < listOb.size(); s++) {
                //获取到的单个VolumeInfo 对象
                Object volumeInfoObject = listOb.get(s);
                Object diskInfoObject = methodGetDisk.invoke(volumeInfoObject);

                /*Field[] fs = clsVolumeInfo.getDeclaredFields();
                for(int i = 0 ; i < fs.length; i++){
                    Field f = fs[i];
                    if(f.getName().equals("disk")) {
                        f.setAccessible(true); //设置些属性是可以访问的
                        Object val = f.get(volumeInfoObject);//得到此属性的值
                        sdcardVolume = val;
                    }
                }*/


                MyVolumeInfo info = new MyVolumeInfo();
                Field[] fs = clsVolumeInfo.getDeclaredFields();
                for (int i = 0; i < fs.length; i++) {
                    Field f = fs[i];
                    if (f.getName().equals("id")) {
                        f.setAccessible(true); //设置些属性是可以访问的
                        Object val = f.get(volumeInfoObject);//得到此属性的值
                        info.id = (String) val;
                    } else if (f.getName().equals("path")) {
                        f.setAccessible(true); //设置些属性是可以访问的
                        Object val = f.get(volumeInfoObject);//得到此属性的值
                        info.path = (String) val;
                    } else if (f.getName().equals("internalPath")) {
                        f.setAccessible(true); //设置些属性是可以访问的
                        Object val = f.get(volumeInfoObject);//得到此属性的值
                        info.internalPath = (String) val;
                    } else if (f.getName().equals("type")) {
                        f.setAccessible(true); //设置些属性是可以访问的
                        Object val = f.get(volumeInfoObject);//得到此属性的值
                        info.type = (int) val;
                    } else if (f.getName().equals("state")) {
                        f.setAccessible(true); //设置些属性是可以访问的
                        Object val = f.get(volumeInfoObject);//得到此属性的值
                        info.state = (int) val;
                    }

                }

                rets.add(info);
            }


            return rets;


        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }


        return null;
    }


}
