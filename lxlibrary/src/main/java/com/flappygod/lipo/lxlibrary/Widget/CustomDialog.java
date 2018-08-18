package com.flappygod.lipo.lxlibrary.Widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.TextView;

import com.flappygod.lipo.lxlibrary.BaseView.Dialog.BaseDialog;
import com.flappygod.lipo.lxlibrary.R;


/**********
 * 默认的对话框
 * Package Name:com.holpe.maike.salesplus.widget <br/>
 * ClassName: CustomDialog <br/> 
 * Function: TODO 功能说明 <br/> 
 * date: 2015-9-15 下午3:56:15 <br/> 
 * 
 * @author lijunlin
 */
public class CustomDialog extends BaseDialog {

	public CustomDialog(Context context, int theme) {
		super(context, theme);
	}

	public CustomDialog(Context context) {
		super(context);
	}
	
	public static class Builder {
		//上下文
		private Context context;
		//标题
		private String title;
		//消息
		private String message;
		//消息
		private SpannableString messageSpannable;
		//确认按钮
		private DialogInterface.OnClickListener confirm;
		//确认按钮显示
		private String confirmStr;
		//取消按钮
		private DialogInterface.OnClickListener cancel;
		//取消按钮显示String
		private String cancelStr;
		//title
		private TextView titleView;
		//消息
		private TextView messageView;
		//确认按钮
		private Button confrimBtn;
		//取消按钮
		private Button cancleBtn;
		
		public Builder(Context context) {
			this.context = context;
		}
		public Builder setMessage(String message) {
			this.message = message;
			return this;
		}
		public Builder setMessage(SpannableString message) {
			this.messageSpannable = message;
			return this;
		}
		public Builder setTitle(String title) {
			this.title = title;
			return this;
		}
		public Builder setConfrimButton(String str,
				DialogInterface.OnClickListener listener) {
			confirm=listener;
			confirmStr=str;
			return this;
		}
		public Builder setCancelButton(String str,
				DialogInterface.OnClickListener listener) {
			cancel=listener;
			cancelStr=str;
			return this;
		}
		/**********
		 * 创建dialog
		 * @return
		 */
		@SuppressLint("InflateParams")
		public CustomDialog create() {
			//构建dialog
			final CustomDialog dialog = new CustomDialog(context, R.style.lxlibrary_dialog_custom);
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View view = inflater.inflate(R.layout.lxlibrary_dialog_custom, null);
			titleView = (TextView) view.findViewById(R.id.lxlibrary_dialog_custom_title);
			messageView = (TextView) view.findViewById(R.id.lxlibrary_dialog_custom_message);
			confrimBtn= (Button) view.findViewById(R.id.lxlibrary_dialog_custom_confrim);
			cancleBtn= (Button) view.findViewById(R.id.lxlibrary_dialog_custom_cancel);
			dialog.addContentView(view, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
			//设置title
			titleView.setText(title);
			if(message!=null){
				messageView.setText(message);
			}
			if(messageSpannable!=null){
				messageView.setText(messageSpannable);
			}
			confrimBtn.setText(confirmStr);
			confrimBtn.setOnClickListener(new View.OnClickListener(){
				@Override
				public void onClick(View v) {
					if(confirm!=null){
						confirm.onClick(dialog, 0);
					}
				}
			});
			cancleBtn.setText(cancelStr);
			cancleBtn.setOnClickListener(new View.OnClickListener(){
				@Override
				public void onClick(View v) {
					if(cancel!=null){
						cancel.onClick(dialog, 0);
					}
				}
			});
			return  dialog;
		}
	}
}
