package com.flappygod.lipo.lxlibrary.Widget.MaterialDesign;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

public class CustomView extends RelativeLayout {

    //diable的颜色
    private int disabledBackgroundColor = Color.parseColor("#F0F0F0");
    //之前的颜色
    private int formerBackgroundColor = Color.parseColor("#FFFFFF");

    public CustomView(Context context) {
        super(context);
    }

    public CustomView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setBackgroundColor(int color) {
        //设置颜色
        super.setBackgroundColor(color);
        //保存设置的颜色
        formerBackgroundColor = color;
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        if (enabled)
            super.setBackgroundColor(formerBackgroundColor);
        else
            super.setBackgroundColor(disabledBackgroundColor);
    }
}
