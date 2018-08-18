package com.flappygod.lipo.lxlibrary.Widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.TextView;

/***************************
 * 一直不停的跑马灯textview
 ***************************/
public class MarqueeText extends android.support.v7.widget.AppCompatTextView {
	public MarqueeText(Context con) {
		super(con);
	}

	public MarqueeText(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public MarqueeText(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
    public void startMarquee(){
    	this.invalidate();
    }
	@Override
	public boolean isFocused() {
		return true;
	}

	@Override
	protected void onFocusChanged(boolean focused, int direction,
			Rect previouslyFocusedRect) {
		super.onFocusChanged(focused,direction,previouslyFocusedRect);
	}
}
