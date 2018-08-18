package com.flappygod.lipo.lxlibrary.Widget;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.FrameLayout;
import android.widget.Scroller;

import com.flappygod.lipo.lxlibrary.Tools.DensityTool;


/*********************************
 * Created by yang on 2016/9/23.
 */
public class SlideFramelayout extends FrameLayout {


    //滑动的状态
    public enum SlideState {
        SlideOut,
        SlideIn
    }

    //滑动的方向
    public enum SlideDirection {
        Left,
        Right,
        Top,
        Bottom
    }

    //滚动的方向，
    private SlideDirection slideDirection;
    //当前的滚动状态,
    private SlideState slideState;
    //即将展现的状态
    private SlideState willSlideState= SlideState.SlideOut;

    private boolean willSlideAnimation=false;


    //滑动出去后边缘允许的滚动范围
    private int edgeWidth = 100;
    //显示的宽度
    private int showWidth = 0;
    //动画时间
    private int duration = 500;


    // 滚动helper
    private Scroller mScroller;
    //监听
    private SlideListener listener;

    //透明度改变
    private boolean alphaChange = true;
    //首次显示的动画
    private boolean firstShowAnimation = true;


    private boolean  canDrag=true;

    public boolean isCanDrag() {
        return canDrag;
    }

    public void setCanDrag(boolean canDrag) {
        this.canDrag = canDrag;
    }

    /**********
     * 构造器
     *
     * @param context 上下文
     */
    public SlideFramelayout(Context context) {
        super(context);
        init(context);
    }


    /***********
     * 获取滑动方向
     *
     * @return
     */
    public SlideDirection getSlideDirection() {
        return slideDirection;
    }

    /***********
     * 获取状态
     *
     * @return
     */
    public SlideState getSlideState() {
        return slideState;
    }

    /*************
     * 构造器
     *
     * @param context 上下文
     * @param attrs   参数
     */
    public SlideFramelayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    /**************
     * 构造器
     *
     * @param context      上下文
     * @param attrs        参数
     * @param defStyleAttr 参数
     */
    public SlideFramelayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    /**********
     * 获取边缘可被拉出的宽度
     *
     * @return
     */
    public int getEdgeWidth() {
        return edgeWidth;
    }

    /************
     * 设置边缘可被拉出的宽度
     *
     * @param edgeWidth 边缘宽度
     */
    public void setEdgeWidth(int edgeWidth) {
        this.edgeWidth = edgeWidth;
    }

    /*********
     * 获取动画时间
     *
     * @return
     */
    public int getDuration() {
        return duration;
    }

    /************
     * 设置动画时间
     *
     * @param duration
     */
    public void setDuration(int duration) {
        this.duration = duration;
    }

    /**********
     * 滑动过程中的背景是否变换
     *
     * @return
     */
    public boolean isAlphaChange() {
        return alphaChange;
    }

    /*************
     * 设置滑动过程中是否变换背景颜色alpha
     *
     * @param alphaChange
     */
    public void setAlphaChange(boolean alphaChange) {
        this.alphaChange = alphaChange;
        refreshAlphaSet(slideState);
    }

    /**********
     * 获取显示的宽度
     *
     * @return
     */
    public int getShowWidth() {
        return showWidth;
    }

    /**********
     * 设置显示的宽度
     *
     * @param showWidth 显示宽度
     */
    public void setShowWidth(int showWidth) {
        this.showWidth = showWidth;
    }


    /***********
     * 初始化
     *
     * @param context 上下文
     */
    private void init(Context context) {
        mScroller = new Scroller(context);
        //滚动的方向，默认是左边
        slideDirection= SlideDirection.Left;
        //当前的滚动状态,默认进入状态
        slideState= SlideState.SlideIn;
        //设置变换
        setAlphaChange(true);
        //默认颜色
        int color = Color.argb(0, 0, 0, 0);
        setBackgroundColor(color);
    }

    //设置滚动方向
    public void setSlideDirection(SlideDirection slideDirection) {
        this.slideDirection = slideDirection;
        smoothScrollTo(0, 0, true);
        //showIn();
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        //显示的宽度
        if (showWidth == 0) {
            //左右方向取宽度
            if (slideDirection == SlideDirection.Left || slideDirection == SlideDirection.Right) {
                showWidth = getWidth();
            } else {
                //上下方向取高度
                showWidth = getHeight();
            }
        }
        //显示的宽度
        if (showWidth == 0) {
            showWidth = 1;
        }

        if(willSlideState== SlideState.SlideIn) {
            showInInit();
        }else{
            showOutInit();
        }
    }
    private void showInInit(){
        showIn(willSlideAnimation);
    }
    private void showOutInit(){
        showOut(willSlideAnimation);
    }

