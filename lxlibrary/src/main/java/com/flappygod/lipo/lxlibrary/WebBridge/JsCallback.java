/*
 * Copyright 2013 The JA-SIG Collaborative. All rights reserved.
 * distributed with this file and available online at
 * http://www.etong.com/
 */
package com.flappygod.lipo.lxlibrary.WebBridge;



import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Handler;
import android.webkit.WebView;



/**
 * <p>Title: JsCallback</p>
 * <p>Description: 异步回调页面JS函数管理对象</p>
 * @since 1.0
 */
@SuppressLint("DefaultLocale")
public class JsCallback {
    private static final String CALLBACK_JS_FORMAT = "javascript:*.callback(%d, %d %s);";
    private int index;
    private String injectName;
    private WebView webView;
    private int isPermanent;
    private final static Handler handler = new Handler();

    public JsCallback(WebView view, String injectName, int index) {
        this.index = index;
        this.webView = view;
        this.injectName = injectName;
        if (Build.VERSION.SDK_INT >= 11 && Build.VERSION.SDK_INT < 17) {
            try {
                webView.removeJavascriptInterface("searchBoxJavaBridge_");
                webView.removeJavascriptInterface("accessibility");
                webView.removeJavascriptInterface("accessibilityTraversal");
            } catch (Throwable tr) {
                tr.printStackTrace();
            }
        }
    }

    public void apply (Object... args) {
        StringBuilder sb = new StringBuilder();
        for (Object arg : args){
            sb.append(",");
            boolean isStrArg = arg instanceof String;
            if (isStrArg) {
                sb.append("\"");
            }
            sb.append(String.valueOf(arg));
            if (isStrArg) {
                sb.append("\"");
            }
        }
        final String execJs = String.format(CALLBACK_JS_FORMAT.replace("*", injectName), index, isPermanent, sb.toString());
        handler.post(new Runnable() {
            public void run() { 
            	webView.loadUrl(execJs);
            }
        });
    }

    public void setPermanent (boolean value) {
        isPermanent = value ? 1 : 0;
    }
}
