package com.flappygod.lipo.lxlibrary.BaseView.PopupView;

import android.animation.Animator;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewAnimationUtils;

/**
 * Created by yang on 2016/8/30.
 */
public class BaseRipplePopWindow extends BasepopupWindow {

    public BaseRipplePopWindow(Context context) {
        super(context);
    }

    public BaseRipplePopWindow(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BaseRipplePopWindow(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public BaseRipplePopWindow(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public BaseRipplePopWindow(View contentView) {
        super(contentView);
    }

    public BaseRipplePopWindow(int width, int height) {
        super(width, height);
    }

    public BaseRipplePopWindow(View contentView, int width, int height) {
        super(contentView, width, height);
    }

    public BaseRipplePopWindow(View contentView, int width, int height, boolean focusable) {
        super(contentView, width, height, focusable);
    }

    boolean Flag = false;

    public void dismiss() {
        //找到当前的contentView
        final View myView = getContentView();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && Flag == true) {
            Flag = false;
            int cx = (myView.getLeft() + myView.getRight()) / 2;
            int cy = myView.getBottom();
            int finalRadius = myView.getWidth() > myView.getHeight() ? myView.getWidth() : myView.getHeight();
            Animator anim = ViewAnimationUtils.createCircularReveal(myView, cx, cy, finalRadius, 0);
            anim.setDuration(500);
            anim.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    BaseRipplePopWindow.super.dismiss();
                }

                @Override
                public void onAnimationCancel(Animator animation) {
                }

                @Override
                public void onAnimationRepeat(Animator animation) {
                }
            });
            anim.start();
        }else{
            BaseRipplePopWindow.super.dismiss();
        }
    }

    public void showAsDropDown(View parent) {
        super.showAsDropDown(parent);
        startAnimation();
    }

    public void showAtLocation(View parent, int gravity, int x, int y) {
        super.showAtLocation(parent, gravity, x, y);
        startAnimation();
    }

    //显示
    public void showAsDropDown(View anchor, int xoff, int yoff, int gravity) {
        super.showAsDropDown(anchor,xoff,yoff,gravity);
        startAnimation();
    }

    private void startAnimation() {
        //找到当前的contentView
        final View myView = getContentView();
        myView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && Flag == false) {
                    Flag = true;
                    int cx = (myView.getLeft() + myView.getRight()) / 2;
                    int cy = myView.getBottom();
                    int finalRadius = myView.getWidth() > myView.getHeight() ? myView.getWidth() : myView.getHeight();
                    Animator anim = ViewAnimationUtils.createCircularReveal(myView, cx, cy, 0, finalRadius);
                    anim.setDuration(500);
                    anim.start();
                }
            }
        });
    }
}
