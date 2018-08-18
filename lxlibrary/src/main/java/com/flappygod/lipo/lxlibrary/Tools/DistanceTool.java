package com.flappygod.lipo.lxlibrary.Tools;

/**********
 * 计算经纬度的距离
 * 
 * @author yang
 * version  1.0.0
 */
public class DistanceTool {

	/********
	 * 计算两个经纬度之间的距离
	 * 
	 * @param lat1
	 *            第一点纬度
	 * @param lng1
	 *            第一点经度
	 * @param lat2
	 *            第二点纬度
	 * @param lng2
	 *            第二点经度
	 * @return
	 */
	public static double distanceByLngLat(double lat1, double lng1,
			double lat2, double lng2) {
		double radLat1 = lat1 * Math.PI / 180;
		double radLat2 = lat2 * Math.PI / 180;
		double a = radLat1 - radLat2;
		double b = lng1 * Math.PI / 180 - lng2 * Math.PI / 180;
		double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2)
				+ Math.cos(radLat1) * Math.cos(radLat2)
				* Math.pow(Math.sin(b / 2), 2)));
		/****
		 *  取WGS84标准参考椭球中的地球长半径(单位:m)
		 */
		s = s * 6378137.0;
		s = Math.round(s * 10000) / 10000;
		return s;
	}
}
