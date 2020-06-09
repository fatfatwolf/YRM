package com.hybunion.yirongma.payment.activity;

import android.app.Dialog;
import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
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
import com.hybunion.yirongma.payment.bean.HuiListBean;
import com.hybunion.yirongma.payment.bean.HuiValueCardBean;
import com.hybunion.yirongma.payment.bean.StoreManageBean;
import com.hybunion.yirongma.payment.utils.RequestIndex;
import com.hybunion.yirongma.payment.utils.SharedPreferencesUtil;
import com.hybunion.yirongma.R;
import com.hybunion.yirongma.payment.utils.SharedPConstant;
import com.hybunion.yirongma.payment.Fragment.LMFPaymentFragment;
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

public class HuiValueCardActivity extends BasicActivity implements View.OnClickListener{
    @Bind(R.id.lv_ruler)
    ListView lv_ruler;
    @Bind(R.id.iv_add_ruler)
    ImageView iv_add_ruler;
    @Bind(R.id.iv_agree)
    ImageView iv_agree;
    @Bind(R.id.rv_use_shop)
    RelativeLayout rv_use_shop;
    @Bind(R.id.tv_customer)
    TextView tv_customer;
    @Bind(R.id.tv_shop_name)
    TextView tv_shop_name;
    @Bind(R.id.tv_merchant_myself)
    TextView tv_merchant_myself;
    @Bind(R.id.tv_consume_service)
    TextView tv_consume_service;
    @Bind(R.id.rv_others_bollow)
    RelativeLayout rv_others_bollow;
    @Bind(R.id.switchButton)
    Switch switchButton;
    @Bind(R.id.ll_count_limit)
    LinearLayout ll_count_limit;
    @Bind(R.id.tv_count_limit)
    TextView tv_count_limit;
    @Bind(R.id.tv_count)
    TextView tv_count;
    @Bind(R.id.bt_save)
    Button bt_save;

    List<HuiListBean> list;
    CommonAdapter1 adapter1;
    private String mLoginType;
    private String mStoreId, mStoreName;
    boolean isRead = true;
    private String merId;
    private String popFlag = "1";
    private String isShare = "0";
    private String shareNum = "10";
    private List<StoreManageBean.ObjBean> mStoreList = new ArrayList(); // 加入商圈的门店 List
    @Override
    protected BasePresenter getPresenter() {
        return null;
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_hui_value_card;
    }


