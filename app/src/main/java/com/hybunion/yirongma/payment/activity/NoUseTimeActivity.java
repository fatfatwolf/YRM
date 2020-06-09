package com.hybunion.yirongma.payment.activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hybunion.yirongma.payment.bean.ChooseDayBean;
import com.hybunion.yirongma.payment.bean.ChooseTimeBean;
import com.hybunion.yirongma.payment.utils.SharedPreferencesUtil;
import com.hybunion.yirongma.R;
import com.hybunion.yirongma.payment.utils.SharedPConstant;
import com.hybunion.yirongma.payment.adapter.GridViewDayAdapter;
import com.hybunion.yirongma.payment.adapter.GridViewTimeAdapter;
import com.hybunion.yirongma.payment.base.BasePresenter;
import com.hybunion.yirongma.payment.base.BasicActivity;
import com.hybunion.yirongma.payment.utils.ToastUtil;
import com.hybunion.yirongma.payment.view.TitleBar;
import com.hybunion.yirongma.payment.utils.YrmUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

public class NoUseTimeActivity extends BasicActivity implements View.OnClickListener{

    @Bind(R.id.gridView_day)
    GridView gridView_day;
    @Bind(R.id.gridView_time)
    GridView gridView_time;
    @Bind(R.id.titleBar)
    TitleBar titleBar;
    @Bind(R.id.tv_no_use_time1)
    TextView tv_no_use_time1;
    @Bind(R.id.tv_no_use_time2)
    TextView tv_no_use_time2;
    @Bind(R.id.tv_no_time)
    TextView tv_no_time;
    @Bind(R.id.tv_no_time1)
    TextView tv_no_time1;
    @Bind(R.id.tv_no_time2)
    TextView tv_no_time2;
    @Bind(R.id.bt_save)
    Button bt_save;

    GridViewDayAdapter gridViewDayAdapter;
    GridViewTimeAdapter gridViewTimeAdapter;
    List<ChooseDayBean> list;
    List<ChooseTimeBean> dataList;//第一个时间段的list
    List<ChooseTimeBean> dataList2;//第二个时间段的list


    int isSaveType = 1;//0表示第一次保存，1表示修改不可用时间段1  2表示修改不可用时间段2
    private int day = 0;//判断第一个时间段是否有被选中的日期
    private int time = 0;//判断第一个时间段是否有被选中的时间
    private  int day1 = 0;//判断第二个时间段是否有被选中的日期
    private int time1 = 0;//判断第二个时间段是否有被选中的时间
    String noUseTime1,noUseTime2;
    String isHaveTime1;//判断是否有保存在第一个时间段上
    String isHaveTime2;
    StringBuffer buffer = new StringBuffer("");
    StringBuffer buffer2 = new StringBuffer("");
    Gson gson;
    String disableTimeOne = "",disableTimeTwo = "";
    String json1,json2,json3;
    private String clickType;//判断从哪个界面跳转过来的 ，1 制券 2 修改券信息
    @Override
    protected BasePresenter getPresenter() {
        return null;
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_no_use_time;
    }

