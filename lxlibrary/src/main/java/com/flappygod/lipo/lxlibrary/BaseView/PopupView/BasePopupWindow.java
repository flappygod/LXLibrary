package com.flappygod.lipo.lxlibrary.BaseView.PopupView;

import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.PopupWindow;

/**
 * Created by yang on 2017/3/9.
 */
public class BasePopupWindow extends PopupWindow {

    public BasePopupWindow(Context context) {
        super(context);
    }

    public BasePopupWindow(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BasePopupWindow(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public BasePopupWindow(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public BasePopupWindow(View contentView) {
        super(contentView);
    }

    public BasePopupWindow(int width, int height) {
        super(width, height);
    }

    public BasePopupWindow(View contentView, int width, int height) {
        super(contentView, width, height);
    }

    public BasePopupWindow(View contentView, int width, int height, boolean focusable) {
        super(contentView, width, height, focusable);
    }


    public void showAsDropDown(View anchor) {
        if (Build.VERSION.SDK_INT < 24) {
            super.showAsDropDown(anchor);
        } else {
            // 适配 android 7.0
            if(anchor.getContext()!=null) {
                Rect rect = new Rect();
                anchor.getGlobalVisibleRect(rect);
                int h = anchor.getResources().getDisplayMetrics().heightPixels - rect.bottom ;
                setHeight(h);
            }
            super.showAsDropDown(anchor);
        }
    }
}
