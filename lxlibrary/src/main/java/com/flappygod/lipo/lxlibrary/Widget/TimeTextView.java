package com.flappygod.lipo.lxlibrary.Widget;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;

import java.util.Date;

public class TimeTextView extends android.support.v7.widget.AppCompatTextView {

    private Date startDate;

    private TimeCountListener listener;

    public  interface  TimeCountListener{
        void  countDownEnd();
    }

    public void setTimeCountListener(TimeCountListener listener){
        this.listener=listener;
    }



    /*********
     * 获取开始时间
     *
     * @return
     */
    public Date getStartDate() {
        return startDate;
    }

    /************
     * 设置开始时间
     * @param date 时间
     */
    public void setStartDate(Date date) {
        if (date.getTime() < new Date().getTime()) {
            this.startDate = date;
        } else {
            this.startDate = new Date();
        }
    }

    public void setDownCountSecond(long second){
        long time=new Date().getTime();
        this.startDate=new Date(time+second*1000);
    }


    /************
     * 计算时间
     */
    private void caculateTime() {
        if (startDate == null) {
            setText(0 + "分" + 0 + "秒");
            return;
        } else {
            long delay = new Date().getTime() - startDate.getTime();
            long day=delay / (1000 * 60 * 60*24);
            long shi = delay / (1000 * 60 * 60)% 24;
            long fen = delay / (1000 * 60)% 60;
            long miao = delay / 1000 % 60;


            if(delay>=0) {
                String str = "";
                if (day != 0) {
                    str = str + day + "天";
                }
                if (day != 0 || shi != 0) {
                    str = str + shi + "时";
                }
                if (day != 0 || shi != 0 || fen != 0) {
                    str = str + fen + "分";
                }
                if (day != 0 || shi != 0 || fen != 0 || miao != 0) {
                    str = str + miao + "秒";
                }
                setText(str);
            }else{
                if(listener!=null){
                    startDate=null;
                    listener.countDownEnd();
                }
            }
        }
    }

    /***********
     *
     * @param canvas
     */
    protected void onDraw(Canvas canvas) {
        caculateTime();
        super.onDraw(canvas);
        this.postInvalidateDelayed(1000);
    }

    public TimeTextView(Context context) {
        super(context);
    }

    public TimeTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TimeTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

}
