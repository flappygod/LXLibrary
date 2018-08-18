package com.flappygod.lipo.lxlibrary.Adapters.BaseAdapters;

import android.os.Handler;
import android.os.Message;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by yang on 2016/3/28.
 */
public abstract class BaseExpandbleDatagetAdapter<T> extends
        BaseExpandableListAdapter {

    // 数据获取的监听
    protected BaseDatagetListener<T> dataListener;
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

    //列表
    private ExpandableListView listView;


    public BaseExpandbleDatagetAdapter() {
        super();
        ListData = new ArrayList<T>();
        handlerCreate();
    }

    /**************
     * 设置刷新的列表
     *
     * @param listView 列表
     */
    public void setExpandedAllRefresh(ExpandableListView listView) {
        this.listView = listView;
    }


    /*******************
     * 设置数据加载监听
     *
     * @param l 监听
     */
    public void setBaseDatagetListener(BaseDatagetListener<T> l) {
        dataListener = l;
    }

    //展开所有数据
    public void ExpandAll() {
        if (listView != null) {
            int count = listView.getExpandableListAdapter().getGroupCount();
            for (int s = 0; s < count; s++)
                listView.expandGroup(s);
        }
    }


    //获取数据
    public List<T> getListData() {
        return ListData;
    }


    //接受返回消息的handler
    static class DataHandler<T> extends Handler {
        private WeakReference<BaseExpandbleDatagetAdapter<T>> weakReferenceadapter;

        public DataHandler(BaseExpandbleDatagetAdapter<T> adapter) {
            this.weakReferenceadapter = new WeakReference<BaseExpandbleDatagetAdapter<T>>(adapter);
        }

        public void handleMessage(Message msg) {
            //判断适配器还是否存在
            BaseExpandbleDatagetAdapter<T> adapter = weakReferenceadapter.get();
            if (adapter == null)
                return;

            //取得监听
            BaseDatagetListener<T> listener = adapter.dataListener;
            if (msg.what == THREADERROR) {
                if (listener != null && msg.obj != null) {
                    listener.ThreadError((Exception) msg.obj);
                } else if (listener != null) {
                    listener.ThreadError(new Exception(
                            "no error message  response"));
                }
                adapter.THREADBUSY = false;
            }
            if (msg.what == ADDITEMS) {
                ArrayList<T> list = (ArrayList<T>) msg.obj;
                for (int s = 0; s < list.size(); s++) {
                    adapter.ListData.add(list.get(s));
                }
                adapter.notifyDataSetChanged();
                adapter.ExpandAll();
                // 数据完成了
                if (listener != null) {
                    listener.DataReady(adapter.ListData);
                }
            }
        }
    }

    //创建handler
    private void handlerCreate() {
        myHandler = new DataHandler(this);
    }

    /*****************
     * 获取数据的线程
     *****************/
    public synchronized boolean startDataThread() {
        if (dataListener != null) {
            dataListener.Start();
        }
        // 如果线程不忙碌，那么就把它置为忙碌
        if (!THREADBUSY)
            THREADBUSY = true;
            // 如果线程忙碌
        else
            return false;

        new Thread() {
            public void run() {
                List<T> list;
                try {
                    list = BaseExpandbleDatagetAdapter.this.getDataThreadRun();
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
        return  true;
    }

    /*****************************
     * 这里已经使用了异步加载 只需要传入获取数据的方式即可 这里不允许做界面刷新的操作
     *
     * @throws Exception
     *****************************/
    protected abstract List<T> getDataThreadRun() throws Exception;


}
