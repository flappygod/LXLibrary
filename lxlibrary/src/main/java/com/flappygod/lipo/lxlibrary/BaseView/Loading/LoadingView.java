package com.flappygod.lipo.lxlibrary.BaseView.Loading;

import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.flappygod.lipo.lxlibrary.R;


public class LoadingView extends LinearLayout {


    private TextView  textView;



    public LoadingView(Context context) {
        super(context);
        initLoading();
    }

    public LoadingView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initLoading();
    }

    public LoadingView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initLoading();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public LoadingView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initLoading();
    }


    private void initLoading() {
        //移除所有的view
        removeAllViews();
        //转圈lay
        View view= LayoutInflater.from(getContext()).inflate(R.layout.lxlibrary_loading_view,null);
        //获取控件
        textView=view.findViewById(R.id.lxlibrary_loading_text);
        //添加View
        addView(view,new LinearLayoutCompat.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT));
    }

    public void setText(CharSequence text){
        textView.setText(text);
    }

    public TextView getTextView(){
        return textView;
    }


}