    @Override
    public void initView() {
        super.initView();
        gson = new Gson();
        tv_no_use_time1.setOnClickListener(this);
        tv_no_use_time2.setOnClickListener(this);
        bt_save.setOnClickListener(this);
        list = new ArrayList<>();
        dataList = new ArrayList<>();
        dataList2 = new ArrayList<>();
        clickType = getIntent().getStringExtra("clickType");
        noUseTime1 = getIntent().getStringExtra("noUseTime1");
        noUseTime2 = getIntent().getStringExtra("noUseTime2");
        gridViewDayAdapter = new GridViewDayAdapter(this,list);
        gridView_day.setAdapter(gridViewDayAdapter);
        gridView_day.setSelector(new ColorDrawable(Color.TRANSPARENT));
        if(!TextUtils.isEmpty(noUseTime1)){
            tv_no_use_time1.setText(noUseTime1);
            isHaveTime1 = noUseTime1;
            bt_save.setBackgroundDrawable(getResources().getDrawable(R.drawable.radius3_red_button));
        }
        if(!TextUtils.isEmpty(noUseTime2)){
            isHaveTime2 = noUseTime2;
            tv_no_use_time2.setText(noUseTime2);
            bt_save.setBackgroundDrawable(getResources().getDrawable(R.drawable.radius3_red_button));
        }
        if(clickType.equals("1")){
            json1 = SharedPreferencesUtil.getInstance(NoUseTimeActivity.this).getKey(SharedPConstant.SAVE_DAY_list);
        }else {//制券信息界面跳转的
            json1 = SharedPreferencesUtil.getInstance(NoUseTimeActivity.this).getKey(SharedPConstant.SAVE_DAY_list2);
        }

        if(TextUtils.isEmpty(json1)){
            setListData();
        }else {
            list = gson.fromJson(json1, new TypeToken<List<ChooseDayBean>>() {
            }.getType());
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).isChoose) {
                    day++;
                }
                if (list.get(i).isChoose2) {
                    day1++;
                }
            }

            if (day1 > 0) {//两个时间段都有
                isSaveType = 2;
                tv_no_time.setVisibility(View.VISIBLE);
                tv_no_time.setText("不可用时间段2");
                tv_no_time2.setVisibility(View.GONE);
                tv_no_use_time2.setVisibility(View.GONE);
                tv_no_time1.setVisibility(View.VISIBLE);
                tv_no_use_time1.setVisibility(View.VISIBLE);
            } else {//只有第一个时间段
                isSaveType = 1;
                tv_no_time.setVisibility(View.VISIBLE);
                tv_no_time.setText("不可用时间段1");
                tv_no_time1.setVisibility(View.GONE);
                tv_no_use_time1.setVisibility(View.GONE);
            }
            gridViewDayAdapter.updataList(list,isSaveType);
        }

        gridView_day.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(TextUtils.isEmpty(isHaveTime1) || isSaveType==1){//操作第一个不可用时间段
                    if(list.get(position).isClickable2 == false){
                        list.get(position).isChoose = !list.get(position).isChoose;

                    }
                    list.get(position).isClickable = list.get(position).isChoose;
                    if(list.get(position).isChoose){
                        day++;
                    }else {
                        day--;
                    }

                }else if(TextUtils.isEmpty(isHaveTime2) || isSaveType == 2){//操作第二个时间段
                    if(list.get(position).isClickable == false){
                        list.get(position).isChoose2 = !list.get(position).isChoose2;

                    }
//                    list.get(position).isClickable2 = list.get(position).isChoose;
                    if(list.get(position).isChoose2){
                        day1++;
                    }else {
                        day1--;
                    }
                }

                gridViewDayAdapter.updataList(list,isSaveType);
