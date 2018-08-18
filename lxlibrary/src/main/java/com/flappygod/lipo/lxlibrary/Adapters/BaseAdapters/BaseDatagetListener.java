package com.flappygod.lipo.lxlibrary.Adapters.BaseAdapters;

import java.util.List;


/**************
 * 
 * Package Name:com.holpe.maike.shoppingguide.base.adapter <br/>
 * ClassName: BaseDatagetListener <br/> 
 * Function: TODO 获取数据适配器的接口 <br/> 
 * date: 2015-8-8 上午9:24:53 <br/> 
 * 
 * @author lijunlin
 */
public interface BaseDatagetListener<T> {

	
	/************************
	*数据错误
	************************/
	public void ThreadError(Exception e);
	
	
	/************************
	* 数据获取成功,正好将数据放过来
	************************/
	public void DataReady(List<T> data);
	
	
	/**************
	 * 开始获取数据的监听
	 **************/
	public void Start();
}
