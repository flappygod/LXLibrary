package com.flappygod.lipo.lxlibrary.Widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.GridView;
import android.widget.HorizontalScrollView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Scroller;

/****************
 * 下拉之后显示隐藏的的layout
 *
 * @author lijunlin
 */
public class PullShowLayout extends RelativeLayout {

    //启动的偏移像素
    public static int startPX = 3;

    //拉动多少个点显示
    private float pullShowOffest = 0;
    //拉动多少个点关闭
    private float pullHideOffset = 0;


    //获取滚动多少个像素显示隐藏的view
    public float getPullShowOffest() {
        return pullShowOffest;
    }

    //设置滚动多少个像素显示隐藏的view
    public void setPullShowOffest(float pullShowOffest) {
        this.pullShowOffest = pullShowOffest;
    }

    //获取滚动多少个像素关闭隐藏的view
    public float getPullHideOffset() {
        return pullHideOffset;
    }

    //设置滚动多少个像素关闭隐藏的view
    public void setPullHideOffset(float pullHideOffset) {
        this.pullHideOffset = pullHideOffset;
    }

    // 当前显示的状态
    public enum ViewShowState {
        // view已经被显示出来了
        VIEW_ONSHOW(0),
        // view还没有被显示出来
        VIEW_ISHIDE(1);

        final int code;

        ViewShowState(int t) {
            code = t;
        }
    }

    // 添加的方向
    public enum AddDerection {
        // 左边
        ADD_LEFT(0),
        // 右边
        ADD_RIGHT(1),
        // 顶部
        ADD_TOP(2),
        // 底部
        ADD_BOTTOM(3);

        final int code;

        AddDerection(int t) {
            code = t;
        }
    }

    // 顶部被Hide的view
    private ViewGroup hideView;

    // 滚动helper
    private Scroller mScroller;

    // ontouch事件的拦截
    private EventRecord touchEvRecord;

    // 监听
    private OnStateChangeListener listener;

    // 顶部view的显示的状态
    private ViewShowState topViewstate = ViewShowState.VIEW_ISHIDE;

    // 添加的位置
    private AddDerection nowerDerection;

    class EventRecord {
        // touch事件按下时的X坐标
        float downX;
        // touch事件按下时的Y坐标
        float downY;
        // touch事件最近的X坐标
        float lastX;
        // touch事件最近的Y坐标
        float lastY;
        // touch事件最近移动的X坐标
        float lastXdistance;
        // touch事件最近移动的Y坐标
        float lastYdistance;
        // 开始时候的滑动值
        float startScrollY;

        // 重置
        void reSet() {
            downX = 0;
            downY = 0;
            lastX = 0;
            lastXdistance = 0;
            lastY = 0;
            lastYdistance = 0;
            startScrollY = 0;
        }
    }

    public interface OnStateChangeListener {
        // 开始被拉
        void startPull();

        // 正在拉
        void pulling(int position);

        // 被拉起来
        void pulledUp();

        // 结束后复原
        void pulledHide();
    }

    //获取当前hideView的显示状态，是隐藏还是显示
    public ViewShowState getTopViewstate() {
        return topViewstate;
    }


    /************
     * 设置监听
     *
     * @param listener 监听
     */
    public void setOnStateChangeListener(OnStateChangeListener listener) {
        this.listener = listener;
    }

    public PullShowLayout(Context context) {
        super(context);
        init(context);
    }

    public PullShowLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public PullShowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public PullShowLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    //初始化
    private void init(Context context) {
        //初始化按下的记录
        touchEvRecord = new EventRecord();
        //初始化scroller滚动
        mScroller = new Scroller(context);
    }

    /**************
     * 设置顶部被hide的View
     *
     * @param view      添加的view
     * @param derection 添加的方向
     */
    public void setHideView(ViewGroup view, AddDerection derection) {
        if (derection == AddDerection.ADD_TOP
                || derection == AddDerection.ADD_BOTTOM) {
            setHideView(view,
                    derection,
                    LayoutParams.MATCH_PARENT,
                    LayoutParams.WRAP_CONTENT);
        } else if (derection == AddDerection.ADD_LEFT
                || derection == AddDerection.ADD_RIGHT) {

            setHideView(view,
                    derection,
                    LayoutParams.WRAP_CONTENT,
                    LayoutParams.MATCH_PARENT);
        }
    }

