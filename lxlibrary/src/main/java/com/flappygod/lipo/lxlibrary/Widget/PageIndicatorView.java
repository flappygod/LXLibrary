package com.flappygod.lipo.lxlibrary.Widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

public class PageIndicatorView extends View {

	private int UN_SELECTED_COLOR = 0xFFFFFFFF;

	private int SELECTED_COLOR = 0xFFFF0000;

	private int radius = 10;

	private int space = 12;


	public int getUN_SELECTED_COLOR() {
		return UN_SELECTED_COLOR;
	}

	public void setUN_SELECTED_COLOR(int UN_SELECTED_COLOR) {
		this.UN_SELECTED_COLOR = UN_SELECTED_COLOR;
	}

	public int getSELECTED_COLOR() {
		return SELECTED_COLOR;
	}

	public void setSELECTED_COLOR(int SELECTED_COLOR) {
		this.SELECTED_COLOR = SELECTED_COLOR;
	}

	public PageIndicatorView(Context context) {
		super(context);
	}

	public PageIndicatorView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public PageIndicatorView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	/********
	 * 设置两点的间隔
	 * @param px
	 */
	public void setSpace(int px) {
		space = px;
		invalidate();
	}

	/************
	 * 设置点的半径
	 * @param radius
	 */
	public void setRadius(int radius) {

		this.radius = radius;
		invalidate();
	}

	private int mCurrentPage = -1;
	private int mTotalPage = 0;

	public void setTotalPage(int nPageNum) {
		mTotalPage = nPageNum;
		if (mCurrentPage >= mTotalPage)
			mCurrentPage = mTotalPage - 1;
		this.invalidate();
	}

	public int getCurrentPage() {
		return mCurrentPage;
	}

	public void setCurrentPage(int nPageIndex) {
		if (nPageIndex < 0 || nPageIndex >= mTotalPage)
			return;

		if (mCurrentPage != nPageIndex) {
			mCurrentPage = nPageIndex;
			this.invalidate();
		}
	}

	@SuppressLint("DrawAllocation")
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG
				| Paint.FILTER_BITMAP_FLAG);
		paint.setStyle(Paint.Style.FILL);
		paint.setColor(Color.BLACK);

		Rect r = new Rect();
		this.getDrawingRect(r);

		int iconWidth = radius * 2;
		int iconHeight = radius * 2;

		int x = (r.width() - (iconWidth * mTotalPage + space * (mTotalPage - 1))) / 2;
		int y = (r.height() - iconHeight) / 2;

		for (int i = 0; i < mTotalPage; i++) {

			paint.setColor(UN_SELECTED_COLOR);

			if (i == mCurrentPage) {
				paint.setColor(SELECTED_COLOR);
			}

			Rect r1 = new Rect();
			r1.left = x;
			r1.top = y;
			r1.right = x + iconWidth;
			r1.bottom = y + iconHeight;

			canvas.drawCircle(r1.left + radius, r1.top + radius, radius, paint);

			x += iconWidth + space;

		}

	}

	/*public void DrawImage(Canvas canvas, Bitmap mBitmap, int x, int y, int w,
			int h, int bx, int by) {
		Rect src = new Rect();
		Rect dst = new Rect();
		src.left = bx;
		src.top = by;
		src.right = bx + w;
		src.bottom = by + h;

		dst.left = x;
		dst.top = y;
		dst.right = x + w;
		dst.bottom = y + h;

		// canvas.drawBitmap(mBitmap, src, dst, mPaint);
		src = null;
		dst = null;
	}*/

}
