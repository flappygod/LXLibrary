package com.flappygod.lipo.lxlibrary.WebBridge;

import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;


public class InjectedWebViewClient extends WebViewClient {

    // tag
    private final String TAG = "InjectedWebViewClient";
    // call
    private JsCallJava mJsCallJava;
    // js调用的对象
    private Object callObject;
    //成功注入的监听
    private OnJsInjectListener listener;


    public interface OnJsInjectListener {
        /**********
         * 成功注入
         */
        void onJsInjected();
    }

    /*************
     * 设置注入的监听
     *
     * @param listener 监听
     */
    public void setOnJsInjectListener(OnJsInjectListener listener) {
        this.listener = listener;
    }


    /****************
     * 构造器
     *
     * @param injectedName 注入方法大名称
     * @param callObject   方法返回调用的对象
     */
    public InjectedWebViewClient(String injectedName, Object callObject) {
        // 当传入的对象为空的时候,使用默认的InjectMethodInterface
        if (callObject == null) {
            this.callObject = null;
            this.mJsCallJava = new JsCallJava(injectedName,
                    InjectMethodInterface.class, this.callObject);
        } else {
            // 否则就使用当前动态传入的对象
            this.callObject = callObject;
            this.mJsCallJava = new JsCallJava(injectedName,
                    this.callObject.getClass(), this.callObject);
        }
    }

    /****************
     * 构造器
     *
     * @param injectedName 注入方法大名称
     * @param callObject   方法返回调用的对象
     * @param listener     注入成功的监听
     */
    public InjectedWebViewClient(String injectedName, Object callObject, OnJsInjectListener listener) {
        // 当传入的对象为空的时候,使用默认的InjectMethodInterface
        if (callObject == null) {
            this.callObject = null;
            this.mJsCallJava = new JsCallJava(injectedName,
                    InjectMethodInterface.class, this.callObject);
        } else {
            // 否则就使用当前动态传入的对象
            this.callObject = callObject;
            this.mJsCallJava = new JsCallJava(injectedName,
                    this.callObject.getClass(), this.callObject);
        }
        this.listener = listener;
    }


    @Override
    public void onPageFinished(WebView view, String url) {

        super.onPageFinished(view, url);
        //最后注入代码，肯定不会错
        view.loadUrl(mJsCallJava.getPreloadInterfaceJS());
        //log
        Log.d(TAG, mJsCallJava.getPreloadInterfaceJS());
        //判断是否存在JSBridgeReady 方法,存在则调用
        view.loadUrl("javascript:if(typeof JSBridgeReady!= 'undefined'){try{JSBridgeReady();}catch(e){}}");
        if (this.listener != null) {
            listener.onJsInjected();
        }
    }

    /************
     * 获取callJava
     *
     * @return
     */
    public JsCallJava getmJsCallJava() {
        return mJsCallJava;
    }


}
