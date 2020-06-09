package com.hybunion.yirongma.payment.view;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.hybunion.yirongma.R;
import com.hybunion.yirongma.payment.base.CommonAdapter1;
import com.hybunion.yirongma.payment.base.ViewHolder;

import java.util.List;

/**
 * 从下面弹出，带选择条目的 Dialog
 */

public class MyBottomDialog extends Dialog {
    private Context mContext;
    private TextView mTitle;
    private ListView mListView;
    private CommonAdapter1 mAdapter;

    public MyBottomDialog(Context context) {
        super(context);
        mContext = context;
        initView();
    }

    public MyBottomDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    protected MyBottomDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    private void initView() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_my_bottom_dialog, null);
        setContentView(view);
        Window window = this.getWindow();
        window.setGravity(Gravity.BOTTOM);
        window.setBackgroundDrawableResource(R.drawable.shape_my_bottom_dialog);
        window.setWindowAnimations(R.style.dialog_anim);
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(params);
        mTitle = view.findViewById(R.id.storeName_my_bottom_dialog);
        view.findViewById(R.id.cancel_my_bottom_dialog).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyBottomDialog.this.dismiss();
            }
        });
        mListView = view.findViewById(R.id.listView_my_bottom_dialog);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mListener != null)
                    mListener.itemListener(position);
            }
        });

    }

    // 外部调用
    public void showThisDialog(String name, List<String> itemStings, OnDialogItemListener listener) {
        mTitle.setText(name);
        if (mAdapter == null) {
            mListener = listener;
            mListView.setAdapter(mAdapter = new CommonAdapter1<String>(mContext, itemStings, R.layout.item_my_bottom_dialog) {
                @Override
                public void convert(ViewHolder holder, String item, int position) {
                    TextView itemTextView = holder.findView(R.id.tv_item_my_bottom_dialog);
                    itemTextView.setText(item);
                }
            });
        }
        this.show();
    }

    public OnDialogItemListener mListener;

    public interface OnDialogItemListener {
        void itemListener(int position);
    }

}
