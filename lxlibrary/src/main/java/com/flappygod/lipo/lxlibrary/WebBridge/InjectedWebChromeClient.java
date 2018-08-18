package com.flappygod.lipo.lxlibrary.WebBridge;

import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;


import com.flappygod.lipo.lxlibrary.R;
import com.flappygod.lipo.lxlibrary.Widget.CustomDialog;

import org.json.JSONObject;

/**************
 * 注入js代码的WebChromeClient
 *
 * @author lijunlin
 */
public class InjectedWebChromeClient extends WebChromeClient {
    // tag
    private final String TAG = "InjectedWebChromeClient";
    // call
    private JsCallJava mJsCallJava;


    public InjectedWebChromeClient(JsCallJava mJsCallJava) {
        // 当传入的对象为空的时候,使用默认的InjectMethodInterface
        this.mJsCallJava = mJsCallJava;
    }


    public InjectedWebChromeClient(InjectedWebViewClient client) {
        this.mJsCallJava = client.getmJsCallJava();
    }


    @Override
    public void onProgressChanged(WebView view, int newProgress) {
        super.onProgressChanged(view, newProgress);
    }

    // 处理Alert事件
    @Override
    public boolean onJsAlert(WebView view, String url, String message,
                             final JsResult result) {
        showDialog(view.getContext(), message, result);
        return true;
    }

    @Override
    public boolean onJsConfirm(WebView view, String url, String message,
                               final JsResult result) {
        showDialog(view.getContext(), message, result);
        return true;
    }

    /**************
     * 显示dialog
     *
     * @param context 上下文
     * @param message 提示消息
     * @param result  返回结果
     */
    private void showDialog(Context context, String message,
                            final JsResult result) {
        CustomDialog.Builder builder = new CustomDialog.Builder(context)
                .setTitle(context.getString(R.string.lxlibrary_reminder))
                .setMessage(message);
        builder.setCancelButton(context.getString(R.string.lxlibrary_cancel),
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        result.cancel();
                        dialog.dismiss();
                    }
                });
        builder.setConfrimButton(context.getString(R.string.lxlibrary_confirm),
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        result.confirm();
                        dialog.dismiss();
                    }
                });
        builder.create().show();
    }

    @Override
    public boolean onJsPrompt(WebView view, String url, String message,
                              String defaultValue, JsPromptResult result) {
        String retJSon = mJsCallJava.call(view, message);
        try {
            JSONObject jb = new JSONObject(retJSon);
            if (jb.optInt("code") == 200) {
                result.confirm(retJSon);
                Log.d(TAG, retJSon);
                return true;
            } else {
                return super.onJsPrompt(view, url, message, defaultValue,
                        result);
            }
        } catch (Exception e) {
            return super.onJsPrompt(view, url, message, defaultValue, result);
        }
    }

}



