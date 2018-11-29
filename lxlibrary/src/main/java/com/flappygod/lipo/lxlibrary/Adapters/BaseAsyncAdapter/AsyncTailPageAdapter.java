package com.flappygod.lipo.lxlibrary.Adapters.BaseAsyncAdapter;

import android.content.Context;
import android.os.Message;

import com.flappygod.lipo.lxlibrary.Adapters.BaseAdapters.BaseTailPageAdapter;
import com.flappygod.lipo.lxlibrary.Adapters.BaseAsyncAdapter.CallBack.AsyncAdapterCallback;

import java.util.List;

/**
 * Created by yang on 2016/8/16.
 */
public abstract class AsyncTailPageAdapter<T> extends BaseTailPageAdapter<T> {


    /******************
     * 适配器构造
     *
     * @param size    大小
     */
    public AsyncTailPageAdapter( int size) {
        super(size);
    }

    /*****************
     * 适配器构造
     *
     * @param context 上下文
     * @param size    大小
     * @param maxpage 最大页码
     */
    public AsyncTailPageAdapter(Context context, int size, int maxpage) {
        super( size, maxpage);
    }


    /*****************
     * 获取下一批数据
     */
    public synchronized boolean getNextItems() {

        // 如果线程不忙碌，那么就把它置为忙碌
        if (!ThreadBusy)
            ThreadBusy = true;
        else
            // 如果线程忙碌返回false
            return false;
        // 开始加载第几页
        if (tailPageListener != null) {
            tailPageListener.StartLoad(page);
        }
        //开始线程
        getDataThread(page, size, new AsyncAdapterCallback<T>() {
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
     * @param page 组的内容
     * @param size 子页码
     * @return
     * @throws Exception
     */
    protected List<T> getDataThreadRun(int page, int size)
            throws Exception {
        return null;
    }


    //获取数据
    protected abstract void getDataThread(int page, int size,final AsyncAdapterCallback callback);


}
