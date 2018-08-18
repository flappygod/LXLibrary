package com.flappygod.lipo.lxlibrary.Widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CompoundButton;

/*******************
 * 自定义滑动的checkbox Package Name:com.example.testltextview <br/>
 * ClassName: LCheckView <br/>
 * Function: TODO 功能说明 <br/>
 * date: 2015-9-8 下午3:19:06 <br/>
 * 
 * @author lijunlin
 */

public class LCheckView extends CompoundButton {

	// 外部设置的监听
	private OnTouchListener mOuterOnTouchListener;

	// 背景绘制的颜色
	private int CHECK_BACK_COLOR = 0xff55bb9f;
	// 圆形绘制的颜色
	private int CHECK_CIRCLE_COLOR = 0xFF2d9e9f;
	// 没有被选中时的颜色
	private int CHECK_BACK_GRAY_COLOR = 0xFFDDDDDD;
	// 没有被选中时的当中的颜色
	private int CHECK_CIRCLE_GRAY_COLOR = 0xFF999999;

	// 中间横杠背景的大小
	private float CENTER_SCALE = 0.55f;
	// 中间圆形相当于高度的比例
	private float CIRCLE_SCALE = 0.6f;

	// 第一次进入初始化坐标
	private boolean firstInit = false;

	// 动画移动的handler
	private CountHandler positionHandler;

	// 初始的X方向的值
	private float X_position = -1;

	private float lastPosition;
	private float downPositionX;
	private float downPositionY;

	private boolean  enableClick=false;

	public LCheckView(Context context) {
		super(context);
		initOntouch();
	}

