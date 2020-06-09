package com.hybunion.yirongma.payment.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.hybunion.yirongma.R;
import com.hybunion.yirongma.payment.base.BasePresenter;
import com.hybunion.yirongma.payment.base.BasicActivity;
import com.hybunion.yirongma.payment.utils.ToastUtil;
import com.hybunion.yirongma.payment.view.MyDateTimePickDialog;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 制券 自定义时间
 */

public class CustomizeTimeActivity extends BasicActivity {
    @Bind(R.id.tv_start_time_customize_time_activity)
    TextView mTvStartTime;
    @Bind(R.id.tv_end_time_customize_time_activity)
    TextView mTvEndTime;

    private int mStartHour, mStartMinute, mEndHour, mEndMinute;

    @Override
    protected BasePresenter getPresenter() {
        return null;
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_customize_time;
    }


    private MyDateTimePickDialog mStartDialog, mEndDialog;

    @OnClick({R.id.img_start_time_customize_time_activity, R.id.img_end_time_customize_time_activity})
    public void chooseTime(ImageView img) {
        switch (img.getId()) {
            case R.id.img_start_time_customize_time_activity:    // 选择开始时间
                if (mStartDialog == null)
                    mStartDialog = new MyDateTimePickDialog(CustomizeTimeActivity.this);
                mStartDialog.setType(2);
                mStartDialog.showThisDialog(new MyDateTimePickDialog.OnPickViewOkListener() {
                    @Override
                    public void ok(String year, String month, String date, String hour, String minute, String second) {
                        if (hour.matches("[0-9]+"))
                            mStartHour = Integer.parseInt(hour);
                        if (minute.matches("[0-9]+"))
                            mStartMinute = Integer.parseInt(minute);

                        mTvStartTime.setText(hour + ":" + minute);
                        mStartDialog.dismiss();
                    }
                }, new MyDateTimePickDialog.OnPickViewCancelListener() {
                    @Override
                    public void cancel() {
                        mStartDialog.dismiss();
                    }
                });


                break;

            case R.id.img_end_time_customize_time_activity:       // 选择截止时间
                if (mEndDialog == null)
                    mEndDialog = new MyDateTimePickDialog(CustomizeTimeActivity.this);
                mEndDialog.setType(2);
                mEndDialog.showThisDialog(new MyDateTimePickDialog.OnPickViewOkListener() {
                    @Override
                    public void ok(String year, String month, String date, String hour, String minute, String second) {
                        if (hour.matches("[0-9]+"))
                            mEndHour = Integer.parseInt(hour);
                        if (minute.matches("[0-9]+"))
                            mEndMinute = Integer.parseInt(minute);

                        mTvEndTime.setText(hour + ":" + minute);
                        mEndDialog.dismiss();
                    }
                }, new MyDateTimePickDialog.OnPickViewCancelListener() {
                    @Override
                    public void cancel() {
                        mEndDialog.dismiss();
                    }
                });

                break;
        }

    }

    // 保存按钮监听
    @OnClick(R.id.bt_save_customize_time_activity)
    public void saveTime() {
        String startTime = mTvStartTime.getText().toString();
        if (TextUtils.isEmpty(startTime)) {
            ToastUtil.show("请选择开始时间");
            return;
        }
        String endTime = mTvEndTime.getText().toString();
        if (TextUtils.isEmpty(endTime)) {
            ToastUtil.show("请选择截止时间");
            return;
        }

        if (mStartHour>mEndHour || (mStartHour == mEndHour && mStartMinute>=mEndMinute)  ){
            ToastUtil.show("开始时间要早于截止时间");
            return;
        }

        Intent intent = new Intent();
        intent.putExtra("startTime", startTime);
        intent.putExtra("endTime", endTime);
        setResult(11, intent);
        finish();

    }


}
