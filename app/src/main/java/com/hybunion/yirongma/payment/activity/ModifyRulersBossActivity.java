package com.hybunion.yirongma.payment.activity;

import android.app.Dialog;
import android.graphics.Color;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.google.gson.Gson;
import com.hybunion.netlibrary.utils.net.MyOkCallback;
import com.hybunion.netlibrary.utils.net.OkUtils;
import com.hybunion.yirongma.common.net.NetUrl;
import com.hybunion.yirongma.payment.base.BasePresenter;
import com.hybunion.yirongma.payment.bean.base.BaseBean;
import com.hybunion.yirongma.payment.bean.HuiListBean2;
import com.hybunion.yirongma.payment.bean.ModifyRulerBossBean;
import com.hybunion.yirongma.payment.bean.StoreIdBean;
import com.hybunion.yirongma.payment.bean.StoreManageBean;
import com.hybunion.yirongma.payment.utils.RequestIndex;
import com.hybunion.yirongma.payment.utils.SharedPreferencesUtil;
import com.hybunion.yirongma.R;
import com.hybunion.yirongma.payment.utils.SharedPConstant;
import com.hybunion.yirongma.payment.base.BasicActivity;
import com.hybunion.yirongma.payment.base.CommonAdapter1;
import com.hybunion.yirongma.payment.base.ViewHolder;
import com.hybunion.yirongma.payment.utils.ToastUtil;
import com.hybunion.yirongma.payment.view.MyBottomDialog;
import com.hybunion.yirongma.payment.view.MyStorePopWindow;
import com.hybunion.yirongma.payment.view.ToastPopupWindow;
import com.hybunion.yirongma.payment.utils.YrmUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.Bind;

public class ModifyRulersBossActivity extends BasicActivity implements View.OnClickListener{


    @Bind(R.id.switchButton)
    Switch switchButton;
    @Bind(R.id.ll_close_hui)
    LinearLayout ll_close_hui;
    @Bind(R.id.ll_hui_open)
    LinearLayout ll_hui_open;
    @Bind(R.id.tv_close)
    TextView tv_close;
    @Bind(R.id.tv_shop_name)
    TextView tv_shop_name;
    @Bind(R.id.tv_customer)
    TextView tv_customer;
    @Bind(R.id.iv_add_ruler)
    ImageView iv_add_ruler;
    @Bind(R.id.lv_ruler)
    ListView lv_ruler;
    @Bind(R.id.rv_others_bollow)
    RelativeLayout rv_others_bollow;
    @Bind(R.id.switchBollowButton)
    Switch switchBollowButton;
    @Bind(R.id.ll_count_limit)
    LinearLayout ll_count_limit;
    @Bind(R.id.tv_count_limit)
    TextView tv_count_limit;
    @Bind(R.id.bt_save)
    Button bt_save;


    List<HuiListBean2> list;
    List<HuiListBean2> rulerList = new ArrayList<>();
    CommonAdapter1 adapter1;

    private String mLoginType,merId;
    private String mStoreId = "", mStoreName;
    private String type = "0";//0关闭 1 开启
    private String popFlag = "1";//判断回显弹窗
    private String vcSale;//判断惠储值是否开通
    int index = -1;
    private List<StoreManageBean.ObjBean> mStoreList = new ArrayList(); // 加入商圈的门店 List

    List<StoreIdBean> storeIdList;
    boolean isCancel = false;
    boolean isInterfaceGet = false;//判断上面的switchButton
    private String cardId,storeIds;
    private String isShare,shareNum;
    boolean isInterfaceGet2 = false;//判断允许借用的switchButton
    @Override
    protected BasePresenter getPresenter() {
        return null;
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_modify_rulers_boss;
    }


