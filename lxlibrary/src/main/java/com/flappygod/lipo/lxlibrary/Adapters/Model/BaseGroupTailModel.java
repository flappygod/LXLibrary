package com.flappygod.lipo.lxlibrary.Adapters.Model;

import java.util.ArrayList;
import java.util.List;

/***************
 * 可以展开的groupModel
 * Created by yang on 2016/8/16.
 */
public class BaseGroupTailModel<T> {
    // 设置最大的page为负一，表示不限制最大页数，直到拿不到数据
    private int maxPage = -1;
    // 当前属于第几页 默认是第一页
    private int page = 1;
    //子列表
    private List<T> childList=new ArrayList<T>();

    public List<T> getChildList() {
        return childList;
    }

    public void setChildList(List<T> childList) {
        this.childList = childList;
    }

    public int getMaxPage() {
        return maxPage;
    }

    public void setMaxPage(int maxPage) {
        this.maxPage = maxPage;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

}
