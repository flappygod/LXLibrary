package com.flappygod.lipo.lxlibrary.Widget;


import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.TabHost;


/****************
 * Package Name:com.holpe.maike.shoppingguide.widget <br/>
 * ClassName: AnimationFragmentTabhost <br/>
 * Function: TODO 带有animation的fragment <br/>
 * date: 2015-8-8 上午9:27:57 <br/>
 *
 * @author lijunlin
 */
public class AnimationTabhost extends TabHost {

    private Animation slideLeftIn;
    private Animation slideLeftOut;
    private Animation slideRightIn;
    private Animation slideRightOut;

    private boolean isOpenAnimation;
    //从当前开始不进行动画的数量
    private int NoAnimationTime = 0;

    public static boolean ISANIMATING = false;

    private int mTabCount;
    private int selected;
    private AnimateEnd myAnimateEnd;

    /******
     * 设置是否打开动画
     *
     * @param b
     */
    public void setOpenAnimation(boolean b) {
        isOpenAnimation = b;
    }

    /********
     * 设置没有动画
     */
    public void setNoAnimationNexttime() {
        if (isOpenAnimation == true) {
            //设置下一次动画不执行
            NoAnimationTime = 1;
        }
    }

    /*****
     * 获取tab的数量
     *
     * @return
     */
    public int getTabCount() {
        return mTabCount;
    }

    public int getmySelected() {
        return selected;
    }

    public void setCurrentTabByTag(String tag, boolean b) {
        if (!b) {
            setNoAnimationNexttime();
        }
        super.setCurrentTabByTag(tag);
    }

    @Override
    public void addTab(TabSpec tabSpec) {
        mTabCount++;
        super.addTab(tabSpec);
    }

    /********
     * 构造器
     *
     * @param context 上下文
     */
    public AnimationTabhost(Context context) {
        super(context);
        init(context);
    }


    /**********
     * 构造器
     *
     * @param context 上下文
     * @param attrs   参数
     */
    public AnimationTabhost(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }


    private void init(Context context) {

        //slideLeftIn=new TranslateAnimation(Animation.RELATIVE_TO_SELF,-1.0f,Animation.RELATIVE_TO_SELF,0.0f,Animation.RELATIVE_TO_SELF,0.0f,Animation.RELATIVE_TO_SELF,0.0f);
        slideLeftIn=new AlphaAnimation(0.0f,1.0f);
        slideLeftIn.setDuration(250);
        //slideRightOut=new TranslateAnimation(Animation.RELATIVE_TO_SELF,0.0f,Animation.RELATIVE_TO_SELF,1.0f,Animation.RELATIVE_TO_SELF,0.0f,Animation.RELATIVE_TO_SELF,0.0f);
        slideRightOut=new AlphaAnimation(1.0f,0.0f);
        slideRightOut.setDuration(250);


        //slideLeftOut = new TranslateAnimation(Animation.RELATIVE_TO_SELF,0.0f,Animation.RELATIVE_TO_SELF,-1.0f,Animation.RELATIVE_TO_SELF,0.0f,Animation.RELATIVE_TO_SELF,0.0f);
        slideLeftOut=new AlphaAnimation(1.0f,0.0f);
        slideLeftOut.setDuration(250);
        //slideRightIn = new TranslateAnimation(Animation.RELATIVE_TO_SELF,1.0f,Animation.RELATIVE_TO_SELF,0.0f,Animation.RELATIVE_TO_SELF,0.0f,Animation.RELATIVE_TO_SELF,0.0f);
        slideRightIn=new AlphaAnimation(0.0f,1.0f);
        slideRightIn.setDuration(250);


        /*slideLeftIn = AnimationUtils.loadAnimation(context, R.anim.o_exit_tran);
        slideRightOut = AnimationUtils.loadAnimation(context, R.anim.o_enter_tran);

        slideLeftOut = AnimationUtils.loadAnimation(context, R.anim.exit_tran);
        slideRightIn = AnimationUtils.loadAnimation(context, R.anim.enter_tran);*/



        AnimationListener listenr = new AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
                ISANIMATING = true;
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                ISANIMATING = false;
                if (myAnimateEnd != null) {
                    myAnimateEnd.changeTabAnimateEnd();
                }
                //获取当前的View
                /*View currentView = getCurrentView();
                if(currentView instanceof ViewGroup){
                    //关闭
                    if(!currentView.isHardwareAccelerated()){
                        //软件方式
                        currentView.setLayerType(LAYER_TYPE_NONE,null);
                    }
                }*/
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        };
        slideRightOut.setAnimationListener(listenr);
        slideLeftOut.setAnimationListener(listenr);
        //默认情况下打开动画
        isOpenAnimation = true;
        //默认情况下第一次不执行animation
        NoAnimationTime = 0;

    }


    protected void onFinishInflate() {
        super.onFinishInflate();
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (ISANIMATING) {
            return true;
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public void setCurrentTab(int index) {
        //获取当前的tab
        int mCurrentTabID = getCurrentTab();

        //就这里
        if(mCurrentTabID<0&&index==0){
            //设置tab
            super.setCurrentTab(index);
            return;
        }

        //获取当前的View
        View currentView = getCurrentView();
        if(currentView instanceof ViewGroup){
            final ViewGroup group= (ViewGroup) currentView;
            //关闭这个，防止每次都生成一个bitmap
            group.setDrawingCacheEnabled(false);
            //如果没有开启硬件加速
            /*if(!group.isHardwareAccelerated()){
                //软件方式
                group.setLayerType(LAYER_TYPE_SOFTWARE,null);
            }*/
        }
        //不为空的时候
        if (currentView != null) {
            if (isOpenAnimation && NoAnimationTime > 0) {

            } else if (isOpenAnimation) {
                if (index > mCurrentTabID) {
                    currentView.startAnimation(slideLeftOut);
                } else if (index < mCurrentTabID) {
                    currentView.startAnimation(slideRightOut);
                }
            }
        }
        //设置tab
        super.setCurrentTab(index);

        //重新获取当前的view
        currentView = getCurrentView();
        if(currentView instanceof ViewGroup){
            final ViewGroup group= (ViewGroup) currentView;
            //关闭这个，防止每次都生成一个bitmap
            group.setDrawingCacheEnabled(false);
            //如果没有开启硬件加速
            /*if(!group.isHardwareAccelerated()){
                //软件方式
                group.setLayerType(LAYER_TYPE_SOFTWARE,null);
            }*/
        }
        if (currentView != null) {
            if (isOpenAnimation && NoAnimationTime > 0) {
                NoAnimationTime--;
            } else if (isOpenAnimation) {
                if (index > mCurrentTabID) {
                    currentView.startAnimation(slideRightIn);
                } else if (index < mCurrentTabID) {
                    currentView.startAnimation(slideLeftIn);
                }
            }
        }
    }

    /******************************
     * animation结束
     ******************************/
    public interface AnimateEnd {
        void changeTabAnimateEnd();
    }

    /******************************
     * 动画结束的监听
     ******************************/
    public void setAnimateEnd(AnimateEnd a) {
        myAnimateEnd = a;
    }

}