    /**************
     * 设置顶部被hide的View
     *
     * @param view      添加的view
     * @param derection 添加的方向
     * @param width     宽度
     * @param height    高度
     */
    public void setHideView(ViewGroup view, AddDerection derection, int width, int height) {
        // 如果顶部View已经存在，就把它移除
        if (this.hideView != null) {
            this.removeView(hideView);
        }
        //设置hide的位置
        this.nowerDerection = derection;
        // 保存当前的view
        this.hideView = view;
        // 设置View的layoutParams
        // 假如是垂直方向
        if (derection == AddDerection.ADD_TOP
                || derection == AddDerection.ADD_BOTTOM) {
            ViewGroup.LayoutParams params;
            if (hideView.getLayoutParams() != null) {
                params = hideView.getLayoutParams();
                params.width = width;
                params.height = height;
            } else {
                params = new ViewGroup.LayoutParams(width,
                        height);
            }
            hideView.setLayoutParams(params);
        }
        // 假如是水平方向
        else if (derection == AddDerection.ADD_LEFT
                || derection == AddDerection.ADD_RIGHT) {
            ViewGroup.LayoutParams params;
            if (hideView.getLayoutParams() != null) {
                params = hideView.getLayoutParams();
                params.width = width;
                params.height = height;
            } else {
                params = new ViewGroup.LayoutParams(width,
                        height);
            }
            hideView.setLayoutParams(params);
        }
        // 重新进行布局绘制
        requestLayout();
        invalidate();

    }

    /***************
     * 添加顶部View
     *
     * @param widthMeasureSpec  widthMeasureSpec
     * @param heightMeasureSpec heightMeasureSpec
     */
    private void addHideView(int widthMeasureSpec, int heightMeasureSpec) {
        if (hideView != null && hideView.getParent() == null) {
            // 添加到顶部去
            addView(hideView);
            //计算child
            measureChild(hideView, widthMeasureSpec, heightMeasureSpec);
            //获取getLayoutParams
            LayoutParams params = (LayoutParams) hideView.getLayoutParams();
            // 顶部添加
            if (nowerDerection == AddDerection.ADD_TOP) {
                // 设置下顶部距离
                params.topMargin = -hideView.getMeasuredHeight();
                hideView.setLayoutParams(params);
            }
            // 底部添加
            else if (nowerDerection == AddDerection.ADD_BOTTOM) {
                params.addRule(ALIGN_PARENT_BOTTOM, TRUE);
                params.bottomMargin = -hideView.getMeasuredHeight();
                params.height = hideView.getMeasuredHeight();
                hideView.setLayoutParams(params);
            }
            // 左边添加
            else if (nowerDerection == AddDerection.ADD_LEFT) {
                params.leftMargin = -hideView.getMeasuredWidth();
                hideView.setLayoutParams(params);
            }
            // 右边添加
            else if (nowerDerection == AddDerection.ADD_RIGHT) {
                params.addRule(ALIGN_PARENT_RIGHT, TRUE);
                params.rightMargin = -hideView.getMeasuredWidth();
                params.width = hideView.getMeasuredWidth();
                hideView.setLayoutParams(params);
            }
        }
    }

