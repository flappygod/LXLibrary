package com.flappygod.lipo.lxlibrary.Widget;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;

import com.flappygod.lipo.lxlibrary.Tools.DensityTool;


/********************************************
 * 用于软键盘弹出之后的布局，软键盘弹出后整个
 * 页面会上移软键盘的高度并且伴有滑动动画，软
 * 件盘关闭后返回，只适用于可以
 * adjustPan 的 activity
 *
 * <p>
 * 注意:
 * 此layout默认高度为第一次mesure的高度，此layout必须是适应屏幕高度变化的layout
 * <p>
 * Created by yang on 2017/2/20.
 */
public class KeyBoardLayout extends LinearLayout {

    //软键盘状态的监听
    private KeyboardStateChangeListener mListener;
    //view的滚动的监听
    private KeyBoardLayoutChangeListenr mkListener;


    //当前显示的键盘的高度
    private int heyBoardHeight = 0;
    //控件实际的高度，默认初始高度
    private int actualHeight = -1;
    //保持当前的显示状态
    private boolean keepState = false;
    //默认的动画时间
    private int defaultDuration = 300;
    //当前页面是否做了位移的状态
    private boolean isScrolled = false;


    /***********************
     * 当前是否正在保持状态
     *
     * @return
     */
    public boolean isKeepState() {
        return keepState;
    }


    /***********
     *
     * @param keepState 设置是否保持当前的状态，之后的软键盘动作是否影响整个view的滚动动作
     */
    public void setKeepState(boolean keepState) {
        setKeepState(keepState, true);
    }


    //判断软键盘是否显示了
    public boolean isKeyboardUp() {
        if (actualHeight == getVisibleHeight()) {
            return false;
        } else {
            return true;
        }
    }

    //判断当前页面释放进行了位移操作
    public boolean isScrolledUp() {
        return isScrolled;
    }


    /***********
     * @param keepState  设置是否保持当前的状态，之后的软键盘动作是否影响整个view的滚动动作
     * @param update   是否将页面刷新为当前页面状态和软键盘弹出状态一致
     */
    public void setKeepState(boolean keepState, boolean update) {
        //设置是否保持状态，如果保持状态那么当前的layout停止滚动
        if (keepState) {
            this.keepState = keepState;
        } else {
            //释放状态后恢复到应该到的位置
            this.keepState = keepState;
            //是否更新界面显示
            if (update) {
                //如果软键盘还在就=就
                if (isKeyboardUp() && !isScrolledUp()) {
                    ScrollUp(defaultDuration);
                } else if (!isKeyboardUp() && isScrolledUp()) {
                    ScrollDown(defaultDuration);
                }
            }
        }
    }

    /*******
     * 设置软键盘是否弹出状态的监听
     * @param listener
     */
    public void setKeyboardStateChangeListener(KeyboardStateChangeListener listener) {
        this.mListener = listener;
    }

    /**********
     * 设置当前页面上移的监听
     * @param listenr
     */
    public void setKeyBoardLayoutChangeListenr(KeyBoardLayoutChangeListenr listenr) {
        this.mkListener = listenr;
    }

    //当前的keyboard监听
    public interface KeyboardStateChangeListener {
        /************
         * 软键盘关闭
         *
         * @param softKeyboardHeight 软键盘的高度
         */
        void onSoftKeyboardDissmiss(int softKeyboardHeight);

        /*************
         * 软键盘开启
         *
         * @param softKeyboardHeight 软键盘的高度
         */
        void onSoftKeyboardShown(int softKeyboardHeight);
    }


    //当前的滚动的view的监听
    public interface KeyBoardLayoutChangeListenr {

        //布局上移
        void onLayoutShow(int softKeyboardHeight);

        //布局上移完成
        void onLayoutDidShow(int softKeyboardHeight);

        //布局下移
        void onLayoutDissMiss(int softKeyboardHeight);

        //布局下移完成
        void onLayoutDidDissMiss(int softKeyboardHeight);

    }


    public KeyBoardLayout(Context context) {
        super(context);
        init();
    }

    public KeyBoardLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public KeyBoardLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    /************
     * 初始化
     */
    private void init() {
        //默认一个267的高度给当前的键盘高度
        heyBoardHeight = dip2px(getContext(), 267);
    }

    //转换dp为px
    private int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**************
     * 设置实际控件的高度
     *
     * @param height 高度
     */
    public void setActualHeight(int height) {
        actualHeight = height;
        //给一个默认的高度给他
        heyBoardHeight = dip2px(getContext(), 267);
        //刷新页面
        invalidate();
    }


    //需要的高度
    private int mneedheight = 0;

    //更改布局
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        //更改布局
        super.onLayout(changed, l, t, r, b);
        //获取根布局的高度
        int needHeight = getVisibleHeight();

        //限制不重复刷新页面造成不必要的消耗和造成死循环的bug
        if (mneedheight == needHeight) {
            return;
        } else {
            mneedheight = needHeight;
        }