//                gridViewDayAdapter.setSelection(position,list.get(position).isChoose);

                if(day == 0 || time == 0){//如果有保存情况但是被取消掉，则按钮显示灰色
                    if(1==isSaveType){
                        noUseTime1 = "";
                    }else {
                        noUseTime1 = tv_no_use_time1.getText().toString();
                    }

                }else {
                    noUseTime1 = tv_no_use_time1.getText().toString();
                }

                if(TextUtils.isEmpty(noUseTime1)){
                    if(day>0 && time>0){
                        bt_save.setClickable(true);
                        bt_save.setBackgroundDrawable(getResources().getDrawable(R.drawable.radius3_red_button));
                    }else {
                        bt_save.setClickable(false);
                        bt_save.setBackgroundDrawable(getResources().getDrawable(R.drawable.radius3_gray2_button));
                    }
                }else {
                    bt_save.setClickable(true);
                    bt_save.setBackgroundDrawable(getResources().getDrawable(R.drawable.radius3_red_button));
                }
            }
        });


        if(clickType.equals("1")){
            json2 = SharedPreferencesUtil.getInstance(NoUseTimeActivity.this).getKey(SharedPConstant.SAVE_TIME_LIST);
            json3 = SharedPreferencesUtil.getInstance(NoUseTimeActivity.this).getKey(SharedPConstant.SAVE_TIME_LIST2);
        }else {
            json2 = SharedPreferencesUtil.getInstance(NoUseTimeActivity.this).getKey(SharedPConstant.SAVE_TIME_LIST3);
            json3 = SharedPreferencesUtil.getInstance(NoUseTimeActivity.this).getKey(SharedPConstant.SAVE_TIME_LIST4);
        }

        if(TextUtils.isEmpty(json2) && TextUtils.isEmpty(json3)){
            setDataList();
            setDataList2();
            gridViewTimeAdapter = new GridViewTimeAdapter(this,dataList);
        }else if(TextUtils.isEmpty(noUseTime2)){//只有第一个时间段
            dataList = gson.fromJson(json2, new TypeToken<List<ChooseTimeBean>>() {}.getType());
            for(int i=0;i<dataList.size();i++){
                if(dataList.get(i).isChoose){
                    time++;
                }
            }
            setDataList2();
            gridViewTimeAdapter = new GridViewTimeAdapter(this,dataList);
        }else {//两个时间段都有
            dataList = gson.fromJson(json2, new TypeToken<List<ChooseTimeBean>>() {}.getType());
            for(int i=0;i<dataList.size();i++){
                if(dataList.get(i).isChoose){
                    time++;
                }
            }

            dataList2 = gson.fromJson(json3, new TypeToken<List<ChooseTimeBean>>() {}.getType());
            for(int i=0;i<dataList2.size();i++){

                if(dataList2.get(i).isChoose){
                    time1++;
                }
            }
            gridViewTimeAdapter = new GridViewTimeAdapter(this,dataList2);
        }



        gridView_time.setAdapter(gridViewTimeAdapter);
        gridView_time.setSelector(new ColorDrawable(Color.TRANSPARENT));

        gridView_time.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {




                if(TextUtils.isEmpty(isHaveTime1) || isSaveType==1){//第一个时间段
                    if("自定义时间".equals(dataList.get(position).title)){//点击最后一项，跳转到下一页
                        if(dataList.size()>=6){
                            ToastUtil.show("最多可添加两个自定义时间");
                            return;
                        }
                        Intent intent = new Intent(NoUseTimeActivity.this,CustomizeTimeActivity.class);
                        startActivityForResult(intent,0);
                        return;
                    }
                    if(dataList.size()==5 && dataList.get(3).isChoose && position<3){//有一个自定义时间
                        ToastUtil.show("已选择了自定义时间，不能再选高峰期");
                        return;
                    }

                    if(dataList.size()==6 && position<3 && (dataList.get(3).isChoose || dataList.get(4).isChoose)){//有两个自定义时间
                        ToastUtil.show("已选择了自定义时间，不能再选高峰期");
                        return;
                    }

                    dataList.get(position).isChoose = !dataList.get(position).isChoose;
                    if(position == 3 || position == 4){
                        dataList.get(position).isVisible = dataList.get(position).isChoose;
                        for(int i=0;i<3;i++){
                            if(dataList.get(i).isChoose){
                                time--;
                                dataList.get(i).isChoose = false;
                            }
                        }
                    }
                    if(dataList.get(position).isChoose){
                        time++;
                    }else {
                        time--;
                    }

                    gridViewTimeAdapter.updataList(dataList,isSaveType);

                }else if(TextUtils.isEmpty(isHaveTime2) || isSaveType == 2){//第二个时间段
                    if("自定义时间".equals(dataList2.get(position).title)){//点击最后一项，跳转到下一页
                        if(dataList2.size()>=6){
                            ToastUtil.show("最多可添加两个自定义时间");
                            return;
                        }
                        Intent intent = new Intent(NoUseTimeActivity.this,CustomizeTimeActivity.class);
                        startActivityForResult(intent,0);
                        return;
                    }
                    if(dataList2.size()==5 && dataList2.get(3).isChoose && position<3){//有一个自定义时间
                        ToastUtil.show("已选择了自定义时间，不能再选高峰期");
                        return;
                    }

                    if(dataList2.size()==6 && position<3 && (dataList2.get(3).isChoose || dataList2.get(4).isChoose)){//有一个自定义时间
                        ToastUtil.show("已选择了自定义时间，不能再选高峰期");
                        return;
                    }
                    dataList2.get(position).isChoose = !dataList2.get(position).isChoose;
                    if(position == 3 || position == 4){
                        dataList.get(position).isVisible = dataList.get(position).isChoose;
                        for(int i=0;i<3;i++){
                            if(dataList2.get(i).isChoose){
                                time--;
                                dataList2.get(i).isChoose = false;
                            }
                        }
                    }
                    if(dataList2.get(position).isChoose){
                        time1++;
                    }else {
                        time1--;
                    }
                    gridViewTimeAdapter.updataList(dataList2,isSaveType);
                }

                if(day == 0 || time == 0){//如果有保存情况但是被取消掉，则按钮显示灰色
                    if(1==isSaveType){
                        noUseTime1 = "";
                    }else {
                        noUseTime1 = tv_no_use_time1.getText().toString();
                    }

                }else {
                    noUseTime1 = tv_no_use_time1.getText().toString();
                }

                if(TextUtils.isEmpty(noUseTime1)){
                    if(day>0 && time>0){
                        bt_save.setBackgroundDrawable(getResources().getDrawable(R.drawable.radius3_red_button));
                        bt_save.setClickable(true);
                    }else {
                        bt_save.setBackgroundDrawable(getResources().getDrawable(R.drawable.radius3_gray2_button));
                        bt_save.setClickable(false);
                    }
                }else {
                    bt_save.setClickable(true);
                    bt_save.setBackgroundDrawable(getResources().getDrawable(R.drawable.radius3_red_button));
                }

            }
        });
        titleBar.setRightTextClickListener(new TitleBar.OnRightClickListener() {
            @Override
            public void rightClick() {
                getSaveTimeStatus(1);
            }


        });
        titleBar.setTitleBarBackClickListener(new TitleBar.OnTitleBackClickListener() {
            @Override
            public void titleBackClick() {
                String noUseTime1 = tv_no_use_time1.getText().toString().trim();
                String noUseTime2 = tv_no_use_time2.getText().toString().trim();
                if(!TextUtils.isEmpty(noUseTime1)||!TextUtils.isEmpty(noUseTime2)){
                    showDialog("设置未保存，是否退出？");
                }else {
                    if(day>0 && time>0){
                        showDialog("设置未保存，是否退出？");
                    }else {
                        finish();
                    }
                }
            }
        });


        if(TextUtils.isEmpty(json1)&& !TextUtils.isEmpty(noUseTime1)){//不是选择，是回显回来得，需要额外处理
            if(TextUtils.isEmpty(noUseTime2)){//没有第二个时间段
                isSaveType = 1;
                tv_no_time.setVisibility(View.VISIBLE);
                tv_no_time.setText("不可用时间段1");
                tv_no_time1.setVisibility(View.GONE);
                tv_no_use_time1.setVisibility(View.GONE);
                setChangeList(noUseTime1,true);//第一个时间段的处理
            }else {//有第二个时间段
                isSaveType = 2;
                tv_no_time.setVisibility(View.VISIBLE);
                tv_no_time.setText("不可用时间段2");
                tv_no_time2.setVisibility(View.GONE);
                tv_no_use_time2.setVisibility(View.GONE);
                tv_no_time1.setVisibility(View.VISIBLE);
                tv_no_use_time1.setVisibility(View.VISIBLE);
                setChangeList(noUseTime1,false);
                setChangeList2(noUseTime2);
            }

        }

    }


    public boolean isHaveSelf(List<ChooseTimeBean> list, String strTime){
        boolean isMySelfTime = true;
        for(int i=0;i<list.size();i++){
            if(list.get(i).timestamp.equals(strTime)){
                isMySelfTime =false;
            }
        }

        return isMySelfTime;
    }


    public void setChangeCount(int type){
        if(type == 1){//第一个的删除
            time--;
        }else if(type == 2){
            time1--;
        }
    }

    boolean isShow = false;
    //修改第一个时间段
    public void setChangeList(String noUseTime,boolean isRefresh){
        String [] noUse1 = noUseTime.split(" ");
        String day1 = noUse1[0];
        String time1 = noUse1[1];
        String[] chooseDay1 = day1.split(";");
        for(int i=0;i<chooseDay1.length;i++){
            for(int j=0;j<list.size();j++){
                if(chooseDay1[i].equals(list.get(j).day)){
                    day++;
                    list.get(j).isChoose = true;
                    list.get(j).isClickable = list.get(j).isChoose;
                }
            }
        }
        if(isRefresh)

            gridViewDayAdapter.updataList(list,isSaveType);


        String[] chooseTime1 = time1.split(";");

        for (int i=0;i<chooseTime1.length;i++){
           if (isHaveSelf(dataList,chooseTime1[i])){
               isShow = true;
               break;
           }
        }




        if(isShow){//有自定义时间
            String daytime[] = chooseTime1[0].split("-");//截取到自定义的开始和结束时间
            ChooseTimeBean bean = new ChooseTimeBean();
            bean.time = daytime[0]+"~"+daytime[1];
            bean.timestamp = daytime[0]+"-"+daytime[1];
            bean.isChoose = true;
            bean.isVisible = true;
            time = 1;
            bean.title ="自定义时间"+(dataList.size()-3);
            dataList.add(dataList.size()-1,bean);
            if(chooseTime1.length == 2){
                String daytime2[] = chooseTime1[1].split("-");//截取到自定义的开始和结束时间
                ChooseTimeBean bean2 = new ChooseTimeBean();
                bean2.time = daytime2[0]+"~"+daytime2[1];
                bean2.timestamp = daytime2[0]+"-"+daytime2[1];
                bean2.isChoose = true;
                bean2.isVisible = true;
                time = 2;
                bean2.title ="自定义时间"+(dataList.size()-3);
                dataList.add(dataList.size()-1,bean2);
            }

        }else {//没有自定义时间
            for(int i=0;i<chooseTime1.length;i++){
                for(int m=0;m<dataList.size();m++){
                    if(chooseTime1[i].equals(dataList.get(m).timestamp)){
                        time++;
                        dataList.get(m).isChoose = true;
                    }
                }
            }
        }


        if(isRefresh)
            gridViewTimeAdapter.updataList(dataList,isSaveType);

    }

    boolean isShow2 = false;

    //修改第二个时间段
    public void setChangeList2(String noUseTime){
        String [] noUse1 = noUseTime.split(" ");
        String day1 = noUse1[0];
        String time1 = noUse1[1];



        String[] chooseDay1 = day1.split(";");

        for(int i=0;i<chooseDay1.length;i++){
            for(int j=0;j<list.size();j++){
                if(chooseDay1[i].equals(list.get(j).day)){
                    this.day1++;
                    list.get(j).isChoose2 = true;
                    list.get(j).isClickable2 = list.get(j).isChoose2;
                }
            }
        }
        gridViewDayAdapter.updataList(list,isSaveType);


        String[] chooseTime2 = time1.split(";");

        for (int i=0;i<chooseTime2.length;i++){
            if (isHaveSelf(dataList2,chooseTime2[i])){
                isShow = true;
                break;
            }
        }


        if(isShow2){//有自定义时间
            String daytime[] = chooseTime2[0].split("-");//截取到自定义的开始和结束时间
            ChooseTimeBean bean = new ChooseTimeBean();
            bean.time = daytime[0]+"~"+daytime[1];
            bean.timestamp = daytime[0]+"-"+daytime[1];
            bean.isChoose = true;
            bean.isVisible = true;
            this.time1 = 1;
            bean.title ="自定义时间"+(dataList2.size()-3);
            dataList2.add(dataList2.size()-1,bean);
            if(chooseTime2.length == 2){
                String daytime2[] = chooseTime2[1].split("-");//截取到自定义的开始和结束时间
                ChooseTimeBean bean2 = new ChooseTimeBean();
                bean2.time = daytime2[0]+"~"+daytime2[1];
                bean2.timestamp = daytime2[0]+"-"+daytime2[1];
                bean2.isChoose = true;
                bean2.isVisible = true;
                this.time1 = 2;
                bean2.title ="自定义时间"+(dataList2.size()-3);
                dataList2.add(dataList2.size()-1,bean2);
            }
        }else {
            for(int i=0;i<chooseTime2.length;i++){
                for(int m=0;m<dataList2.size();m++){
                    if(chooseTime2[i].equals(dataList2.get(m).timestamp)){
                        this.time1++;
                        dataList2.get(m).isChoose = true;
                    }
                }
            }
        }

        gridViewTimeAdapter.updataList(dataList2,isSaveType);

    }



    public String listToJson(Object list){
        Gson gson = new Gson();
        String str = gson.toJson(list);
        return str;
    }

    public void setListData(){
        ChooseDayBean bean1 = new ChooseDayBean();
        bean1.day = "周一";
        bean1.isChoose = false;
        bean1.daynum = "1";
        list.add(bean1);
        ChooseDayBean bean2 = new ChooseDayBean();
        bean2.day = "周二";
        bean2.isChoose = false;
        bean2.daynum = "2";
        list.add(bean2);
        ChooseDayBean bean3 = new ChooseDayBean();
        bean3.day = "周三";
        bean3.isChoose = false;
        bean3.daynum = "3";
        list.add(bean3);
        ChooseDayBean bean4 = new ChooseDayBean();
        bean4.day = "周四";
        bean4.isChoose = false;
        bean4.daynum = "4";
        list.add(bean4);
        ChooseDayBean bean5 = new ChooseDayBean();
        bean5.day = "周五";
        bean5.isChoose = false;
        bean5.daynum = "5";
        list.add(bean5);
        ChooseDayBean bean6 = new ChooseDayBean();
        bean6.day = "周六";
        bean6.isChoose = false;
        bean6.daynum="6";
        list.add(bean6);
        ChooseDayBean bean7 = new ChooseDayBean();
        bean7.day = "周日";
        bean7.isChoose = false;
        bean7.daynum = "7";
        list.add(bean7);
    }

    public void setDataList(){
        ChooseTimeBean bean1 = new ChooseTimeBean();
        bean1.title = "早高峰";
        bean1.time = "06:00~09:00";
        bean1.isChoose = false;
        bean1.timestamp = "06:00-09:00";
        dataList.add(bean1);
        ChooseTimeBean bean2 = new ChooseTimeBean();
        bean2.title = "午高峰";
        bean2.time = "12:00~13:00";
        bean2.isChoose = false;
        bean2.timestamp = "12:00-13:00";
        dataList.add(bean2);
        ChooseTimeBean bean3 = new ChooseTimeBean();
        bean3.title = "晚高峰";
        bean3.time = "17:30~20:30";
        bean3.isChoose = false;
        bean3.timestamp = "17:30-20:30";
        dataList.add(bean3);
        ChooseTimeBean bean4 = new ChooseTimeBean();
        bean4.title = "自定义时间";
        bean4.time = "";
        bean4.isChoose = false;
        bean4.timestamp="";
        dataList.add(bean4);
    }
    public void setDataList2(){
        ChooseTimeBean bean1 = new ChooseTimeBean();
        bean1.title = "早高峰";
        bean1.time = "06:00~09:00";
        bean1.isChoose = false;
        bean1.timestamp = "06:00-09:00";
        dataList2.add(bean1);
        ChooseTimeBean bean2 = new ChooseTimeBean();
        bean2.title = "午高峰";
        bean2.time = "12:00~13:00";
        bean2.isChoose = false;
        bean2.timestamp = "12:00-13:00";
        dataList2.add(bean2);
        ChooseTimeBean bean3 = new ChooseTimeBean();
        bean3.title = "晚高峰";
        bean3.time = "17:30~20:30";
        bean3.isChoose = false;
        bean3.timestamp = "17:30-20:30";
        dataList2.add(bean3);
        ChooseTimeBean bean4 = new ChooseTimeBean();
        bean4.title = "自定义时间";
        bean4.time = "";
        bean4.isChoose = false;
        bean4.timestamp="";
        dataList2.add(bean4);
    }
    //保存选中状态
    public void getSaveTimeStatus(int type){

        if(TextUtils.isEmpty(isHaveTime1) || isSaveType==1){

            if(day>0 && time>0) {
                tv_no_use_time1.setVisibility(View.VISIBLE);
                tv_no_time1.setVisibility(View.VISIBLE);
                tv_no_use_time2.setVisibility(View.GONE);
                tv_no_time2.setVisibility(View.GONE);
                for(int i=0;i<list.size();i++){
                    if(list.get(i).isChoose){
                        buffer.append(list.get(i).day + "; ");
                    }
                }
                for(int j=0;j<dataList.size();j++){
                    if(dataList.get(j).isChoose){
                        buffer.append(dataList.get(j).time+";");
                    }
                }
                isHaveTime1 = buffer.toString();
                tv_no_use_time1.setText(buffer.toString());
                isSaveType = 2;
                tv_no_time.setText("不可用时间段2");
                gridViewDayAdapter.updataList(list,isSaveType);
                gridViewTimeAdapter.updataList(dataList2,isSaveType);
            }else {
                ToastUtil.show("请选择完日期和时间段");
                return;
            }
        }else if(TextUtils.isEmpty(isHaveTime2) || isSaveType ==2){
            tv_no_time.setText("不可用时间段2");
            tv_no_use_time2.setVisibility(View.GONE);
            tv_no_time2.setVisibility(View.GONE);
            if(day1>0 && time1>0) {

                for(int i=0;i<list.size();i++){
                    if(list.get(i).isChoose2){
                        list.get(i).isClickable2 = list.get(i).isChoose2;
                        buffer2.append(list.get(i).day + "; ");
                    }
                }
                for(int j=0;j<dataList2.size();j++){
                    if(dataList2.get(j).isChoose){
                        buffer2.append(dataList2.get(j).time+";");
                    }
                }
                isHaveTime2 = buffer2.toString();
                tv_no_use_time2.setText(buffer2.toString());
                if(type == 1)
                    ToastUtil.show("不可用时间段2添加成功");
            }else {
                ToastUtil.show("请选择完日期和时间段");
                return;
            }
        }else{
            if(type==1){
                ToastUtil.show("最多可保存两个不可用时间段");
                return;
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_no_use_time1:
                String useTime3 = tv_no_use_time2.getText().toString().trim();
                tv_no_time.setVisibility(View.VISIBLE);
                tv_no_time.setText("不可用时间段1");
                tv_no_time1.setVisibility(View.GONE);
                tv_no_use_time1.setVisibility(View.GONE);
                if(!TextUtils.isEmpty(useTime3)){
                    tv_no_time2.setVisibility(View.VISIBLE);
                    tv_no_use_time2.setVisibility(View.VISIBLE);
                }
                String useTime1 = tv_no_use_time1.getText().toString().trim();
                if(!TextUtils.isEmpty(useTime1)){
                    isSaveType = 1;
                    gridViewTimeAdapter.updataList(dataList,isSaveType);
                    gridViewDayAdapter.updataList(list,isSaveType);
                    buffer.delete(0,buffer.length());
                }
                break;
            case R.id.tv_no_use_time2:
                tv_no_time.setVisibility(View.VISIBLE);
                tv_no_time.setText("不可用时间段2");
                tv_no_time2.setVisibility(View.GONE);
                tv_no_use_time2.setVisibility(View.GONE);
                tv_no_time1.setVisibility(View.VISIBLE);
                tv_no_use_time1.setVisibility(View.VISIBLE);
                String useTime2 = tv_no_use_time1.getText().toString().trim();
                if(!TextUtils.isEmpty(useTime2)){
                    isSaveType = 2;
                    gridViewTimeAdapter.updataList(dataList2,isSaveType);
                    gridViewDayAdapter.updataList(list,isSaveType);
                    buffer2.delete(0,buffer2.length());
                }
                break;



            case R.id.bt_save:


                if(clickType.equals("1")){
                    if(TextUtils.isEmpty(isHaveTime1) || TextUtils.isEmpty(isHaveTime2)){
                        getSaveTimeStatus(0);
                    }
                    //制券使用
                    SharedPreferencesUtil.getInstance(NoUseTimeActivity.this).putKey(SharedPConstant.SAVE_DAY_list,listToJson(list));
                    SharedPreferencesUtil.getInstance(NoUseTimeActivity.this).putKey(SharedPConstant.SAVE_TIME_LIST,listToJson(dataList));
                    SharedPreferencesUtil.getInstance(NoUseTimeActivity.this).putKey(SharedPConstant.SAVE_TIME_LIST2,listToJson(dataList2));
                }else if(clickType.equals("2")){
                    getSaveTimeStatus(0);
                    //修改使用
                    SharedPreferencesUtil.getInstance(NoUseTimeActivity.this).putKey(SharedPConstant.SAVE_DAY_list2,listToJson(list));
                    SharedPreferencesUtil.getInstance(NoUseTimeActivity.this).putKey(SharedPConstant.SAVE_TIME_LIST3,listToJson(dataList));
                    SharedPreferencesUtil.getInstance(NoUseTimeActivity.this).putKey(SharedPConstant.SAVE_TIME_LIST4,listToJson(dataList2));
                }

                StringBuffer buffer1 = new StringBuffer();//第一个的时间
                StringBuffer buffer2 = new StringBuffer();


                    for(int i=0;i<list.size();i++){
                        if(list.get(i).isChoose){
                            buffer1.append(list.get(i).daynum+",");
                        }
                    }
                    if(buffer1.length()>0){
                        String time1 = buffer1.substring(0,buffer1.length()-1);
                        buffer1.delete(0,buffer1.length());
                        buffer1.append(time1);
                        buffer1.append("|");

                        for(int i=0;i<dataList.size();i++){
                            if(dataList.get(i).isChoose){
                                buffer1.append(dataList.get(i).timestamp+"#");
                            }
                        }
                        disableTimeOne = buffer1.substring(0,buffer1.length()-1);
                    }



                for(int i=0;i<list.size();i++){
                    if(list.get(i).isChoose2){
                        buffer2.append(list.get(i).daynum+",");
                    }
                }
                if(buffer2.length()>0){
                    String time2 = buffer2.substring(0,buffer2.length()-1);
                    buffer2.delete(0,buffer2.length());
                    buffer2.append(time2);
                    buffer2.append("|");

                    for (int i = 0; i < dataList2.size(); i++) {
                        if (dataList2.get(i).isChoose) {
                            buffer2.append(dataList2.get(i).timestamp + "#");
                        }
                    }
                    disableTimeTwo = buffer2.substring(0, buffer2.length() - 1);
                }
                Intent intent = new Intent();
                intent.putExtra("noUseTime1",tv_no_use_time1.getText().toString().trim());
                intent.putExtra("noUseTime2",tv_no_use_time2.getText().toString().trim());
                intent.putExtra("disableTimeOne",disableTimeOne);
                intent.putExtra("disableTimeTwo",disableTimeTwo);
                setResult(10, intent);
                finish();

                break;

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        switch (resultCode){
            case  11:
                String startTime = intent.getStringExtra("startTime");
                String endTime = intent.getStringExtra("endTime");
                ChooseTimeBean bean = new ChooseTimeBean();
                bean.title = "自定义时间"+(dataList.size()-3);
                bean.time = startTime+"~"+endTime;
                bean.timestamp = startTime+"-"+endTime;
                bean.isVisible = true;
                if(isSaveType == 1){//第一个时间段的选择
                    bean.isChoose = true;
                    for(int i=0;i<3;i++){
                        if(dataList.get(i).isChoose){
                            time--;
                            dataList.get(i).isChoose = false;
                        }
                    }

                    if(dataList.size() == 4){
                        dataList.add(dataList.size()-1,bean);
                        gridViewTimeAdapter.updataList(dataList,isSaveType);
                        time++;
                        if(TextUtils.isEmpty(noUseTime1)){
                            if(day>0 && time>0){
                                bt_save.setBackgroundDrawable(getResources().getDrawable(R.drawable.radius3_red_button));
                            }else {
                                bt_save.setBackgroundDrawable(getResources().getDrawable(R.drawable.radius3_gray2_button));
                            }
                        }else {
                            bt_save.setBackgroundDrawable(getResources().getDrawable(R.drawable.radius3_red_button));
                        }
                    }else {//比较两次自定义事件是否有重叠
                        if(!TextUtils.isEmpty(dataList.get(3).time)){
                            bean.isChoose = true;
                            String [] timeArray = dataList.get(3).time.split("~");
                            String startTime2 = timeArray[0];
                            String endTime2 = timeArray[1];
                            //第二次开始时间大于第一次结束时间或者第二次结束时间小于第一次开始时间则无重叠
                            if(YrmUtils.TimeChangeLong(startTime2)>YrmUtils.TimeChangeLong(endTime) || YrmUtils.TimeChangeLong(endTime2)<YrmUtils.TimeChangeLong(startTime)){
                                dataList.add(dataList.size()-1,bean);
                                gridViewTimeAdapter.updataList(dataList,isSaveType);
                                time++;
                                if(TextUtils.isEmpty(noUseTime1)){
                                    if(day>0 && time>0){
                                        bt_save.setBackgroundDrawable(getResources().getDrawable(R.drawable.radius3_red_button));
                                    }else {
                                        bt_save.setBackgroundDrawable(getResources().getDrawable(R.drawable.radius3_gray2_button));
                                    }
                                }else {
                                    bt_save.setBackgroundDrawable(getResources().getDrawable(R.drawable.radius3_red_button));
                                }
                            }else {
                                ToastUtil.show("两次不可用时间段不能有重叠");
                                return;
                            }
                        }

                    }
                }else if(isSaveType == 2){//第二个时间段的选择
                    bean.isChoose = true;
                    for(int i=0;i<3;i++){
                        if(dataList2.get(i).isChoose){
                            time1--;
                            dataList2.get(i).isChoose = false;
                        }
                    }
                    if(dataList2.size() == 4){
                        dataList2.add(dataList2.size()-1,bean);
                        gridViewTimeAdapter.updataList(dataList2,isSaveType);
                        time1++;
                        if(TextUtils.isEmpty(noUseTime1)){
                            if(day>0 && time>0){
                                bt_save.setBackgroundDrawable(getResources().getDrawable(R.drawable.radius3_red_button));
                            }else {
                                bt_save.setBackgroundDrawable(getResources().getDrawable(R.drawable.radius3_gray2_button));
                            }
                        }else {
                            bt_save.setBackgroundDrawable(getResources().getDrawable(R.drawable.radius3_red_button));
                        }
                    }else {//比较两次自定义事件是否有重叠
                        if(!TextUtils.isEmpty(dataList2.get(3).time)){
                            bean.isChoose = true;
                            String [] timeArray = dataList2.get(3).time.split("~");
                            String startTime2 = timeArray[0];
                            String endTime2 = timeArray[1];
                            //第二次开始时间大于第一次结束时间或者第二次结束时间小于第一次开始时间则无重叠
                            if(YrmUtils.TimeChangeLong(startTime2)>YrmUtils.TimeChangeLong(endTime) || YrmUtils.TimeChangeLong(endTime2)<YrmUtils.TimeChangeLong(startTime)){
                                dataList2.add(dataList2.size()-1,bean);
                                gridViewTimeAdapter.updataList(dataList2,isSaveType);
                                time1++;
                                if(TextUtils.isEmpty(noUseTime1)){
                                    if(day>0 && time>0){
                                        bt_save.setBackgroundDrawable(getResources().getDrawable(R.drawable.radius3_red_button));
                                    }else {
                                        bt_save.setBackgroundDrawable(getResources().getDrawable(R.drawable.radius3_gray2_button));
                                    }
                                }else {
                                    bt_save.setBackgroundDrawable(getResources().getDrawable(R.drawable.radius3_red_button));
                                }
                            }else {
                                ToastUtil.show("两次不可用时间段不能有重叠");
                                return;
                            }
                        }

                    }
                }




                break;
        }

    }


    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        String noUseTime1 = tv_no_use_time1.getText().toString().trim();
        String noUseTime2 = tv_no_use_time2.getText().toString().trim();
        if(!TextUtils.isEmpty(noUseTime1)||!TextUtils.isEmpty(noUseTime2)){
            showDialog("设置未保存，是否退出？");
        }else {
            if(day>0 && time>0){
                showDialog("设置未保存，是否退出？");
            }else {
                finish();
            }
        }

    }




    private void showDialog(String msg) {
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_save_stuatus, null);
        final Dialog dialog = new Dialog(this, R.style.MyDialog);
        dialog.setContentView(view);
        Button btn_ok = (Button) view.findViewById(R.id.btn_ok);
        Button btn_cancel = (Button) view.findViewById(R.id.btn_cancel);
        TextView tv_msg = (TextView) view.findViewById(R.id.tv_msg);
        tv_msg.setText(msg);
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                finish();
            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }
}
