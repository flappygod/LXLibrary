package com.flappygod.lipo.lxlibrary.Adapters.BaseAdapters;

import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;


import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/***************************
 * Package Name:com.holpe.maike.shoppingguide.base.adapter <br/>
 * ClassName: BaseTailPageAdapter <br/>
 * Function: TODO 适用于下拉加载更多的适配器 <br/>
 * date: 2015-8-8 上午9:26:12 <br/>
 *
 * @author lijunlin
 */
public abstract class BaseTailPageAdapter<T> extends BaseAdapter {

    protected BaseTailPageListener<T> tailPageListener;
    // 每次获取数据的大小
    protected int size;
    // 当前属于第几页 默认是第一页
    protected int page = 1;
    // 存储的listData
    protected List<T> ListData;
    // hadler
    protected Handler myHandler;
    // 线程是否忙碌的标志
    protected boolean THREADBUSY = false;
    // 线程添加的消息
    protected final static int ADDITEMS = 0;
    // 线程错误的消息
    protected final static int THREADERROR = 1;
    // 设置最大的page为负一，表示不限制最大页数，直到拿不到数据
    private int maxpage = -1;


    /********
     * 构造器
     *
     * @param size    分页的大小
     */
    public BaseTailPageAdapter(int size) {
        super();
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
    public BaseTailPageAdapter( int size, int maxpage) {
        super();
        ListData = new ArrayList<T>();
        this.size = size;
        this.maxpage = maxpage;
        handlerCreate();
    }





    /*******************
     * 设置监听
     *******************/
    public void settailPageListener(BaseTailPageListener<T> l) {
        tailPageListener = l;
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

    /***********
     * 开始获取数据
     *
     * @return
     */
    public List<T> getListData() {
        return ListData;
    }


    //接受返回消息的handler
    static class DataHandler<T> extends Handler {
        private WeakReference<BaseTailPageAdapter<T>> weakReferenceadapter;

        public DataHandler(BaseTailPageAdapter<T> adapter) {
            this.weakReferenceadapter = new WeakReference<BaseTailPageAdapter<T>>(adapter);
        }

        public void handleMessage(Message msg) {

            //判断适配器还是否存在
            BaseTailPageAdapter<T> adapter = weakReferenceadapter.get();
            if (adapter == null)
                return;

            //取得监听
            BaseTailPageListener<T> tailPageListener = adapter.tailPageListener;

            if (msg.what == THREADERROR) {
                if (tailPageListener != null && msg.obj != null) {
                    tailPageListener.ThreadError((Exception) msg.obj);
                } else if (tailPageListener != null) {
                    tailPageListener.ThreadError(new Exception(
                            "no error message  response"));
                }
                adapter.THREADBUSY = false;
            }
            if (msg.what == ADDITEMS) {
                ArrayList<T> list = (ArrayList<T>) msg.obj;
                for (int s = 0; s < list.size(); s++) {
                    adapter.ListData.add(list.get(s));
                }
                // 如果正好等于，就表示还没有加载完
                if (list.size() == adapter.size) {
                    adapter.page++;
                    // 刷新界面
                    adapter.notifyDataSetChanged();
                    // 如果为负数，代表不做限制
                    if (adapter.maxpage < 0) {
                        // 可以继续加载下一层了
                        adapter.THREADBUSY = false;
                        if (tailPageListener != null)
                            tailPageListener.PageLoaded(list, adapter.page - 1);
                    }
                    // 如果有最大页数的限制的话
                    else if (adapter.page <= adapter.maxpage) {
                        // 可以继续加载下一层了
                        adapter.THREADBUSY = false;
                        if (tailPageListener != null)
                            tailPageListener.PageLoaded(list, adapter.page - 1);
                    } else {
                        // 加载完成了
                        adapter.THREADBUSY = true;
                        if (tailPageListener != null) {
                            tailPageListener.PageLoaded(list, adapter.page - 1);
                            tailPageListener.PageEnd();
                        }
                    }
                } else {
                    adapter.notifyDataSetChanged();
                    // 数据完成了
                    if (tailPageListener != null) {
                        tailPageListener.PageLoaded(list, adapter.page);
                        tailPageListener.PageEnd();
                        // 如果加载到最后还发现什么数据都没有的话
                        if (adapter.ListData.size() == 0) {
                            tailPageListener.NoneData();
                        }
                    }
                }
            }
        }
    }

    private void handlerCreate() {
        myHandler = new DataHandler<T>(this);
    }

    /*****************
     * 获取下一批数据
     *****************/
    public synchronized boolean getNextItems() {

        // 如果线程不忙碌，那么就把它置为忙碌
        if (!THREADBUSY)
            THREADBUSY = true;
        else
            return false;
        // 如果线程忙碌

        // 开始加载第几页
        if (tailPageListener != null) {
            tailPageListener.StartLoad(page);
        }
        //开始线程
        dataThreadStart();
        //成功执行线程
        return true;
    }


    /************
     * 开始线程
     */
    private void dataThreadStart() {
        new Thread() {
            public void run() {
                List<T> list;
                try {
                    list = BaseTailPageAdapter.this
                            .getDataThreadRun(page, size);
                    if (list == null)
                        list = new ArrayList<T>();
                    Message msg = new Message();
                    msg.what = ADDITEMS;
                    msg.obj = list;
                    myHandler.sendMessage(msg);
                } catch (Exception e) {
                    Message msg = new Message();
                    msg.what = THREADERROR;
                    msg.obj = e;
                    myHandler.sendMessage(msg);
                }
            }
        }.start();
    }

    /*****************************
     * 这里已经使用了异步加载 只需要传入获取数据的方式即可 这里不允许做界面刷新的操作
     *
     * @param page 组的内容
     * @param size 子页码
     * @throws Exception
     *****************************/
    protected abstract List<T> getDataThreadRun(int page, int size)
            throws Exception;


    @Override
    public int getCount() {
        return ListData.size();
    }

    @Override
    public abstract Object getItem(int position);

    @Override
    public abstract long getItemId(int position);

    @Override
    public abstract View getView(int position, View convertView,
                                 ViewGroup parent);

}