package com.flappygod.lipo.lxlibrary.Widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;

import com.flappygod.lipo.lxlibrary.Tools.DensityTool;


/**
 * Created by lijunlin on 2018/3/27.
 */

public class CornerImageView extends android.support.v7.widget.AppCompatImageView{

    private float[] radius;

    public CornerImageView(Context context) {
        super(context);
        setRadius(DensityTool.dip2px(getContext(),5), DensityTool.dip2px(getContext(),5),0f,0f);
    }

    public CornerImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setRadius(DensityTool.dip2px(getContext(),5),DensityTool.dip2px(getContext(),5),0f,0f);
    }

    public CornerImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setRadius(DensityTool.dip2px(getContext(),5),DensityTool.dip2px(getContext(),5),0f,0f);
    }


    public void setRadius(float topLeft,float topRight,float bottomRight,float bottomLeft){
        float[] radius= {topLeft,topLeft,topRight,topRight,bottomRight,bottomRight,bottomLeft,bottomLeft,};
        this.radius=radius;
    }



    /**
     * 画图
     * by Hankkin at:2015-08-30 21:15:53
     * @param canvas
     */
    protected void onDraw(Canvas canvas) {
        Path path = new Path();
        int w = this.getWidth();
        int h = this.getHeight();
        /*向路径中添加圆角矩形。radii数组定义圆角矩形的四个圆角的x,y半径。radii长度必须为8*/
        path.addRoundRect(new RectF(0,0,w,h),radius,Path.Direction.CW);
        canvas.clipPath(path);
        super.onDraw(canvas);
    }


}
