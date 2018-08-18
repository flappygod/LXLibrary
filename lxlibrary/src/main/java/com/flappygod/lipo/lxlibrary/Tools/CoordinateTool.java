package com.flappygod.lipo.lxlibrary.Tools;


import java.text.DecimalFormat;

/************************
 * 百度坐标和火星坐标之间的转换
 *
 * @author yang
 *         version  1.0.0
 */
public class CoordinateTool {


    private final static double x_pi = 3.14159265358979324 * 3000.0 / 180.0;


    public static class LatLng {
        //经度
        public double lng;
        //纬度
        public double lat;

        /******
         * 构造器
         */
        public LatLng() {

        }

        public LatLng(double lat, double lng) {
            this.lat = lat;
            this.lng = lng;

        }
    }

    /*********************
     * 火星坐标转换为百度坐标
     *
     * @param ggLat 纬度
     * @param ggLon 经度
     * @return
     */
    public static LatLng marsToBd(double ggLat, double ggLon) {
        LatLng latlng = new LatLng();
        double x = ggLon, y = ggLat;
        double z = Math.sqrt(x * x + y * y) + 0.00002 * Math.sin(y * x_pi);
        double theta = Math.atan2(y, x) + 0.000003 * Math.cos(x * x_pi);
        DecimalFormat ft = new DecimalFormat("0.000000");
        latlng.lng = Double
                .parseDouble(ft.format(z * Math.cos(theta) + 0.0065));
        latlng.lat = Double.parseDouble(ft.format(z * Math.sin(theta) + 0.006));
        return latlng;
    }

    /*********************
     * 火星坐标转换为百度坐标
     *
     * @param ggLat 纬度
     * @param ggLon 经度
     * @return
     */
    public static LatLng marsToBd(String ggLat, String ggLon) {
        return marsToBd(Double.parseDouble(ggLat), Double.parseDouble(ggLon));
    }

    /************************
     * 百度坐标转换为google坐标，火星坐标
     *
     * @param bdLat 百度纬度
     * @param bdLon 百度经度
     * @return
     */
    public static LatLng bdToMars(double bdLat, double bdLon) {
        LatLng latlng = new LatLng();
        double x = bdLon - 0.0065, y = bdLat - 0.006;
        double z = Math.sqrt(x * x + y * y) - 0.00002 * Math.sin(y * x_pi);
        double theta = Math.atan2(y, x) - 0.000003 * Math.cos(x * x_pi);
        latlng.lng = z * Math.cos(theta);
        latlng.lat = z * Math.sin(theta);
        return latlng;
    }

    /************************
     * 百度坐标转换为google坐标，火星坐标
     *
     * @param bdLat 百度纬度
     * @param bdLon 百度经度
     * @return
     */
    public static LatLng bdToMars(String bdLat, String bdLon) {
        return bdToMars(Double.parseDouble(bdLat), Double.parseDouble(bdLon));
    }


}
