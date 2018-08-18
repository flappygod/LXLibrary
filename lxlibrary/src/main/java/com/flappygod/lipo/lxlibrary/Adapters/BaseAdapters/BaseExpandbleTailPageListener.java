package com.flappygod.lipo.lxlibrary.Adapters.BaseAdapters;




import com.flappygod.lipo.lxlibrary.Adapters.Model.BaseGroupTailModel;

import java.util.List;

/*********************
 * Package Name:com.holpe.maike.shoppingguide.base.adapter <br/>
 * ClassName: BaseExpandbleTailPageListener <br/>
 * Function: TODO expandble分页适配器 <br/>
 * date: 2015-8-8 上午9:25:59 <br/>
 *
 * @author lijunlin
 */
public interface BaseExpandbleTailPageListener<T> {

    /************************
     * 结束
     *
     * @param group 组
     */
    public void PageEnd(BaseGroupTailModel group);

    /************************
     * 加载分页数据结束，但是还存在更多
     *
     * @param data  数据
     * @param group 组
     * @param page  页码
     */
    public void PageLoaded(List<T> data, BaseGroupTailModel group, int page);

    /************************
     * 开始加载第几页
     *
     * @param group 组
     * @param page  页码大小
     */
    public void StartLoad(BaseGroupTailModel group, int page);

    /************************
     * 数据错误
     *
     * @param group 组
     * @param e     错误
     */
    public void ThreadError(BaseGroupTailModel group, Exception e);

    /******************
     * 没有数据
     *
     * @param group 组
     */
    public void NoneData(BaseGroupTailModel group);


}
