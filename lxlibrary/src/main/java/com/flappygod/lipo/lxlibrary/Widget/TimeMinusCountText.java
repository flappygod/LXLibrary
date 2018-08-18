package com.flappygod.lipo.lxlibrary.Widget;

import android.content.Context;
import android.graphics.Canvas;
import android.text.SpannableString;
import android.util.AttributeSet;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 2017/6/22.
 */

public class TimeMinusCountText extends android.support.v7.widget.AppCompatTextView{


    //开始时间
    private Date startDate;
    //结束时间
    private Date endDate;
    //格式
    SimpleDateFormat dateformat2 = new SimpleDateFormat("HH:mm");
    //状态改变
    private StateChangeListener myStateChangeListener;
    //代表是否
    private int STATE=-1;


    public TimeMinusCountText(Context context) {
        super(context);
    }

    public TimeMinusCountText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TimeMinusCountText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public void setStartDateFromNower(long count){
        long time=new Date().getTime();
        this.startDate = new Date(time+count*1000);
    }

    public void setEndDateFromNower(long count){
        long time=new Date().getTime();
        this.endDate = new Date(time+count*1000);
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }



    /******************
     * 0 还未开始 1 正在进行 2 已经结束
     ******************/
    public int isInDate(Date date) {
        if(startDate==null||endDate==null)
            return -1;

        if (date.getTime() < startDate.getTime())
            return 0;
        if (date.getTime() < endDate.getTime()
                && date.getTime() > startDate.getTime()) {
            return 1;
        }
        if (date.getTime() > endDate.getTime())
            return 2;

        return 0;

    }

    public String leftTime(Date date) {
        long time = endDate.getTime() - date.getTime();
        int day = (int) (time / 1000 / 60 / 60 /24);
        int hour = (int) (time / 1000 / 60 / 60 %24);
        int mini = (int) (time / 1000 / 60 % 60);
        int sec = (int) (time / 1000 % 60);

        if(day==0) {
            return getTwoStr(hour) + ":" + getTwoStr(mini) + ":" + getTwoStr(sec) + "";
        }else{
            return day+ ":"+getTwoStr(hour) + ":" + getTwoStr(mini) + ":" + getTwoStr(sec) + "";
        }

    }

    private String getTwoStr(int num){
        if(num<10){
            return "0"+num;
        }else {
            return ""+num;
        }
    }

    public interface StateChangeListener{
        public void changed(int t);
    }
    public void setStateChangeListener(StateChangeListener li){
        myStateChangeListener=li;
    }

    public int init(){

        Date date=new Date();

        int isinornot=isInDate(date);

        if (isinornot == 0) {
            this.setText(dateformat2.format(startDate)+"至"+dateformat2.format(endDate));
        }
        else if (isinornot == 1) {
            String str=leftTime(date);
            {
                SpannableString styledText = new SpannableString(str);
                this.setText(styledText);
            }
        }
        else if (isinornot == 2) {
            this.setText("00:00:00");
        }
        return isinornot;
    }

    public void initChanged(){
        int t=init();
        if(myStateChangeListener!=null)
        {
            myStateChangeListener.changed(t);
        }
    }

    protected void onDraw(Canvas canvas) {

        int isinornot= init();

        if(STATE==-1){
            STATE=isinornot;
        }else if(STATE!=isinornot){
            if(myStateChangeListener!=null)
            {
                myStateChangeListener.changed(isinornot);
            }
            STATE=isinornot;
        }
        super.onDraw(canvas);
        this.postInvalidateDelayed(1000);
    }



}
