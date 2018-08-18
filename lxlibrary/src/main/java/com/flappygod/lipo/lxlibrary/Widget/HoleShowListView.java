package com.flappygod.lipo.lxlibrary.Widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/********
 * 
 * Package Name:com.holpe.maike.shoppingguide.widget <br/>
 * ClassName: HoleShowListView <br/> 
 * Function: TODO 整个显示的listview<br/> 
 * date: 2015-8-8 上午9:28:57 <br/> 
 * 
 * @author lijunlin
 */
public class HoleShowListView extends ListView {
	public HoleShowListView(Context context, AttributeSet attrs) {
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
