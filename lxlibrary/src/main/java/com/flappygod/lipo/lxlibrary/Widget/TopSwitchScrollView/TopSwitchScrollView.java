package com.flappygod.lipo.lxlibrary.Widget.TopSwitchScrollView;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

/****************************************
 * 顶部带有滑标并伴有动画的scrollview
 * Created by yang on 2017/3/13.
 */
public class TopSwitchScrollView extends HorizontalScrollView {

    //设置监听
    public interface OnTopPositionChangedListener {
        void positionChanged(View view, int position);
    }

    /**************
     * 设置页卡切换的监听
     *
     * @param listener 监听
     */
    public void setOnTopPositionChangedListener(OnTopPositionChangedListener listener) {
        this.mOnTopPositionChangedListener = listener;
    }

    private int defaultDuration=250;

    private boolean autoSelected = true;

    private OnTopPositionChangedListener mOnTopPositionChangedListener;
    //适配器
    private TopSwithScrollAdapter mTopSwithScrollAdapter;
    //内层装那个view的layout
    private LinearLayout innerLayout;
    //最外层的layout
    private LinearLayout outerLayout;
    //游标
    private LinearLayout cursorView;

    private int position = 0;

    private int defaultCursorMargin = 0;

    private int lastHeight;
    private int lastWidth;

    public boolean isAutoSelected() {
        return autoSelected;
    }

    public void setAutoSelected(boolean autoSelected) {
        this.autoSelected = autoSelected;
    }

    public TopSwitchScrollView(Context context) {
        super(context);
        this.setHorizontalScrollBarEnabled(false);
    }

    public TopSwitchScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setHorizontalScrollBarEnabled(false);
    }

    public TopSwitchScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }



    /*******
     * 适配器
     *
     * @param adapter
     */
    public void setAdapter(TopSwithScrollAdapter adapter) {
        if (adapter == null) {
            //移除所有的view
            this.removeAllViews();
            mTopSwithScrollAdapter=null;
            return;
        } else {
            mTopSwithScrollAdapter = adapter;
            initAllViews();
        }
    }


    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        //设置layout
        super.onLayout(changed, l, t, r, b);
        //假如外部布局被改变了
        if (lastWidth != getWidth() || lastHeight != getHeight()) {
            lastHeight = getHeight();
            lastWidth = getWidth();
            //设置当前的适配器
            setAdapter(mTopSwithScrollAdapter);
        }
    }

    private void initAllViews() {
        //加入
        if (getMeasuredHeight() == 0) {
            return;
        }
        //移除所有的view
        this.removeAllViews();
        //创建外部layout
        outerLayout = new LinearLayout(getContext());
        //创建内部layout
        innerLayout = new LinearLayout(getContext());
        //垂直居中
        innerLayout.setGravity(Gravity.CENTER_VERTICAL);
        innerLayout.setOrientation(LinearLayout.HORIZONTAL);
        //创建滑标view
        cursorView = new LinearLayout(getContext());
        //垂直的
        outerLayout.setOrientation(LinearLayout.VERTICAL);
        //加入view的装载layout
        LinearLayout.LayoutParams lpone = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, getMeasuredHeight() - defaultCursorMargin * 2 - mTopSwithScrollAdapter.getScrollCursorHeight());
        outerLayout.addView(innerLayout, lpone);

        //加入滑标
        LinearLayout.LayoutParams lptwo = new LinearLayout.LayoutParams(mTopSwithScrollAdapter.getScrollCursorWidth(), mTopSwithScrollAdapter.getScrollCursorHeight());
        lptwo.topMargin = defaultCursorMargin;
        lptwo.bottomMargin = defaultCursorMargin;
        lptwo.leftMargin = 0;
        cursorView.setBackgroundColor(mTopSwithScrollAdapter.getScrollCursorColor());
        outerLayout.addView(cursorView, lptwo);

        //添加所有的View
        for (int s = 0; s < mTopSwithScrollAdapter.getViewCount(); s++) {
            //通过适配器获取view
            View view = mTopSwithScrollAdapter.getViewAtPosition(s);
            //假如适配器设置了宽度，就把宽度设置给这个item
            if (mTopSwithScrollAdapter.getViewWidthAtPosition(s) != -1) {
                //设置LayoutParams
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(mTopSwithScrollAdapter.getViewWidthAtPosition(s), ViewGroup.LayoutParams.WRAP_CONTENT);
                view.setLayoutParams(lp);
            }
            innerLayout.addView(view);
        }

        //当前的view添加这个View
        LinearLayout.LayoutParams lpthree = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
        this.addView(outerLayout, lpthree);

        //重新绘制
        outerLayout.requestLayout();
        outerLayout.invalidate();

        outerLayout.addOnLayoutChangeListener(new OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                setScrollToPosition(position,0);
                outerLayout.removeOnLayoutChangeListener(this);
            }
        });


    }


    /**********
     * 获取某个position的View
     *
     * @param position
     * @return
     */
    public View getViewAtPosition(int position) {
        if (position > innerLayout.getChildCount()) {
            return null;
        } else {
            return innerLayout.getChildAt(position);
        }
    }


    public void setScrollToPosition(int position){
        setScrollToPosition(position,defaultDuration);
    }


    /*************
     * 设置滑动到某个item
     *
     * @param position item编号
     */
    public void setScrollToPosition(int position,int duration) {
        this.position = position;

        if(innerLayout==null){
            return;
        }
        if (innerLayout.getWidth() <= 0) {
            return;
        }
        if (innerLayout.getChildCount() <= 0) {
            return;
        }
        if (innerLayout.getChildAt(0).getWidth() <= 0) {
            return;
        }
        if (position > innerLayout.getChildCount()) {
            position = innerLayout.getChildCount();
        }
        //中点
        int center = 0;
        for (int s = 0; s <= position; s++) {
            if (s == position) {
                center = center + innerLayout.getChildAt(s).getWidth() / 2;
            } else {
                center = center + innerLayout.getChildAt(s).getWidth();
            }

        }
        //总的宽度
        int holeWidth = innerLayout.getWidth();
        //容器的宽度
        int containerWidth = this.getWidth();
        //总体的宽度
        if (holeWidth > containerWidth) {
            //设置
            int offset = center - containerWidth / 2;

            if(duration!=0) {
                //滚动到指定位置
                this.smoothScrollTo(offset, 0);
            }else{
                this.scrollTo(offset,0);
            }
        }
        //滑标
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) cursorView.getLayoutParams();
        //之前的margin
        int formerlLeftMargin = layoutParams.leftMargin;
        //新的margin
        int newlLeftMargin = center - mTopSwithScrollAdapter.getScrollCursorWidth() / 2;
        layoutParams.leftMargin = newlLeftMargin;
        cursorView.setLayoutParams(layoutParams);

        if(duration!=0) {
            TranslateAnimation animation = new TranslateAnimation(formerlLeftMargin - newlLeftMargin, 0, 0, 0);
            animation.setDuration(duration);
            cursorView.startAnimation(animation);
        }
        //通知监听
        if (mOnTopPositionChangedListener != null) {
            mOnTopPositionChangedListener.positionChanged(innerLayout.getChildAt(position), position);
        }
        //通知适配器
        if (mTopSwithScrollAdapter != null) {
            mTopSwithScrollAdapter.positionChanged(innerLayout.getChildAt(position), position);
        }
        if (autoSelected) {
            for(int s=0;s<innerLayout.getChildCount();s++){
                innerLayout.getChildAt(s).setSelected(false);
            }
            innerLayout.getChildAt(position).setSelected(true);
        }
    }
}