    /**************
     * 大小Mesure
     */
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (!isInEditMode()) {
            addHideView(widthMeasureSpec, heightMeasureSpec);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    /**************************
     * mov动作，通过movY的0.3比例来滑动
     **************************/
    private void doMovement(EventRecord record) {

        if (nowerDerection == AddDerection.ADD_TOP) {
            // 不能让它向上拉动
            if (-mScroller.getFinalY() + record.lastYdistance < 0) {
                return;
            }
            // 进行滑动
            smoothScrollBy(0, (int) record.lastYdistance);
        } else if (nowerDerection == AddDerection.ADD_BOTTOM) {
            // 不能让它向上拉动
            if (-mScroller.getFinalY() + record.lastYdistance > 0) {
                return;
            }
            // 进行滑动
            smoothScrollBy(0, (int) record.lastYdistance);
        } else if (nowerDerection == AddDerection.ADD_LEFT) {
            // 不能让它向上拉动
            if (-mScroller.getFinalX() + record.lastXdistance < 0) {
                return;
            }
            // 进行滑动
            smoothScrollBy((int) record.lastXdistance, 0);
        } else if (nowerDerection == AddDerection.ADD_RIGHT) {
            // 不能让它向上拉动
            if (-mScroller.getFinalX() + record.lastXdistance > 0) {
                return;
            }
            // 进行滑动
            smoothScrollBy((int) record.lastXdistance, 0);
        }

    }

    /************
     * 按键抬起
     */
    private void doTouchUp() {
        //拉动多少个像素开启
        if (pullShowOffest == 0) {
            pullShowOffest = hideView.getHeight();
        }

        //拉动多少个像素关闭
        if (pullHideOffset == 0) {
            pullHideOffset = hideView.getHeight();
        }

        //如果是顶部添加
        if (nowerDerection == AddDerection.ADD_TOP) {

            //假如是hide状态
            if (topViewstate == ViewShowState.VIEW_ISHIDE) {
                if (-mScroller.getCurrY() >= pullShowOffest) {
                    // 打开顶部
                    openPull();
                } else {
                    // 回复原地
                    finishPull();
                }
            }
            //假如是显示状态
            else if (topViewstate == ViewShowState.VIEW_ONSHOW) {
                if (-mScroller.getCurrY() <= hideView.getHeight() - pullHideOffset) {
                    // 打开顶部
                    finishPull();
                } else {
                    // 回复原地
                    openPull();
                }
            }
        }
        //如果是底部添加
        else if (nowerDerection == AddDerection.ADD_BOTTOM) {

            //假如是hide状态
            if (topViewstate == ViewShowState.VIEW_ISHIDE) {
                if (-mScroller.getCurrY() <= -pullShowOffest) {
                    // 打开顶部
                    openPull();
                } else {
                    // 回复原地
                    finishPull();
                }
            }
            //假如是显示状态
            else if (topViewstate == ViewShowState.VIEW_ONSHOW) {
                if (-mScroller.getCurrY() >= -(hideView.getHeight() - pullHideOffset)) {
                    // 打开顶部
                    finishPull();
                } else {
                    // 回复原地
                    openPull();
                }
            }
        }

        //如果是左方添加
        else if (nowerDerection == AddDerection.ADD_LEFT) {

            if (topViewstate == ViewShowState.VIEW_ISHIDE) {
                if (-mScroller.getCurrX() >= pullShowOffest) {
                    // 打开顶部
                    openPull();
                } else {
                    // 回复原地
                    finishPull();
                }
            } else if (topViewstate == ViewShowState.VIEW_ONSHOW) {
                if (-mScroller.getCurrX() <= (hideView.getWidth() - pullHideOffset)) {
                    // 打开顶部
                    finishPull();
                } else {
                    // 回复原地
                    openPull();
                }
            }
        }

        //如果是右方添加
        else if (nowerDerection == AddDerection.ADD_RIGHT) {

            if (topViewstate == ViewShowState.VIEW_ISHIDE) {
                if (-mScroller.getCurrX() <= -pullShowOffest) {
                    // 打开顶部
                    openPull();
                } else {
                    // 回复原地
                    finishPull();
                }
            } else if (topViewstate == ViewShowState.VIEW_ONSHOW) {
                if (-mScroller.getCurrX() >= -(hideView.getWidth() - pullHideOffset)) {
                    // 打开顶部
                    finishPull();
                } else {
                    // 回复原地
                    openPull();
                }

            }
        }
    }

    /***********
     * 打开
     */
    public void openPull() {

        if (nowerDerection == AddDerection.ADD_TOP) {
            smoothScrollTo(0, hideView.getHeight());
        } else if (nowerDerection == AddDerection.ADD_BOTTOM) {
            smoothScrollTo(0, -hideView.getHeight());
        } else if (nowerDerection == AddDerection.ADD_LEFT) {
            smoothScrollTo(hideView.getWidth(), 0);
        } else if (nowerDerection == AddDerection.ADD_RIGHT) {
            smoothScrollTo(-hideView.getWidth(), 0);
        }

        //设置为打开状态
        topViewstate = ViewShowState.VIEW_ONSHOW;
        if (listener != null) {
            listener.pulledUp();
        }

    }

    /***********
     * 结束
     */
    public void finishPull() {
        // 回复原地
        smoothScrollTo(0, 0);
        //设置为关闭状态
        topViewstate = ViewShowState.VIEW_ISHIDE;
        if (listener != null) {
            listener.pulledHide();
        }
    }


    /*private boolean canIntercept=true;
    public boolean dispatchTouchEvent(MotionEvent ev) {
        boolean ret = super.dispatchTouchEvent(ev);
        if (ret) {
            canIntercept=false;
        }else{
            canIntercept=true;
        }
        return ret;
    }*/

    /********************************************************************
     * 返回true时被截断，这样才能是的onTouchEvent执行详见：
     * http://www.cnblogs.com/kingcent/archive/2011/03/08/1977064.html
     *********************************************************************/
    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {

        if (hideView != null) {

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    /**********************************
                     * 1。不被父View拦截的情况下，这里必然后响应
                     **********************************/
                    touchEvRecord.lastXdistance = 0;
                    touchEvRecord.lastYdistance = 0;

                    touchEvRecord.downX = event.getX();
                    touchEvRecord.downY = event.getY();

                    touchEvRecord.lastX = event.getX();
                    touchEvRecord.lastY = event.getY();

                    break;
                case MotionEvent.ACTION_MOVE:

                    /**********************************
                     * 1.不被父View拦截的情况下，这里必然后响应一次
                     * 2。子view或者自身没有响应ontouch事件之前这里事件会持续，事件没有被消耗
                     * 3。子view或者自身响应ontouch后，这里将不再继续进行拦截，事件已经被消耗
                     **********************************/
                    touchEvRecord.lastXdistance = (int) (event.getX() - touchEvRecord.lastX);
                    touchEvRecord.lastYdistance = (int) (event.getY() - touchEvRecord.lastY);

                    touchEvRecord.lastX = (int) event.getX();
                    touchEvRecord.lastY = (int) event.getY();

                    // 垂直方向
                    if (nowerDerection == AddDerection.ADD_BOTTOM
                            || nowerDerection == AddDerection.ADD_TOP) {
                        // Y方向移动才触发
                        if ((touchEvRecord.lastXdistance) > Math
                                .abs(touchEvRecord.lastYdistance)) {
                            return false;
                        }
                    }
                    // 水平方向
                    else if (nowerDerection == AddDerection.ADD_LEFT
                            || nowerDerection == AddDerection.ADD_RIGHT) {
                        // X方向移动才触发
                        if ((touchEvRecord.lastXdistance) < Math
                                .abs(touchEvRecord.lastYdistance)) {
                            return false;
                        }
                    }

                    // 顶部添加
                    if (nowerDerection == AddDerection.ADD_TOP) {
                        // Y方向移动了3个像素点的时候才进行响应滑动操作
                        float m = touchEvRecord.lastY - touchEvRecord.downY;
                        // 如果是没有显示，除非是朝下面拉，才进行操作
                        if (topViewstate == ViewShowState.VIEW_ISHIDE && m > startPX
                                && canScroll(event)) {
                            // 开始
                            if (listener != null) {
                                listener.startPull();
                            }
                            return true;
                        }
                    }
                    // 底部添加
                    else if (nowerDerection == AddDerection.ADD_BOTTOM) {
                        // Y方向移动了3个像素点的时候才进行响应滑动操作
                        float m = touchEvRecord.lastY - touchEvRecord.downY;
                        // 如果是没有显示，除非是朝下面拉，才进行操作
                        if (topViewstate == ViewShowState.VIEW_ISHIDE && m < -startPX
                                && canScroll(event)) {
                            // 开始
                            if (listener != null) {
                                listener.startPull();
                            }
                            return true;
                        }
                    }
                    // 左边添加
                    else if (nowerDerection == AddDerection.ADD_LEFT) {
                        // Y方向移动了3个像素点的时候才进行响应滑动操作
                        float m = touchEvRecord.lastX - touchEvRecord.downX;
                        // 如果是没有显示，除非是朝下面拉，才进行操作
                        if (topViewstate == ViewShowState.VIEW_ISHIDE && m > startPX
                                && canScroll(event)) {
                            // 开始
                            if (listener != null) {
                                listener.startPull();
                            }
                            return true;
                        }
                    }
                    // 右边添加
                    else if (nowerDerection == AddDerection.ADD_RIGHT) {
                        // Y方向移动了3个像素点的时候才进行响应滑动操作
                        float m = touchEvRecord.lastX - touchEvRecord.downX;
                        // 如果是没有显示，除非是朝下面拉，才进行操作
                        if (topViewstate == ViewShowState.VIEW_ISHIDE && m < -startPX
                                && canScroll(event)) {
                            // 开始
                            if (listener != null) {
                                listener.startPull();
                            }
                            return true;
                        }
                    }

                    break;
                case MotionEvent.ACTION_UP:
                    touchEvRecord.reSet();
                    break;
                case MotionEvent.ACTION_CANCEL:
                    touchEvRecord.reSet();
                    break;
            }

            // 假如顶部区域已经显示出来
            if (topViewstate == ViewShowState.VIEW_ONSHOW) {
                // 假如事件的位置处于最上方则不进行拦截
                if (nowerDerection == AddDerection.ADD_TOP) {
                    if (event.getY() < hideView.getMeasuredHeight()) {
                        // Y方向移动了3个像素点的时候才进行响应滑动操作
                        float m = touchEvRecord.lastY - touchEvRecord.downY;
                        if (canScrollHide(event) && m < -startPX) {
                            return true;
                        }
                        return false;
                    }
                } else if (nowerDerection == AddDerection.ADD_BOTTOM) {
                    if (event.getY() > getHeight() - hideView.getMeasuredHeight()) {
                        // Y方向移动了3个像素点的时候才进行响应滑动操作
                        float m = touchEvRecord.lastY - touchEvRecord.downY;
                        if (canScrollHide(event) && m > startPX) {
                            return true;
                        }
                        return false;
                    }
                } else if (nowerDerection == AddDerection.ADD_LEFT) {
                    if (event.getX() < hideView.getMeasuredWidth()) {
                        // Y方向移动了3个像素点的时候才进行响应滑动操作
                        float m = touchEvRecord.lastX - touchEvRecord.downX;
                        if (canScrollHide(event) && m < -startPX) {
                            return true;
                        }
                        return false;
                    }
                } else if (nowerDerection == AddDerection.ADD_RIGHT) {
                    if (event.getX() > getWidth() - hideView.getMeasuredWidth()) {
                        // Y方向移动了3个像素点的时候才进行响应滑动操作
                        float m = touchEvRecord.lastX - touchEvRecord.downX;
                        if (canScrollHide(event) && m > startPX) {
                            return true;
                        }
                        return false;
                    }
                }
                return true;
            }
        }
        return super.onInterceptTouchEvent(event);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (hideView != null) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:

                    // 假如底层不消费事件，我就消费咯，斜眼笑
                    return true;
                // break;
                case MotionEvent.ACTION_MOVE:

                    //假如被拦截了，那么根据移动的距离滚动当前的scroller
                    touchEvRecord.lastXdistance = (int) (event.getX() - touchEvRecord.lastX);
                    touchEvRecord.lastYdistance = (int) (event.getY() - touchEvRecord.lastY);
                    touchEvRecord.lastX = (int) event.getX();
                    touchEvRecord.lastY = (int) event.getY();

                    //滚动
                    doMovement(touchEvRecord);

                    break;
                case MotionEvent.ACTION_UP:
                    //弹起
                    doTouchUp();
                    break;
                case MotionEvent.ACTION_CANCEL:
                    //弹起
                    doTouchUp();
                    break;
            }

        }
        return super.onTouchEvent(event);
    }

    /*****************
     * 获取当前Event所在的View
     *
     * @param view   view
     * @param event  event
     * @param father 此View的fatherview相对于event已经存在的坐标
     * @return
     */
    private View getInnerScrollView(View view,
                                    MotionEvent event,
                                    Rect father) {

        View retview = null;
        if (!(view instanceof ViewGroup)) {
            //取得这个View
            View mem = view;
            //创建
            int[] memLo = new int[2];
            //获取在平面上的位置
            mem.getLocationOnScreen(memLo);
            //屏幕上的rect
            int left = memLo[0];
            int right = memLo[0] + mem.getWidth();
            int top = memLo[1];
            int bottom = memLo[1] + mem.getHeight();
            // 当前这个view相对于最上层view的相对坐标
            Rect nower = new Rect(left, top, right, bottom);

            //按理不可能到这里了，因为这几个滚动的view都继承自ViewGroup
            if (mem instanceof ListView
                    || mem instanceof GridView
                    || mem instanceof ScrollView
                    || mem instanceof HorizontalScrollView
                    || mem instanceof WebView) {


                // 假如正好在这个范围之中
                if (event.getRawX() >= nower.left
                        && event.getRawX() <= nower.right
                        && event.getRawY() <= nower.bottom
                        && event.getRawY() >= nower.top) {
                    retview = mem;
                    return retview;
                }
            }
        } else {
            //如果是ViewGroup
            ViewGroup group = (ViewGroup) view;
            for (int s = 0; s < group.getChildCount(); s++) {
                //获取第Sge
                View mem = group.getChildAt(s);
                //创建用于保存
                int[] memLo = new int[2];
                //屏幕位置
                mem.getLocationOnScreen(memLo);
                int left = memLo[0];
                int right = memLo[0] + mem.getWidth();
                int top = memLo[1];
                int bottom = memLo[1] + mem.getHeight();
                //屏幕rect
                Rect nower = new Rect(left, top, right, bottom);

                //如果是其中之一
                if (mem instanceof ListView
                        || mem instanceof GridView
                        || mem instanceof ScrollView
                        || mem instanceof HorizontalScrollView
                        || mem instanceof WebView) {


                    // 假如正好在这个范围之中
                    if (event.getRawX() >= nower.left
                            && event.getRawX() <= nower.right
                            && event.getRawY() <= nower.bottom
                            && event.getRawY() >= nower.top) {
                        retview = mem;
                        return retview;
                    }
                }
                //如果是ViewGroup
                else if (mem instanceof ViewGroup) {
                    retview = getInnerScrollView((ViewGroup) mem, event, nower);
                    if (retview != null) {
                        return retview;
                    }
                }
            }
        }
        return retview;
    }


    /**************
     * hideview状态下是否能够滚动
     *
     * @param event event
     * @return
     */
    private boolean canScrollHide(MotionEvent event) {

        if (hideView == null) {
            return true;
        }
        // 取得当前可滚动的view,这里只允许当前的layout中包含一个滚动的View
        View childView = getInnerScrollView(hideView,
                event,
                new Rect(0, 0, this.getWidth(), this.getHeight()));

        //假如获取到了可以滑动的view
        if (childView != null) {
            if (nowerDerection == AddDerection.ADD_BOTTOM) {
                //如果是列表模式
                if (childView instanceof ListView) {
                    //转换为列表
                    ListView list = (ListView) childView;
                    //如果cont大于零
                    if (list.getChildCount() > 0) {
                        //获取第一个
                        View first = list.getChildAt(0);
                        //第一个view的顶部
                        int top = first.getTop();
                        //获取padding
                        int pad = list.getListPaddingTop();
                        //如果为零且满足条件，就可以滚动
                        if (Math.abs(top - pad) < startPX && list.getFirstVisiblePosition() == 0) {
                            return true;
                        } else {
                            //否则不可以滚动
                            return false;
                        }
                    } else {
                        //默认可以滚动，因为没有子
                        return true;
                    }
                }
                //如果是gridView
                else if (childView instanceof GridView) {
                    //转换为GridView
                    GridView list = (GridView) childView;
                    //如果有child
                    if (list.getChildCount() > 0) {
                        //获取第一个
                        View first = list.getChildAt(0);
                        //获取顶部
                        int top = first.getTop();
                        //获取padding
                        int pad = list.getListPaddingTop();
                        //判断是否能够滚动
                        if (Math.abs(top - pad) < startPX
                                && list.getFirstVisiblePosition() == 0) {
                            //可以滚动
                            return true;
                        } else {
                            //不能不能动
                            return false;
                        }
                    } else {
                        //默认可以滚动，因为没有子
                        return true;
                    }
                } else if (childView instanceof ScrollView) {
                    ScrollView scrollView = (ScrollView) childView;
                    //如果是ScrollView，判断滚动距离就行了
                    if (scrollView.getScrollY() < startPX) {
                        return true;
                    } else {
                        return false;
                    }
                } else if (childView instanceof WebView) {
                    WebView webView = (WebView) childView;
                    if (webView.getScrollY() < startPX) {
                        return true;
                    } else {
                        return false;
                    }
                }
            }
            //如果是添加在底部
            if (nowerDerection == AddDerection.ADD_TOP) {
                //如果是列表
                if (childView instanceof ListView) {
                    //转换为列表
                    ListView list = (ListView) childView;
                    if (list.getChildCount() > 0) {
                        //获取最后一条
                        View last = list.getChildAt(list.getChildCount() - 1);
                        //最后一条的底部
                        int bootom = last.getBottom();
                        //获取padding
                        int pad = list.getListPaddingBottom();
                        //是最后一条而且滚动的距离到头了
                        if ((bootom + pad) + startPX > list.getHeight()
                                && list.getLastVisiblePosition() == list.getCount() - 1) {
                            return true;
                        } else {
                            return false;
                        }
                    } else {
                        return true;
                    }
                } else if (childView instanceof GridView) {
                    //转换为grid
                    GridView list = (GridView) childView;
                    //如果有子
                    if (list.getChildCount() > 0) {
                        //获取最后一个
                        View last = list.getChildAt(list.getChildCount() - 1);
                        //获取bottom
                        int bootom = last.getBottom();
                        //获取padding
                        int pad = list.getListPaddingTop();
                        //如果时候最后一条而且滚动到底了
                        if ((bootom + pad) + startPX > list.getHeight()
                                && list.getLastVisiblePosition() == list.getCount() - 1) {
                            return true;
                        } else {
                            return false;
                        }
                    } else {
                        return true;
                    }
                } else if (childView instanceof ScrollView) {
                    //强制转换为ScrollView
                    ScrollView scrollView = (ScrollView) childView;
                    //如果有子
                    if (scrollView.getChildCount() > 0) {
                        if (scrollView.getScrollY() + scrollView.getHeight() + startPX >=
                                scrollView.getChildAt(0).getMeasuredHeight()) {
                            return true;
                        } else {
                            return false;
                        }
                    } else {
                        return true;
                    }
                } else if (childView instanceof WebView) {
                    //转换为webview
                    WebView webView = (WebView) childView;
                    //判断是否滚动到底部
                    if ((webView.getHeight() + webView.getScrollY()) + startPX > webView.getContentHeight() * webView.getScale()) {
                        return true;
                    } else {
                        return false;
                    }
                }
            }
            if (nowerDerection == AddDerection.ADD_RIGHT) {
                if (childView instanceof HorizontalScrollView) {
                    HorizontalScrollView horizontalScrollView = (HorizontalScrollView) childView;
                    if (horizontalScrollView.getScrollX() < startPX) {
                        return true;
                    } else {
                        return false;
                    }
                }
            }
            if (nowerDerection == AddDerection.ADD_LEFT) {
                if (childView instanceof HorizontalScrollView) {
                    HorizontalScrollView horizontalScrollView = (HorizontalScrollView) childView;
                    if (horizontalScrollView.getScrollX() + horizontalScrollView.getWidth() + startPX >=
                            horizontalScrollView.getChildAt(0).getMeasuredWidth()) {
                        return true;
                    } else {
                        return false;
                    }
                }
            }

        }
        return true;
    }

    /******************
     * 判断是否能够继续滑动，非hide的view
     */
    private boolean canScroll(MotionEvent event) {

        /*if(!canIntercept){
            return false;
        }*/
        // 取得当前可滚动的view,这里只允许当前的layout中包含一个滚动的View
        View childView = getInnerScrollView(this, event, new Rect(0, 0, this.getWidth(), this.getHeight()));

        //假如获取到了可以滑动的view
        if (childView != null) {
            if (nowerDerection == AddDerection.ADD_TOP) {
                //如果是列表模式
                if (childView instanceof ListView) {
                    //转换为列表
                    ListView list = (ListView) childView;
                    //如果cont大于零
                    if (list.getChildCount() > 0) {
                        //获取第一个
                        View first = list.getChildAt(0);
                        //第一个view的顶部
                        int top = first.getTop();
                        //获取padding
                        int pad = list.getListPaddingTop();
                        //如果为零且满足条件，就可以滚动
                        if (Math.abs(top - pad) < startPX && list.getFirstVisiblePosition() == 0) {
                            return true;
                        } else {
                            //否则不可以滚动
                            return false;
                        }
                    } else {
                        //默认可以滚动，因为没有子
                        return true;
                    }
                }
                //如果是gridView
                else if (childView instanceof GridView) {
                    //转换为GridView
                    GridView list = (GridView) childView;
                    //如果有child
                    if (list.getChildCount() > 0) {
                        //获取第一个
                        View first = list.getChildAt(0);
                        //获取顶部
                        int top = first.getTop();
                        //获取padding
                        int pad = list.getListPaddingTop();
                        //判断是否能够滚动
                        if (Math.abs(top - pad) < startPX
                                && list.getFirstVisiblePosition() == 0) {
                            //可以滚动
                            return true;
                        } else {
                            //不能不能动
                            return false;
                        }
                    } else {
                        //默认可以滚动，因为没有子
                        return true;
                    }
                } else if (childView instanceof ScrollView) {
                    ScrollView scrollView = (ScrollView) childView;
                    //如果是ScrollView，判断滚动距离就行了
                    if (scrollView.getScrollY() < startPX) {
                        return true;
                    } else {
                        return false;
                    }
                } else if (childView instanceof WebView) {
                    WebView webView = (WebView) childView;
                    if (webView.getScrollY() < startPX) {
                        return true;
                    } else {
                        return false;
                    }
                }
            }
            //如果是添加在底部
            if (nowerDerection == AddDerection.ADD_BOTTOM) {
                //如果是列表
                if (childView instanceof ListView) {
                    //转换为列表
                    ListView list = (ListView) childView;
                    if (list.getChildCount() > 0) {
                        //获取最后一条
                        View last = list.getChildAt(list.getChildCount() - 1);
                        //最后一条的底部
                        int bootom = last.getBottom();
                        //获取padding
                        int pad = list.getListPaddingBottom();
                        //是最后一条而且滚动的距离到头了
                        if ((bootom + pad) + startPX > list.getHeight()
                                && list.getLastVisiblePosition() == list.getCount() - 1) {
                            return true;
                        } else {
                            return false;
                        }
                    } else {
                        return true;
                    }
                } else if (childView instanceof GridView) {
                    //转换为grid
                    GridView list = (GridView) childView;
                    //如果有子
                    if (list.getChildCount() > 0) {
                        //获取最后一个
                        View last = list.getChildAt(list.getChildCount() - 1);
                        //获取bottom
                        int bootom = last.getBottom();
                        //获取padding
                        int pad = list.getListPaddingTop();
                        //如果时候最后一条而且滚动到底了
                        if ((bootom + pad) + startPX > list.getHeight()
                                && list.getLastVisiblePosition() == list.getCount() - 1) {
                            return true;
                        } else {
                            return false;
                        }
                    } else {
                        return true;
                    }
                } else if (childView instanceof ScrollView) {
                    //强制转换为ScrollView
                    ScrollView scrollView = (ScrollView) childView;
                    //如果有子
                    if (scrollView.getChildCount() > 0) {
                        if (scrollView.getScrollY() + scrollView.getHeight() + startPX >=
                                scrollView.getChildAt(0).getMeasuredHeight()) {
                            return true;
                        } else {
                            return false;
                        }
                    } else {
                        return true;
                    }
                } else if (childView instanceof WebView) {
                    //转换为webview
                    WebView webView = (WebView) childView;
                    //判断是否滚动到底部
                    if ((webView.getHeight() + webView.getScrollY()) + startPX > webView.getContentHeight() * webView.getScale()) {
                        return true;
                    } else {
                        return false;
                    }
                }
            }
            if (nowerDerection == AddDerection.ADD_LEFT) {
                if (childView instanceof HorizontalScrollView) {
                    HorizontalScrollView horizontalScrollView = (HorizontalScrollView) childView;
                    if (horizontalScrollView.getScrollX() < startPX) {
                        return true;
                    } else {
                        return false;
                    }
                }
            }
            if (nowerDerection == AddDerection.ADD_RIGHT) {
                if (childView instanceof HorizontalScrollView) {
                    HorizontalScrollView horizontalScrollView = (HorizontalScrollView) childView;
                    if (horizontalScrollView.getScrollX() + horizontalScrollView.getWidth() + startPX >=
                            horizontalScrollView.getChildAt(0).getMeasuredWidth()) {
                        return true;
                    } else {
                        return false;
                    }
                }
            }

        }
        return true;
    }

    // 调用此方法滚动到目标位置
    public void smoothScrollTo(int fx, int fy) {
        int dx = -fx - mScroller.getFinalX();
        int dy = -fy - mScroller.getFinalY();
        smoothScrollBy(-dx, -dy);
    }

    // 调用此方法设置滚动的相对偏移
    public void smoothScrollBy(int dx, int dy) {
        // 设置mScroller的滚动偏移量
        mScroller.startScroll(mScroller.getFinalX(), mScroller.getFinalY(),
                -dx, -dy);
        // 这里必须调用invalidate()才能保证computeScroll()会被调用，否则不一定会刷新界面，看不到滚动效果
        invalidate();
    }

    @Override
    public void computeScroll() {
        // 先判断mScroller滚动是否完成
        if (mScroller.computeScrollOffset()) {
            // 这里调用View的scrollTo()完成实际的滚动
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            // 必须调用该方法，否则不一定能看到滚动效果
            postInvalidate();
            // 如果监听不为空
            if (listener != null) {
                int progress = 0;
                // 判断不同方向设置进度
                if (nowerDerection == AddDerection.ADD_TOP) {
                    progress = (int) ((float) (-mScroller.getCurrY())
                            / (float) hideView.getHeight() * 100);
                } else if (nowerDerection == AddDerection.ADD_BOTTOM) {
                    progress = (int) ((float) (mScroller.getCurrY())
                            / (float) hideView.getHeight() * 100);

                } else if (nowerDerection == AddDerection.ADD_LEFT) {
                    progress = (int) ((float) (-mScroller.getCurrX())
                            / (float) hideView.getWidth() * 100);

                } else if (nowerDerection == AddDerection.ADD_RIGHT) {
                    progress = (int) ((float) (mScroller.getCurrX())
                            / (float) hideView.getWidth() * 100);
                }

                if (progress > 100) {
                    progress = 100;
                }
                if (progress < 0) {
                    progress = 0;
                }
                listener.pulling(progress);
            }

        } else {

			/*if (listener != null) {
                if (nowerDerection == AddDerection.ADD_TOP) {
					if ((-mScroller.getFinalY() - hideView.getHeight()) < Math
							.abs(-mScroller.getFinalY())) {
						topViewstate = ViewShowState.VIEW_ONSHOW;
					} else {
						listener.pulledHide();
						topViewstate = ViewShowState.VIEW_ISHIDE;
					}
				} else if (nowerDerection == AddDerection.ADD_BOTTOM) {
					if ((-mScroller.getFinalY() + hideView.getHeight()) < Math
							.abs(-mScroller.getFinalY())) {
						topViewstate = ViewShowState.VIEW_ONSHOW;
					} else {
						listener.pulledHide();
						topViewstate = ViewShowState.VIEW_ISHIDE;
					}
				} else if (nowerDerection == AddDerection.ADD_LEFT) {
					if ((-mScroller.getFinalX() - hideView.getWidth()) < Math
							.abs(-mScroller.getFinalX())) {
						topViewstate = ViewShowState.VIEW_ONSHOW;
					} else {
						listener.pulledHide();
						topViewstate = ViewShowState.VIEW_ISHIDE;
					}
				} else if (nowerDerection == AddDerection.ADD_RIGHT) {
					if ((-mScroller.getFinalX() + hideView.getWidth()) < Math
							.abs(-mScroller.getFinalX())) {
						topViewstate = ViewShowState.VIEW_ONSHOW;
					} else {
						listener.pulledHide();
						topViewstate = ViewShowState.VIEW_ISHIDE;
					}
				}
			}*/

        }
        super.computeScroll();
    }

}