    /**********
     * 假如在创建之后直接调用了showin，这时候还没有layout,
     */
    public void showIn(boolean animation) {
        willSlideState= SlideState.SlideIn;
        willSlideAnimation=animation;
        //没有设置宽度时不理会
        if (getWidth() == 0) {
            return;
        }
        //假如是坐标进入的情况下
        if (slideDirection == SlideDirection.Left) {
            //开始从左边滑动进来
            mScroller.startScroll(showWidth, 0, -showWidth, 0, animation? duration:0);
        } else if (slideDirection == SlideDirection.Right) {
            //开始从右边滑动进来
            mScroller.startScroll(-showWidth, 0, showWidth, 0, animation? duration:0);
        } else if (slideDirection == SlideDirection.Top) {
            //开始从顶部滑动进来
            mScroller.startScroll(0, showWidth, 0, -showWidth, animation? duration:0);
        } else if (slideDirection == SlideDirection.Bottom) {
            //开始从下方滑动进入过来
            mScroller.startScroll(0, -showWidth, 0, showWidth, animation? duration:0);
        }
        invalidate();
    }

    /***************
     * 显示out
     *
     * @param animation 是否动画
     */
    public void showOut(boolean animation) {

        willSlideState= SlideState.SlideOut;
        willSlideAnimation=animation;
        //没有设置宽度时不理会
        if (getWidth() == 0) {
            return;
        }
        //开始滑出去
        if (this.slideDirection == SlideDirection.Left) {
            //向左边滑动
            smoothScrollTo(-showWidth, 0, animation);
        } else if (this.slideDirection == SlideDirection.Right) {
            //向右边滑动
            smoothScrollTo(showWidth, 0, animation);
        }
        //向上滑动出去
        else if (this.slideDirection == SlideDirection.Top) {
            smoothScrollTo(0, -showWidth, animation);
        }
        //向下滑动出去
        else if (this.slideDirection == SlideDirection.Bottom) {
            smoothScrollTo(0, showWidth, animation);
        }

    }


    /*********************
     * 立即设置alpha状态，用于跳变时候的设置
     */
    private void  refreshAlphaSet(SlideState state){
        //假如开启了change
        if (alphaChange) {
            if (state == SlideState.SlideIn) {
                int color = Color.argb(100, 0, 0, 0);
                setBackgroundColor(color);
                /*buildDrawingCache();*/
            } else {
                int color = Color.argb(0, 0, 0, 0);
                setBackgroundColor(color);
            }
        } else {
            int color = Color.argb(0, 0, 0, 0);
            setBackgroundColor(color);
        }
    }


    //当前的XY
    private float x;
    private float y;
    //按下的X
    private float downX;
    //按下的Y
    private float downY;

