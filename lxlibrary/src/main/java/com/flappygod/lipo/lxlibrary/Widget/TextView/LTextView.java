/*
 * Copyright 2013 The JA-SIG Collaborative. All rights reserved.
 * distributed with this file and available online at
 * http://www.etong.com/
 */
package com.flappygod.lipo.lxlibrary.Widget.TextView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.Spanned;
import android.util.AttributeSet;
import android.widget.TextView;

/******************
 * @author:李俊霖
 * 
 * @version:2015-6-5上午10:59:33
 * 
 * @since 1.0 经过排版的TextView 对其的
 */
@SuppressLint("AppCompatCustomView")
public class LTextView extends TextView {

	private int lineSpace = 0;// 列间距
	private int rowSpace = 0;// 行间距

	public LTextView(Context context) {
		super(context);
	}

	public LTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public LTextView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	/***********
	 * 设置列间距 px
	 * 
	 * @param space
	 */
	public void setLineSpace(int space) {
		lineSpace = space;
		requestLayout();
		invalidate();
	}

	/****************
	 * 设置行间距 px
	 * 
	 * @param space
	 */
	public void setrowSpace(int space) {
		rowSpace = space;
		requestLayout();
		invalidate();
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);

		// 如果说使用了html样式的文字，就不管它，直接走super
		if (this.getText() instanceof Spanned) {
			return;
		}

		int heightMode = MeasureSpec.getMode(heightMeasureSpec);
		int specSizeHeight = MeasureSpec.getSize(heightMeasureSpec);

		int widthMode = MeasureSpec.getMode(widthMeasureSpec);
		int specSizeWidth = MeasureSpec.getSize(widthMeasureSpec);
		/*** 不做任何限制的时候 ***/
		if (widthMode == MeasureSpec.UNSPECIFIED) {
			/*** 获取paint，设置当前的值 ****/
			Paint paint = this.getPaint();
			paint.setTextSize(this.getTextSize());
			int mwidth = this.getPaddingLeft()
					+ this.getPaddingRight()
					+ LTextViewLayout.getLayoutWidth(lineSpace, rowSpace, this
							.getText().toString(), paint);
			setMeasuredDimension(mwidth, this.getMeasuredHeight());
		}
		/*** 最多是当前大小的时候***/
		if (widthMode == MeasureSpec.AT_MOST) {
			/*** 获取paint，设置当前的值 ****/
			Paint paint = this.getPaint();
			paint.setTextSize(this.getTextSize());
			int mwidth = this.getPaddingLeft()
					+ this.getPaddingRight()
					+ LTextViewLayout.getLayoutWidth(lineSpace, rowSpace, this
							.getText().toString(), paint);

			mwidth = Math.min(mwidth, specSizeWidth);
			setMeasuredDimension(mwidth, this.getMeasuredHeight());
		}

		if (heightMode == MeasureSpec.UNSPECIFIED) {
			/*** 获取paint，设置当前的值 ****/
			Paint paint = this.getPaint();
			paint.setTextSize(this.getTextSize());
			int mheight = this.getPaddingTop()
					+ this.getPaddingBottom()
					+ LTextViewLayout.getLayoutHeight(lineSpace, rowSpace, this
							.getText().toString(), paint,
							this.getMeasuredWidth() - this.getPaddingRight()
									- this.getPaddingLeft());

			setMeasuredDimension(this.getMeasuredWidth(), mheight);
		}
		if (heightMode == MeasureSpec.AT_MOST) {
			/*** 获取paint，设置当前的值 ****/
			Paint paint = this.getPaint();
			paint.setTextSize(this.getTextSize());
			int mheight = this.getPaddingTop()
					+ this.getPaddingBottom()
					+ LTextViewLayout.getLayoutHeight(lineSpace, rowSpace, this
							.getText().toString(), paint,
							this.getMeasuredWidth() - this.getPaddingRight()
									- this.getPaddingLeft());
			mheight = Math.min(mheight, specSizeHeight);
			setMeasuredDimension(this.getMeasuredWidth(), mheight);
		}

	}

	@Override
	protected void onDraw(Canvas canvas) {
		// 如果说使用了html样式的文字，就不管它，直接走super
		if (this.getText() instanceof Spanned) {
			super.onDraw(canvas);
			return;
		}

		/*** 获取paint，设置当前的值 ****/
		Paint paint = this.getPaint();
		paint.setTextSize(this.getTextSize());
		paint.setColor(this.getTextColors().getDefaultColor());
		LTextViewLayout.stringLayout(canvas, lineSpace, rowSpace, this
				.getText().toString(), this.getPaddingLeft(), this
				.getPaddingTop(),
				this.getRight() - this.getLeft() - this.getPaddingRight(),
				this.getBottom() - this.getTop() - this.getPaddingBottom(),
				this.getPaint(), 0, true);
	}

}
