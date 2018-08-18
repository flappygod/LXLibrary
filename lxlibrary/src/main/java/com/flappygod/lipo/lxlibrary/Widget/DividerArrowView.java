package com.flappygod.lipo.lxlibrary.Widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;


/**
 * Created by Administrator on arrowWidth18/2/26.
 */

public class DividerArrowView extends View {
    
    private  int  arrowWidth=10;
    //上面
    private boolean arrowUP=true;
    //颜色
    private String bgColor= Integer.toString(Color.WHITE);
    //颜色
    private String lineColor=Integer.toString(Color.GRAY);
    //显示arrow
    private boolean showArrow = true;

    private int strokwidth=2;

    public int getBgColor() {
        return Integer.parseInt(bgColor);
    }

    public void setBgColor(int bgColor) {
        this.bgColor = Integer.toString(bgColor);
    }

    public int getLineColor() {
        return Integer.parseInt(bgColor);
    }

    public void setLineColor(int lineColor) {
        this.lineColor = Integer.toString(lineColor);
    }

    public int getStrokwidth() {
        return strokwidth;
    }

    public void setStrokwidth(int strokwidth) {
        this.strokwidth = strokwidth;
        invalidate();
    }

    public boolean isShowArrow() {
        return showArrow;
    }

    public void setShowArrow(boolean showArrow) {
        this.showArrow = showArrow;
        invalidate();
    }

    public DividerArrowView(Context context) {
        super(context);
    }

    public DividerArrowView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DividerArrowView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void onDraw(Canvas cavas) {
        super.onDraw(cavas);

        Paint paint = new Paint();
        paint.setStrokeWidth(strokwidth);
        paint.setColor(Integer.parseInt(lineColor));

        int  YZERO=0+2;
        int  YBOTT=getHeight()-2;

        int  YZERO_TRUE=0;
        int  YBOTT_TRUE=getHeight();

        if (showArrow) {
            if(arrowUP)
            {
                cavas.drawLine(0, YBOTT, getWidth() / 2 - arrowWidth, YBOTT, paint);
                cavas.drawLine(getWidth() / 2 - arrowWidth, YBOTT, getWidth() / 2, YBOTT-arrowWidth, paint);
                cavas.drawLine(getWidth() / 2, YBOTT-arrowWidth, getWidth() / 2 + arrowWidth, YBOTT, paint);
                cavas.drawLine(getWidth() / 2 + arrowWidth, YBOTT, getWidth(), YBOTT, paint);
                paint.setColor(Integer.parseInt(bgColor));
                paint.setStrokeWidth(0);
                /*画一个实心三角形*/
                Path path=new Path();
                path.moveTo(getWidth() / 2 - arrowWidth,YBOTT_TRUE);
                path.lineTo(getWidth() / 2,YBOTT_TRUE-arrowWidth);
                path.lineTo(getWidth() / 2 + arrowWidth,YBOTT_TRUE);
                path.close();
                cavas.drawPath(path, paint);

            }else {
                cavas.drawLine(0, YZERO, getWidth() / 2 - arrowWidth, 0, paint);
                cavas.drawLine(getWidth() / 2 - arrowWidth, YZERO, getWidth() / 2, arrowWidth, paint);
                cavas.drawLine(getWidth() / 2, arrowWidth, getWidth() / 2 + arrowWidth, YZERO, paint);
                cavas.drawLine(getWidth() / 2 + arrowWidth, YZERO, getWidth(), YZERO, paint);
                paint.setColor(Integer.parseInt(bgColor));
                paint.setStrokeWidth(0);
                /*画一个实心三角形*/
                Path path=new Path();
                path.moveTo(getWidth() / 2 - arrowWidth,0);
                path.lineTo(getWidth() / 2,arrowWidth);
                path.lineTo(getWidth() / 2 + arrowWidth,YZERO_TRUE);
                path.close();
                cavas.drawPath(path, paint);
            }
        } else {
            if(arrowUP){
                cavas.drawLine(0, YBOTT, getWidth(), YBOTT, paint);
            }else{
                cavas.drawLine(0, YZERO, getWidth(), YZERO, paint);
            }
        }

    }
}
