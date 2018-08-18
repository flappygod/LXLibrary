package com.flappygod.lipo.lxlibrary.Widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

/*****************
 * 自动换行的线性布局
 * Package Name:com.holpe.maike.salesplus.widget <br/>
 * ClassName: NewLineLinearyLayout <br/>
 * Function: TODO 功能说明 <br/>
 * date: 2015-9-22 上午8:45:08 <br/>
 *
 * @author lijunlin
 */
public class NewLineLinearyLayout extends LinearLayout {

    public NewLineLinearyLayout(Context context) {
        super(context);
        this.setOrientation(LinearLayout.HORIZONTAL);
    }

    public NewLineLinearyLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setOrientation(LinearLayout.HORIZONTAL);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);

        /*** 最大是当前值的时候或者固定值，默认进行换行 ***/
        if (widthMode == MeasureSpec.AT_MOST
                || widthMode == MeasureSpec.EXACTLY) {

            int maxHeight = 0;// 最大高度
            int maxtopmargin = 0;// 最大顶部距离
            int maxbuttommargin = 0;// 最大底部距离
            int row = 1; // 默认只有一行
            int width = 0; // 宽度计算

            for (int index = 0; index < getChildCount(); index++) {
                final View child = getChildAt(index);
                child.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
                int cheight = child.getMeasuredHeight();
                int cwidth = child.getMeasuredWidth();

                LayoutParams lp = (LayoutParams) child
                        .getLayoutParams();
                int topmargin = lp.topMargin;
                int buttonmargin = lp.bottomMargin;
                int leftmargin = lp.leftMargin;
                int rightmargin = lp.rightMargin;
                // 拿到最大的height
                if (maxHeight < cheight) {
                    maxHeight = cheight;


                }
                // 拿到最大的topmargin
                if (maxtopmargin < topmargin) {
                    maxtopmargin = topmargin;
                }
                // 拿到最大的buttommargin
                if (maxbuttommargin < buttonmargin) {
                    maxbuttommargin = buttonmargin;
                }
                width = width + cwidth + leftmargin + rightmargin;
                if (width > this.getMeasuredWidth() - this.getPaddingLeft()
                        - this.getPaddingRight()) {
                    row++;
                    width = cwidth + leftmargin + rightmargin;
                }
            }
            setMeasuredDimension(
                    this.getMeasuredWidth(),
                    row * (maxHeight + maxtopmargin + maxbuttommargin)
                            + this.getPaddingTop() + this.getPaddingBottom());
        }
    }

    @Override
    protected void onLayout(boolean arg0, int arg1, int arg2, int arg3, int arg4) {

        int maxHeight = 0;// 最大高度
        int maxtopmargin = 0;// 最大顶部距离
        int maxbuttommargin = 0;// 最大底部距离
        int row = 1; // 默认只有一行
        int width = 0; // 宽度计算
        for (int index = 0; index < getChildCount(); index++) {
            final View child = getChildAt(index);
            int cheight = child.getMeasuredHeight();
            LayoutParams lp = (LayoutParams) child
                    .getLayoutParams();
            int topmargin = lp.topMargin;
            int buttonmargin = lp.bottomMargin;
            // 拿到最大的height
            if (maxHeight < cheight) {
                maxHeight = cheight;
            }
            // 拿到最大的topmargin
            if (maxtopmargin < topmargin) {
                maxtopmargin = topmargin;
            }
            // 拿到最大的buttommargin
            if (maxbuttommargin < buttonmargin) {
                maxbuttommargin = buttonmargin;
            }
        }

        for (int index = 0; index < getChildCount(); index++) {
            final View child = getChildAt(index);
            child.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);

            int cwidth = child.getMeasuredWidth();
            LayoutParams lp = (LayoutParams) child
                    .getLayoutParams();
            int leftmargin = lp.leftMargin;
            int rightmargin = lp.rightMargin;
            width = width + cwidth + leftmargin + rightmargin;
            if (width > this.getMeasuredWidth() - this.getPaddingLeft()
                    - this.getPaddingRight()) {
                row++;
                width = cwidth + leftmargin + rightmargin;
            }
            child.layout(this.getPaddingLeft() + width - cwidth - rightmargin,
                    this.getPaddingTop() + (row - 1)
                            * (maxHeight + maxtopmargin + maxbuttommargin)
                            + maxtopmargin, this.getPaddingLeft() + width
                            - rightmargin, this.getPaddingTop() + row
                            * (maxHeight + maxtopmargin + maxbuttommargin)
                            - maxbuttommargin);
        }
    }

}