package com.flappygod.lipo.lxlibrary.Tools;

import android.os.Handler;
import android.os.Message;
import android.widget.Button;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;


/******************
 * version  1.0.0
 */
public class CountDownTool {


    //计时器
    private List<BtnHandler> countModels = new ArrayList<>();
    //计数文本格式
    private String fromat;

    //构建
    public CountDownTool() {
        this.fromat = "TT秒";
    }

    //构建
    public CountDownTool(String fo) {
        if (fo.contains("TT")) {
            this.fromat = fo;
        } else {
            this.fromat = "TT秒";
        }
    }

    /**************
     * 清理掉所有打开的线程
     */
    public void clear() {
        synchronized (countModels) {
            for (int s = 0; s < countModels.size(); s++) {
                Button button = countModels.get(s).safebtn.get();
                //设置可点击
                if (button != null) {
                    button.setEnabled(true);
                }
                //取消timer
                countModels.get(s).timer.cancel();
            }
            countModels = new ArrayList<>();
        }
    }


    /************
     *
     * @param btn   按钮
     * @param time  时间
     * @param endStr 结束显示的字符串
     */
    public synchronized void setBtnCountDown(Button btn, int time, String endStr) {

        synchronized (countModels) {
            //移除掉btn已经被销毁的或者是当前的按钮
            for (int s = 0; s < countModels.size(); s++) {
                Button button = countModels.get(s).safebtn.get();
                if (button == btn) {
                    countModels.get(s).timer.cancel();
                    countModels.remove(s);
                    s--;
                }
            }
            //设置不可点击
            btn.setEnabled(false);
            //创建timer
            Timer timer = new Timer(true);
            //创建handler
            BtnHandler handleVerif = new BtnHandler(btn, timer, endStr);
            //添加到列表中
            countModels.add(handleVerif);
            //开始执行
            timer.schedule(new LTimerTask(time, handleVerif), 1000, 1000);
        }
    }

    /********************
     * 使用弱引用优化后的handler
     *
     * @author lijunlin
     *
     ********************/
    class BtnHandler extends Handler {

        private WeakReference<Button> safebtn;
        private Timer timer;
        private String endStr;

        BtnHandler(Button btn,
                   Timer timer,
                   String endStr) {
            this.safebtn = new WeakReference<Button>(btn);
            this.timer = timer;
            this.endStr = endStr;
        }

        public void handleMessage(Message msg) {
            //时间
            int timeCount = (Integer) msg.obj;
            //获取按钮
            Button btn = safebtn.get();
            //不为空
            if (btn != null) {
                //设置时间
                btn.setText(fromat.replace("@", Integer.toString(timeCount)));
                //小于零
                if (timeCount <= 0) {
                    //设置endStr
                    btn.setText(endStr);
                    //可点击
                    btn.setEnabled(true);
                    //取消timer
                    timer.cancel();
                    //移除这个Handler
                    synchronized (countModels) {
                        countModels.remove(this);
                    }
                }
            }
        }

    }

    /********
     * timertask
     *
     * @author 李俊霖
     *
     */
    class LTimerTask extends TimerTask {
        private int timeCount;
        private Handler handler;

        public LTimerTask(int time, Handler handler) {
            this.timeCount = time;
            this.handler = handler;
        }

        @Override
        public void run() {
            timeCount--;
            Message msg = handler.obtainMessage(0, timeCount);
            handler.sendMessage(msg);
        }

    }

}
