package com.flappygod.lipo.lxlibrary.Adapters.BaseAdapters;

import android.os.Handler;
import android.os.Message;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;


import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/********************
 * 二级可展开的适配器，此适配器适用于二级列表组的加载，
 * 极少使用，但是还是把它卸载这里了
 *
 * @author lijunlin
 */
public abstract class BaseExpandbleGroupPageAdapter<T> extends
        BaseExpandableListAdapter {


    // 线程添加的消息
    protected final int ADDITEMS = 0;
    // 线程错误的消息
    protected final int THREADERROR = 1;


    //加载监听
    protected BaseExpandbleGroupPageListener<T> mbaseListener;
    // 存储的listData
    public List<T> ListData;
    // hadler
    protected Handler dataHandler;
    // 线程是否忙碌的标志
    protected boolean ThreadBusy = false;


    // 每次获取数据的大小
    protected int size;
    // 当前属于第几页 默认是第一页
    protected int page = 1;
    // 设置最大的page为负一，表示不限制最大页数，直到拿不到数据
    private int maxpage = -1;


    // 用于当前是否忙碌的hanlder
    protected Handler busyHandler;
    //是否忙碌
    protected boolean Busy = false;


    //列表
    private ExpandableListView listView;


    /***************
     * 通过大小构建适配器
     * @param size 每页大小
     */
    public BaseExpandbleGroupPageAdapter(int size) {
        ListData = new ArrayList<T>();
        this.size = size;
        handlerCreate();
    }

    /*******
     * 构造器
     *
     * @param size    分页的大小
     * @param maxpage 分页的最大页数
     */
    public BaseExpandbleGroupPageAdapter(int size, int maxpage) {
        super();
        ListData = new ArrayList<T>();
        this.size = size;
        this.maxpage = maxpage;
        handlerCreate();
    }


    //判断当前是否忙碌
    public boolean isBusy() {
        return Busy;
    }

    /*****************************
     * 设置正在滑动，延迟加载
     */
    public void setBusy(boolean flag) {

        //如果为空则创建handler
        if (busyHandler == null) {
            busyHandler = new DHandler(this, false);
        }

        //先清空为零的信息
        busyHandler.removeMessages(0);

        if (flag == true) {
            //当前非常忙碌
            Busy = true;
            //然后创建消息，500毫秒后执行为不忙碌
            Message m = busyHandler.obtainMessage(0);
            //500毫秒后执行
            busyHandler.sendMessageDelayed(m, 500);
        }
        //如果当前的状态是false
        else {
            //当前非常忙碌
            Busy = false;
            //然后立即刷新页面
            BaseExpandbleGroupPageAdapter.this.notifyDataSetChanged();
        }
    }

    /***********
     * handler
     */
    static class DHandler extends Handler {
        //弱引用
        private WeakReference<BaseExpandbleGroupPageAdapter> weakAdapter;
        //下一个状态
        private boolean nextFlag;

        DHandler(BaseExpandbleGroupPageAdapter adapter, boolean nextFlag) {
            this.weakAdapter = new WeakReference<BaseExpandbleGroupPageAdapter>(adapter);
            this.nextFlag = nextFlag;
        }

        public void handleMessage(Message msg) {
            BaseExpandbleGroupPageAdapter adapter = weakAdapter.get();
            if (adapter != null) {
                //不再忙碌的时候就执行刷新操作
                if (nextFlag == false) {
                    adapter.notifyDataSetChanged();
                }
            }
        }
    }


    /***************
     * 设置最大页数
     *
     * @param maxpage 最大页数
     */
    public void setMaxPage(int maxpage) {
        this.maxpage = maxpage;
    }

    /*******
     * 获取最大页数
     *
     * @return
     */
    public int getMaxPage() {
        return maxpage;
    }

    /*********
     * 设置listview
     *
     * @param listView
     */
    public void setExpandedAllRefresh(ExpandableListView listView) {
        this.listView = listView;
    }


    /*******************
     * 设置加载的监听
     * @param l
     */
    public void setOnLoadStateListenser(BaseExpandbleGroupPageListener<T> l) {
        mbaseListener = l;
    }

    /********
     * 自动打开所有的item
     */
    private void ExpandAll() {
        if (listView != null) {
            int count = listView.getExpandableListAdapter().getGroupCount();
            for (int s = 0; s < count; s++)
                listView.expandGroup(s);
        }
    }


    /************
     * 获取listdata
     *
     * @return
     */
    public List<T> getListData() {
        return ListData;
    }


    //接受返回消息的handler
    static class DataHandler<T> extends Handler {
        //弱引用
        private WeakReference<BaseExpandbleGroupPageAdapter<T>> weakReferenceadapter;

        //构造器
        public DataHandler(BaseExpandbleGroupPageAdapter<T> adapter) {
            this.weakReferenceadapter = new WeakReference<BaseExpandbleGroupPageAdapter<T>>(adapter);
        }

        public void handleMessage(Message msg) {
            //判断适配器还是否存在
            BaseExpandbleGroupPageAdapter<T> adapter = weakReferenceadapter.get();
            if (adapter == null)
                return;

            //取得监听
            BaseExpandbleGroupPageListener<T> mbaseListener = adapter.mbaseListener;
            if (msg.what == adapter.THREADERROR) {
                if (mbaseListener != null && msg.obj != null) {
                    mbaseListener.ThreadError((Exception) msg.obj);
                } else if (mbaseListener != null) {
                    mbaseListener.ThreadError(new Exception(
                            "no error message   response"));
                }
                adapter.ThreadBusy = false;

            }
            if (msg.what == adapter.ADDITEMS) {
                @SuppressWarnings("unchecked")
                ArrayList<T> list = (ArrayList<T>) msg.obj;

                adapter.ListData.addAll(list);
                /* for (int s = 0; s < list.size(); s++) {
                    adapter.ListData.add(list.get(s));
                }*/
                // 如果正好等于，就表示还没有加载完
                if (list.size() == adapter.size) {
                    adapter.page++;
                    // 刷新界面
                    adapter.notifyDataSetChanged();
                    // 如果为负数，代表不做限制
                    if (adapter.maxpage < 0) {
                        // 可以继续加载下一层了
                        adapter.ThreadBusy = false;
                        if (mbaseListener != null)
                            mbaseListener.PageLoaded(list, adapter.page - 1);
                    } // 如果有最大页数的限制的话
                    else if (adapter.page <= adapter.maxpage) {
                        // 可以继续加载下一层了
                        adapter.ThreadBusy = false;
                        if (mbaseListener != null)
                            mbaseListener.PageLoaded(list, adapter.page - 1);
                    } else {
                        // 加载完成了
                        adapter.ThreadBusy = true;
                        if (mbaseListener != null) {
                            mbaseListener.PageLoaded(list, adapter.page - 1);
                            mbaseListener.PageEnd();
                        }
                    }
                    adapter.ExpandAll();
                } else {
                    //刷新页面
                    adapter.notifyDataSetChanged();
                    //展开所有item
                    adapter.ExpandAll();
                    // 数据完成了
                    if (mbaseListener != null) {
                        //页面加载成功
                        mbaseListener.PageLoaded(list, adapter.page);
                        //完结撒花
                        mbaseListener.PageEnd();
                    }
                    //没有数据的时候
                    if (mbaseListener != null && adapter.page == 1
                            && list.size() == 0) {
                        mbaseListener.NoneData();
                    }
                }
            }
        }
    }

    //创建handler
    private void handlerCreate() {
        dataHandler = new DataHandler(this);
    }

    /*****************
     * 获取下一批数据
     *****************/
    protected synchronized boolean getNextItems() {
        // 如果线程不忙碌，那么就把它置为忙碌
        if (!ThreadBusy)
            ThreadBusy = true;
            // 如果线程忙碌
        else
            return false;

        if (mbaseListener != null)
            mbaseListener.StartLoad(page);

        new Thread() {
            public void run() {
                List<T> list;
                try {
                    list = BaseExpandbleGroupPageAdapter.this.getDataThreadRun(
                            page, size);
                    if (list == null) {
                        list = new ArrayList<T>();
                    }
                    Message msg = new Message();
                    msg.what = ADDITEMS;
                    msg.obj = list;
                    dataHandler.sendMessage(msg);
                } catch (Exception e) {
                    Message msg = new Message();
                    msg.what = THREADERROR;
                    msg.obj = e;
                    dataHandler.sendMessage(msg);
                }

            }
        }.start();
        return true;
    }

    /*****************************
     * 这里已经使用了异步加载 只需要传入获取数 据的方式即可 这里不允许做界面刷新的操作
     *
     * @param page 页码
     * @param size 大小
     * @throws Exception
     *****************************/
    protected abstract List<T> getDataThreadRun(int page, int size)
            throws Exception;


}
