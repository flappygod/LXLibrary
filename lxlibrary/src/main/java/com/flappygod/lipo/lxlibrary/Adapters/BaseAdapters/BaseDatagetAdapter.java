package com.flappygod.lipo.lxlibrary.Adapters.BaseAdapters;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;


import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/*********************
 * Package Name:com.holpe.maike.mtm.base.adapter <br/>
 * ClassName: BaseDatagetAdapter <br/>
 * Function: TODO 自动获取数据的适配器，要求数据格式T已知 <br/>
 * date: 2015-7-27 上午10:18:32 <br/>
 *
 * @author lijunlin
 */

public abstract class BaseDatagetAdapter<T> extends BaseAdapter {

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
    //是否忙碌
    private boolean BUSY = false;
    //是否忙碌
    private boolean nowBUSY = false;

    public BaseDatagetAdapter(Context context) {
        super();
        ListData = new ArrayList<T>();
        handlerCreate();
    }

    /*****************************
     * 优化性能，当busy的时候就不再加载，这个可以再滑动的时候动态设置
     */
    public void setBusy(final Boolean b) {
        nowBUSY = b;
        if (b) {
            BUSY = b;
            return;
        }
        Handler handler = new BaseDatagetAdapter.DHandler(this, b);
        Message m = handler.obtainMessage(0);
        handler.sendMessageDelayed(m, 500);
    }

    /***********
     * handler
     */
    static class DHandler extends Handler {
        private WeakReference<BaseDatagetAdapter> weakReferenceadapter;
        private boolean b;

        DHandler(BaseDatagetAdapter adapter, boolean b) {
            this.weakReferenceadapter = new WeakReference<BaseDatagetAdapter>(adapter);
            this.b = b;
        }

        public void handleMessage(Message msg) {
            BaseDatagetAdapter adapter = weakReferenceadapter.get();
            if (adapter != null && !adapter.nowBUSY) {
                adapter.BUSY = b;
                adapter.notifyDataSetChanged();
            }
        }
    }


    /*******************
     * 设置监听
     */
    public void setBaseDatagetListener(BaseDatagetListener<T> l) {
        dataListener = l;
    }

    /************
     * 获取数据
     * @return
     */
    public List<T> getListData() {
        return ListData;
    }



    //接受返回消息的handler
    static class DataHandler<T> extends Handler {
        //弱引用防止内存泄露，虽然一般并没什么鸟用
        private WeakReference<BaseDatagetAdapter<T>> weakReferenceadapter;

        //构造器
        public DataHandler(BaseDatagetAdapter<T> adapter) {
            this.weakReferenceadapter = new WeakReference<BaseDatagetAdapter<T>>(adapter);
        }

        //发送消息过来
        public void handleMessage(Message msg) {
            //判断适配器还是否存在
            BaseDatagetAdapter<T> adapter = weakReferenceadapter.get();
            //已经不存在了呗回收了就不再刷新
            if (adapter == null)
                return;
            //取得监听
            BaseDatagetListener<T> listener = adapter.dataListener;
            //线程执行的方法中出现异常
            if (msg.what == THREADERROR) {
                //回调不为空而且异常不为空
                if (listener != null && msg.obj != null) {
                    listener.ThreadError((Exception) msg.obj);
                }
                //回调不为空但是异常为空，这种情况应该几乎没有
                else if (listener != null) {
                    listener.ThreadError(new Exception(
                            "no error message  response"));
                }
                //适配器状态更改
                adapter.THREADBUSY = false;
            }
            if (msg.what == ADDITEMS) {
                //获取到数据
                ArrayList<T> list = (ArrayList<T>) msg.obj;

                adapter.ListData.clear();
                //添加所有数据
                adapter.ListData.addAll(list);
                /*for (int s = 0; s < list.size(); s++) {
                    adapter.ListData.add(list.get(s));
                }*/
                adapter.notifyDataSetChanged();
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

        // 如果线程不忙碌，那么就把它置为忙碌
        if (!THREADBUSY)
            THREADBUSY = true;
            // 如果线程忙碌
        else
            return false;

        if (dataListener != null) {
            dataListener.Start();
        }

        new Thread() {
            public void run() {
                List<T> list;
                try {
                    list = BaseDatagetAdapter.this.getDataThreadRun();
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

        return true;
    }

    /*****************************
     * 这里已经使用了异步加载 只需要传入获取数据的方式即可 这里不允许做界面刷新的操作
     * @throws Exception
     *****************************/
    protected abstract List<T> getDataThreadRun() throws Exception;



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