package com.flappygod.lipo.lxlibrary.BaseView.PopupView;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.PopupWindow;

/**
 * Created by yang on 2017/3/9.
 */
public class BasepopupWindow extends PopupWindow {

    public BasepopupWindow(Context context) {
        super(context);
    }

    public BasepopupWindow(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BasepopupWindow(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public BasepopupWindow(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public BasepopupWindow(View contentView) {
        super(contentView);
    }

    public BasepopupWindow(int width, int height) {
        super(width, height);
    }

    public BasepopupWindow(View contentView, int width, int height) {
        super(contentView, width, height);
    }

    public BasepopupWindow(View contentView, int width, int height, boolean focusable) {
        super(contentView, width, height, focusable);
    }

    public void showAsDropDown(View anchor, int xoff, int yoff,int gravity) {
        if (Build.VERSION.SDK_INT != 24) {
            super.showAsDropDown(anchor, xoff, yoff,gravity);
        }else{
            // 适配 android 7.0
            int[] location = new int[2];
            anchor.getLocationOnScreen(location);//re_hotel_address是一个view，表示显示在她的上方
            int x = location[0]+xoff;
            int y = location[1]+yoff;
            super.showAtLocation(anchor, Gravity.NO_GRAVITY, x,y + anchor.getHeight());
        }
    }

    public void showAsDropDown(View anchor) {

        if (Build.VERSION.SDK_INT != 24) {
            super.showAsDropDown(anchor, 0, 0);
        } else {
            // 适配 android 7.0
            int[] location = new int[2];
            anchor.getLocationOnScreen(location);//re_hotel_address是一个view，表示显示在她的上方
            int x = location[0];
            int y = location[1];
            super.showAtLocation(anchor, Gravity.NO_GRAVITY, 0,y + anchor.getHeight());
        }
    }
}
