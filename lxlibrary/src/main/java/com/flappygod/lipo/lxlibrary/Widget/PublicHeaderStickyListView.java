package com.flappygod.lipo.lxlibrary.Widget;



import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ListAdapter;

public class PublicHeaderStickyListView extends ExpandableListView {

	private OnScrollListener myStickyListScroll;
	protected HeadViewInterface mAdapter;
	/** 显示在顶端的item */
	private View mHeaderView;
	private boolean mHeaderViewVisible;
	private int mHeaderViewWidth;
	protected int mHeaderViewHeight;

	public View getmHeaderView() {
		return mHeaderView;
	}

	public PublicHeaderStickyListView(Context context) {
		super(context);
	}

	public PublicHeaderStickyListView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public PublicHeaderStickyListView(Context context, AttributeSet attrs,
                                      int defStyle) {
		super(context, attrs, defStyle);
	}

	public interface HeadViewInterface {
		/************************
		 * 通过所有view的一个总的view position 来判断view是否属于顶部View
		 ************************/
		/*
		 * public boolean IsHeadView(int position);
		 *//************************
		 * 将总position 转换为 group position 这样方便确定顶部条目显示那一条的数据
		 ************************/
		/*
		 * public int PositionChange(int position);
		 */

		/**
		 * Pinned header state: don't show the header.
		 */
		public static final int PINNED_HEADER_GONE = 0;

		/**
		 * Pinned header state: show the header at the top of the list.
		 */
		public static final int PINNED_HEADER_VISIBLE = 1;
		/**
		 * Pinned header state: show the header. If the header extends beyond
		 * the bottom of the first shown element, push it up and clip.
		 */
		public static final int PINNED_HEADER_PUSHED_UP = 2;

		/**
		 * 用来得到分组标签状态 Computes the desired state of the pinned header for the
		 * given position of the first visible list item. Allowed return values
		 * are {@link #PINNED_HEADER_GONE}, {@link #PINNED_HEADER_VISIBLE} or
		 * {@link #PINNED_HEADER_PUSHED_UP}.
		 */
		int getPinnedHeaderState(int position);

		/**
		 * 用来设置分组标签的标题 Configures the pinned header view to match the first
		 * visible list item.
		 * 
		 * @param header
		 *            pinned header view.
		 * @param position
		 *            position of the first visible list item.
		 */
		void configurePinnedHeader(View header, int position);

	}
 /*****************************
  * 重写了设置滚动置顶的代码
 *****************************/
	public void setOnScrollListener(OnScrollListener li) {
		myStickyListScroll = li;
		super.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				myStickyListScroll.onScrollStateChanged(view, scrollState);
			}
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
								 int visibleItemCount, int totalItemCount) {
				if (view instanceof PublicHeaderStickyListView) {
					((PublicHeaderStickyListView) view)
							.configureHeaderView(firstVisibleItem);
				}
				myStickyListScroll.onScroll(view, firstVisibleItem,
						visibleItemCount, totalItemCount);
			}

		});
	}
	/*****************************
	  * 判断是否是headerview
	 *****************************/
	public boolean IsHeadView(int position) {
		if (position == 0) {
			return true;
		}
		int total = 0;
		for (int z = 0; z < this.getExpandableListAdapter().getGroupCount(); z++) {
			total++;
			for (int s = 0; s < this.getExpandableListAdapter()
					.getChildrenCount(z); s++) {
				total++;
			}
			if (total == position)
				return true;
		}
		return false;
	}
	/*****************************
	  * 重写了设置滚动置顶的代码
	 *****************************/
	public int PositionChange(int position) {
		int total = 0;
		int head = 0;
		for (int z = 0; z < this.getExpandableListAdapter().getGroupCount(); z++) {
			total++;
			for (int w = 0; w < this.getExpandableListAdapter()
					.getChildrenCount(z); w++) {
				total++;
			}
			if (position < total)
				return head;
			head++;
		}
		return 0;
	}

	@Override
	public void setAdapter(ListAdapter adapter) {
		super.setAdapter(adapter);
		mAdapter = (HeadViewInterface) adapter;
	}

	@Override
	public void setAdapter(ExpandableListAdapter adapter) {
		super.setAdapter(adapter);
		mAdapter = (HeadViewInterface) adapter;
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		if (mHeaderView != null
				&& ev.getY() < mHeaderViewHeight + mHeaderView.getTop()) {
			return true;
		}
		return super.onInterceptTouchEvent(ev);
	}

	/**********************************
	 * 设置headview
	 **********************************/
	public void setPinnedHeaderView(View view) {
		mHeaderView = view;
		if (mHeaderView != null) {
			// 设置边框渐变的长度
			setFadingEdgeLength(0);
		}
	}

	// 在这里获得headview的宽与高
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		// 得到mHeaderViewz的宽、高
		if (mHeaderView != null) {
			measureChild(mHeaderView, widthMeasureSpec, heightMeasureSpec);
			mHeaderViewWidth = mHeaderView.getMeasuredWidth();
			mHeaderViewHeight = mHeaderView.getMeasuredHeight();
		}
	}

	@Override
	protected void onLayout(boolean changed, int left, int top, int right,
			int bottom) {
		super.onLayout(changed, left, top, right, bottom);
		if (mHeaderView != null) {
			mHeaderView.layout(0, 0, mHeaderViewWidth, mHeaderViewHeight);
			configureHeaderView(getFirstVisiblePosition());
		}

	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		if (mHeaderView != null) {
			mHeaderView.layout(0, 0, mHeaderViewWidth, mHeaderViewHeight);
			configureHeaderView(getFirstVisiblePosition());
		}
	}

	public int getNowerheaderPosiotion() {
		int position = this.getFirstVisiblePosition();
		position = position - this.getHeaderViewsCount();
		return PositionChange(position);
	}

	/***************************************
	 * 控制上方draw view的位置
	 ***************************************/
	public void configureHeaderView(int position) {
		position = position - this.getHeaderViewsCount();
		if (position < 0) {
			mHeaderViewVisible = false;
			return;
		}
		if (mHeaderView == null) {
			return;
		}
		int state = mAdapter.getPinnedHeaderState(position);
		switch (state) {
		case HeadViewInterface.PINNED_HEADER_GONE: {
			break;
		}
		case HeadViewInterface.PINNED_HEADER_VISIBLE: {
			mAdapter.configurePinnedHeader(mHeaderView,
			/* mAdapter. */PositionChange(position));
			break;
		}
		case HeadViewInterface.PINNED_HEADER_PUSHED_UP: {
			View firstView = getChildAt(0);
			if (firstView == null)
				break;
			int bottom = firstView.getBottom();
			int headerHeight = mHeaderView.getHeight();
			int y = 0;
			if (bottom < headerHeight && /* mAdapter. */IsHeadView(position + 1)) {
				y = (bottom - headerHeight);
			} else {
				y = 0;
			}
			mAdapter.configurePinnedHeader(mHeaderView,
			/* mAdapter. */PositionChange(position));
			if (mHeaderView.getTop() != y) {
				mHeaderView.layout(0, y, mHeaderViewWidth, mHeaderViewHeight
						+ y);
			}
			mHeaderViewVisible = true;
			break;
		}
		}
	}

	@Override
	protected void dispatchDraw(Canvas canvas) {
		super.dispatchDraw(canvas);
		if (mHeaderViewVisible
				&& this.getExpandableListAdapter().getGroupCount() != 0) {
			drawChild(canvas, mHeaderView, getDrawingTime());
		}
	}
}
