package com.flappygod.lipo.lxlibrary.Tools;

import android.view.View;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ListView;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by lijunlin on 2016/4/6.
 */
public class ListViewTool {
    private static Field mFlingEndField = null;
    private static Method mFlingEndMethod = null;

    static {
        try {
            mFlingEndField = AbsListView.class.getDeclaredField("mFlingRunnable");
            mFlingEndField.setAccessible(true);
            mFlingEndMethod = mFlingEndField.getType().getDeclaredMethod("endFling");
            mFlingEndMethod.setAccessible(true);
        } catch (Exception e) {
            mFlingEndMethod = null;
        }
    }

    /***********
     * 停止列表的滚动
     *
     * @param list listview
     */
    public static void stop(ListView list) {
        if (mFlingEndMethod != null) {
            try {
                mFlingEndMethod.invoke(mFlingEndField.get(list));
            } catch (Exception e) {
            }
        }
    }


    /***********
     * 滚动到底部
     *
     * @param list listview
     */
    public static void scrollToEnd(final ListView list) {
        //滚动到底部
        final BaseAdapter adapter = (BaseAdapter) list
                .getAdapter();
        if (adapter != null) {

            list.requestFocusFromTouch();//获取焦点
            //滚动到底部
            list.setSelection(adapter.getCount() - 1);
        }
    }


    //高度列表
    public static int getScrollY(AbsListView view,ConcurrentHashMap<Integer, Integer> heightMap){
        int firstVisibleItem=view.getFirstVisiblePosition();
        //保存当前的高度
        View firstView = null;
        if (view.getChildCount() > 0) {
            firstView = view.getChildAt(0);
        }
        if (firstView != null) {
            heightMap.put(new Integer(firstVisibleItem), firstView.getHeight());
            int fh = 0;
            for (int s = 0; s < firstVisibleItem; s++) {
                if (heightMap.get(new Integer(s)) == null) {
                    fh += 0;
                } else {
                    fh += heightMap.get(new Integer(s));
                }
            }
            fh += -firstView.getTop();
            return fh;
        }
        return 0;
    }



}
