package com.flappygod.lipo.lxlibrary.BaseView.Activity;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.DisplayMetrics;
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
	protected Activity  myActivity;
	//弹出层
	private Toast myToast;
	//进度等待条
	private ProgressbarDialog progressDialog;

	@SuppressLint("ShowToast")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//保存activity
		myActivity=getActivity();
		//保存toast
		myToast = Toast.makeText(this.getActivity(), "",Toast.LENGTH_SHORT);
		//dialog
		progressDialog = ProgressbarDialog.createDialog(this.getActivity());
	}

	/***
	 * 显示转圈的dialog
	 * 
	 * @param b
	 *            是否可以取消
	 * @throws
	 */
	public void showProgressbarDialog(boolean b) {
		if (progressDialog != null) {
			progressDialog.setCancelable(b);
			progressDialog.show();
		}
	}

	/****************
	 * 取消掉正在显示的dialog
	 * 
	 * @throws
	 ******/
	public void dissMissProgressbarDialog() {
		if (progressDialog != null && progressDialog.isShowing()) {
			progressDialog.dismiss();
		}
	}

	/*****************
	 * 显示一个吐丝
	 * 
	 * @param msg
	 *            需要显示的文字
	 */
	public void showMsg(String msg) {
		myToast.setText(msg);
		myToast.setDuration(Toast.LENGTH_SHORT);
		myToast.show();
	}


	/************
	 * 获取屏幕宽度
	 *
	 * @return
	 */
	public int getScreenWidth() {
		return ScreenTool.getScreenWidth(myActivity);
	}

	/**************
	 * 获取屏幕高度
	 *
	 * @return
	 */
	public int getScreenHeight() {
		return ScreenTool.getScreenHeight(myActivity);
	}

	//获取activity
	public Activity getMyActivity(){
		return myActivity;
	}


	//获取上下文
	public Context getContext(){
		if(myActivity!=null){
			return myActivity;
		}
		return getContext();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		//处理fragment中的跳转返回
		FragmentManager fragmentManager = getChildFragmentManager();
		List<Fragment> fragments = fragmentManager.getFragments();
		if (fragments != null) {
			for (int s = 0; s < fragments.size(); s++) {
				fragments.get(s).onActivityResult(requestCode, resultCode, data);
			}
		}
	}

}