        //假如当前控件设置的实际高度好没有初始化就进行初始化，默认第一次的时候就是真实高度
        if (actualHeight == -1) {
            actualHeight = needHeight;
        }

        //假如根布局的高度发生了变化，以高度差判断是否是软键盘弹出了，默认大于50dp就是软键盘，小于的我们认为是华为类型的奇葩手机的屏幕内按钮
        if (Math.abs(needHeight - actualHeight) < DensityTool.dip2px(getContext(), 60)) {
            actualHeight = needHeight;
        }

        //弹出框的高度
        int diff = actualHeight - needHeight;
        //是否弹出键盘
        boolean isShow = diff > DensityTool.dip2px(getContext(), 50);
        //如果软键盘弹出了
        if (isShow) {
            //通知监听
            if (mListener != null) {
                mListener.onSoftKeyboardShown(diff);
            }
            //保存软键盘的高度
            heyBoardHeight = diff;
            //软键盘弹出的滚动动画
            if (!keepState) {
                ScrollUp(defaultDuration);
            }
        } else {
            //通知监听
            if (mListener != null) {
                //软键盘高度
                mListener.onSoftKeyboardDissmiss(heyBoardHeight);
            }
            //软键盘弹出的滚动动画
            if (!keepState && heyBoardHeight > 0) {
                ScrollDown(defaultDuration);
            }

        }
    }


    /***********
     * 获取根布局的View
     *
     * @param parent 当前的View
     * @return
     */
    private View getDecorView(ViewParent parent) {
        if (parent instanceof ViewGroup) {
            ViewGroup father = (ViewGroup) parent;
            if (father.getParent() == null || father.getParent().getParent() == null) {
                return father;
            } else {
                return getDecorView(father.getParent());
            }
        } else {
            return null;
        }
    }

    /************
     * 获取可见高度
     *
     * @return
     */
    private int getVisibleHeight() {
        //获取当前view的最跟布局的view
        View view = getDecorView(this);
        //获取根布局的高度
        Rect rect = new Rect();
        //获取根布局的frame
        view.getWindowVisibleDisplayFrame(rect);
        return rect.height();
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //高度还是之前的高度
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }



    public void ScrollUp(){
        ScrollUp(defaultDuration);
    }

    public  void ScrollDown(){
        ScrollDown(defaultDuration);
    }
    /*************
     * 整个界面上移
     */
    public void ScrollUp(int duration) {
        //假如已经上移了上来，那么久不做处理
        if (isScrolled) {
            return;
        }

        if (getChildCount() > 0) {

            //通知监听
            if (mkListener != null) {
                mkListener.onLayoutShow(heyBoardHeight);
            }
            //已经被移动
            isScrolled = true;

            final View view = getChildAt(0);
            //清理动画
            view.clearAnimation();
            //后面进行的补帧动画
            TranslateAnimation translateAnimation = new TranslateAnimation(0, 0, heyBoardHeight, 0);
            translateAnimation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    view.requestLayout();
                    //通知监听
                    if (mkListener != null) {
                        mkListener.onLayoutDidShow(heyBoardHeight);
                    }
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            translateAnimation.setInterpolator(new DecelerateInterpolator(1.8f));
            //就在结束位置不要动了
            translateAnimation.setFillBefore(true);
            translateAnimation.setFillAfter(true);
            translateAnimation.setDuration(duration);

            //然后设置最终位置
            LayoutParams layoutParams = (LayoutParams) view.getLayoutParams();
            layoutParams.bottomMargin = heyBoardHeight;
            layoutParams.topMargin = -heyBoardHeight;
            view.setLayoutParams(layoutParams);
            //开始动画
            view.startAnimation(translateAnimation);
        }
    }


    /************
     * 整个页面下移
     */
    public void ScrollDown(int duration) {
        //假如已经下移了那么就不做处理
        if (!isScrolled) {
            return;
        }
        if (getChildCount() > 0) {
            //通知监听
            if (mkListener != null) {
                mkListener.onLayoutDissMiss(heyBoardHeight);
            }
            //回来了，没有被上移
            isScrolled = false;

            //获取第一个子view
            final View view = getChildAt(0);
            //清理动画
            view.clearAnimation();
            //先设置结束位置
            LayoutParams layoutParams = (LayoutParams) view.getLayoutParams();
            layoutParams.bottomMargin = 0;
            layoutParams.topMargin = 0;
            view.setLayoutParams(layoutParams);

            //然后进行位移操作
            TranslateAnimation translateAnimation = new TranslateAnimation(0, 0, -heyBoardHeight, 0);
            translateAnimation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    //通知监听
                    if (mkListener != null) {
                        mkListener.onLayoutDidDissMiss(heyBoardHeight);
                    }
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            translateAnimation.setInterpolator(new DecelerateInterpolator(1.8f));
            translateAnimation.setFillAfter(true);
            translateAnimation.setDuration(duration);
            view.startAnimation(translateAnimation);
        }
        //smoothScrollTo(0, 0, duration);
    }


}
