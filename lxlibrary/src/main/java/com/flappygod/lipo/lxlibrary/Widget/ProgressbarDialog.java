package com.flappygod.lipo.lxlibrary.Widget;


import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.ViewGroup.LayoutParams;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.flappygod.lipo.lxlibrary.BaseView.Dialog.BaseDialog;
import com.flappygod.lipo.lxlibrary.R;
import com.wang.avi.AVLoadingIndicatorView;


/**
 * 转圈等待的dialog
 *
 * @author Administrator
 * @ClassName: MTMProgressbarDialog
 * @Description: TODO
 * @date 2015-3-23 下午2:03:49
 */
public class ProgressbarDialog extends BaseDialog {

    //当前的dialog
    private static ProgressbarDialog myprogressbardialog = null;
    //显示的文本提示
    private static TextView textView;
    //progress默认
    private static AVLoadingIndicatorView prgress;

    //构造器
    public ProgressbarDialog(Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    //构造器
    private ProgressbarDialog(Context context, int theme) {
        super(context, theme);
    }

    //设置文本的显示文字
    public void setTextString(String str) {
        if (textView != null) {
            textView.setText(str);
        }
    }

    /********
     * 创建dialog
     *
     * @param context 上下文
     * @return
     */
    public static ProgressbarDialog createDialog(Context context) {
        myprogressbardialog = new ProgressbarDialog(context, R.style.lxlibrary_dialog_custom);
        myprogressbardialog.setContentView(R.layout.lxlibrary_dialog_progress);
        //找到控件
        textView = (TextView) myprogressbardialog.findViewById(R.id.lxlibrary_dialog_progress_text);
        prgress = (AVLoadingIndicatorView) myprogressbardialog.findViewById(R.id.lxlibrary_dialog_prgress_bar);

        //设置gravity
        myprogressbardialog.getWindow().getAttributes().gravity = Gravity.CENTER;
        myprogressbardialog.getWindow().getAttributes().width = LayoutParams.MATCH_PARENT;
        return myprogressbardialog;
    }

    /*******
     * 创建dialog
     *
     * @param context 上下文
     * @param text    显示的textView的文字
     * @return
     */
    public static ProgressbarDialog createDialog(Context context, String text) {
        myprogressbardialog = new ProgressbarDialog(context, R.style.lxlibrary_dialog_custom);
        myprogressbardialog.setContentView(R.layout.lxlibrary_dialog_progress);
        textView = (TextView) myprogressbardialog.findViewById(R.id.lxlibrary_dialog_progress_text);
        prgress = (AVLoadingIndicatorView) myprogressbardialog.findViewById(R.id.lxlibrary_dialog_prgress_bar);
        //第一种动画不显示
        myprogressbardialog.getWindow().getAttributes().gravity = Gravity.CENTER;
        myprogressbardialog.getWindow().getAttributes().width = LayoutParams.MATCH_PARENT;
        textView.setText(text);
        return myprogressbardialog;
    }


}