    @Override
    public void initView() {
        super.initView();
        list = new ArrayList<>();
        setList();
        mLoginType = SharedPreferencesUtil.getInstance(this).getKey("loginType");
        merId = SharedPreferencesUtil.getInstance(this).getKey(SharedPConstant.MERCHANT_ID);
        if("1".equals(mLoginType)){
            mStoreName = SharedPreferencesUtil.getInstance(HuiValueCardActivity.this).getKey("storeName");
            mStoreId = SharedPreferencesUtil.getInstance(HuiValueCardActivity.this).getKey("storeId");
            tv_shop_name.setText(mStoreName);
        }
        iv_add_ruler.setOnClickListener(this);
        iv_agree.setOnClickListener(this);
        rv_use_shop.setOnClickListener(this);
        rv_others_bollow.setOnClickListener(this);
        tv_customer.setOnClickListener(this);
        tv_shop_name.setOnClickListener(this);
        tv_merchant_myself.setOnClickListener(this);
        tv_consume_service.setOnClickListener(this);
        bt_save.setOnClickListener(this);
        adapter1 = new CommonAdapter1<HuiListBean>(this,list,R.layout.item_hui_card_ruler){
            @Override
            public void convert(ViewHolder holder, HuiListBean item, final int position) {
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
                        list.get(position).actualAmount = "";
                        list.get(position).givenAmount = "";
                        list.remove(position);

                        adapter1.updateList(list);
                    }
                });

            }
        };

        lv_ruler.setAdapter(adapter1);
        lv_ruler.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

            }
        });

        switchButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    isShare = "0";
                    tv_count.setVisibility(View.VISIBLE);
                }else {
                    showMyDialog3();
                }
            }
        });

    }


    @Override
    protected void load() {
        super.load();
        showLoading();
        getCoupon();
    }

    // type=1 storeId 为商户 id ，即老板
    // type=2 storeId 为门店 id， 即店长
    public void  getCoupon(){
        String url = NetUrl.QUERY_STORE_LIST;
        JSONObject jsonObject = new JSONObject();
        String mLoginType = SharedPreferencesUtil.getInstance(HuiValueCardActivity.this).getKey("loginType");
        try {
            if (mLoginType.equals("0")) {
                jsonObject.put("merId", SharedPreferencesUtil.getInstance(HuiValueCardActivity.this).getKey(com.hybunion.yirongma.payment.utils.Constants.MERCHANTID));
            } else {
                jsonObject.put("merId", SharedPreferencesUtil.getInstance(HuiValueCardActivity.this).getKey("shopId"));
                jsonObject.put("storeId", SharedPreferencesUtil.getInstance(HuiValueCardActivity.this).getKey("storeId"));
            }
            jsonObject.put("query", "");
            jsonObject.put("limit", 10000);
            jsonObject.put("start", 0);
        }catch (Exception e){
            e.printStackTrace();
        }

        OkUtils.getInstance().postNoHeader(HuiValueCardActivity.this, url, jsonObject, new MyOkCallback<StoreManageBean>() {
            @Override
            public void onStart() {
                showLoading();
            }

            @Override
            public void onSuccess(StoreManageBean bean) {
                List<StoreManageBean.ObjBean> dataList = bean.getData();
                if (!YrmUtils.isEmptyList(dataList)) {   // 没有加入商圈的门店
                    mStoreList.addAll(dataList);
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
                return StoreManageBean.class;
            }
        });

    }


    public void setList(){
        HuiListBean bean = new HuiListBean();
        bean.actualAmount = "";
        bean.givenAmount = "";
        list.add(bean);
    }
    ToastPopupWindow popupWindow;
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_add_ruler:
                if(YrmUtils.isFastDoubleClick())  return;

                if(list.size()<6){
                    HuiListBean bean = new HuiListBean();
                    bean.actualAmount = "";
                    bean.givenAmount = "";
                    list.add(bean);
                }else {
                    ToastUtil.show("最多可制定六个规则");
                    return;
                }
                adapter1.updateList(list);
                break;
            case R.id.iv_agree:
                if(isRead){
                    iv_agree.setBackground(getResources().getDrawable(R.drawable.symbolsno));
                    isRead = !isRead;
                }else {
                    iv_agree.setBackground(getResources().getDrawable(R.drawable.img_hui_agree));
                    isRead = !isRead;
                }
                break;
            case R.id.rv_use_shop:
                popupWindow = new ToastPopupWindow(this,rv_use_shop,R.layout.layout_toast_popupwindow);
                break;
            case R.id.rv_others_bollow:
                popupWindow = new ToastPopupWindow(this,rv_others_bollow,R.layout.layout_toast_other_popupwindow);
                break;
            case R.id.tv_customer:
                showMyBottomDialog();
                break;
            case R.id.ll_count_limit:
                showMyBottomDialog2();
                break;
            case R.id.tv_shop_name:
                if (!"0".equals(mLoginType)) return;  // 不是老板不用选择门店。店长直接显示自己所属门店即可。
                showStoreList();
                break;
            case R.id.tv_merchant_myself:
                Intent intent = new Intent(HuiValueCardActivity.this,LMFRedRainActivity.class);
                intent.putExtra("webViewUrl","8");
                startActivity(intent);
                break;
            case R.id.tv_consume_service:
                Intent intent2 = new Intent(HuiValueCardActivity.this,LMFRedRainActivity.class);
                intent2.putExtra("webViewUrl","9");
                startActivity(intent2);
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

                Gson gson = new Gson();
                String cardRules = gson.toJson(list);
                if(TextUtils.isEmpty(cardRules)){
                    ToastUtil.show("请输入使用规则");
                    return;
                }

                if(TextUtils.isEmpty(mStoreId)){
                    ToastUtil.show("请选择门店");
                    return;
                }
                if(!isRead){
                    ToastUtil.show("请先勾选已阅读协议");
                    return;
                }

                addMerCard(merId,mStoreId,cardRules,popFlag,isShare,shareNum);
//
                break;
        }
    }


    public void addMerCard(String merId, String storeIds, String cardRules, String popFlag,String isShare,String shareNum){
        String url = NetUrl.ADD_MER_CARD;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("merId", merId);
            jsonObject.put("storeIds",storeIds);
            jsonObject.put("cardRules",cardRules);
            jsonObject.put("popFlag",popFlag);
            jsonObject.put("isShare",isShare);
            jsonObject.put("shareNum",shareNum);
        } catch (JSONException e) {
            e.printStackTrace();
        }
       OkUtils.getInstance().post(HuiValueCardActivity.this, url, jsonObject, new MyOkCallback<HuiValueCardBean>() {
           @Override
           public void onStart() {
               showLoading();
           }

           @Override
           public void onSuccess(HuiValueCardBean huiValueCardBean) {
               if(!TextUtils.isEmpty(huiValueCardBean.getMessage())){
                   ToastUtil.show(huiValueCardBean.getMessage());
               }
               if("0".equals(huiValueCardBean.getStatus())){
                   Intent msgIntent = new Intent(LMFPaymentFragment.HUI_VALUE_CARD_INTENT_RECEIVER);
                   HuiValueCardActivity.this.sendBroadcast(msgIntent);
                   hideLoading();
                   finish();
               }
           }

           @Override
           public void onError(Exception e) {

           }

           @Override
           public void onFinish() {
                hideLoading();
           }

           @Override
           public Class getClazz() {
               return HuiValueCardBean.class;
           }
       });
    }
    StringBuffer storeIdBuffer;
    private MyStorePopWindow popWindow;
    private int storePosition = -1;
    private int chooseShop = 0;
    private void showStoreList() {
        if (popWindow == null)
            popWindow = new MyStorePopWindow(this, mStoreList,0);


        if(storeIdBuffer == null)
            storeIdBuffer = new StringBuffer();



        popWindow.showPopupWindow(storePosition);
        popWindow.setStoreItemListListener(new MyStorePopWindow.OnStoreItemListener() {
            @Override
            public void setStoreItemListener(int position) {
                if(mStoreList.get(position).isStoreChoose3){
                    mStoreList.get(position).isStoreChoose3 = false;
                    chooseShop--;
                }else {
                    mStoreList.get(position).isStoreChoose3 = true;
                    chooseShop++;
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
//                if (TextUtils.isEmpty(mSelectedStoreId)) {
//                    ToastUtil.show("请先选择门店");
//                    return;
//                }
                if(storeIdBuffer.length()>0)
                    storeIdBuffer.delete(0,storeIdBuffer.length());


                boolean isSelected = false;
                for(int i=0;i<mStoreList.size();i++){
                    if(mStoreList.get(i).isStoreChoose){
                        isSelected = true;
                        storeIdBuffer.append(mStoreList.get(i).getStoreId()+",");
                    }
                }

                if(!isSelected){
                    ToastUtil.show("请先选择门店");
                    return;
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
        mMyDialog3.setContentView(R.layout.activity_hui_bollow_dialog);
        mMyDialog3.setCanceledOnTouchOutside(false);
        mMyDialog3.findViewById(R.id.btn_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isShare = "1";
                tv_count.setVisibility(View.INVISIBLE);
                mMyDialog3.dismiss();
            }
        });
        mMyDialog3.findViewById(R.id.btn_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isShare = "0";
                switchButton.setChecked(true);
                tv_count.setVisibility(View.VISIBLE);
                mMyDialog3.dismiss();
            }
        });
        mMyDialog3.show();
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
    public void showInfo(Map map, RequestIndex type) {
        super.showInfo(map, type);

        if (map == null) {
            ToastUtil.show("网络错误");
            return;
        }

        switch (type) {
            case QUERY_STORE_LIST:
                StoreManageBean bean = (StoreManageBean) map.get("bean");
                if (bean == null) {
                    ToastUtil.show("网络错误");
                    return;
                }


                break;
            case ADD_MER_CARD:
                HuiValueCardBean huiValueCardBean = (HuiValueCardBean) map.get("huiValueCardBean");
                if("0".equals(huiValueCardBean.getStatus())){
                    Intent msgIntent = new Intent(LMFPaymentFragment.HUI_VALUE_CARD_INTENT_RECEIVER);
                    this.sendBroadcast(msgIntent);
                     finish();
                }
                break;
        }

    }
}
