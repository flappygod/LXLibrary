package com.flappygod.lipo.lxlibrary.Tools;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.FileProvider;


import com.flappygod.lipo.lxlibrary.BuildConfig;

import java.io.File;
import java.io.IOException;

public class VersionTool {
    /***********************
     * 获取应用的版本号
     ***********************/
    public static String getVersionName(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            // getPackageName()是你当前类的包名，0代表是获取版本信息
            PackageInfo packInfo = packageManager.getPackageInfo(
                    context.getPackageName(), 0);
            String version = packInfo.versionName;
            return version;
        } catch (Exception e) {
            return "can't get appversion";
        }
    }

    /*******
     * 获取版本code
     *
     * @param context
     * @return
     * @throws Exception
     */
    public static int getVersionCode(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            // getPackageName()是你当前类的包名，0代表是获取版本信息
            PackageInfo packInfo = packageManager.getPackageInfo(
                    context.getPackageName(), 0);
            return packInfo.versionCode;
        } catch (Exception e) {
            return -1;
        }
    }

    /*******
     * 获取版本信息
     *
     * @param context
     * @return
     * @throws Exception
     */
    public static PackageInfo getVersion(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            // getPackageName()是你当前类的包名，0代表是获取版本信息
            PackageInfo packInfo = packageManager.getPackageInfo(
                    context.getPackageName(), 0);
            return packInfo;
        } catch (Exception e) {
            return new PackageInfo();
        }
    }

    /******************
     * 获取前面的String
     *
     * @param context
     * @return
     */
    public static String getSignStr(Context context) {
        try {
            StringBuilder builder = new StringBuilder();
            PackageManager manager = context.getPackageManager();
            /** 通过包管理器获得指定包名包含签名的包信息 **/
            PackageInfo packageInfo = manager.getPackageInfo(
                    context.getPackageName(), PackageManager.GET_SIGNATURES);
            /******* 通过返回的包信息获得签名数组 *******/
            Signature[] signatures = packageInfo.signatures;
            /******* 循环遍历签名数组拼接应用签名 *******/
            for (Signature signature : signatures) {
                builder.append(signature.toCharsString());
            }
            /************** 得到应用签名 **************/
            return MD5.MD5Encode(builder.toString());
        } catch (NameNotFoundException e) {
            return e.getMessage();
        }

    }

    public static boolean isPkgInstalled(Context context, String pkgName) {
        PackageInfo packageInfo = null;
        try {
            packageInfo = context.getPackageManager().getPackageInfo(pkgName, 0);
        } catch (NameNotFoundException e) {
            packageInfo = null;
            e.printStackTrace();
        }
        if (packageInfo == null) {
            return false;
        } else {
            return true;
        }
    }


    //弹出安装程序的对话框
    public static void gotoInstallApk(Context context, String authority, String filePath) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            try {
                String[] command = {"chmod", "777", filePath};
                ProcessBuilder builder = new ProcessBuilder(command);
                builder.start();
            } catch (IOException ignored) {

            }
            //假如是androidN以上
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                Uri contentUri = FileProvider.getUriForFile(context, authority, new File(filePath));
                intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
            } else {
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setDataAndType(Uri.parse("file://" + filePath), "application/vnd.android.package-archive");
            }
            context.startActivity(intent);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
