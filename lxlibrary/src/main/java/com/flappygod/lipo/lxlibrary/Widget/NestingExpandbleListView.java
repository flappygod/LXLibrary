package com.flappygod.lipo.lxlibrary.Widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ExpandableListView;

/**
 * Created by Administrator on 2017/3/31.
 */

public class NestingExpandbleListView extends ExpandableListView {

    private float lastX;
    private float lastY;

    public NestingExpandbleListView(Context context) {
        super(context);
    }

    public NestingExpandbleListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NestingExpandbleListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public boolean onInterceptTouchEvent(MotionEvent ev) {
        boolean result = false;
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:

                lastX = ev.getX();
                lastY = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                int distanceX =(int) Math.abs( ev.getX() - lastX);
                int distanceY = (int) Math.abs(ev.getY()-lastY);
                if(distanceY>distanceX && distanceX>2){
                    result = true;
                }else{
                    result = false;
                }
                break;
            default:
                break;
        }
        return result;
    }

}
