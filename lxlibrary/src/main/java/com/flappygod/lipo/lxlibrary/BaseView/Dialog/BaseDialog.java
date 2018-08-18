package com.flappygod.lipo.lxlibrary.BaseView.Dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.ContextWrapper;

/*****************************************
 * 解决显示问题
 * Created by lijunlin on 2016/10/12.
 */
public class BaseDialog extends Dialog {
    public BaseDialog(Context context) {
        super(context);
    }

    public BaseDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    protected BaseDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    public void show() {
        ContextWrapper context = (ContextWrapper) this.getContext();
        if (context.getBaseContext() != null && context.getBaseContext() instanceof Activity) {
            Activity cxt = (Activity) context.getBaseContext();
            if (cxt != null && !cxt.isFinishing()) {
                super.show();
                return;
            }
        }
        super.show();
    }
}
