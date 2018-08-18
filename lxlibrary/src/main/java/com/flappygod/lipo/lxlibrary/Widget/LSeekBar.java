package com.flappygod.lipo.lxlibrary.Widget;

import android.content.Context;
import android.support.v7.widget.AppCompatSeekBar;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.SeekBar;

public class LSeekBar extends AppCompatSeekBar {

	private boolean  isSeeking;
	//最大值
	private int maxValue;
	//最小值
	private int minValue;
    //我的value的监听
	private ValueSelectListener listener;
	//用户的seek
	private OnSeekBarChangeListener userListener;

	public LSeekBar(Context context) {
		super(context);
		initListener();
	}

	public LSeekBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		initListener();
	}

	public LSeekBar(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		initListener();
	}

	public boolean isSeeking() {
		return isSeeking;
	}

	public void setSeeking(boolean seeking) {
		isSeeking = seeking;
	}

	public boolean onTouchEvent(MotionEvent event){

		switch (event.getAction()){
			case MotionEvent.ACTION_DOWN:
				isSeeking=true;
				break;
			case MotionEvent.ACTION_UP:
			case MotionEvent.ACTION_CANCEL:
				isSeeking=false;
				break;
		}


		return super.onTouchEvent(event);
	}




	private void initListener() {
		super.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			@Override
			public void onProgressChanged(SeekBar arg0, int arg1, boolean arg2) {
				if (userListener != null) {
					userListener.onProgressChanged(arg0, arg1, arg2);
				}
				if (listener != null) {

					int value = (int) (minValue + (maxValue - minValue) * ((float)arg1/getMax()));
					listener.onValueChanged(value);
				}
			}

			@Override
			public void onStartTrackingTouch(SeekBar arg0) {
				if (userListener != null) {
					userListener.onStartTrackingTouch(arg0);
				}
				if (listener != null) {

				}
			}

			@Override
			public void onStopTrackingTouch(SeekBar arg0) {
				if (userListener != null) {
					userListener.onStopTrackingTouch(arg0);
				}
				if (listener != null) {

				}
			}

		});
	}

	public int getMaxValue() {
		return maxValue;
	}

	public void setMaxValue(int maxValue) {
		this.maxValue = maxValue;
	}

	public int getMinValue() {
		return minValue;
	}

	public void setMinValue(int minValue) {
		this.minValue = minValue;
	}

	public void setSeekValue(int value) {
		if (value > maxValue || value < minValue)
			return;
		if (maxValue <= minValue)
			return;
		float progress = ((float)(value - minValue)) / (maxValue - minValue);
		this.setProgress((int) (progress*getMax()));
	}
	
	public int  getSeekValue(){

		return  (int) (minValue + (maxValue - minValue) * ((float)this.getProgress()/this.getMax()));
	}

	public void setOnSeekBarChangeListener(OnSeekBarChangeListener li) {
		userListener = li;
	}

	public void setValueSelectListener(ValueSelectListener li) {
		listener = li;
	}

	public interface ValueSelectListener {
		void onValueChanged(int value);
	}

}
