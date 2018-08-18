package com.flappygod.lipo.lxlibrary.Widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

public class CameraSurfaceView extends View {

    private int color = Color.argb(0x88, 0x00, 0x00, 0x00);

    private int cornerColor = Color.argb(0xff, 0x4c, 0xac, 0xeb);

    private int lineColor = Color.argb(0x55, 0x98, 0xf5, 0xff);

    private int centerWidth = 480;
    private int centerHeight = 320;
    private Paint backPaint;
    private Paint cornerPaint;
    private Paint linePaint;

    private final int cornerW = 30;
    private final int cornerH = 10;
    private final int lineHeight = 4;

    private Rect topRect;
    private Rect buttomRect;
    private Rect leftRect;
    private Rect rightRect;

    private int centerOffset = 0;

    private void init() {

        backPaint = new Paint();
        backPaint.setColor(color);

        cornerPaint = new Paint();
        cornerPaint.setColor(cornerColor);

        linePaint = new Paint();
        linePaint.setColor(lineColor);
        initRect();
    }

    public int getCenterWidth() {
        return centerWidth;
    }

    public int getCenterHeight() {
        return centerHeight;
    }

    public CameraSurfaceView(Context context) {
        super(context);
        init();
    }

    public CameraSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CameraSurfaceView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void initRect() {
        topRect = new Rect(0, 0, this.getWidth(),
                (this.getHeight() - centerHeight) / 2);
        buttomRect = new Rect(0, (this.getHeight() + centerHeight) / 2,
                this.getWidth(), this.getHeight());
        leftRect = new Rect(0, (this.getHeight() - centerHeight) / 2,
                (this.getWidth() - centerWidth) / 2,
                (this.getHeight() + centerHeight) / 2);
        rightRect = new Rect((this.getWidth() + centerWidth) / 2,
                (this.getHeight() - centerHeight) / 2, this.getWidth(),
                (this.getHeight() + centerHeight) / 2);
    }

    public void setCenterWidthAndheight(int width, int height) {
        this.centerWidth = width;
        this.centerHeight = height;
        initRect();
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawMyCenter(canvas);
        postInvalidate();
    }

    private void drawMyCenter(Canvas canvas) {
        // canvas.drawRect(new
        // Rect(0,0,this.getWidth()-centerWidth,this.getHeight()-centerHeight),
        // backPaint);
        initRect();
        canvas.drawRect(topRect, backPaint);
        canvas.drawRect(buttomRect, backPaint);
        canvas.drawRect(leftRect, backPaint);
        canvas.drawRect(rightRect, backPaint);
        drawCorners(canvas);
        drawCenterLine(canvas);
    }

    private void drawCenterLine(Canvas canvas) {
        Rect lineRect = new Rect((this.getWidth() - centerWidth) / 2 + cornerH,
                (this.getHeight() - centerHeight) / 2 + cornerH,
                (this.getWidth() + centerWidth) / 2 - cornerH,
                (this.getHeight() - centerHeight) / 2 + cornerH + lineHeight);

        if (lineRect.bottom + centerOffset > (this.getHeight() + centerHeight)
                / 2 - cornerH) {
            centerOffset = 0;
        } else {
            centerOffset = centerOffset + 5;
        }

        Rect drawLineRect = new Rect(lineRect.left,
                lineRect.top + centerOffset, lineRect.right, lineRect.bottom
                + centerOffset);

        canvas.drawRect(drawLineRect, linePaint);
    }

    private void drawCorners(Canvas canvas) {
        Rect topleft1 = new Rect((this.getWidth() - centerWidth) / 2,
                (this.getHeight() - centerHeight) / 2,
                (this.getWidth() - centerWidth) / 2 + cornerW,
                (this.getHeight() - centerHeight) / 2 + cornerH);
        Rect topleft2 = new Rect((this.getWidth() - centerWidth) / 2,
                (this.getHeight() - centerHeight) / 2 + cornerH,
                (this.getWidth() - centerWidth) / 2 + cornerH,
                (this.getHeight() - centerHeight) / 2 + cornerW);

        canvas.drawRect(topleft1, cornerPaint);
        canvas.drawRect(topleft2, cornerPaint);

        Rect topRight1 = new Rect(
                (this.getWidth() + centerWidth) / 2 - cornerW,
                (this.getHeight() - centerHeight) / 2,
                (this.getWidth() + centerWidth) / 2,
                (this.getHeight() - centerHeight) / 2 + cornerH);
        Rect topRight2 = new Rect(
                (this.getWidth() + centerWidth) / 2 - cornerH,
                (this.getHeight() - centerHeight) / 2 + cornerH,
                (this.getWidth() + centerWidth) / 2,
                (this.getHeight() - centerHeight) / 2 + cornerW);

        canvas.drawRect(topRight1, cornerPaint);
        canvas.drawRect(topRight2, cornerPaint);

        Rect bottomLeft1 = new Rect((this.getWidth() - centerWidth) / 2,
                (this.getHeight() + centerHeight) / 2 - cornerH,
                (this.getWidth() - centerWidth) / 2 + cornerW,
                (this.getHeight() + centerHeight) / 2);
        Rect bottomLeft2 = new Rect((this.getWidth() - centerWidth) / 2,
                (this.getHeight() + centerHeight) / 2 - cornerW,
                (this.getWidth() - centerWidth) / 2 + cornerH,
                (this.getHeight() + centerHeight) / 2 - cornerH);

        canvas.drawRect(bottomLeft1, cornerPaint);
        canvas.drawRect(bottomLeft2, cornerPaint);

        Rect bottomRight1 = new Rect((this.getWidth() + centerWidth) / 2
                - cornerW, (this.getHeight() + centerHeight) / 2 - cornerH,
                (this.getWidth() + centerWidth) / 2,
                (this.getHeight() + centerHeight) / 2);
        Rect bottomRight2 = new Rect((this.getWidth() + centerWidth) / 2
                - cornerH, (this.getHeight() + centerHeight) / 2 - cornerW,
                (this.getWidth() + centerWidth) / 2,
                (this.getHeight() + centerHeight) / 2 - cornerH);

        canvas.drawRect(bottomRight1, cornerPaint);
        canvas.drawRect(bottomRight2, cornerPaint);

    }

}
