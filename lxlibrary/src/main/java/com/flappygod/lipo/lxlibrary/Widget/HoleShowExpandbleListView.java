package com.flappygod.lipo.lxlibrary.Widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ExpandableListView;

/**
 * Created by Administrator on 2017/4/12.
 */

public class HoleShowExpandbleListView extends ExpandableListView{

    public HoleShowExpandbleListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    /***********************
     * 设置不滚动
     ***********************/
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int mExpandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, mExpandSpec);
    }
}