    @Override
    public void initView() {
        super.initView();
        mLoginType = SharedPreferencesUtil.getInstance(this).getKey("loginType");
        merId = SharedPreferencesUtil.getInstance(this).getKey(SharedPConstant.MERCHANT_ID);
        list = new ArrayList<>();
        setList();
        String str = "业务状态: 关闭";
        SpannableString spannableString = new SpannableString(str);
        spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#F84B33")),6,8 , 0);
        tv_close.setText(spannableString);
        tv_shop_name.setOnClickListener(this);
        tv_customer.setOnClickListener(this);
        iv_add_ruler.setOnClickListener(this);
//        ll_count_limit.setOnClickListener(this);
        rv_others_bollow.setOnClickListener(this);
        bt_save.setOnClickListener(this);
        adapter1 = new CommonAdapter1<HuiListBean2>(this,list,R.layout.item_hui_card_ruler){
            @Override
            public void convert(ViewHolder holder, HuiListBean2 item, final int position) {
                final EditText et_recharg_amount = holder.findView(R.id.et_recharg_amount);
                final EditText et_given_amount = holder.findView(R.id.et_given_amount);
                final TextView tv_discount = holder.findView(R.id.tv_discount);
                ImageView iv_close = holder.findView(R.id.iv_close);
                et_recharg_amount.setTag(position);
                et_given_amount.setTag(position);
                if(!TextUtils.isEmpty(list.get(position).actualAmount)){
                    et_recharg_amount.setText(list.get(position).actualAmount);
                }else {
                    et_recharg_amount.setText("");
                }

                if(!TextUtils.isEmpty(list.get(position).givenAmount)){
                    et_given_amount.setText(list.get(position).givenAmount);
                }else {
                    et_given_amount.setText("");
                }

                if(!TextUtils.isEmpty(list.get(position).discount)){
                    tv_discount.setText(YrmUtils.changetoHuiDiscount(list.get(position).actualAmount,list.get(position).givenAmount)+"折");
                    tv_discount.setVisibility(View.VISIBLE);
                }else {
                    if(TextUtils.isEmpty(list.get(position).actualAmount) || TextUtils.isEmpty(list.get(position).givenAmount)){
                        tv_discount.setVisibility(View.INVISIBLE);
                    }else {
                        tv_discount.setVisibility(View.VISIBLE);
                        tv_discount.setText(YrmUtils.changetoHuiDiscount(list.get(position).actualAmount,list.get(position).givenAmount)+"折");
                    }
                }


                et_recharg_amount.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        if(TextUtils.isEmpty(s.toString())){
                            tv_discount.setVisibility(View.INVISIBLE);
                            return;
                        }
                        int position1 = (int) et_recharg_amount.getTag();
                        list.get(position1).actualAmount = s.toString();
                        String givenAmount = list.get(position1).givenAmount;
                        if(!TextUtils.isEmpty(givenAmount)){

                            tv_discount.setVisibility(View.VISIBLE);
                            tv_discount.setText(YrmUtils.changetoHuiDiscount(list.get(position1).actualAmount,givenAmount)+"折");
                        }else {
                            tv_discount.setVisibility(View.INVISIBLE);
                        }
                    }
                });


