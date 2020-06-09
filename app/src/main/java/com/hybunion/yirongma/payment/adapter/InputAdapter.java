package com.hybunion.yirongma.payment.adapter;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.hybunion.yirongma.R;
import com.hybunion.yirongma.payment.db.LoginModel;
import com.hybunion.yirongma.payment.db.RemoveListner;
import com.hybunion.yirongma.payment.utils.LogUtils;

import java.util.List;

public class InputAdapter extends BaseAdapter {
	private Context mContext;
	public List<LoginModel> list_msg;
	private RemoveListner removeListner;
	private View dialog_Signture;
	private Dialog inputDialog;
	private Button btn_cancel, btn_ok;
	TextView tv_context;
	public InputAdapter(Context mContext, List<LoginModel> list_msg, RemoveListner removeListner) {
		super();
		this.mContext = mContext;
		this.list_msg = list_msg;
		this.removeListner = removeListner;
	}

	@Override
	public int getCount() {
		return list_msg.size();
	}

	@Override
	public Object getItem(int position) {
		return list_msg.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder vh = null;
		String proName = null;

		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(R.layout.item_message, null);
			vh = new ViewHolder();
			vh.message_name = (TextView) convertView.findViewById(R.id.message_name);
			vh.message_msg = (ImageView) convertView.findViewById(R.id.message_msg);
			convertView.setTag(vh);
		} else {
			vh = (ViewHolder) convertView.getTag();
		}
		try {
			final LoginModel result = list_msg.get(position);
			vh.message_name.setText(result.getUname());
			vh.message_msg.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					inputDialog(position,result);

				}
			});
		} catch (Exception e) {
		}

		return convertView;
	}

	class ViewHolder {
		TextView message_name;
		ImageView message_msg;
	}
	public void inputDialog(final int position, final LoginModel result) {
		dialog_Signture = LayoutInflater.from(mContext).inflate(R.layout.layout_delete_num, null);
		inputDialog = new Dialog(mContext, R.style.Dialog_image);
		inputDialog.setContentView(dialog_Signture);
		inputDialog.setContentView(dialog_Signture);
		inputDialog.setCanceledOnTouchOutside(true);
		inputDialog.getWindow().setGravity(Gravity.FILL);
		tv_context = (TextView) dialog_Signture.findViewById(R.id.tv_context);
		String num=list_msg.get(position).getUname();
		LogUtils.dlyj(num+"需删除的账号");
		tv_context.setText("您确定删除"+num+"这个号码吗?");
		btn_ok = (Button) dialog_Signture.findViewById(R.id.submit_ok);
		btn_ok.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				removeListner.setRemove(position, result);
				inputDialog.dismiss();
				}
		});
		btn_cancel = (Button) dialog_Signture.findViewById(R.id.submit_no);
		btn_cancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				inputDialog.dismiss();
			}
		});
		inputDialog.show();
	}
}
