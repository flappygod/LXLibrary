package com.flappygod.lipo.lxlibrary.Adapters.BaseAdapters;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;


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

    // 线程添加的消息
    protected final static int ADDITEMS = 0;
    // 线程错误的消息
    protected final static int THREADERROR = 1;



    // 数据获取的监听
    protected BaseDatagetListener<T> dataListener;
    // 存储的listData
    protected List<T> ListData;
    // 用于数据下载的Handler
    protected Handler dataHandler;
    // 线程是否忙碌的标志
    protected boolean ThreadBusy = false;




    // 用于当前是否忙碌的hanlder
    protected Handler busyHandler;
    //是否忙碌
    protected boolean Busy = false;



    public BaseDatagetAdapter(Context context) {
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
            BaseDatagetAdapter.this.notifyDataSetChanged();
        }
    }

    /***********
     * handler
     */
    static class DHandler extends Handler {
        //弱引用
        private WeakReference<BaseDatagetAdapter> weakAdapter;
        //下一个状态
        private boolean nextFlag;

        DHandler(BaseDatagetAdapter adapter, boolean nextFlag) {
            this.weakAdapter = new WeakReference<BaseDatagetAdapter>(adapter);
            this.nextFlag = nextFlag;
        }

        public void handleMessage(Message msg) {
            BaseDatagetAdapter adapter = weakAdapter.get();
            if (adapter != null ) {
                //不再忙碌的时候就执行刷新操作
                if(nextFlag==false) {
                    adapter.notifyDataSetChanged();
                }
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
                    listener.ThreadError(new Exception("No error message  response"));
                }
                //适配器状态更改
                adapter.ThreadBusy = false;
            }
            if (msg.what == ADDITEMS) {
                //获取到数据
                ArrayList<T> list = (ArrayList<T>) msg.obj;

                adapter.ListData.clear();
                //添加所有数据
                adapter.ListData.addAll(list);
                //刷新页面
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
        dataHandler = new DataHandler(this);
    }

    /*****************
     * 获取数据的线程
     *****************/
    public synchronized boolean startDataThread() {

        // 如果线程不忙碌，那么就把它置为忙碌
        if (!ThreadBusy)
            ThreadBusy = true;
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