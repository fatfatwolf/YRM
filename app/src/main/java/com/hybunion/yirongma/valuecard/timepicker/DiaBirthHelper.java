package com.hybunion.yirongma.valuecard.timepicker;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hybunion.yirongma.R;
import com.hybunion.yirongma.valuecard.view.DialogValueCardMake;

import java.util.Calendar;
import java.util.List;

/**
 * 功能描述：时间选择器
 * 编写人： myy
 * 创建时间：2017/3/2
 */
public class DiaBirthHelper extends Dialog {

    public static final int BUTTONDELETE = 0;//删除符号
    public static final int BUTTONONE = 1;//第一个按钮


    public DiaBirthHelper(Context context, int theme) {
        super(context, theme);
    }


    public static class Builder {
        private Context context;
        private String title;
        private OnClickListener onDeleteClick;
        private onButtonClickLisener onButtonClick1;
        private List<String> listYear, listMonth, listDay;
        private String year, month, day;

        public Builder(Context context) {
            this.context = context;
        }

        /**
         * 初始化数据
         */
        private void initData() {
            listYear = DatePackerUtil.getBirthYearList();
            listMonth = DatePackerUtil.getBirthMonthList();
            listDay = DatePackerUtil.getBirthDay31List();
        }

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder onDeleteClick(OnClickListener listener) {
            this.onDeleteClick = listener;
            return this;
        }

        public Builder onButtonClick1(
                onButtonClickLisener listener) {
            this.onButtonClick1 = listener;
            return this;
        }


        public DialogValueCardMake create() {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            // instantiate the dialog with the custom Theme
            final DialogValueCardMake dialog = new DialogValueCardMake(context, R.style.Dialog_image);
            View layout = inflater.inflate(R.layout.dialog_valuecard_maketime, null);
            dialog.addContentView(layout, new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            // set the dialog title
            ((TextView) layout.findViewById(R.id.tv_dialogTitle)).setText(title);
            final LoopView loopView1 = (LoopView) layout.findViewById(R.id.loopView1);
            final LoopView loopView2 = (LoopView) layout.findViewById(R.id.loopView2);
            final LoopView loopView3 = (LoopView) layout.findViewById(R.id.loopView3);
            initData();
            loopView1.setList(listYear);
            // loopView1.setNotLoop();
            loopView1.setCyclic(true);
            loopView1.setCurrentItem(0);//定位到当今年
            loopView2.setList(listMonth);
            // loopView2.setNotLoop();
            loopView2.setCyclic(true);
            Calendar c = Calendar.getInstance();
            c.setTimeInMillis(System.currentTimeMillis() + 1000 * 60 * 60 * 24);
            year = c.get(Calendar.YEAR) + ""; // 获取当前年份
            int mMonth = (c.get(Calendar.MONTH) + 1);// 获取当前月份
            month = mMonth + "";// 获取当前月份
            loopView2.setCurrentItem(mMonth - 1);
            int mDay = c.get(Calendar.DAY_OF_MONTH);//获取当前日期
            day = mDay + "";
            if (month.equals("2")) {
                if (!TextUtils.isEmpty(year) && DatePackerUtil.isRunYear(year)) {
                    listDay = DatePackerUtil.getBirthDay29List();
                    loopView3.setList(listDay);
                } else if (!TextUtils.isEmpty(year) && !DatePackerUtil.isRunYear(year)) {
                    listDay = DatePackerUtil.getBirthDay28List();
                    loopView3.setList(listDay);
                }
            } else if ((month.equals("1") || month.equals("3") || month.equals("5") || month.equals("7") || month.equals("8") || month.equals("10") || month.equals("12")) ) {
                listDay = DatePackerUtil.getBirthDay31List();
                loopView3.setList(listDay);
            } else if ((month.equals("4") || month.equals("6") || month.equals("9") || month.equals("11")) ) {
                listDay = DatePackerUtil.getBirthDay30List();
                loopView3.setList(listDay);
            }

            loopView3.setCyclic(true);
            loopView3.setCurrentItem(mDay-1);

            loopView1.setListener(new LoopListener() {
                @Override
                public void onItemSelect(int item) {
                    String select_item = listYear.get(item);
                    if (TextUtils.isEmpty(year)) {
                        year = "2017";
                    } else {
                        year = select_item.replace("年", "");
                    }
                    if (!TextUtils.isEmpty(month) && month.equals("2")) {
                        if (DatePackerUtil.isRunYear(year) && listDay.size() != 29) {
                            listDay = DatePackerUtil.getBirthDay29List();
                            loopView3.setList(listDay);
                            loopView3.setCurrentItem(0);
                        } else if (!DatePackerUtil.isRunYear(year) && listDay.size() != 28) {
                            listDay = DatePackerUtil.getBirthDay28List();
                            loopView3.setList(listDay);
                            loopView3.setCurrentItem(0);
                        }
                    }
                }
            });
            loopView2.setListener(new LoopListener() {
                @Override
                public void onItemSelect(int item) {
                    String select_item = listMonth.get(item);
                    if (TextUtils.isEmpty(month)) {
                        month = "1";
                    } else {
                        month = select_item.replace("月", "");
                    }
                    if (month.equals("2")) {
                        if (!TextUtils.isEmpty(year) && DatePackerUtil.isRunYear(year) && listDay.size() != 29) {
                            listDay = DatePackerUtil.getBirthDay29List();
                            loopView3.setList(listDay);
                            loopView3.setCurrentItem(0);
                        } else if (!TextUtils.isEmpty(year) && !DatePackerUtil.isRunYear(year) && listDay.size() != 28) {
                            listDay = DatePackerUtil.getBirthDay28List();
                            loopView3.setList(listDay);
                            loopView3.setCurrentItem(0);
                        }
                    } else if ((month.equals("1") || month.equals("3") || month.equals("5") || month.equals("7") || month.equals("8") || month.equals("10") || month.equals("12")) && listDay.size() != 31) {
                        listDay = DatePackerUtil.getBirthDay31List();
                        loopView3.setList(listDay);
                        loopView3.setCurrentItem(0);
                    } else if ((month.equals("4") || month.equals("6") || month.equals("9") || month.equals("11")) && listDay.size() != 30) {
                        listDay = DatePackerUtil.getBirthDay30List();
                        loopView3.setList(listDay);
                        loopView3.setCurrentItem(0);
                    }

                }
            });
            loopView3.setListener(new LoopListener() {
                @Override
                public void onItemSelect(int item) {
                    String select_item = listDay.get(item);
                    if (TextUtils.isEmpty(day)) {
                        day = "1";
                    } else {
                        day = select_item.replace("日", "");
                    }
                }
            });

            // set the  button1

            if (onButtonClick1 != null) {
                (layout.findViewById(R.id.tv_dialogbtn1))
                        .setOnClickListener(new View.OnClickListener() {
                            public void onClick(View v) {
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        onButtonClick1.onSubmit(dialog, year + "-" + month + "-" + day);
                                    }
                                }, 500);
                            }
                        });
            }

            // set the delete
            if (onDeleteClick != null) {
                (layout.findViewById(R.id.imgv_delete))
                        .setOnClickListener(new View.OnClickListener() {
                            public void onClick(View v) {
                                onDeleteClick.onClick(dialog,
                                        BUTTONDELETE);
                            }
                        });
            }

            dialog.setContentView(layout);
            return dialog;
        }
    }

    public interface onButtonClickLisener {
        void onSubmit(DialogValueCardMake dialog, String time);
    }

}
