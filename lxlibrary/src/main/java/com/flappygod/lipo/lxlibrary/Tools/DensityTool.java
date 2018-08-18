package com.flappygod.lipo.lxlibrary.Tools;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;



/********
 * 密度装换工具
 * @author 李俊霖
 * version  1.0.0
 *
 */
public class DensityTool {

	/**
	 * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
	 */
	public static int dip2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	/**
	 * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
	 */
	public static int px2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}

	public static void getAndroiodScreenProperty(Context context){
		WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics dm = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(dm);
		int width = dm.widthPixels;// 屏幕宽度（像素）
		int height= dm.heightPixels; // 屏幕高度（像素）
		float density = dm.density;//屏幕密度（0.75 / 1.0 / 1.5）
		int densityDpi = dm.densityDpi;//屏幕密度dpi（120 / 160 / 240）
		//屏幕宽度算法:屏幕宽度（像素）/屏幕密度
		int screenWidth = (int) (width/density);//屏幕宽度(dp)
		int screenHeight = (int)(height/density);//屏幕高度(dp)
	}


	public static void  generateDpw(String dirPath,float  scale){
		String str="<resources>\n";
		for(int s=0;s<1080;s++){
			str+="<dimen name=\"d_"+s+"\">"+(int)(s*scale)+"dp</dimen>\n";
		}
		for(int s=0;s<1080;s++){
			str+="<dimen name=\"s_"+s+"\">"+(int)(s*scale)+"sp</dimen>\n";
		}
		str+="</resources>";
		SDcardTool.writeFileSdcard(dirPath,"dimens.xml",str);
	}

}
