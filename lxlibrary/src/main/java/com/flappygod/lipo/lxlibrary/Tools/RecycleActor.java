package com.flappygod.lipo.lxlibrary.Tools;

import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yang on 2015/10/28.
 */
public class RecycleActor {
    //回收的view
    private List<View> viewList;

    public RecycleActor() {
        viewList = new ArrayList<View>();
    }


    /************
     * 回收并排除
     *
     * @param group  回收的view
     * @param tClass 排除的类
     */
    public void RecycleExcept(ViewGroup group, Class tClass) {
        int t = group.getChildCount();
        for (int s = 0; s < t; s++) {
            //清理动画
            group.getChildAt(s).clearAnimation();
            if (!tClass.isInstance(group.getChildAt(s))) {
                viewList.add(group.getChildAt(s));
            }
        }
        group.removeAllViews();
    }


    /**************
     * 对ViewGroup进行回收
     *
     * @param group 提供回收的viewgroup
     */
    public void Recycle(ViewGroup group) {
        int t = group.getChildCount();
        for (int s = 0; s < t; s++) {
            viewList.add(group.getChildAt(s));
        }
        group.removeAllViews();
    }

    /******************
     * 获取被回收的View
     *
     * @return 可以使用的View
     */
    public View getRecycledView() {
        if (viewList.size() == 0) {
            return null;
        } else {
            View v = viewList.get(0);
            viewList.remove(0);
            return v;
        }
    }

}
