package com.flappygod.lipo.lxlibrary.Adapters.BaseAsyncAdapter;

import android.content.Context;
import android.os.Message;

import com.flappygod.lipo.lxlibrary.Adapters.BaseAdapters.BaseExpandbleDatagetAdapter;
import com.flappygod.lipo.lxlibrary.Adapters.BaseAsyncAdapter.CallBack.AsyncAdapterCallback;

import java.util.List;

/***********************
 * Created by yang on 2016/8/29.
 ***********************/
public abstract class AsyncExpandbleDatagetAdapter<T> extends BaseExpandbleDatagetAdapter<T> {

    public AsyncExpandbleDatagetAdapter(Context context) {
        super();
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

        //开始线程
        getDataThread(new AsyncAdapterCallback<T>() {
            @Override
            public void success(List<T> data) {
                Message msg = new Message();
                msg.what = ADDITEMS;
                msg.obj = data;
                dataHandler.sendMessage(msg);
            }

            @Override
            public void failure(Exception e) {
                Message msg = new Message();
                msg.what = THREADERROR;
                msg.obj = e;
                dataHandler.sendMessage(msg);
            }
        });
        return true;
    }


    /*************
     * 重写了这个方法，因为没有用来着
     *
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
