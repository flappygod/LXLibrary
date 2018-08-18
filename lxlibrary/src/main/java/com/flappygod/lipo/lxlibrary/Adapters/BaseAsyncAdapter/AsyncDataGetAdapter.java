package com.flappygod.lipo.lxlibrary.Adapters.BaseAsyncAdapter;

import android.content.Context;
import android.os.Message;


import com.flappygod.lipo.lxlibrary.Adapters.BaseAdapters.BaseDatagetAdapter;
import com.flappygod.lipo.lxlibrary.Adapters.BaseAsyncAdapter.CallBack.AsyncAdapterCallback;

import java.util.List;

/**
 * Created by yang on 2016/8/25.
 */
public abstract  class AsyncDataGetAdapter<T> extends BaseDatagetAdapter<T> {



    //适配器构造
    public AsyncDataGetAdapter(Context context) {
        super(context);
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

        //开始线程
        getDataThread(new AsyncAdapterCallback<T>() {
            @Override
            public void success(List<T> data) {
                Message msg = new Message();
                msg.what = ADDITEMS;
                msg.obj = data;
                myHandler.sendMessage(msg);
                THREADBUSY = false;
            }

            @Override
            public void failure(Exception e) {
                Message msg = new Message();
                msg.what = THREADERROR;
                msg.obj = e;
                myHandler.sendMessage(msg);
                THREADBUSY = false;
            }
        });
        return true;
    }



    /*************
     * 重写了这个方法，因为没有用来着
     * @return
     * @throws Exception
     */
    protected List<T> getDataThreadRun()
            throws Exception {
        return null;
    }


    //获取数据
    protected abstract void getDataThread(final AsyncAdapterCallback<T> callback);

}
