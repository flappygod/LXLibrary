package com.flappygod.lipo.lxlibrary.Widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.flappygod.lipo.lxlibrary.R;


/**
 * Created by yang on 2016/9/22.
 */
public class LRatingBar extends View {
    //监听
    private OnStarChangedListener listener;
    //星星的宽度
    private int ratingBarWidth;
    //默认每个星星的间隔
    private int starSpace = 2;
    //默认的星星个数
    private int starNum = 5;
    //当前的rating
    private float rating = 0.0f;
    //填充了的rating
    private Bitmap filledBitmap;
    //没有填充的rating
    private Bitmap emptyBitmap;

    public LRatingBar(Context context) {
        super(context);
    }

    public LRatingBar(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray mTypedArray = context.obtainStyledAttributes(attrs, R.styleable.RatingBar);
        this.starNum = mTypedArray.getInteger(R.styleable.RatingBar_lxlibrary_starCount, 5);
        this.emptyBitmap = ((BitmapDrawable) mTypedArray.getDrawable(R.styleable.RatingBar_lxlibrary_starEmpty)).getBitmap();
        this.filledBitmap = ((BitmapDrawable) mTypedArray.getDrawable(R.styleable.RatingBar_lxlibrary_starFill)).getBitmap();
    }

    public LRatingBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    /***********
     * @param w    Current width of this view.
     * @param h    Current height of this view.
     * @param oldw Old width of this view
     * @param oldh Old height of this view.
     */
    public void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        caculateWidth(w, h);
    }

    /*********
     * 计算出当前星星的宽高
     *
     * @param width  宽度
     * @param height
     */
    private void caculateWidth(int width, int height) {
        //0个不允许
        if (starNum == 0)
            return;
        //计算出星星的宽度
        int starW = (width - (starNum - 1) * starSpace) / starNum;
        /*if (height < starW) {
            starW = height;
        }*/
        ratingBarWidth = starW;
    }

    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //计算宽高
        if (ratingBarWidth == 0) {
            caculateWidth(getWidth(), getHeight());
        }
        drawStars(canvas);
    }

    /***************
     * 绘制星星
     *
     * @param canvas
     */
    private void drawStars(Canvas canvas) {

        for (int s = 0; s < starNum; s++) {
            //获取中心点
            int centerY = getHeight() / 2;

            int left = s * (ratingBarWidth + starSpace);
            int top = centerY - (ratingBarWidth / 2);
            int right = s * (ratingBarWidth + starSpace) + ratingBarWidth;
            int bottom = centerY + (ratingBarWidth / 2);

            //绘制下方
            Rect rect = new Rect(left, top, right, bottom);
            canvas.drawBitmap(emptyBitmap, null, rect, null);

            float topF = rating - s;
            if (topF >= 1) {
                canvas.drawBitmap(filledBitmap, null, rect, null);
            } else if (topF > 0) {
                Rect srcRect = new Rect(0, 0, (int) (filledBitmap.getWidth() * topF), filledBitmap.getHeight());
                Rect desRect = new Rect(rect.left, rect.top, (int) (rect.left + rect.width() * topF), rect.bottom);
                canvas.drawBitmap(filledBitmap, srcRect, desRect, null);
            }
        }
    }

    /**********
     * 设置rating
     *
     * @param rating
     */
    public void setRating(float rating) {
        this.rating = rating;
        invalidate();
    }


    public float getRating() {
        return rating;
    }


    /************
     * 设置星星数量
     *
     * @param number
     */
    public void setStarNumber(int number) {
        this.starNum = number;
        invalidate();
    }

    /**************
     * 设置间隔
     *
     * @param space
     */
    public void setStarSpace(int space) {
        this.starSpace = space;
    }


    public interface OnStarChangedListener {
        void onStarChanged(int starNum);
    }


    public void setOnStarChangedListener(OnStarChangedListener listener) {
        this.listener = listener;
    }


    //判断是不是点击事件
    private float x;
    private float y;

    public boolean onTouchEvent(MotionEvent event) {
        if (isEnabled()) {
            //默认响应touch时间
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                x=event.getX();
                y=event.getY();
            }
            /*if (event.getAction() == MotionEvent.ACTION_MOVE) {
                isClick = false;
            }*/
            //默认响应touch时间
            if (event.getAction() == MotionEvent.ACTION_UP) {
                if (Math.abs(x-event.getX())<6&& Math.abs(y-event.getY())<6) {
                    float x = event.getX();
                    int star = (int) (x/(getWidth()/starNum)+1);
                    setRating(star);
                    if (listener != null && star <= starNum) {
                        listener.onStarChanged(star);
                    }
                }
            }
            return true;
        } else {
            return super.onTouchEvent(event);
        }

    }


}