	public LCheckView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initOntouch();
	}

	public LCheckView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	public void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		initFirstPosition();
		drawaCenterBack(canvas);
		drawaCenterCircle(canvas);
	}

	public boolean isEnableClick() {
		return enableClick;
	}

	public void setEnableClick(boolean enableClick) {
		this.enableClick = enableClick;
	}

	/************
	 * 设置控件的颜色
	 * 
	 * @param checkedLineColor
	 *            选中时横杠颜色
	 * @param checkedCircleColor
	 *            选中时圆的颜色
	 * @param uncheckLineColor
	 *            未选中时横杠颜色
	 * @param uncheckCircleColor
	 *            未选中时的圆的颜色
	 */
	public void setWidgetColor(int checkedLineColor, int checkedCircleColor,
			int uncheckLineColor, int uncheckCircleColor) {
		this.CHECK_BACK_COLOR = checkedLineColor;
		this.CHECK_CIRCLE_COLOR = checkedCircleColor;
		this.CHECK_BACK_GRAY_COLOR = uncheckLineColor;
		this.CHECK_CIRCLE_GRAY_COLOR = uncheckCircleColor;
		invalidate();
	}

	/****************
	 * 设置选中状态
	 * 
	 * @param checked
	 ****************/
	public void setChecked(boolean checked) {
		// 如果第一次初始化都没有完成，那就不理他
		if (!firstInit) {
			super.setChecked(checked);
			return;
		}
		if (checked) {
			setToMaxX();
		} else {
			setToMinX();
		}
	}

	/****************
	 * 绘制中间的back
	 * 
	 * @param canvas
	 */
	private void drawaCenterBack(Canvas canvas) {
		int width = this.getWidth() - this.getPaddingLeft()
				- this.getPaddingRight();
		int height = this.getHeight() - this.getPaddingBottom()
				- this.getPaddingTop();
		if (height > width) {
			return;
		}
		// 计算出中间的radius
		int radius = (int) (height * CENTER_SCALE) / 2;
		Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG
				| Paint.FILTER_BITMAP_FLAG);
		paint.setStyle(Paint.Style.FILL);
		if (this.isChecked()) {
			paint.setColor(CHECK_BACK_COLOR);
		} else {
			paint.setColor(CHECK_BACK_GRAY_COLOR);
		}
		canvas.drawCircle(this.getPaddingLeft() + radius, this.getHeight() / 2,
				radius, paint);
		canvas.drawCircle(this.getWidth() - this.getPaddingRight() - radius,
				this.getHeight() / 2, radius, paint);
		canvas.drawRect(this.getPaddingLeft() + radius, this.getHeight() / 2
				- radius, this.getWidth() - this.getPaddingRight() - radius,
				this.getHeight() / 2 + radius, paint);

	}

	/****************
	 * 绘制当中的圆形
	 * 
	 * @param canvas
	 */
	private void drawaCenterCircle(Canvas canvas) {
		// 计算尺圆的半径
		int height = this.getHeight() - this.getPaddingBottom()
				- this.getPaddingTop();
		int radius = (int) (height * CIRCLE_SCALE) / 2;

		int minX = this.getPaddingLeft() + radius;
		int maxX = this.getWidth() - this.getPaddingRight() - radius;

		Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG
				| Paint.FILTER_BITMAP_FLAG);
		paint.setStyle(Paint.Style.FILL);
		// 绘制阴影
		if (X_position <= minX) {
			paint.setColor(0x33000000);
			canvas.drawCircle(minX, this.getHeight() / 2 + 1, radius, paint);
			paint.setColor(0x11000000);
			canvas.drawCircle(minX, this.getHeight() / 2 + 3, radius, paint);
		} else if (X_position >= maxX) {
			paint.setColor(0x33000000);
			canvas.drawCircle(maxX, this.getHeight() / 2 + 1, radius, paint);
			paint.setColor(0x11000000);
			canvas.drawCircle(maxX, this.getHeight() / 2 + 3, radius, paint);
		} else {
			paint.setColor(0x33000000);
			canvas.drawCircle(X_position, this.getHeight() / 2 + 1, radius,
					paint);
			paint.setColor(0x11000000);
			canvas.drawCircle(X_position, this.getHeight() / 2 + 3, radius,
					paint);
		}

		// 设置选中颜色
		if (this.isChecked()) {
			paint.setColor(CHECK_CIRCLE_COLOR);
		} else {
			paint.setColor(CHECK_CIRCLE_GRAY_COLOR);
		}
		// 绘制中心圆
		if (X_position <= minX) {
			canvas.drawCircle(minX, this.getHeight() / 2, radius, paint);
		} else if (X_position >= maxX) {
			canvas.drawCircle(maxX, this.getHeight() / 2, radius, paint);
		} else {
			canvas.drawCircle(X_position, this.getHeight() / 2, radius, paint);
		}

	}

	/*************
	 * 坐标变换的回调 Package Name:com.example.testltextview <br/>
	 * ClassName: PositionCallBack <br/>
	 * Function: TODO 功能说明 <br/>
	 * date: 2015-9-8 下午1:36:56 <br/>
	 * 
	 * @author lijunlin
	 */
	private interface PositionCallBack {
		void positionMove(float positon);
	}

	/************
	 * 坐标移动动画的handler Package Name:com.example.testltextview <br/>
	 * ClassName: CountHandler <br/>
	 * Function: TODO 功能说明 <br/>
	 * date: 2015-9-8 下午1:37:13 <br/>
	 * 
	 * @author lijunlin
	 */
	static class CountHandler extends Handler {
		private float nower;
		private float destination;
		private PositionCallBack callback;
		public static float proportion = 0.10f;
		public static int minMove = 3;
		public static int duration = 5;

		private boolean canceled = false;

		public CountHandler(float nower, float destination,
				PositionCallBack callback) {
			this.nower = nower;
			this.destination = destination;
			this.callback = callback;
		}

		// 取消掉
		public void cancle() {
			canceled = true;
		}

		public void handleMessage(Message msg) {
			// 如果已经取消了就不再进行
			if (canceled) {
				return;
			}
			// 否则计算移动量
			float s = (destination - nower) * proportion;
			if (Math.abs(s) < minMove) {
				if (destination > nower) {
					nower = nower + minMove;
				} else if (destination < nower) {
					nower = nower - minMove;
				}
			} else {
				nower = nower + s;
			}
			// 如果到达终点了

			if (Math.abs(destination - nower) <= minMove) {
				nower = destination;
				callback.positionMove(nower);
			}
			// 没有到达终点就继续
			else {
				callback.positionMove(nower);
				this.sendEmptyMessageDelayed(0, duration);
			}

		}
	}

	/***********
	 * 移动到某个位置
	 * 
	 * @param position
	 */
	private void ScrollTo(int position) {
		if (positionHandler != null) {
			positionHandler.cancle();
		}
		positionHandler = new CountHandler(X_position, position,
				new PositionCallBack() {

					@Override
					public void positionMove(float positon) {
						X_position = positon;
						invalidate();
					}
				});
		positionHandler.sendEmptyMessage(0);
	}

	/************
	 * 设置到最小的值 取消选中
	 */
	private void setToMinX() {
		// 计算尺圆的半径
		int height = this.getHeight() - this.getPaddingBottom()
				- this.getPaddingTop();
		int radius = (int) (height * CIRCLE_SCALE) / 2;
		ScrollTo(this.getPaddingLeft() + radius);
		super.setChecked(false);
		invalidate();
	}

	/**********************
	 * 设置到最大的值 选中
	 **********************/
	private void setToMaxX() {
		// 计算尺圆的半径
		int height = this.getHeight() - this.getPaddingBottom()
				- this.getPaddingTop();
		int radius = (int) (height * CIRCLE_SCALE) / 2;
		ScrollTo(this.getWidth() - this.getPaddingRight() - radius);
		super.setChecked(true);
		invalidate();
	}

	/**********************
	 * 设置touchListener
	 **********************/
	public void setOnTouchListener(OnTouchListener listener) {
		this.mOuterOnTouchListener = listener;
	}

	/**********************
	 * 初始化touch
	 **********************/
	private void initOntouch() {
		super.setOnTouchListener(new OnTouchListener() {
			@SuppressLint("ClickableViewAccessibility")
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (mOuterOnTouchListener != null) {
					return mOuterOnTouchListener.onTouch(v, event);
				}
				if(enableClick){
					return false;
				}
				// 当按键按下的时候
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					downPositionX = event.getX();
					downPositionY = event.getY();
					lastPosition = event.getX();
					// X_position = (int) event.getX();
				} else if (event.getAction() == MotionEvent.ACTION_MOVE) {
					float deltaX = event.getX() - lastPosition;
					lastPosition = event.getX();
					X_position += deltaX;
				} else if (event.getAction() == MotionEvent.ACTION_UP||event.getAction() == MotionEvent.ACTION_CANCEL) {
					if (Math.abs(event.getX() - downPositionX) < 6
							&& Math.abs(event.getY() - downPositionY) < 6) {
						setChecked(!isChecked());
					} else {

						if (X_position > LCheckView.this.getWidth() / 2) {
							setToMaxX();
						} else {
							setToMinX();
						}

					}
				}
				invalidate();
				return true;
			}
		});
	}

	/**********
	 * 首次绘图初始化信息
	 */
	private void initFirstPosition() {
		if (!firstInit) {
			firstInit = true;
			if (this.isChecked()) {
				int height = this.getHeight() - this.getPaddingBottom()
						- this.getPaddingTop();
				int radius = (int) (height * CIRCLE_SCALE) / 2;
				X_position = (this.getWidth() - this.getPaddingRight() - radius);
			} else {
				// 计算尺圆的半径
				int height = this.getHeight() - this.getPaddingBottom()
						- this.getPaddingTop();
				int radius = (int) (height * CIRCLE_SCALE) / 2;
				X_position = (this.getPaddingLeft() + radius);
			}
		}
	}
}
