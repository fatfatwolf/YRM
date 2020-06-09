package com.hybunion.yirongma.valuecard.view;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hybunion.yirongma.R;

/**
 * Created by Administrator on 2017/2/28.
 */
public class DialogValueCardMake extends Dialog {

    public static final int BUTTONDELETE = 0;//删除符号
    public static final int BUTTONONE = 1;//第一个按钮
    public static final int BUTTONTWO = 2;//第二个按钮
    public static final int BUTTONTHREE = 3;//第三个按钮
    public static final int BUTTONFOUR = 4;//第四个按钮

    public DialogValueCardMake(Context context, int theme) {
        super(context, theme);
    }

    public static class Builder {
        private Context context;
        private String title;
        private int isClick;

        // 按钮文字
        private String ButtonText1;
        private String ButtonText2;
        private String ButtonText3;
        private String ButtonText4;

        private int number; // 按钮数量
        private ImageView imgv_delete;
        private TextView tv_dialogTitle, tv_dialogbtn1, tv_dialogbtn2, tv_dialogbtn3, tv_dialogbtn4;

        private DialogInterface.OnClickListener onDeleteClick;
        private DialogInterface.OnClickListener onButtonClick1;
        private DialogInterface.OnClickListener onButtonClick2;
        private DialogInterface.OnClickListener onButtonClick3;
        private DialogInterface.OnClickListener onButtonClick4;

        public Builder(Context context, int number) {
            this.context = context;
            this.number = number;
        }


        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder setClick(int isClick) {
            this.isClick = isClick;
            return this;
        }

        public Builder onDeleteClick(DialogInterface.OnClickListener listener) {
            this.onDeleteClick = listener;
            return this;
        }

        public Builder onButtonClick1(String ButtonText1,
                                      DialogInterface.OnClickListener listener) {
            this.ButtonText1 = ButtonText1;
            this.onButtonClick1 = listener;
            return this;
        }

        public Builder onButtonClick2(String ButtonText2,
                                      DialogInterface.OnClickListener listener) {
            this.ButtonText2 = ButtonText2;
            this.onButtonClick2 = listener;
            return this;
        }

        public Builder onButtonClick3(String ButtonText3,
                                      DialogInterface.OnClickListener listener) {
            this.ButtonText3 = ButtonText3;
            this.onButtonClick3 = listener;
            return this;
        }

        public Builder onButtonClick4(String ButtonText4,
                                      DialogInterface.OnClickListener listener) {
            this.ButtonText4 = ButtonText4;
            this.onButtonClick4 = listener;
            return this;
        }

        public DialogValueCardMake create() {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final DialogValueCardMake dialog = new DialogValueCardMake(context, R.style.Dialog_image);
            final View layout = inflater.inflate(R.layout.dialog_valuecard_make, null);
            dialog.addContentView(layout, new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            imgv_delete = ((ImageView) layout.findViewById(R.id.imgv_delete));
            tv_dialogTitle = ((TextView) layout.findViewById(R.id.tv_dialogTitle));
            tv_dialogbtn1 = ((TextView) layout.findViewById(R.id.tv_dialogbtn1));
            tv_dialogbtn2 = ((TextView) layout.findViewById(R.id.tv_dialogbtn2));
            tv_dialogbtn3 = ((TextView) layout.findViewById(R.id.tv_dialogbtn3));
            tv_dialogbtn4 = ((TextView) layout.findViewById(R.id.tv_dialogbtn4));
            tv_dialogTitle.setText(title);
            changeColor(context, isClick, tv_dialogbtn1, tv_dialogbtn2, tv_dialogbtn3,tv_dialogbtn4);
            if (ButtonText1 != null) {
                tv_dialogbtn1.setText(ButtonText1);
                if (onButtonClick1 != null) {
                    tv_dialogbtn1.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            changeColor(context, 0, tv_dialogbtn1, tv_dialogbtn2, tv_dialogbtn3,tv_dialogbtn4);
                            onButtonClick1.onClick(dialog,
                                    BUTTONONE);
                        }
                    });
                }
            } else {
                tv_dialogbtn1.setVisibility(View.GONE);
            }

