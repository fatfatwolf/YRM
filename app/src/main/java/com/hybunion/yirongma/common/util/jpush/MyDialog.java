package com.hybunion.yirongma.common.util.jpush;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.hybunion.yirongma.R;

/**
 * Created by Iron Man on 2016/7/28.
 */
public class MyDialog extends Dialog {
    public MyDialog(Context context, int theme) {
        super(context, theme);

    }

    /**
     * dialog的助手类
     */
    public static class Builder{
        //获取需要自定义的组件
        private String tv_title,tv_content; //标题和内容
        private String btn_cancel,btn_confirm;//两种按钮的内容
        private DialogInterface.OnClickListener cancelListener,confirmListener;//回调接口对象
        private Context context;//上下文对象

        public Builder(Context context){
            this.context = context;
        }

        /**
         * 直接使用字符串来设置标题
         */
        public Builder setTitle(String title){
            this.tv_title = title;
            return this;
        }

        /**
         * 使用资源文件设置标题
         */
        public Builder setTitle(int id){
            this.tv_title = (String) context.getText(id);
            return this;
        }

        /**
         * 使用字符串设置dialog的提示内容
         */
        public Builder setContent(String content){
            this.tv_content = content;
            return this;
        }

        /**
         * 使用资源文件来设置dialog的内容
         */
        public Builder setContent(int id){
            this.tv_content = (String) context.getText(id);
            return this;
        }

        /**
         * 设置左边按钮的文字提示和回调函数
         */
        public Builder setCancelButton(String cancel,DialogInterface.OnClickListener cancelListener){
            this.btn_cancel = cancel;
            this.cancelListener = cancelListener;
            return this;
        }

        /**
         * 使用资源文件设置左边按钮的挑剔提示和回调函数
         */
        public Builder setCancelButton(int id,DialogInterface.OnClickListener cancelListener){
            this.btn_cancel = (String) context.getText(id);
            this.cancelListener = cancelListener;
            return this;
        }

        /**
         * 设置右边确定按钮的文字提示和回调函数
         */
        public Builder setConfirmButton(String confirm,DialogInterface.OnClickListener confirmListener){
            this.btn_confirm = confirm;
            this.confirmListener = confirmListener;
            return this;
        }

        /**
         * 使用资源文件来设置确定按钮的文字提示和回调函数
         */
        public Builder setConfirmButton(int id,DialogInterface.OnClickListener confirmListener){
            this.btn_confirm = (String) context.getText(id);
            this.confirmListener = confirmListener;
            return this;
        }

        /**
         * 在create方法中完成初始化和事件的注册
         */
        public MyDialog create(){

            //获取自定义的布局文件
            View viewRoot = LayoutInflater.from(context).inflate(R.layout.dialog_layout,null);

            //获取自定义dialog实例，并引入自定义的样式
            final MyDialog dialog = new MyDialog(context,R.style.JPushDialog);

            //设置标题显示
            if(tv_title != null){
                ((TextView)viewRoot.findViewById(R.id.tv_title)).setText(tv_title);
            }

            //设置显示的内容
            if(tv_content != null){
                ((TextView)viewRoot.findViewById(R.id.tv_content)).setText(tv_content);
            }

            //设置左边按钮的文本和实现回调函数
            if(btn_cancel != null){
                Button button_cancel = (Button) viewRoot.findViewById(R.id.btn_cancel);
                button_cancel.setText(btn_cancel);
                if(cancelListener != null) {
                    button_cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            cancelListener.onClick(dialog, DialogInterface.BUTTON_NEGATIVE);
                        }
                    });
                }
            }else {
                viewRoot.findViewById(R.id.btn_cancel).setVisibility(View.GONE);
            }

            //设置确定按钮的文本和实现回调函数
            if(btn_confirm != null){
                Button button_confirm = (Button) viewRoot.findViewById(R.id.btn_confirm);
                button_confirm.setText(btn_confirm);
                if(confirmListener != null){
                    button_confirm.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            confirmListener.onClick(dialog,DialogInterface.BUTTON_NEGATIVE);
                        }
                    });
                }

            }else {
                viewRoot.findViewById(R.id.btn_confirm).setVisibility(View.GONE);
            }

            dialog.setContentView(viewRoot);
            return dialog;
        }
    }

}
