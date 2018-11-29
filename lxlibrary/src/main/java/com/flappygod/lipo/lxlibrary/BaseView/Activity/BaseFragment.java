package com.flappygod.lipo.lxlibrary.BaseView.Activity;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.widget.Toast;

import com.flappygod.lipo.lxlibrary.Tools.ScreenTool;
import com.flappygod.lipo.lxlibrary.Widget.ProgressbarDialog;

import java.util.List;


/***********************
 * 
 * Package Name:com.holpe.maike.shoppingguide.base.activity <br/>
 * ClassName: BaseFragment <br/> 
 * Function:  基础fragment <br/> 
 * date: 2015-8-8 上午9:24:35 <br/> 
 * 
 * @author lijunlin
 */
public class BaseFragment extends Fragment {

	//我的activity
	private Activity safeActivity;


	//弹出层
	private Toast fragmentToast;
	//进度等待条
	private ProgressbarDialog fragmentProgressDialog;


	@SuppressLint("ShowToast")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//保存activity
		safeActivity =getActivity();
		//保存toast
		fragmentToast = Toast.makeText(this.getActivity(), "",Toast.LENGTH_SHORT);
		//dialog
		fragmentProgressDialog = ProgressbarDialog.createDialog(this.getActivity());
	}





	/***
	 * 显示转圈的dialog
	 * 
	 * @param b
	 *            是否可以取消
	 * @throws
	 */
	public void showProgressbarDialog(boolean b) {
		if (fragmentProgressDialog != null) {
			fragmentProgressDialog.setCancelable(b);
			fragmentProgressDialog.show();
		}
	}

	/****************
	 * 取消掉正在显示的dialog
	 * 
	 * @throws
	 ******/
	public void dissMissProgressbarDialog() {
		if (fragmentProgressDialog != null && fragmentProgressDialog.isShowing()) {
			fragmentProgressDialog.dismiss();
		}
	}


	/*****************
	 * 显示一个吐丝
	 * 
	 * @param msg
	 *            需要显示的文字
	 */
	public void showMsg(String msg) {
		fragmentToast.setText(msg);
		fragmentToast.setDuration(Toast.LENGTH_SHORT);
		fragmentToast.show();
	}


	/************
	 * 获取屏幕宽度
	 *
	 * @return
	 */
	public int getScreenWidth() {
		return ScreenTool.getScreenWidth(safeActivity);
	}

	/**************
	 * 获取屏幕高度
	 * @return
	 */
	public int getScreenHeight() {
		return ScreenTool.getScreenHeight(safeActivity);
	}

	//获取activity
	public Activity getSafeActivity(){
		return safeActivity;
	}


	//获取上下文
	public Context getContext(){
		if(safeActivity !=null){
			return safeActivity;
		}
		return getContext();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		//处理fragment中的跳转返回
		FragmentManager fragmentManager = getChildFragmentManager();
		//fragments
		List<Fragment> fragments = fragmentManager.getFragments();
		//遍历
		if (fragments != null) {
			//主动调用
			for (int s = 0; s < fragments.size(); s++) {
				fragments.get(s).onActivityResult(requestCode, resultCode, data);
			}
		}
	}

}