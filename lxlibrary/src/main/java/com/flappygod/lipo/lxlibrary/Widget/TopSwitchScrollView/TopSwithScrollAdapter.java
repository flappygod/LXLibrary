package com.flappygod.lipo.lxlibrary.Widget.TopSwitchScrollView;

import android.view.View;

/**
 * Created by yang on 2017/3/13.
 */
public abstract class TopSwithScrollAdapter implements TopSwitchScrollView.OnTopPositionChangedListener{


    //切换到某个position
    public void positionChanged(View  view,int position){
    }

    //获取这个View的kuandu
    public int  getViewWidthAtPosition(int position){
        return -1;
    }
    /**********
     * 获取某个View
     * @param position  坐标
     * @return
     */
    protected abstract View getViewAtPosition(int position);

    /*********
     * 获取View的数量
     * @return
     */
    protected abstract int getViewCount();

    /*******
     * 获取滑标的高度
     * @return
     */
    protected abstract int getScrollCursorHeight();

    /**************
     * 获取滑标的宽度
     * @return
     */
    protected abstract int getScrollCursorWidth();

    /***********
     * 获取滑标的颜色
     * @return
     */
    protected abstract int getScrollCursorColor();

}
