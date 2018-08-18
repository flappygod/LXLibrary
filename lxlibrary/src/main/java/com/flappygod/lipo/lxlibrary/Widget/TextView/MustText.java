package com.flappygod.lipo.lxlibrary.Widget.TextView;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.widget.TextView;


/**
 * Created by yang on 2016/11/14.
 */
public class MustText extends TextView {


    private boolean isMust;


    public boolean isMust() {
        return isMust;
    }

    public void setMust(boolean must) {
        isMust = must;
        invalidate();
    }

    public MustText(Context context) {
        super(context);
        isMust=true;
    }

    public MustText(Context context, AttributeSet attrs) {
        super(context, attrs);
        isMust=true;
    }

    public MustText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        isMust=true;
    }


    public void onDraw(Canvas canvas) {
        //假如是必须的
        if (isMust) {
            //设置包含must
            if (!getText().toString().endsWith("*")) {
                String s = getText().toString() + "*";
                SpannableString styledText = new SpannableString(s);
                styledText.setSpan(new ForegroundColorSpan(Color.RED),
                        styledText.length() - 1, styledText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                setText(styledText);
            }
        } else {
            //设置不包含must
            if (getText().toString().endsWith("*")) {
                String s = getText().toString().substring(0, getText().toString().length() - 1);
                setText(s);
            }
        }
        super.onDraw(canvas);
    }


}
