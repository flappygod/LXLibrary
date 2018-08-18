package com.flappygod.lipo.lxlibrary.Adapters.BaseAdapters;

import java.util.List;

/*************************
 * 
 * Package Name:com.holpe.maike.shoppingguide.base.adapter <br/>
 * ClassName: BaseTailPageListener <br/> 
 * Function: TODO 适用于下拉加载更多的适配器  <br/> 
 * date: 2015-8-8 上午9:26:22 <br/> 
 * 
 * @author lijunlin
 */
public interface BaseTailPageListener<T> {
	
	
	/************************
	* 分页结束
	************************/
	public void PageEnd();
	/************************
	* 加载分页数据结束，但是还存在更多
	************************/
	public void PageLoaded(List<T> data, int page);
	/************************
	* 开始加载第几页
	************************/
	public void StartLoad(int page);
	/************************
	*数据错误
	************************/
	public void ThreadError(Exception e);
	
	/******************
	 * 没有数据
	 */
	public void NoneData();
	
	
}
