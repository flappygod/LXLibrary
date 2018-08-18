package com.flappygod.lipo.lxlibrary.Adapters.BaseAdapters;

import android.os.Handler;
import android.os.Message;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;


import com.flappygod.lipo.lxlibrary.Adapters.Model.BaseGroupTailModel;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/*****************
 * 可展开的列表增加适配器，添加group之后调用getGroupItems进行加载
 *
 * @author lijunlin
 */
public abstract class BaseExpandbleTailPageAdapter<T> extends
        BaseExpandableListAdapter {

    //分页的大小
    private int pageSize;
    //监听
    public BaseExpandbleTailPageListener<T> tailPageListener;
    //列表
    private ExpandableListView listView;
    // 存储的listData
    public List<BaseGroupTailModel<T>> ListData;
    // hadler
    private Handler myHandler;
    // 线程是否忙碌的标志
    private boolean THREADBUSY = false;
    // 线程添加的消息
    private final int ADDITEMS = 0;
    // 线程错误的消息
    private final int THREADERROR = 1;


    /********
     * 通过每页大小更新数据
     *
     * @param size 大小
     */
    public BaseExpandbleTailPageAdapter(int size) {
        ListData = new ArrayList<BaseGroupTailModel<T>>();
        this.pageSize = size;
        handlerCreate();
    }

    /*******
     * 设置刷新列表
     *
     * @param listView 列表控件
     */
    public void setExpandedAllRefresh(ExpandableListView listView) {
        this.listView = listView;
    }


    /*******************
     * 设置加载监听
     *
     * @param l 加载监听
     */
    public void setBaseExpandbleTailPageListener(
            BaseExpandbleTailPageListener<T> l) {
        tailPageListener = l;
    }


    /**************
     * 添加group到列表之中
     *
     * @param model model
     */
    public boolean addGroupToList(BaseGroupTailModel model) {
        //添加到当前列表
        if (model != null&&THREADBUSY == false) {
            ListData.add(model);
            return true;
        }
        return false;
    }

    /**************************
     * 获取当前的GroupModel
     */
    public void getGroupItems() {
        //获取当前的group
        BaseGroupTailModel model = getNowerGroup();
        if (model != null) {
            getNextItems(model);
        }
    }


    /************
     * 获取列表数据
     * @return 数据
     */
    public List<BaseGroupTailModel<T>> getListData() {
        return ListData;
    }


    /*************
     * 展开所有项目
     */
    private void ExpandAll() {
        if (listView != null) {
            int count = listView.getExpandableListAdapter().getGroupCount();
            for (int s = 0; s < count; s++) {
                listView.expandGroup(s);
            }
        }
    }


    //接受返回消息的handler
    class DataHandler<T> extends Handler {

        //弱引用这个适配器
        private WeakReference<BaseExpandbleTailPageAdapter<T>> weakReferenceadapter;

        //构造器
        public DataHandler(BaseExpandbleTailPageAdapter<T> adapter) {
            this.weakReferenceadapter = new WeakReference<BaseExpandbleTailPageAdapter<T>>(adapter);
        }

        public void handleMessage(Message msg) {
            //判断适配器还是否存在
            BaseExpandbleTailPageAdapter<T> adapter = weakReferenceadapter.get();
            if (adapter == null)
                return;
            //获取当前的最后一个Group
            BaseGroupTailModel model = adapter.getNowerGroup();
            if (model == null) {
                return;
            }
            //取得监听
            BaseExpandbleTailPageListener<T> tailPageListener = adapter.tailPageListener;
            //线程错误
            if (msg.what == adapter.THREADERROR) {
                if (tailPageListener != null && msg.obj != null) {
                    tailPageListener.ThreadError(model, (Exception) msg.obj);
                } else if (tailPageListener != null) {
                    tailPageListener.ThreadError(model, new Exception(
                            "no error message  response"));
                }
                adapter.THREADBUSY = false;
            }
            //添加items
            if (msg.what == adapter.ADDITEMS) {
                @SuppressWarnings("unchecked")
                ArrayList<T> list = (ArrayList<T>) msg.obj;
                //添加到当前的Group列表之中
                model.getChildList().addAll(list);
                // 如果正好等于，就表示还没有加载完
                if (list.size() == adapter.pageSize) {
                    //页码加一
                    model.setPage(model.getPage() + 1);
                    // 刷新界面
                    adapter.notifyDataSetChanged();
                    //展开所有
                    adapter.ExpandAll();
                    // 如果为负数，代表不做限制
                    if (model.getMaxPage() < 0) {
                        // 可以继续加载下一层了
                        adapter.THREADBUSY = false;
                        if (tailPageListener != null) {
                            tailPageListener.PageLoaded(list, model, model.getPage() - 1);
                        }
                    }
                    // 如果有最大页数的限制的话，还没有达到最大页码
                    else if (model.getPage() <= model.getMaxPage()) {
                        // 可以继续加载下一层了
                        adapter.THREADBUSY = false;
                        // 页码加载完成
                        if (tailPageListener != null) {
                            tailPageListener.PageLoaded(list, model, model.getPage() - 1);
                        }
                    }
                    //达到了最大页码
                    else {
                        // 加载完成了
                        adapter.THREADBUSY = false;
                        if (tailPageListener != null) {
                            tailPageListener.PageLoaded(list, model, model.getPage() - 1);
                            tailPageListener.PageEnd(model);
                        }
                    }
                } else {
                    //页码加一
                    model.setPage(model.getPage() + 1);
                    //刷新列表
                    adapter.notifyDataSetChanged();
                    //展开所有
                    adapter.ExpandAll();
                    //false
                    adapter.THREADBUSY = false;
                    // 数据完成了
                    if (tailPageListener != null) {
                        tailPageListener.PageLoaded(list, model, model.getPage());
                        tailPageListener.PageEnd(model);
                        // 如果加载到最后还发现什么数据都没有的话
                        if (adapter.ListData.size() == 0) {
                            tailPageListener.NoneData(model);
                        }
                    }
                }
            }
        }
    }


    /**********
     * 创建handler
     */
    private void handlerCreate() {
        myHandler = new DataHandler(this);
    }

    /*****************
     * 获取下一批数据
     *
     * @param groupModel 当前的Group
     */
    private synchronized void getNextItems(final BaseGroupTailModel groupModel) {
        // 如果线程不忙碌，那么就把它置为忙碌
        if (!THREADBUSY)
            THREADBUSY = true;
            // 如果线程忙碌
        else
            return;

        if (tailPageListener != null)
            tailPageListener.StartLoad(groupModel, groupModel.getPage());

        new Thread() {
            public void run() {
                List<T> list;
                try {
                    list = BaseExpandbleTailPageAdapter.this.getDataThreadRun(groupModel,
                            groupModel.getPage(), pageSize);
                    if (list == null) {
                        list = new ArrayList<T>();
                    }
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


    /***********************
     * 获取当前需要加载的group
     *
     * @return
     */
    private BaseGroupTailModel getNowerGroup() {
        if (ListData != null && ListData.size() > 0) {
            return ListData.get(ListData.size() - 1);
        }
        return null;
    }

    /*****************************
     * 这里已经使用了异步加载 只需要传入获取数据的方式即可 这里不允许做界面刷新的操作
     *
     * @param group 组的内容
     * @param page  页码
     * @param size  每页大小
     * @throws Exception
     *****************************/
    public abstract List<T> getDataThreadRun(BaseGroupTailModel group, int page, int size)
            throws Exception;

}
