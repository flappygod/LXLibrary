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



    // 线程添加的消息
    protected final static int ADDITEMS = 0;
    // 线程错误的消息
    protected final static int THREADERROR = 1;


    // 数据获取的监听
    protected BaseDatagetListener<T> dataListener;
    // 存储的listData
    protected List<T> ListData;
    // hadler
    protected Handler dataHandler;
    // 线程是否忙碌的标志
    protected boolean ThreadBusy = false;




    // 用于当前是否忙碌的hanlder
    protected Handler busyHandler;
    //是否忙碌
    protected boolean Busy = false;



    //列表
    private ExpandableListView listView;


    public BaseExpandbleDatagetAdapter() {
        super();
        ListData = new ArrayList<T>();
        handlerCreate();
    }

    //判断当前是否忙碌
    public boolean isBusy() {
        return Busy;
    }

    /*****************************
     * 设置正在滑动，延迟加载
     */
    public void setBusy(boolean  flag) {

        //如果为空则创建handler
        if(busyHandler==null) {
            busyHandler = new DHandler(this, false);
        }

        //先清空为零的信息
        busyHandler.removeMessages(0);

        if(flag==true){
            //当前非常忙碌
            Busy = true;
            //然后创建消息，500毫秒后执行为不忙碌
            Message m = busyHandler.obtainMessage(0);
            //500毫秒后执行
            busyHandler.sendMessageDelayed(m, 500);
        }
        //如果当前的状态是false
        else{
            //当前非常忙碌
            Busy = false;
            //然后立即刷新页面
            BaseExpandbleDatagetAdapter.this.notifyDataSetChanged();
        }
    }

    /***********
     * handler
     */
    static class DHandler extends Handler {
        //弱引用
        private WeakReference<BaseExpandbleDatagetAdapter> weakAdapter;
        //下一个状态
        private boolean nextFlag;

        DHandler(BaseExpandbleDatagetAdapter adapter, boolean nextFlag) {
            this.weakAdapter = new WeakReference<BaseExpandbleDatagetAdapter>(adapter);
            this.nextFlag = nextFlag;
        }

        public void handleMessage(Message msg) {
            BaseExpandbleDatagetAdapter adapter = weakAdapter.get();
            if (adapter != null ) {
                //不再忙碌的时候就执行刷新操作
                if(nextFlag==false) {
                    adapter.notifyDataSetChanged();
                }
            }
        }
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
                adapter.ThreadBusy = false;
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
        dataHandler = new DataHandler(this);
    }

    /*****************
     * 获取数据的线程
     *****************/
    public synchronized boolean startDataThread() {
        if (dataListener != null) {
            dataListener.Start();
        }
        // 如果线程不忙碌，那么就把它置为忙碌
        if (!ThreadBusy)
            ThreadBusy = true;
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
                    dataHandler.sendMessage(msg);
                } catch (Exception e) {
                    Message msg = new Message();
                    msg.what = THREADERROR;
                    msg.obj = e;
                    dataHandler.sendMessage(msg);
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