    public boolean onInterceptTouchEvent(MotionEvent event) {

        if(!canDrag){
            return super.onInterceptTouchEvent(event);
        }

        if (!canResponedTouch(event)) {
            return super.onInterceptTouchEvent(event);
        }
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                moved=false;
                //设置当前XY
                x = event.getX();
                y = event.getY();

                //设置按下的XY
                downX = event.getX();
                downY = event.getY();

                break;
            case MotionEvent.ACTION_MOVE:
                float deltaX = event.getX() - x;
                float deltaY = event.getY() - y;
                switch (slideDirection) {
                    //左边滑动进入
                    case Left:
                        if (Math.abs(deltaX) > Math.abs(deltaY) && Math.abs(deltaX) > DensityTool.dip2px(getContext(), 4)) {
                            x = event.getX();
                            y = event.getY();
                            return true;
                        }
                        break;
                    case Right:
                        //右边滑动进入
                        if (Math.abs(deltaX) > Math.abs(deltaY) && Math.abs(deltaX) > DensityTool.dip2px(getContext(), 4)) {
                            x = event.getX();
                            y = event.getY();
                            return true;
                        }
                        break;
                    case Top:
                        //从上方滑入
                        if (Math.abs(deltaY) > Math.abs(deltaX) && Math.abs(deltaY) > DensityTool.dip2px(getContext(), 4)) {
                            x = event.getX();
                            y = event.getY();
                            return true;
                        }
                        break;
                    case Bottom:
                        //从下方滑入
                        if (Math.abs(deltaY) > Math.abs(deltaX) && Math.abs(deltaY) > DensityTool.dip2px(getContext(), 4)) {
                            x = event.getX();
                            y = event.getY();
                            return true;
                        }
                        break;
                }
                break;
            case MotionEvent.ACTION_UP:
                break;
            case MotionEvent.ACTION_CANCEL:
                break;
        }
        return false;
    }


    private boolean moved=false;

    /**************
     * @param event
     * @return
     */
    public boolean onTouchEvent(MotionEvent event) {
        if(!canDrag){
            return super.onTouchEvent(event);
        }
        if (!canResponedTouch(event)) {
            return false;
        }
        if(!moved) {
            super.onTouchEvent(event);
        }
        //按下事件
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //设置按下位置
                downX = event.getX();
                downY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                moved=true;
                switch (slideDirection) {
                    //坐标滑入的情况
                    case Left: {
                        float moveX = event.getX() - downX;
                        if (mScroller.getCurrX() >= 0 && mScroller.getCurrX() <= showWidth && Math.abs(moveX) > 2) {
                            if (mScroller.getFinalX() - moveX < 0) {
                                moveX = mScroller.getFinalX();
                            }
                            if (mScroller.getFinalX() - moveX > showWidth) {
                                moveX = mScroller.getFinalX() - showWidth;
                            }
                            downX = event.getX();
                            downY = event.getY();
                            smoothScrollBy((int) moveX, 0, true);
                        }
                    }
                    break;
                    //右边滑入的情况
                    case Right: {
                        float moveX = event.getX() - downX;
                        //限制滑动区域
                        if (mScroller.getCurrX() <= 0 && mScroller.getCurrX() >= -showWidth && Math.abs(moveX) > 2) {

                            if (mScroller.getFinalX() - moveX > 0) {
                                moveX = mScroller.getFinalX();
                            }
                            if (mScroller.getFinalX() - moveX < -showWidth) {
                                moveX = showWidth + mScroller.getFinalX();
                            }
                            downX = event.getX();
                            downY = event.getY();
                            smoothScrollBy((int) moveX, 0, true);
                        }
                    }
                    break;
                    //顶部滑入的情况
                    case Top: {
                        float moveY = event.getY() - downY;
                        if (mScroller.getCurrY() >= 0 && mScroller.getCurrY() <= showWidth && Math.abs(moveY) > 2) {
                            if (mScroller.getFinalY() - moveY < 0) {
                                moveY = mScroller.getFinalY();
                            }
                            if (mScroller.getFinalY() - moveY > showWidth) {
                                moveY = mScroller.getFinalY() - showWidth;
                            }
                            downX = event.getX();
                            downY = event.getY();
                            smoothScrollBy(0, (int) moveY, true);
                        }
                    }
                    break;
                    //底部滑入的情况
                    case Bottom: {
                        float moveY = event.getY() - downY;
                        //限制滑动区域
                        if (mScroller.getCurrY() <= 0 && mScroller.getCurrY() >= -showWidth && Math.abs(moveY) > 2) {
                            if (mScroller.getFinalY() - moveY > 0) {
                                moveY = mScroller.getFinalY();
                            }
                            if (mScroller.getFinalY() - moveY < -showWidth) {
                                moveY = showWidth + mScroller.getFinalY();
                            }
                            downX = event.getX();
                            downY = event.getY();
                            smoothScrollBy(0, (int) moveY, true);
                        }
                    }
                    break;
                }
                break;

            //取消和up的时候
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                switch (slideDirection) {
                    case Left: {
                        if (mScroller.getCurrX() > showWidth / 2) {
                            smoothScrollTo(-showWidth, 0, true);
                        } else {
                            smoothScrollTo(0, 0, true);
                        }
                    }
                    break;
                    case Right: {
                        if (mScroller.getCurrX() < -showWidth / 2) {
                            smoothScrollTo(showWidth, 0, true);
                        } else {
                            smoothScrollTo(0, 0, true);
                        }
                    }
                    break;
                    case Top: {
                        if (mScroller.getCurrY() > showWidth / 2) {
                            smoothScrollTo(0, -showWidth, true);
                        } else {
                            smoothScrollTo(0, 0, true);
                        }
                    }
                    break;
                    case Bottom: {
                        if (mScroller.getCurrY() < -showWidth / 2) {
                            smoothScrollTo(0, showWidth, true);
                        } else {
                            smoothScrollTo(0, 0, true);
                        }
                    }
                    break;
                }
                break;
        }
        return true;
    }


    /****************
     * 是否响应touch事件
     *
     * @param event  事件
     * @return
     */

    private boolean returnState = false;

    private boolean canResponedTouch(MotionEvent event) {
        //如果说当前正好是滑动进来的
        if (this.slideState == SlideState.SlideIn) {
            //可以执行事件
            return true;
        } else {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                switch (slideDirection) {
                    case Left:
                        if (event.getX() < edgeWidth) {
                            returnState = true;
                            return true;
                        } else {
                            returnState = false;
                        }
                        break;
                    case Right:
                        if (event.getX() > getWidth() - edgeWidth) {
                            returnState = true;
                            return true;
                        } else {
                            returnState = false;
                        }
                        break;
                    case Top:
                        if (event.getY() < edgeWidth) {
                            returnState = true;
                            return true;
                        } else {
                            returnState = false;
                        }
                        break;
                    case Bottom:
                        if (event.getY() > getHeight() - edgeWidth) {
                            returnState = true;
                            return true;
                        } else {
                            returnState = false;
                        }
                        break;
                }
            } else {
                return returnState;
            }
            //否则不执行事件
            return false;
        }
    }

    // 调用此方法滚动到目标位置
    public void smoothScrollTo(int fx, int fy, boolean animation) {
        int dx = -fx - mScroller.getFinalX();
        int dy = -fy - mScroller.getFinalY();
        smoothScrollBy(-dx, -dy, animation);
    }

    // 调用此方法设置滚动的相对偏移
    public void smoothScrollBy(int dx, int dy, boolean animation) {
        //是否动画
        int dur = duration;
        if (!animation) {
            dur = 0;
        }
        // 设置mScroller的滚动偏移量
        mScroller.startScroll(mScroller.getFinalX(), mScroller.getFinalY(),
                -dx, -dy, dur);
        // 这里必须调用invalidate()才能保证computeScroll()会被调用，否则不一定会刷新界面，看不到滚动效果
        invalidate();
    }

    @Override
    public void computeScroll() {
        // 先判断mScroller滚动是否完成
        if (mScroller.computeScrollOffset()) {
            // 这里调用View的scrollTo()完成实际的滚动
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            int progress = 0;
            switch (slideDirection) {
                case Left:
                    progress = Math.abs(mScroller.getCurrX()) * 100 / showWidth;
                    break;
                case Right:
                    progress = Math.abs(mScroller.getCurrX()) * 100 / showWidth;
                    break;
                case Top:
                    progress = Math.abs(mScroller.getCurrY()) * 100 / showWidth;
                    break;
                case Bottom:
                    progress = Math.abs(mScroller.getCurrY()) * 100 / showWidth;
                    break;
            }


            //背景颜色变化
            if (alphaChange) {
                int t = (int) (100 - progress);
                if (t >= 0 && t < 255) {
                    int color = Color.argb(t, 0, 0, 0);
                    setBackgroundColor(color);
                }
            }
            // 必须调用该方法，否则不一定能看到滚动效果
            postInvalidate();
//            if(firstShowAnimation){
//                return;
//            }
            //监听
            if (listener != null) {
                listener.slideProgress(progress);
            }

        } else {
//            if(firstShowAnimation){
//                firstShowAnimation=false;
//                return;
//            }
            switch (slideDirection) {
                case Left:
                    //当前的滑动
                    if (mScroller.getFinalX() == showWidth) {
                        slideState = SlideState.SlideOut;
                        if (listener != null) {
                            listener.slideOut();
                        }
                    } else if (mScroller.getFinalX() == 0) {
                        slideState = SlideState.SlideIn;
                        if (listener != null) {
                            listener.slideIn();
                        }
                    }
                    break;
                case Right:
                    //当前的滑动
                    if (mScroller.getFinalX() == -showWidth) {
                        slideState = SlideState.SlideOut;
                        if (listener != null) {
                            listener.slideOut();
                        }
                    } else if (mScroller.getFinalX() == 0) {
                        slideState = SlideState.SlideIn;
                        if (listener != null) {
                            listener.slideIn();
                        }
                    }
                    break;
                case Top:
                    //当前的滑动
                    if (mScroller.getFinalY() == showWidth) {
                        slideState = SlideState.SlideOut;
                        if (listener != null) {
                            listener.slideOut();
                        }
                    } else if (mScroller.getFinalY() == 0) {
                        slideState = SlideState.SlideIn;
                        if (listener != null) {
                            listener.slideIn();
                        }
                    }
                    break;
                case Bottom:
                    //当前的滑动
                    if (mScroller.getFinalY() == -showWidth) {
                        slideState = SlideState.SlideOut;
                        if (listener != null) {
                            listener.slideOut();
                        }
                    } else if (mScroller.getFinalY() == 0) {
                        slideState = SlideState.SlideIn;
                        if (listener != null) {
                            listener.slideIn();
                        }
                    }
                    break;
            }
        }
        super.computeScroll();
    }

    /**************
     * 监听
     */
    public interface SlideListener {
        /**********
         * 滑动进来了
         */
        void slideIn();

        /*******
         * 退出
         */
        void slideOut();

        /************
         * 进度
         *
         * @param progress 进度
         */
        void slideProgress(int progress);
    }

    /**********
     * 设置滑动出去的监听
     *
     * @param listener 监听
     */
    public void setSlideListener(SlideListener listener) {
        this.listener = listener;
    }


}
