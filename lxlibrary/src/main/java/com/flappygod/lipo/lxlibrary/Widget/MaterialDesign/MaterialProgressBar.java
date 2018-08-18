package com.flappygod.lipo.lxlibrary.Widget.MaterialDesign;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.util.AttributeSet;

public class MaterialProgressBar extends CustomView {

    //默认安卓的attrs命名空间
    final static String ANDROIDXML = "http://schemas.android.com/apk/res/android";
    //转圈的宽度
    private int circleWidth = 2;
    //第一个动画是否绘制
    boolean firstAnimationOver = false;
    //背景颜色
    private int DEFAULT_BACKGROUND = Color.parseColor("#20B2AA");
    //宽度
    private int DEFAULT_CIRCULAR_WIDTH = 24;


    public MaterialProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        initCircular(attrs);
    }

    public MaterialProgressBar(Context context) {
        super(context);
        initCircular();
    }

    //设置attributes
    protected void initCircular(AttributeSet attrs) {
        setBackgroundColor(DEFAULT_BACKGROUND);
        //获取设置的背景颜色
        int bacgroundColor = attrs.getAttributeResourceValue(ANDROIDXML, "background", -1);
        if (bacgroundColor != -1) {
            //假如存在颜色就设置背景颜色
            setBackgroundColor(getResources().getColor(bacgroundColor));
        } else {
            //设置颜色
            int background = attrs.getAttributeIntValue(ANDROIDXML, "background", -1);
            if (background != -1)
                setBackgroundColor(background);
            else
                setBackgroundColor(DEFAULT_BACKGROUND);
        }
    }


    //初始化
    private void initCircular() {
        //设置最大宽高
        setBackgroundColor(DEFAULT_BACKGROUND);
    }

    /**
     * 获取RGB
     * 半透明化
     *
     * @return
     */
    protected int makePressColor() {
        int r = (this.DEFAULT_BACKGROUND >> 16) & 0xFF;
        int g = (this.DEFAULT_BACKGROUND >> 8) & 0xFF;
        int b = (this.DEFAULT_BACKGROUND >> 0) & 0xFF;
        // r = (r+90 > 245) ? 245 : r+90;
        // g = (g+90 > 245) ? 245 : g+90;
        // b = (b+90 > 245) ? 245 : b+90;
        return Color.argb(128, r, g, b);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //是否绘制第一个animation
        if (firstAnimationOver == false) {
            drawFirstAnimation(canvas);
        } else {
            drawSecondAnimation(canvas);
        }
        invalidate();

    }


    public boolean isFirstAnimationOver() {
        return firstAnimationOver;
    }

    public void setFirstAnimationOver(boolean firstAnimationOver) {
        this.firstAnimationOver = firstAnimationOver;
    }

    public int getCircleWidth() {
        return circleWidth;
    }

    public void setCircleWidth(int circleWidth) {
        this.circleWidth = circleWidth;
    }

    //第一个animation的配置
    float radiusFirst = 0;
    float radiusSecond = 0;
    int cont = 0;

    /**
     * Draw first animation of view
     *
     * @param canvas
     */
    private void drawFirstAnimation(Canvas canvas) {
        //假如第一个半径小于宽度除以二
        if (radiusFirst < getWidth() / 2) {
            Paint paint = new Paint();
            //抗锯齿
            paint.setAntiAlias(true);
            //设置颜色
            paint.setColor(makePressColor());
            //增加这个半径
            radiusFirst = (radiusFirst >= getWidth() / 2) ? (float) getWidth() / 2 : radiusFirst + 1;
            //绘制图
            canvas.drawCircle(getWidth() / 2, getHeight() / 2, radiusFirst, paint);
        } else {
            Bitmap bitmap = Bitmap.createBitmap(canvas.getWidth(), canvas.getHeight(), Bitmap.Config.ARGB_8888);
            Canvas temp = new Canvas(bitmap);
            Paint paint = new Paint();
            paint.setAntiAlias(true);
            paint.setColor(makePressColor());
            temp.drawCircle(getWidth() / 2, getHeight() / 2, getHeight() / 2,
                    paint);
            Paint transparentPaint = new Paint();
            transparentPaint.setAntiAlias(true);
            transparentPaint.setColor(getResources().getColor(
                    android.R.color.transparent));
            transparentPaint.setXfermode(new PorterDuffXfermode(
                    PorterDuff.Mode.CLEAR));
            if (cont >= 50) {
                radiusSecond = (radiusSecond >= getWidth() / 2) ? (float) getWidth() / 2
                        : radiusSecond + 1;
            } else {
                radiusSecond = (radiusSecond >= getWidth() / 2
                        - Utils.dpToPx(circleWidth, getResources())) ? (float) getWidth()
                        / 2 - Utils.dpToPx(circleWidth, getResources())
                        : radiusSecond + 1;
            }
            temp.drawCircle(getWidth() / 2, getHeight() / 2, radiusSecond,
                    transparentPaint);
            canvas.drawBitmap(bitmap, 0, 0, new Paint());
            if (radiusSecond >= getWidth() / 2
                    - Utils.dpToPx(circleWidth, getResources()))
                cont++;
            if (radiusSecond >= getWidth() / 2)
                firstAnimationOver = true;
        }
    }

    int arcD = 1;
    int arcO = 0;
    float rotateAngle = 0;
    int limite = 0;

    /**
     * Draw second animation of view
     *
     * @param canvas
     */
    private void drawSecondAnimation(Canvas canvas) {
        if (arcO == limite)
            arcD += 6;
        if (arcD >= 290 || arcO > limite) {
            arcO += 6;
            arcD -= 6;
        }
        if (arcO > limite + 290) {
            limite = arcO;
            arcO = limite;
            arcD = 1;
        }
        rotateAngle += circleWidth;
        canvas.rotate(rotateAngle, getWidth() / 2, getHeight() / 2);

        Bitmap bitmap = Bitmap.createBitmap(canvas.getWidth(), canvas.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas temp = new Canvas(bitmap);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(DEFAULT_BACKGROUND);

        temp.drawArc(new RectF(0, 0, getWidth(), getHeight()), arcO, arcD, true, paint);

        Paint transparentPaint = new Paint();
        transparentPaint.setAntiAlias(true);
        transparentPaint.setColor(getResources().getColor(
                android.R.color.transparent));
        transparentPaint.setXfermode(new PorterDuffXfermode(
                PorterDuff.Mode.CLEAR));
        temp.drawCircle(getWidth() / 2, getHeight() / 2, (getWidth() / 2)
                - Utils.dpToPx(circleWidth, getResources()), transparentPaint);

        canvas.drawBitmap(bitmap, 0, 0, new Paint());
    }

    // 设置颜色
    public void setBackgroundColor(int color) {
        //设置背景为透明
        super.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        //将颜色设置为
        this.DEFAULT_BACKGROUND = color;
    }

}
