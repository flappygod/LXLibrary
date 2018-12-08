package com.flappygod.lipo.lxlibrary.Tools;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;

import java.io.File;

/**
 * Created by macbookair on 17/5/5.
 */

public class MapJumpTool {


    /**
     * 启动高德App进行导航
     *
     * @param appname     必填 第三方调用应用名称。如 amap
     * @param destination 非必填 POI 名称
     * @param lat         必填 纬度
     * @param lon         必填 经度
     * @param dev         必填 是否偏移(0:lat 和 lon 是已经加密后的,不需要国测加密; 1:需要国测加密)
     * @param style       必填 导航方式(0 速度快; 1 费用少; 2 路程短; 3 不走高速；4 躲避拥堵；5 不走高速且避免收费；6 不走高速且躲避拥堵；7 躲避收费和拥堵；8 不走高速躲避收费和拥堵))
     */
    public static void goToNaviAmapActivity(Context context,
                                            String appname,
                                            String destination,
                                            String lat,
                                            String lon,
                                            String dev,
                                            String style) {

        if (isInstallByRead("com.autonavi.minimap")) {
            try {
                StringBuffer stringBuffer = new StringBuffer("androidamap://navi?sourceApplication=")
                        .append(appname);
                if (!TextUtils.isEmpty(destination)) {
                    stringBuffer.append("&poiname=").append(destination);
                }
                stringBuffer.append("&lat=").append(lat)
                        .append("&lon=").append(lon)
                        .append("&dev=").append(dev)
                        .append("&style=").append(style);

                Intent intent = new Intent("android.intent.action.VIEW", android.net.Uri.parse(stringBuffer.toString()));
                intent.setPackage("com.autonavi.minimap");
                context.startActivity(intent);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 启动高德App进行路径规划
     *
     * @param context  上下文
     * @param sAddress 起点地址
     * @param slat     起点地址
     * @param slon     起点地址
     * @param dlat     终点地址
     * @param dlon     终点地址
     * @param dAddress 终点地址
     * @param dev      必填 是否偏移(0:lat 和 lon 是已经加密后的,不需要国测加密; 1:需要国测加密)
     * @param style    必填 导航方式(0 速度快; 1 费用少; 2 路程短; 3 不走高速；4 躲避拥堵；5 不走高速且避免收费；6 不走高速且躲避拥堵；7 躲避收费和拥堵；8 不走高速躲避收费和拥堵))
     */
    public static void goToWayAmapActivity(Context context,
                                           String slat,
                                           String slon,
                                           String sAddress,
                                           String dlat,
                                           String dlon,
                                           String dAddress,
                                           String dev,
                                           String style) {

        if (isInstallByRead("com.autonavi.minimap")) {
            try {
                Uri uri = Uri.parse("amapuri://route/plan/?sid=BGVIS1&slat=" + slat + "&slon=" + slon + "&sname=" + sAddress +
                        "&did=BGVIS2&dlat=" + dlat + "&dlon=" + dlon + "&dname=" + dAddress + "&dev=" + dev + "&t=" + style);
                Intent intent = new Intent("android.intent.action.VIEW", uri);
                intent.setPackage("com.autonavi.minimap");
                context.startActivity(intent);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    /********
     * 前往百度地图
     * @param context   上下文
     * @param appname   应用名称
     * @param destination  目的地名称
     * @param lat   维度
     * @param lng   经度
     */
    public static void goToNaviBaiduActivity(Context context, String appname, String destination, String lat, String lng) {
        if (isInstallByRead("com.baidu.BaiduMap")) {
            try {
                Intent intent = Intent.getIntent("intent://map/direction?" +
                        //起点此处不传值默认选择当前位置"origin=latlng:"+"34.264642646862,108.95108518068&" +
                        //终点
                        "destination=latlng:"
                        + lat
                        + ","
                        + lng
                        + "|name:"
                        + destination
                        //导航路线方式
                        + "&mode=driving&"
                        + "region="
                        + "&src="
                        + appname
                        + "#Intent;scheme=bdapp;package=com.baidu.BaiduMap;end");
                context.startActivity(intent); //启动调用
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    /**********
     * 前往百度地图的路径规划
     * @param context    上下文
     * @param slat       起点地址
     * @param slon       起点地址
     * @param sAddress   起点地址
     * @param dlat       终点地址
     * @param dlon       终点地址
     * @param dAddress   终点地址
     */
    public static void goToWayBaiduActivity(Context context,
                                            String slat,
                                            String slon,
                                            String sAddress,
                                            String dlat,
                                            String dlon,
                                            String dAddress) {

        if (isInstallByRead("com.baidu.BaiduMap")) {
            try {
                String uri = String.format("baidumap://map/direction?origin=%s,%s&destination=" +
                                "%s,%s&mode=driving&src=com.flappygod.lipo", slat, slon,
                        dlat, dlon);
                Intent intent = new Intent();
                intent.setData(Uri.parse(uri));
                context.startActivity(intent);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }


    /********
     * 根据包名检测某个APP是否安装
     * @param packageName 包名
     * @return true 安装 false 没有安装
     */
    public static boolean isInstallByRead(String packageName) {
        return new File("/data/data/" + packageName).exists();
    }
}
