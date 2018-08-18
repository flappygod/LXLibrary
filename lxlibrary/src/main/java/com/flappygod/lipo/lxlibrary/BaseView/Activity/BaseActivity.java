package com.flappygod.lipo.lxlibrary.BaseView.Activity;


import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.KeyEvent;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Toast;

import com.flappygod.lipo.lxlibrary.BaseView.Loading.LoadingView;
import com.flappygod.lipo.lxlibrary.R;
import com.flappygod.lipo.lxlibrary.Tools.ScreenTool;
import com.flappygod.lipo.lxlibrary.Widget.CustomDialog;
import com.flappygod.lipo.lxlibrary.Widget.ProgressbarDialog;

import java.util.List;


/*******************
 * Package Name:com.holpe.maike.shoppingguide.base.activity <br/>
 * ClassName: BaseActivity <br/>
 * Function: 基础activity<br/>
 * date: 2015-8-8 上午9:24:12 <br/>
 *
 * @author lijunlin
 */
public class BaseActivity extends FragmentActivity {

    //是否被销毁
    protected Boolean isDestoryed = false;
    // 吐死
    private Toast activityToast;
    // 等待dialog
    private ProgressbarDialog progressDialog;
    //是否关闭页面
    private boolean finishIfNoPermission = false;
    //等待
    private LoadingView loadingView;
    //测试
    private final static int REQ_PERMISSION = 38;

    @SuppressLint("ShowToast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //隐藏系统标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //初始化Toast
        activityToast = Toast.makeText(this, "", Toast.LENGTH_SHORT);
        //等待
        progressDialog = ProgressbarDialog.createDialog(BaseActivity.this);
    }

    //进入
    public void onResume() {
        super.onResume();
    }

    //暂停
    public void onPause() {
        super.onPause();
    }

    //销毁
    protected void onDestroy() {
        //被销毁标志
        isDestoryed = true;
        //关闭
        dissMissProgressbarDialog();
        //销毁
        super.onDestroy();
    }


    //获取是否销毁
    public Boolean getIsDestoryed() {
        return isDestoryed;
    }

    //设置是否销毁
    public void setIsDestoryed(Boolean isDestoryed) {
        this.isDestoryed = isDestoryed;
    }


    @TargetApi(Build.VERSION_CODES.M)
    public void checkMyPsermisstion(List<String> permissions) {
        checkMyPsermisstion(permissions, false);
    }


    /**********
     * 显示温馨提醒
     *
     * @param message 消息内容
     */
    public void alertMessage(String message) {
        CustomDialog.Builder builder = new CustomDialog.Builder(this)
                .setTitle(this.getString(R.string.lxlibrary_reminder))
                .setMessage(message);
        builder.setCancelButton(this.getString(R.string.lxlibrary_cancel),
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        builder.setConfrimButton(this.getString(R.string.lxlibrary_confirm),
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        builder.create().show();
    }

    /***
     * 显示转圈的dialog
     *
     * @param b 是否可以取消
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

    /********
     * 显示转圈
     * @param viewGroup
     */
    public void showLoading(ViewGroup viewGroup) {
        if (loadingView != null) {
            ViewGroup father = (ViewGroup) loadingView.getTag();
            father.removeView(loadingView);
            loadingView = null;
        } else {
            loadingView = new LoadingView(this);
            viewGroup.addView(loadingView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            loadingView.setTag(viewGroup);
        }
    }

    /********
     * 转圈消失
     */
    public void dismissLoading() {
        if (loadingView != null) {
            ViewGroup father = (ViewGroup) loadingView.getTag();
            father.removeView(loadingView);
            loadingView = null;
        }
    }

    /*****************
     * 显示一个吐丝
     *
     * @param msg 需要显示的文字
     */
    public void showMsg(String msg) {
        activityToast.setText(msg);
        activityToast.setDuration(Toast.LENGTH_SHORT);
        activityToast.show();
    }

    /*****************
     * 显示一个吐丝
     *
     * @param msg 需要显示的文字
     */
    public void showMsg(int msg) {
        activityToast.setText(Integer.toString(msg));
        activityToast.setDuration(Toast.LENGTH_SHORT);
        activityToast.show();
    }


    //检查权限
    @TargetApi(Build.VERSION_CODES.M)
    public void checkMyPsermisstion(List<String> permissions, boolean closeActivity) {
        this.finishIfNoPermission = closeActivity;
        //小于return
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M)
            return;
        //初始化
        int Perm[] = new int[permissions.size()];
        int size = 0;
        for (int s = 0; s < permissions.size(); s++) {
            int hasWriteContactsPermission = checkSelfPermission(permissions.get(s));
            if (hasWriteContactsPermission != PackageManager.PERMISSION_GRANTED) {
                Perm[s] = 1;
                size++;
            }
        }
        if (size != 0) {
            int t = 0;
            String[] reqp = new String[size];
            for (int w = 0; w < Perm.length; w++) {
                if (Perm[w] == 1) {
                    reqp[t] = permissions.get(w);
                    t++;
                }
            }
            requestPermissions(reqp, REQ_PERMISSION);
        }
    }

    //5.0以上关于权限的回调
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQ_PERMISSION) {
            boolean noPermission = false;
            for (int s = 0; s < grantResults.length; s++) {
                if (grantResults[s] != PackageManager.PERMISSION_GRANTED) {
                    noPermission = true;
                }
            }
            //提示
            if (noPermission) {
                //提示没有获取到必要权限
                showMsg(getResources().getString(R.string.lxlibrary_nopermission));
                //handler关闭
                Handler handler = new Handler() {
                    public void handleMessage(Message msg) {
                        if (finishIfNoPermission) {
                            finish();
                        }
                    }
                };
                handler.sendMessageDelayed(handler.obtainMessage(), 1000);
            }
        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            isDestoryed = true;
            finish();
            overridePendingTransition(R.anim.lxlibrary_o_exit_tran, R.anim.lxlibrary_o_enter_tran);
            return true;
        }
        return false;
    }

    //获取宽度
    public int getScreenWidth(){
        return ScreenTool.getScreenWidth(this);
    }

    //获取高度
    public int getScreenHeight(){
        return ScreenTool.getScreenHeight(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //获取manager
        FragmentManager fragmentManager = getSupportFragmentManager();
        //得到列表
        List<Fragment> fragments = fragmentManager.getFragments();
        if (fragments != null) {
            for (int s = 0; s < fragments.size(); s++) {
                try {
                    if (fragments.get(s) != null) {
                        fragments.get(s).onActivityResult(requestCode, resultCode, data);
                    }
                } catch (Exception e) {
                }
            }
        }
    }
}
