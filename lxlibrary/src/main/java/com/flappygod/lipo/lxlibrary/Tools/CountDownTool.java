package com.flappygod.lipo.lxlibrary.Tools;

import android.os.Handler;
import android.os.Message;
import android.widget.Button;

import java.lang.ref.WeakReference;
import java.util.Iterator;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;


/******************
 * version  1.0.0
 */
public class CountDownTool {

	private ConcurrentHashMap<Button, Timer> timers = new ConcurrentHashMap<Button, Timer>();

	/**************
	 * 清理掉所有打开的线程
	 */
	public void clear(){
		Iterator<Button> it1 = timers.keySet().iterator();
		while(it1.hasNext()){
            Button key = it1.next();
            timers.get(key).cancel();
        }
		timers.clear();
		timers=null;
	}
	
	
	public synchronized void setVerifBtnCantClick(final Button btn, int time) {
		// 如果正在
		if (timers.containsKey(btn)) {
			Timer itmer = timers.get(btn);
			itmer.cancel();
			timers.remove(btn);
		}
		btn.setClickable(false);
		final Timer timer = new Timer(true);
		final Handler handleVerif = new BtnHandler(btn, timer, timers);
		timer.schedule(new LTimerTask(time, handleVerif), 1000, 1000);
		timers.put(btn, timer);
	}

	/********************
	 * 使用弱引用优化后的handler
	 * 
	 * @author lijunlin
	 * 
	 ********************/
	static class BtnHandler extends Handler {

		private WeakReference<Button> safebtn;
		private WeakReference<Timer> safetimer;
		private WeakReference<ConcurrentHashMap<Button, Timer>> timers;

		BtnHandler(Button btn, Timer timer,
				   ConcurrentHashMap<Button, Timer> timers) {
			this.safebtn = new WeakReference<Button>(btn);
			this.safetimer = new WeakReference<Timer>(timer);
			this.timers = new WeakReference<ConcurrentHashMap<Button, Timer>>(
					timers);
		}

		public void handleMessage(Message msg) {
			int timeCount = (Integer) msg.obj;
			Button btn = safebtn.get();
			if (btn != null) {
				btn.setText(timeCount + "秒");
				if (timeCount <= 0) {
					btn.setText("获取验证码");
					btn.setClickable(true);
					Timer timer = safetimer.get();
					if (timer != null) {
						timer.cancel();
						ConcurrentHashMap<Button, Timer> mtimers = timers.get();
						if (mtimers != null) {
							mtimers.remove(btn);
						}
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
