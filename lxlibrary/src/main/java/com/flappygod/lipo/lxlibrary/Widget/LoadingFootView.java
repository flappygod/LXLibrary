package com.flappygod.lipo.lxlibrary.Widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.flappygod.lipo.lxlibrary.R;
import com.flappygod.lipo.lxlibrary.Tools.DensityTool;
import com.flappygod.lipo.lxlibrary.Widget.MaterialDesign.MaterialProgressBar;


/*************
 * 底部view
 * Package Name:com.holpe.maike.salesplus.widget <br/>
 * ClassName: LoadingFootView <br/>
 * Function: TODO 功能说明 <br/>
 * date: 2015-9-21 下午4:55:16 <br/>
 *
 * @author lijunlin
 */
public class LoadingFootView extends LinearLayout {

    //提示View
    private TextView tipsView;
    //imageView
    private ImageView imageView;
    //转圈
    private MaterialProgressBar progress;


    public LoadingFootView(Context context) {
        super(context);
        init();
    }

    public LoadingFootView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    //初始化
    private void init() {

        //使用水平布局
        this.setOrientation(LinearLayout.VERTICAL);
        this.setGravity(Gravity.CENTER);

        {
            imageView = new ImageView(getContext());
            imageView.setBackgroundColor(getResources().getColor(R.color.lxlibrary_divider));
            LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
            layoutParams.height = 1;
            imageView.setVisibility(VISIBLE);
            addView(imageView, layoutParams);
        }

        {
            //布局
            LinearLayout layout = new LinearLayout(getContext());
            layout.setGravity(Gravity.CENTER);
            layout.setOrientation(LinearLayout.HORIZONTAL);
            //加入转圈
            {
                //转圈lay
                LinearLayout progresslay = new LinearLayout(getContext());
                LayoutParams lpl = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
                lpl.width = DensityTool.dip2px(getContext(), 16);
                lpl.height = DensityTool.dip2px(getContext(), 16);
                lpl.rightMargin = DensityTool.dip2px(getContext(), 5);
                layout.addView(progresslay, lpl);
                //添加一个progress
                progress = new MaterialProgressBar(getContext());
                progress.setFirstAnimationOver(true);
                LayoutParams lpp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
                progresslay.addView(progress, lpp);

            }
            //加入textView
            {
                tipsView = new TextView(getContext());
                tipsView.setGravity(Gravity.CENTER);
                tipsView.setTextColor(0xFF999999);
                tipsView.setTextSize(12);
                tipsView.setSingleLine();


                tipsView.setMinHeight(DensityTool.dip2px(getContext(), 40));
                LayoutParams layoutParamsT = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
                layoutParamsT.width = DensityTool.dip2px(getContext(), 90);
                layoutParamsT.height = DensityTool.dip2px(getContext(), 40);
                layout.addView(tipsView, layoutParamsT);
            }
            {
                //转圈lay
                LinearLayout progresslay = new LinearLayout(getContext());
                LayoutParams lpl = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
                lpl.width = DensityTool.dip2px(getContext(), 16);
                lpl.height = DensityTool.dip2px(getContext(), 16);
                lpl.leftMargin = DensityTool.dip2px(getContext(), 5);
                layout.addView(progresslay, lpl);
            }
            addView(layout);
        }
    }

    public void showDivider(boolean flag) {
        if (imageView != null) {
            if (flag)
                imageView.setVisibility(VISIBLE);
            else
                imageView.setVisibility(GONE);
        }
    }


    //每页加载完成
    public void PageLoaded() {
        if (tipsView != null)
            tipsView.setText(getResources().getString(R.string.lxlibrary_load_pullup));
        if (progress != null)
            progress.setVisibility(GONE);

    }

    //所有加载完成
    public void PageEnd() {
        if (tipsView != null)
            tipsView.setText(getResources().getString(R.string.lxlibrary_load_nomore));
        if (progress != null)
            progress.setVisibility(GONE);
    }

    //开始加载
    public void StartLoad() {
        if (tipsView != null)
            tipsView.setText(getResources().getString(R.string.lxlibrary_loading));
        if (progress != null)
            progress.setVisibility(VISIBLE);

    }

    //加载失败
    public void ThreadError() {
        if (tipsView != null)
            tipsView.setText(getResources().getString(R.string.lxlibrary_load_error));
        if (progress != null)
            progress.setVisibility(GONE);

    }

    //每页数据
    public void NoneData() {
        if (tipsView != null)
            tipsView.setText(getResources().getString(R.string.lxlibrary_load_none));
        if (progress != null)
            progress.setVisibility(GONE);


    }


}