            if (ButtonText2 != null) {
                tv_dialogbtn2.setText(ButtonText2);
                if (onButtonClick2 != null) {
                    tv_dialogbtn2.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            changeColor(context, 1, tv_dialogbtn1, tv_dialogbtn2, tv_dialogbtn3,tv_dialogbtn4);
                            onButtonClick2.onClick(dialog,
                                    BUTTONTWO);
                        }
                    });
                }
            } else {
                tv_dialogbtn2.setVisibility(View.GONE);
            }

            if (onDeleteClick != null) {
                imgv_delete.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        onDeleteClick.onClick(dialog,
                                BUTTONDELETE);
                    }
                });
            }

            if (ButtonText3 != null) {
                tv_dialogbtn3.setText(ButtonText3);
                if (onButtonClick3 != null) {
                    tv_dialogbtn3.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            changeColor(context, 2, tv_dialogbtn1, tv_dialogbtn2, tv_dialogbtn3,tv_dialogbtn4);
                            onButtonClick3.onClick(dialog,
                                    BUTTONTHREE);
                        }
                    });
                }
            } else {
                tv_dialogbtn3.setVisibility(View.GONE);
            }

            if (ButtonText4 != null) {
                tv_dialogbtn4.setText(ButtonText4);
                if (onButtonClick4 != null) {
                    tv_dialogbtn4.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            changeColor(context, 3, tv_dialogbtn1, tv_dialogbtn2, tv_dialogbtn3,tv_dialogbtn4);
                            onButtonClick4.onClick(dialog,
                                    BUTTONFOUR);
                        }
                    });
                }
            } else {
                tv_dialogbtn4.setVisibility(View.GONE);
            }

//            if (number == 3) {
//
//            } else if (number == 2) {
//                tv_dialogbtn3.setVisibility(View.GONE);
//            }

            dialog.setContentView(layout);
            return dialog;
        }

        private void changeColor(Context context, int aClick, TextView tv_dialogbtn1
                , TextView tv_dialogbtn2, TextView tv_dialogbtn3, TextView tv_dialogbtn4) {
            switch (aClick) {
                case 0:
                    tv_dialogbtn1.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.btn_dialog_orange));
                    tv_dialogbtn2.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.btn_dialog_white));
                    tv_dialogbtn3.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.btn_dialog_white));
                    tv_dialogbtn4.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.btn_dialog_white));
                    tv_dialogbtn1.setTextColor(context.getResources().getColor(R.color.white));
                    tv_dialogbtn2.setTextColor(context.getResources().getColor(R.color.textColor_2));
                    tv_dialogbtn3.setTextColor(context.getResources().getColor(R.color.textColor_2));
                    tv_dialogbtn4.setTextColor(context.getResources().getColor(R.color.textColor_2));
                    break;
                case 1:
                    tv_dialogbtn1.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.btn_dialog_white));
                    tv_dialogbtn2.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.btn_dialog_orange));
                    tv_dialogbtn3.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.btn_dialog_white));
                    tv_dialogbtn4.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.btn_dialog_white));
                    tv_dialogbtn1.setTextColor(context.getResources().getColor(R.color.textColor_2));
                    tv_dialogbtn2.setTextColor(context.getResources().getColor(R.color.white));
                    tv_dialogbtn3.setTextColor(context.getResources().getColor(R.color.textColor_2));
                    tv_dialogbtn4.setTextColor(context.getResources().getColor(R.color.textColor_2));
                    break;
                case 2:
                    tv_dialogbtn1.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.btn_dialog_white));
                    tv_dialogbtn2.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.btn_dialog_white));
                    tv_dialogbtn3.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.btn_dialog_orange));
                    tv_dialogbtn4.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.btn_dialog_white));
                    tv_dialogbtn1.setTextColor(context.getResources().getColor(R.color.textColor_2));
                    tv_dialogbtn2.setTextColor(context.getResources().getColor(R.color.textColor_2));
                    tv_dialogbtn3.setTextColor(context.getResources().getColor(R.color.white));
                    tv_dialogbtn4.setTextColor(context.getResources().getColor(R.color.textColor_2));
                    break;
                case 3:
                    tv_dialogbtn1.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.btn_dialog_white));
                    tv_dialogbtn2.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.btn_dialog_white));
                    tv_dialogbtn3.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.btn_dialog_white));
                    tv_dialogbtn4.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.btn_dialog_orange));
                    tv_dialogbtn1.setTextColor(context.getResources().getColor(R.color.textColor_2));
                    tv_dialogbtn2.setTextColor(context.getResources().getColor(R.color.textColor_2));
                    tv_dialogbtn3.setTextColor(context.getResources().getColor(R.color.textColor_2));
                    tv_dialogbtn4.setTextColor(context.getResources().getColor(R.color.white));
                    break;
                default:
                    tv_dialogbtn1.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.btn_dialog_white));
                    tv_dialogbtn2.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.btn_dialog_white));
                    tv_dialogbtn3.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.btn_dialog_white));
                    tv_dialogbtn1.setTextColor(context.getResources().getColor(R.color.textColor_2));
                    tv_dialogbtn2.setTextColor(context.getResources().getColor(R.color.textColor_2));
                    tv_dialogbtn3.setTextColor(context.getResources().getColor(R.color.textColor_2));
                    break;
            }
        }
    }

}