                et_given_amount.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        if(TextUtils.isEmpty(s.toString())){
                            tv_discount.setVisibility(View.INVISIBLE);
                            return;
                        }
                        int position2 = (int) et_given_amount.getTag();
                        list.get(position2).givenAmount = s.toString();
                        String actualAmount = list.get(position2).actualAmount;
                        if(!TextUtils.isEmpty(actualAmount)){

                            tv_discount.setVisibility(View.VISIBLE);
                            tv_discount.setText(YrmUtils.changetoHuiDiscount(actualAmount,list.get(position2).givenAmount)+"折");
                        }else {
                            tv_discount.setVisibility(View.INVISIBLE);
                        }
                    }
                });


                    if(list.size()>1){
                        iv_close.setVisibility(View.VISIBLE);
                    }else {
                        iv_close.setVisibility(View.GONE);
                    }

                iv_close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int position2 = (int) et_recharg_amount.getTag();
                        Log.i("xjz---position2",""+position2);
                        list.get(position).status = "3";
                        rulerList.add(list.get(position2));
                        list.remove(position2);
                        adapter1.updateList(list);
                        Log.i("xjz---传递参数","list.size"+list.size()+"    rulerList.size"+rulerList.size());
                    }
                });

            }
        };

        lv_ruler.setAdapter(adapter1);


        switchButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isInterfaceGet){
                    isInterfaceGet = false;
                    return;
                }
                if(isCancel){
                    isCancel = false;
                    return;
                }
                if(!isChecked){
                    showMyDialog3();
                }else {
                    type = "0";
                    setCardFlag(merId,type);
                    ll_close_hui.setVisibility(View.GONE);
                    ll_hui_open.setVisibility(View.VISIBLE);
                }
            }
        });

        switchBollowButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isInterfaceGet2){
                    isInterfaceGet2 = false;
                    return;
                }
                if(isChecked){
                    isShare = "0";
                    tv_count_limit.setVisibility(View.VISIBLE);
                }else {
                    showMyDialog4();
                }
            }
        });
    }

    public void setList(){
        HuiListBean2 bean = new HuiListBean2();
        bean.actualAmount = "";
        bean.givenAmount = "";
        list.add(bean);
    }

    private MyBottomDialog mDialog2;
    //退款
    public void showMyBottomDialog2() {

        List<String> strList = new ArrayList<>();
        strList.add("每张卡可借用六次");
        strList.add("不限次");
        if (mDialog2 == null) {
            mDialog2 = new MyBottomDialog(this);
        }
        mDialog2.showThisDialog("借用次数设置", strList, new MyBottomDialog.OnDialogItemListener() {
            @Override
            public void itemListener(int position) {
                if (position == 0) {
                    shareNum = "6";
                    tv_count_limit.setText("每张卡可借用六次");
                }else if(position == 1){
                    shareNum = "0";
                    tv_count_limit.setText("不限次");
                }
                if (mDialog2 != null)
                    mDialog2.dismiss();
            }
        });

    }



    @Override
    protected void load() {
        super.load();
        showLoading();
        if ("0".equals(mLoginType)) {
            getCoupon();
        }

    }

    public void getCoupon(){
        String url = NetUrl.QUERY_STORE_LIST;
        JSONObject jsonObject = new JSONObject();
        String mLoginType = SharedPreferencesUtil.getInstance(ModifyRulersBossActivity.this).getKey("loginType");
        try {
            if (mLoginType.equals("0")) {
                jsonObject.put("merId", SharedPreferencesUtil.getInstance(ModifyRulersBossActivity.this).getKey(com.hybunion.yirongma.payment.utils.Constants.MERCHANTID));
            } else {
                jsonObject.put("merId", SharedPreferencesUtil.getInstance(ModifyRulersBossActivity.this).getKey("shopId"));
                jsonObject.put("storeId", SharedPreferencesUtil.getInstance(ModifyRulersBossActivity.this).getKey("storeId"));
            }
            jsonObject.put("query", "");
            jsonObject.put("limit", 10000);
            jsonObject.put("start", 0);
        }catch (Exception e){
            e.printStackTrace();
        }

        OkUtils.getInstance().postNoHeader(ModifyRulersBossActivity.this, url, jsonObject, new MyOkCallback<StoreManageBean>() {
            @Override
            public void onStart() {
                showLoading();
            }

            @Override
            public void onSuccess(StoreManageBean bean) {
                List<StoreManageBean.ObjBean> dataList = bean.getData();
                if (!YrmUtils.isEmptyList(dataList)) {
                    mStoreList.addAll(dataList);
                }
                queryMerCardInfo(merId);
            }

            @Override
            public void onError(Exception e) {
                ToastUtil.show("网络连接不佳");
            }

            @Override
            public void onFinish() {
                hideLoading();
            }

            @Override
            public Class getClazz() {
                return StoreManageBean.class;
            }
        });

    }

    public void queryMerCardInfo(String merId){
        String url = NetUrl.QUERY_MER_CARD_INFO;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("merId", merId);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        OkUtils.getInstance().post(ModifyRulersBossActivity.this, url, jsonObject, new MyOkCallback<ModifyRulerBossBean>() {
            @Override
            public void onStart() {
                showLoading();
            }

            @Override
            public void onSuccess(ModifyRulerBossBean modifyRulerBossBean) {
                if(null!=modifyRulerBossBean){
                    if(null == modifyRulerBossBean.getData() | modifyRulerBossBean.getData().size() == 0) return;
                    List<HuiListBean2> cardRules = modifyRulerBossBean.getData().get(0).cardRules;
                    if(!YrmUtils.isEmptyList(cardRules)){
                        list.clear();
                        list.addAll(cardRules);
                        adapter1.updateList(list);
                    }
                    cardId = modifyRulerBossBean.getData().get(0).id;
                    popFlag = modifyRulerBossBean.getData().get(0).popFlag;
                    isShare = modifyRulerBossBean.getData().get(0).isShare;
                    shareNum = modifyRulerBossBean.getData().get(0).shareNum;
                    if("0".equals(isShare)){
                        isInterfaceGet2 = false;
                        switchBollowButton.setChecked(true);
                        tv_count_limit.setVisibility(View.VISIBLE);
                    }else {
                        isInterfaceGet2 = true;
                        switchBollowButton.setChecked(false);
                        tv_count_limit.setVisibility(View.GONE);
                    }
                    if("0".equals(shareNum)){
                        tv_count_limit.setText("不限次");
                    }else{
                        tv_count_limit.setText("每张卡可借用"+shareNum+"次");
                    }

                    if("1".equals(popFlag)){
                        tv_customer.setText("所有顾客弹窗指引");
                    }else if("2".equals(popFlag)){
                        tv_customer.setText("到店消费两次以上顾客弹窗指引");
                    }else {
                        tv_customer.setText("返回有误，请重新选择");
                    }
                    if(storeIdBuffer == null)
                        storeIdBuffer = new StringBuffer();

                    storeIdList =  modifyRulerBossBean.getData().get(0).storeIds;
                    if(!YrmUtils.isEmptyList(storeIdList)){
                        chooseShop = storeIdList.size();
                        tv_shop_name.setText("已选择 "+storeIdList.size()+" 门店");
                    }
                    for(int i=0;i<mStoreList.size();i++){
                        for(int j=0;j<storeIdList.size();j++){
                            if(mStoreList.get(i).getStoreId().equals(storeIdList.get(j).storeId)){//门店Id一致，被选中
                                mStoreList.get(i).isStoreChoose = true;
                            }
                        }
                    }
                    if(popWindow == null)
                        popWindow = new MyStorePopWindow(ModifyRulersBossActivity.this, mStoreList,1);//有不可取消的门店存在


                    vcSale = modifyRulerBossBean.getData().get(0).vcSale;
                    if("0".equals(vcSale)){//关闭
                        type = "0";
                        isInterfaceGet = true;
                        ll_close_hui.setVisibility(View.VISIBLE);
                        ll_hui_open.setVisibility(View.GONE);
                        switchButton.setChecked(false);
                    }else {//开启
                        type = "1";
                        ll_close_hui.setVisibility(View.GONE);
                        ll_hui_open.setVisibility(View.VISIBLE);
                        switchButton.setChecked(true);
                    }

                }
            }

            @Override
            public void onError(Exception e) {
                ToastUtil.show("网络连接不佳");
            }

            @Override
            public void onFinish() {
                hideLoading();
            }

            @Override
            public Class getClazz() {
                return ModifyRulerBossBean.class;
            }
        });

    }



    @Override
    public void showInfo(Map map, RequestIndex type) {
        super.showInfo(map, type);

        if (map == null) {
            ToastUtil.show("网络错误");
            return;
        }

        switch (type) {
            case QUERY_STORE_LIST:   // 加入商圈的门店
                StoreManageBean bean = (StoreManageBean) map.get("bean");
                if (bean == null) {
                    ToastUtil.show("网络错误");
                    return;
                }

                List<StoreManageBean.ObjBean> dataList = bean.getData();
                if (!YrmUtils.isEmptyList(dataList)) {
                    mStoreList.addAll(dataList);
                }
                queryMerCardInfo(merId);
                break;
            case SET_CARD_FLAG:
                BaseBean huiValueCardBean = (BaseBean) map.get("huiValueCardBean");
                if("0".equals(huiValueCardBean.getStatus())){
                    if("1".equals(this.type)){
                        ll_close_hui.setVisibility(View.VISIBLE);
                        ll_hui_open.setVisibility(View.GONE);
                    }else {

//                        isInterfaceGet = true;
                        switchButton.setChecked(true);
                        ll_close_hui.setVisibility(View.GONE);
                        ll_hui_open.setVisibility(View.VISIBLE);
                    }

                }
                if(!TextUtils.isEmpty(huiValueCardBean.getMessage())){
                    ToastUtil.show(huiValueCardBean.getMessage());
                }

                break;

                case QUERY_MER_CARD_INFO:
                    ModifyRulerBossBean modifyRulerBossBean = (ModifyRulerBossBean) map.get("modifyRulerBossBean");
                    if(null!=modifyRulerBossBean){
                        if(null == modifyRulerBossBean.getData() | modifyRulerBossBean.getData().size() == 0) return;
                        List<HuiListBean2> cardRules = modifyRulerBossBean.getData().get(0).cardRules;
                        if(!YrmUtils.isEmptyList(cardRules)){
                            list.clear();
                            list.addAll(cardRules);
                            adapter1.updateList(list);
                        }
                        cardId = modifyRulerBossBean.getData().get(0).id;
                        popFlag = modifyRulerBossBean.getData().get(0).popFlag;
                        isShare = modifyRulerBossBean.getData().get(0).isShare;
                        shareNum = modifyRulerBossBean.getData().get(0).shareNum;
                        if("0".equals(isShare)){
                            isInterfaceGet2 = false;
                            switchBollowButton.setChecked(true);
                            tv_count_limit.setVisibility(View.VISIBLE);
                        }else {
                            isInterfaceGet2 = true;
                            switchBollowButton.setChecked(false);
                            tv_count_limit.setVisibility(View.GONE);
                        }
                        if("0".equals(shareNum)){
                            tv_count_limit.setText("不限次");
                        }else{
                            tv_count_limit.setText("每张卡可借用"+shareNum+"次");
                        }

                        if("1".equals(popFlag)){
                            tv_customer.setText("所有顾客弹窗指引");
                        }else if("2".equals(popFlag)){
                            tv_customer.setText("到店消费两次以上顾客弹窗指引");
                        }else {
                            tv_customer.setText("返回有误，请重新选择");
                        }
                        if(storeIdBuffer == null)
                            storeIdBuffer = new StringBuffer();

                        storeIdList =  modifyRulerBossBean.getData().get(0).storeIds;
                        if(!YrmUtils.isEmptyList(storeIdList)){
                            chooseShop = storeIdList.size();
                            tv_shop_name.setText("已选择 "+storeIdList.size()+" 门店");
                        }
                        for(int i=0;i<mStoreList.size();i++){
                            for(int j=0;j<storeIdList.size();j++){
                                if(mStoreList.get(i).getStoreId().equals(storeIdList.get(j).storeId)){//门店Id一致，被选中
                                    mStoreList.get(i).isStoreChoose = true;
                                }
                            }
                        }
                        if(popWindow == null)
                             popWindow = new MyStorePopWindow(this, mStoreList,1);//有不可取消的门店存在


                        vcSale = modifyRulerBossBean.getData().get(0).vcSale;
                        if("0".equals(vcSale)){//关闭
                            this.type = "0";
                            isInterfaceGet = true;
                            ll_close_hui.setVisibility(View.VISIBLE);
                            ll_hui_open.setVisibility(View.GONE);
                            switchButton.setChecked(false);
                        }else {//开启
                            this.type = "1";
                            ll_close_hui.setVisibility(View.GONE);
                            ll_hui_open.setVisibility(View.VISIBLE);
                            switchButton.setChecked(true);
                        }

                    }

                        break;


        }

    }
    StringBuffer storeIdBuffer;
    private MyStorePopWindow popWindow;
    private int storePosition = -1;
    private int chooseShop = 0;
    private void showStoreList() {
        if (popWindow == null)
            popWindow = new MyStorePopWindow(this, mStoreList,1);//有不可取消的门店存在


        if(storeIdBuffer == null)
            storeIdBuffer = new StringBuffer();


        popWindow.showPopupWindow(storePosition);
        popWindow.setStoreItemListListener(new MyStorePopWindow.OnStoreItemListener() {
            @Override
            public void setStoreItemListener(int position) {
                if(mStoreList.get(position).isStoreChoose){
                    mStoreList.get(position).isStoreChoose3 = false;
                }else {
                    if(mStoreList.get(position).isStoreChoose3){
                        mStoreList.get(position).isStoreChoose3 = false;
                        chooseShop--;
                    }else {
                        mStoreList.get(position).isStoreChoose3 = true;
                        chooseShop++;
                    }
                }
            }
        });

        popWindow.setDissmissListener(new MyStorePopWindow.onDissmissListener() {
            @Override
            public void setDissmissListener() {
            }
        });

        popWindow.setOnCloseListener(new MyStorePopWindow.onCloseListener() {
            @Override
            public void setOnCloseListener() {

            }
        });

        popWindow.setButtonClickListener(new MyStorePopWindow.OnSureClickListener() {
            @Override
            public void setButtonClickListener() {
                if(storeIdBuffer.length()>0)
                    storeIdBuffer.delete(0,storeIdBuffer.length());


                for(int i=0;i<mStoreList.size();i++){
                    if(mStoreList.get(i).isStoreChoose2){
                        storeIdBuffer.append(mStoreList.get(i).getStoreId()+",");
                    }
                }


                if(storeIdBuffer.length()>0)
                    storeIdBuffer = storeIdBuffer.deleteCharAt(storeIdBuffer.length()-1);

                mStoreId = storeIdBuffer.toString();
                tv_shop_name.setText("已选择 "+chooseShop+" 门店");

            }
        });

    }



    Dialog mMyDialog3;
    private void showMyDialog3() {
        mMyDialog3 = new Dialog(this);
        mMyDialog3.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mMyDialog3.setContentView(R.layout.activity_hui_back_dialog);
        mMyDialog3.setCanceledOnTouchOutside(false);
        mMyDialog3.findViewById(R.id.btn_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type = "1";
                setCardFlag(merId,type);
                mMyDialog3.dismiss();
            }
        });
        mMyDialog3.findViewById(R.id.btn_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isCancel = true;
                switchButton.setChecked(true);
                mMyDialog3.dismiss();
            }
        });
        mMyDialog3.show();
    }

    public void setCardFlag(String merId ,String type2){
        String url = NetUrl.SET_CARD_FLAG;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("merId", merId);
            jsonObject.put("type",type2);

        } catch (JSONException e) {
            e.printStackTrace();
        }
       OkUtils.getInstance().post(ModifyRulersBossActivity.this, url, jsonObject, new MyOkCallback<BaseBean>() {
           @Override
           public void onStart() {
               showLoading();
           }

           @Override
           public void onSuccess(BaseBean huiValueCardBean) {
               if("0".equals(huiValueCardBean.getStatus())){
                   if("1".equals(type)){
                       ll_close_hui.setVisibility(View.VISIBLE);
                       ll_hui_open.setVisibility(View.GONE);
                   }else {
                       switchButton.setChecked(true);
                       ll_close_hui.setVisibility(View.GONE);
                       ll_hui_open.setVisibility(View.VISIBLE);
                   }

               }
               if(!TextUtils.isEmpty(huiValueCardBean.getMessage())){
                   ToastUtil.show(huiValueCardBean.getMessage());
               }
           }

           @Override
           public void onError(Exception e) {
               ToastUtil.show("网络连接不佳");
           }

           @Override
           public void onFinish() {
               hideLoading();
           }

           @Override
           public Class getClazz() {
               return null;
           }
       });
    }

    Dialog mMyDialog4;
    private void showMyDialog4() {
        mMyDialog4 = new Dialog(this);
        mMyDialog4.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mMyDialog4.setContentView(R.layout.activity_hui_bollow_dialog);
        mMyDialog4.setCanceledOnTouchOutside(false);
        mMyDialog4.findViewById(R.id.btn_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isShare = "1";
                tv_count_limit.setVisibility(View.GONE);
                mMyDialog4.dismiss();
            }
        });
        mMyDialog4.findViewById(R.id.btn_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isShare = "0";
                switchBollowButton.setChecked(true);
                tv_count_limit.setVisibility(View.VISIBLE);
                mMyDialog4.dismiss();
            }
        });
        mMyDialog4.show();
    }

    private MyBottomDialog mDialog;
    //退款
    public void showMyBottomDialog() {

        List<String> strList = new ArrayList<>();
        strList.add("所有顾客弹窗指引");
        strList.add("到店消费两次以上顾客弹窗指引");
        if (mDialog == null) {
            mDialog = new MyBottomDialog(this);
        }
        mDialog.showThisDialog("收款码充值弹框提醒", strList, new MyBottomDialog.OnDialogItemListener() {
            @Override
            public void itemListener(int position) {
                if (position == 0) {
                    tv_customer.setText("所有顾客弹窗指引");
                    popFlag = "1";
                }else if(position == 1){
                    tv_customer.setText("到店消费两次以上顾客弹窗指引");
                    popFlag = "2";
                }
                if (mDialog != null)
                    mDialog.dismiss();
            }
        });

    }
    ToastPopupWindow popupWindow;
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_shop_name:
                if (!"0".equals(mLoginType)) return;  // 不是老板不用选择门店。店长直接显示自己所属门店即可。
                showStoreList();
                break;
            case R.id.tv_customer:
                showMyBottomDialog();
                break;
            case R.id.iv_add_ruler:
                if(YrmUtils.isFastDoubleClick())  return;

                if(list.size()<6){
                    HuiListBean2 bean = new HuiListBean2();
                    bean.actualAmount = "";
                    bean.givenAmount = "";
                    list.add(bean);
                }else{
                    ToastUtil.show("最多可制作六个卡规则");
                    return;
                }
                adapter1.updateList(list);
                break;
            case R.id.rv_others_bollow:
                popupWindow = new ToastPopupWindow(this,rv_others_bollow,R.layout.layout_toast_other_popupwindow);
                break;
            case R.id.ll_count_limit:
                showMyBottomDialog2();
                break;
            case R.id.bt_save:
                for(int i=0;i<list.size();i++){
                    if( YrmUtils.compareToBigDecimal(list.get(i).actualAmount,"10") == 2||YrmUtils.compareToBigDecimal(list.get(i).givenAmount,"1") == 2){
                        ToastUtil.show("请输入充值金额与赠送金额");
                        return;
                    }

                    if( YrmUtils.compareToBigDecimal(list.get(i).actualAmount,"10") == -1){
                        ToastUtil.show("充值金额不能小于10元");
                        return;
                    }
                    if(YrmUtils.compareToBigDecimal(list.get(i).givenAmount,"1") == -1){
                        ToastUtil.show("赠送金额不能小于1元");
                        return;
                    }
                }
                Log.i("xjz---传递参数","list.size"+list.size()+"    rulerList.size"+rulerList.size());
                for(int i=0;i<rulerList.size();i++){
                    if(TextUtils.isEmpty(rulerList.get(i).ruleId)){
                        rulerList.remove(i);
                    }
                }
                if(!YrmUtils.isEmptyList(rulerList)){
                    list.addAll(rulerList);
                }
                Log.i("xjz---传递参数","tijiao-list.size"+list.size());
                for(int i=0;i<list.size();i++){
                    if(TextUtils.isEmpty(list.get(i).ruleId)){//新增
                        list.get(i).status = "1";
                    }else if("3".equals(list.get(i).status)){
                        list.get(i).status = "3";
                    }else {
                        list.get(i).status = "2";
                    }
                }
                Gson gson = new Gson();
                String cardRules = gson.toJson(list);

                if(TextUtils.isEmpty(cardRules)){
                    ToastUtil.show("请输入使用规则");
                    return;
                }


                Log.i("xjz---传递参数","cardId"+cardId);
                Log.i("xjz---传递参数","mStoreId"+mStoreId);
                Log.i("xjz---传递参数","cardRules"+cardRules);
                Log.i("xjz---传递参数","popFlag"+popFlag);
                updateMerCardInfo(cardId,mStoreId,cardRules,popFlag,isShare,shareNum);
                break;

        }
    }


    public void updateMerCardInfo(String cardId,String storeIds,String cardRules,String popFlag,String isShare,String shareNum){
        String url = NetUrl.UPDATE_MER_CARD_INFO;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("cardId", cardId);
            jsonObject.put("storeIds", storeIds);
            jsonObject.put("cardRules", cardRules);
            jsonObject.put("popFlag", popFlag);
            jsonObject.put("isShare",isShare);
            jsonObject.put("shareNum",shareNum);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        OkUtils.getInstance().post(ModifyRulersBossActivity.this, url, jsonObject, new MyOkCallback<BaseBean>() {
            @Override
            public void onStart() {
                showLoading();
            }

            @Override
            public void onSuccess(BaseBean result) {
                if(!TextUtils.isEmpty(result.getMessage())){
                    ToastUtil.show(result.getMessage());
                }
                if("0".equals(result.getStatus())){
                    finish();
                }
            }

            @Override
            public void onError(Exception e) {
                ToastUtil.show("网络连接不佳");
            }

            @Override
            public void onFinish() {
                hideLoading();
            }

            @Override
            public Class getClazz() {
                return BaseBean.class;
            }
        });
    }
}
