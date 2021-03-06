package com.flappygod.lipo.lxlibrary.Widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.EditText;

import com.flappygod.lipo.lxlibrary.Tools.DensityTool;

import java.util.ArrayList;


/*************
 * 可以显示删除按钮的textView Package Name:com.example.testltextview <br/>
 * ClassName: CustomEditText <br/>
 * Function: TODO 功能说明 <br/>
 * date: 2015-8-11 下午3:38:59 <br/>
 *
 * @author lijunlin
 */
@SuppressLint("AppCompatCustomView")
public class CustomEditText extends EditText {

    //叉叉颜色
    private int CROSS_COLOR = 0xFFFFFFFF;
    //原型颜色
    private int CIRCLE_COLOR = 0x66000000;

    //圆圈的大小
    private int CROSS_RADIUS;

    //圆圈中x符号与圆角的距离  除以2为真实值
    private int CROSS_MARGINCIRCLE;

    //圆圈中x符号的宽度
    private int CROSS_WIDTH;

    //圆圈与文字的边距离
    private int CROSS_EDGE;

    //是否显示删除
    private boolean showDelete = true;

    //监听方便清理
    private ArrayList<TextWatcher> mListeners = null;

    public CustomEditText(Context context) {
        super(context);
        init(context);
    }

    public CustomEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CustomEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public CustomEditText(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    public boolean isShowDelete() {
        return showDelete;
    }

    public void setShowDelete(boolean showDelete) {
        this.showDelete = showDelete;
    }



    private boolean drawDeleteFlag;


    private void init(Context context) {
        CROSS_WIDTH = DensityTool.dip2px(context, 2);
        CROSS_MARGINCIRCLE = DensityTool.dip2px(context, 6);
        CROSS_EDGE = DensityTool.dip2px(context, 5);
        CROSS_RADIUS = DensityTool.dip2px(getContext(), 8);
        //初始化
        left = getPaddingLeft();
        right = getPaddingRight();
        top = getPaddingTop();
        bottom = getPaddingBottom();
        //清空
        clearTextChangedListeners();
        //添加
        addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                drawDeleteFlag = length() != 0;
                setPadding(left, top, right, bottom);
                requestLayout();
                invalidate();
            }
        });
    }

    private int left;
    private int top;
    private int right;
    private int bottom;

    //设置padding
    public void setPadding(int left, int top, int right, int bottom) {
        if (!showDelete) {
            super.setPadding(left, top, right, bottom);
            return;
        }
        this.left = left;
        this.right = right;
        this.top = top;
        this.bottom = bottom;
        if (drawDeleteFlag) {
            //当前的状态
            super.setPadding(left, top, right + CROSS_RADIUS * 2 + CROSS_EDGE, bottom);
        } else {
            //原本的状态
            super.setPadding(left, top, right, bottom);
        }
    }

    public void onDraw(Canvas canvas) {
        //绘制
        setPadding(left, top, right, bottom);
        super.onDraw(canvas);
        if (showDelete) {
            darwDeleteBtn(canvas);
        }
    }

    /*************
     * 绘制删除按钮
     * @param canvas
     */
    private void darwDeleteBtn(Canvas canvas) {

        if (!drawDeleteFlag)
            return;

        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG
                | Paint.FILTER_BITMAP_FLAG);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(CIRCLE_COLOR);
        int width = getWidth();
        int height = getHeight();
        Rect rect = new Rect();

        rect.left = getScrollX() + width - this.getPaddingRight() + CROSS_EDGE;
        rect.right = getScrollX() + width - this.getPaddingRight() + CROSS_RADIUS * 2 + CROSS_EDGE;
        rect.top = getScrollY() + height / 2 - CROSS_RADIUS;
        rect.bottom = getScrollY() + height / 2 + CROSS_RADIUS;

        canvas.save();
        canvas.drawCircle(rect.left + rect.width() / 2,
                rect.top + rect.height() / 2, CROSS_RADIUS, paint);
        canvas.translate(rect.left + CROSS_RADIUS, getScrollY() + height / 2);
        canvas.rotate(45);
        paint.setColor(CROSS_COLOR);
        Rect crossRect = new Rect(-CROSS_RADIUS + CROSS_MARGINCIRCLE / 2, -CROSS_WIDTH / 2, CROSS_RADIUS - CROSS_MARGINCIRCLE / 2, CROSS_WIDTH / 2);
        canvas.drawRect(crossRect, paint);
        canvas.rotate(90);
        canvas.drawRect(crossRect, paint);
        canvas.restore();


    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    /********************
     * touch事件，
     */
    public boolean onTouchEvent(MotionEvent event) {

        if (event.getAction() == MotionEvent.ACTION_UP && drawDeleteFlag) {
            int x = (int) event.getX();
            int y = (int) event.getY();
            boolean isInnerWidth = (x >= (getWidth() - getPaddingRight()) && x < this
                    .getWidth() - getPaddingRight() + CROSS_RADIUS * 2);
            boolean isInnerHeight = (y >= (getHeight() / 2 - CROSS_RADIUS) && y < (getHeight() / 2 + CROSS_RADIUS));

            if (isInnerWidth && isInnerHeight) {
                setText("");
                requestLayout();
                invalidate();
            }
        }
        return super.onTouchEvent(event);

    }

    @Override
    /*********************
     * 焦点切换的时候判断是否绘制
     */
    protected void onFocusChanged(boolean focused, int direction,
                                  Rect previouslyFocusedRect) {
        if (this.hasFocus() == true) {
            drawDeleteFlag = length() != 0;
        } else {
            drawDeleteFlag = false;
        }
        super.onFocusChanged(focused, direction, previouslyFocusedRect);
        setPadding(left, top, right, bottom);
        requestLayout();
        invalidate();
    }


    @Override
    public void addTextChangedListener(TextWatcher watcher) {
        if (mListeners == null) {
            mListeners = new ArrayList<TextWatcher>();
        }
        mListeners.add(watcher);

        super.addTextChangedListener(watcher);
    }

    @Override
    public void removeTextChangedListener(TextWatcher watcher) {
        if (mListeners != null) {
            int i = mListeners.indexOf(watcher);
            if (i >= 0) {
                mListeners.remove(i);
            }
        }
        super.removeTextChangedListener(watcher);
    }

    public void clearTextChangedListeners() {
        if (mListeners != null) {
            for (TextWatcher watcher : mListeners) {
                super.removeTextChangedListener(watcher);
            }
            mListeners.clear();
            mListeners = null;
        }
    }

}
