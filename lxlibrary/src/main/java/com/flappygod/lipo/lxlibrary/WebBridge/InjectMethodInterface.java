package com.flappygod.lipo.lxlibrary.WebBridge;

import android.content.Context;
import android.webkit.WebView;
import android.widget.Toast;


/****************
 * js交互类
 *InjectMethodInterface
 * @author lijunlin
 */
public class InjectMethodInterface {

	public static void showToast(WebView webView, String str, JsCallback js) {
		Context context = webView.getContext();
		Toast.makeText(context, str, Toast.LENGTH_SHORT).show();
	}

}
