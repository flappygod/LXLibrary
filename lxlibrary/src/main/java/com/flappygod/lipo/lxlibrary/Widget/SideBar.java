package com.flappygod.lipo.lxlibrary.Widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ListView;
import android.widget.SectionIndexer;

import com.flappygod.lipo.lxlibrary.Tools.DensityTool;


/******************
 * sideBar
 */
public class SideBar extends View {
    //字符串
    private char[] l;
    //section选择
    private SectionIndexer sectionIndexter = null;
    //列表
    private ListView list;
    //绘制的每个item的高度
    private int m_nItemHeight = 29;

    //paint
    private Paint paint;

    //弹起时候的颜色
    public static int ON_COLOR = 0x22f8f8f8;
    //按下时候的颜色
    public static int PRESS_COLOR = 0x11000000;
    //文字的颜色
    public static int TEXT_COLOR = 0xFF888888;

    public SideBar(Context context) {
        super(context);
        init();
    }

    public SideBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SideBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }


    public interface SideBarListener {
        //开始
        void sideBegan();

        //开始改变
        void sideChanging(String text);

        //结束
        void sideEnd();
    }

    public SideBarListener listener;

    public void setSideBarListener(SideBarListener listener) {
        this.listener = listener;
    }



    /**********
     * 初始化
     */
    private void init() {
        //初始化字符
        l = new char[]{'*', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S',
                'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};

        //初始化paint
        paint = new Paint();
        paint.setColor(TEXT_COLOR);
        paint.setTextSize(DensityTool.dip2px(getContext(), 12));
        paint.setTextAlign(Paint.Align.CENTER);

        //设置背景色
        setBackgroundColor(ON_COLOR);
    }

    /************
     * 设置文字的颜色
     *
     * @param textColor 文字颜色
     */
    public void setTextColor(int textColor) {
        Paint mem = new Paint();
        mem.setColor(textColor);
        mem.setTextSize(DensityTool.dip2px(getContext(), 12));
        mem.setTextAlign(Paint.Align.CENTER);
        paint = mem;
    }

    /************
     * 设置当前的indexs
     *
     * @param indexs indexs
     */
    public void setCharArray(char[] indexs) {
        this.l = indexs;
        invalidate();
    }


    /**************
     * 设置listview
     *
     * @param list           列表
     * @param sectionIndexer section选择器
     */
    public void setListView(ListView list, SectionIndexer sectionIndexer) {
        this.list = list;
        this.sectionIndexter = sectionIndexer;
    }

    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);

        if (sectionIndexter == null) {
            return true;
        }

        int i = (int) event.getY();
        int idx = i / m_nItemHeight;
        if (idx >= l.length) {
            idx = l.length - 1;
        } else if (idx < 0) {
            idx = 0;
        }
        //按下事件
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (listener != null) {
                listener.sideBegan();
                if (l.length > 0)
                    listener.sideChanging(String.valueOf(l[idx]));
            }
        }
        if (event.getAction() == MotionEvent.ACTION_DOWN || event.getAction() == MotionEvent.ACTION_MOVE) {
            //设置按下的颜色
            setBackgroundColor(PRESS_COLOR);
            int position = sectionIndexter.getPositionForSection(l[idx]);
            if (position == -1) {
                return true;
            }
            list.setSelection(position);
            if (listener != null) {
                listener.sideChanging(String.valueOf(l[idx]));
            }
        } else if (event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL) {
            //设置弹起的颜色
            setBackgroundColor(ON_COLOR);
            if (listener != null) {
                listener.sideEnd();
            }
        }
        return true;
    }

    //绘制
    protected void onDraw(Canvas canvas) {
        int oneHeight = getHeight() / 28;
        if (m_nItemHeight != oneHeight) {
            m_nItemHeight = oneHeight;
        }
        float widthCenter = getMeasuredWidth() / 2;
        for (int i = 0; i < l.length; i++) {
            canvas.drawText(String.valueOf(l[i]), widthCenter, m_nItemHeight + (i * m_nItemHeight), paint);
        }
        super.onDraw(canvas);
    }
}
