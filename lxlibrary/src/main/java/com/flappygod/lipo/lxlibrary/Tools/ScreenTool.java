package com.flappygod.lipo.lxlibrary.Tools;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;

public class ScreenTool {

    /************
     * 获取屏幕宽度
     *
     * @return
     */
    public static int getScreenWidth(Activity context) {
        DisplayMetrics dm = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm.widthPixels;
    }
    /**************
     * 获取屏幕高度
     *
     * @return
     */
    public static int getScreenHeight(Activity context) {
        DisplayMetrics dm = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm.heightPixels;
    }
}
