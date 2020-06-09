package com.hybunion.yirongma.payment.db;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.baidu.android.common.logging.Log;
import com.hybunion.yirongma.payment.bean.QueryTransBean;
import com.hybunion.yirongma.payment.activity.ScreeningListActivity;
import com.hybunion.yirongma.payment.model.QueryDataBean;
import com.hybunion.yirongma.payment.utils.YrmUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 订单明细列表使用
 */

public class BillingDataListDBManager {
    private static BillingDataListDBManager mDbManager;
    private LMFDataBaseHelper mDbHelper;
    private Context mContext;
    private SQLiteDatabase mDataBase;
    private String mTableName = "billing_data";
    private int DB_VERSION = 3;

    private BillingDataListDBManager(Context context) {
        mContext = context;
        mDbHelper = new LMFDataBaseHelper(context, "BillingDataBase.db", DB_VERSION);
        mDataBase = mDbHelper.getWritableDatabase();
        deleteNotToday();

    }

    public static void setNull(){
        mDbManager = null;
    }

    public static BillingDataListDBManager getInstance(Context context) {
        if (mDbManager == null)
            synchronized (BillingDataListDBManager.class) {
                if (mDbManager == null)
                    mDbManager = new BillingDataListDBManager(context);
            }

        return mDbManager;
    }

    public void insertData(QueryTransBean.DataBean bean) {
        insertData(bean, null);
    }

    /**
     * 插入一条数据 （收款码）
     *
     * @param bean
     * @param listener 插入结果回调
     */
    public void insertData(QueryTransBean.DataBean bean, OnInsertDataListener listener) {
        if (mDataBase == null) return;
        // 插入重复数据会报错，catch 里面做更新操作
        try {
            String sql = "insert into " + mTableName + " (UUID, transAmount, transTime, transTimeLong, payType, " +
                    "payChannel, orderNo, tidName, isAmount, tId, sourceTid, transType, storeId, kuanTaiId,iconUrl, payableAmount, isHuiChuZhi) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            Log.d("sql333333", "插入的 sql 语句是：" + sql);
            mDataBase.execSQL(sql, new Object[]{bean.UUID, bean.transAmount, bean.transTime, bean.getTransTimeLong(),
                    bean.payType, bean.payChannel, bean.orderNo, bean.tidName, bean.isAmount, bean.tId, bean.sourceTid, bean.transType, bean.storeId, bean.kuanTaiId, bean.iconUrl, bean.payableAmount,"N"});
            if (listener != null)
                listener.insertSuccess();
        } catch (SQLException e) {
            e.printStackTrace();
            // 更新重复插入的数据
            updateData(bean);
        }
    }

    /**
     *  插入一条数据 （惠储值）
     */
    public void insertHuiChuZhiData(QueryTransBean.DataBean bean, OnInsertDataListener listener){
        if (mDataBase == null) return;
        try {
            String sql = "insert into " + mTableName + "(transAmount, transTime, transTimeLong, isHuiChuZhi, orderType, payChannel, orderNo)" +
                    " values (?,?,?,?,?,?,?)";
            mDataBase.execSQL(sql, new Object[]{bean.transAmount, bean.transTime, bean.getTransTimeLong(),"Y",bean.orderType, bean.payChannel, bean.orderNo});
            if (listener != null)
                listener.insertSuccess();
        } catch (SQLException e) {
            e.printStackTrace();
            updateHuiChuZhiData(bean);
        }
    }

    /**
     * 更新  （惠储值） 只更新了 payChannel 交易状态接口
     */
    public void updateHuiChuZhiData(QueryTransBean.DataBean bean){
        if (mDataBase == null) return;
        if (TextUtils.isEmpty(bean.payChannel))
            return;
        try {
            StringBuffer sb = new StringBuffer("update " + mTableName + " set ");
            List<Object> list = new ArrayList<>();
            if (!TextUtils.isEmpty(bean.payChannel)) {
                sb.append("payChannel=?");
                list.add(bean.payChannel);
            }
            sb.append(" where orderNo =?");
            list.add(bean.orderNo);
            Log.d("sql333333", "更新的 sql 语句是：" + sb.toString());
            mDataBase.execSQL(sb.toString(), list.toArray());
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    /**
     *  查询（惠储值） 每次查询 前 20*(page+1) 条数据
     */
    public List<QueryTransBean.DataBean> huiChuZhiQueryData(String startLong, String endLong, int page){
        List<QueryTransBean.DataBean> list = new ArrayList<>();
        if (mDataBase == null) return list;
        String query = "select * from " + mTableName + " where transTimeLong>=? and transTimeLong<=? and isHuiChuZhi=? order by transTimeLong desc limit "+(20*(page+1))+" offset 0";
        Log.d("sql333333", "列表页面 查询语句是：" + query);
        Cursor cursor = mDataBase.rawQuery(query, new String[]{startLong, endLong,"Y"});
        if (cursor != null) {
            while (cursor.moveToNext()) {
                QueryTransBean.DataBean bean = new QueryTransBean().new DataBean();
                bean.transAmount = cursor.getString(cursor.getColumnIndex("transAmount"));
                bean.transTime = cursor.getString(cursor.getColumnIndex("transTime"));
                bean.orderNo = cursor.getString(cursor.getColumnIndex("orderNo"));
                bean.orderType = cursor.getString(cursor.getColumnIndex("orderType"));
                bean.payChannel = cursor.getString(cursor.getColumnIndex("payChannel"));
                list.add(bean);
            }
        }
        return list;
    }


    public interface OnInsertDataListener {
        void insertSuccess();
    }

    /**
     * 更新数据 的筛选条件字段。有值才更新。
     * 需要更新的字段有：payChannel(支付/退款状态)  isAmount(查询金额范围）  tId(收款码id)  sourceTid(收银插件id)
     * transType(是否是退款)  storeId  kuanTaiId  tidName(收银插件名称)  iconUrl(图标)
     *
     * @param bean
     */
    private boolean mHaveParam ;
    private void updateData(QueryTransBean.DataBean bean) {
        mHaveParam = false;
        if (mDataBase == null) return;
        // 需要更新的字段，都为空，就不用更新了。
        if (TextUtils.isEmpty(bean.UUID) && TextUtils.isEmpty(bean.payChannel) && TextUtils.isEmpty(bean.isAmount) && TextUtils.isEmpty(bean.tId)
                && TextUtils.isEmpty(bean.sourceTid) && TextUtils.isEmpty(bean.transType) && TextUtils.isEmpty(bean.storeId)
                && TextUtils.isEmpty(bean.kuanTaiId) && TextUtils.isEmpty(bean.tidName) && TextUtils.isEmpty(bean.iconUrl))
            return;

        try {
            StringBuffer sb = new StringBuffer("update " + mTableName + " set ");
            List<Object> list = new ArrayList<>();
            if (!TextUtils.isEmpty(bean.UUID)) {
                sb.append("UUID=?");
                list.add(bean.UUID);
                mHaveParam = true;
            }

            if (!TextUtils.isEmpty(bean.payChannel)) {
                if (mHaveParam)
                    sb.append(",");
                sb.append("payChannel=?");
                list.add(bean.payChannel);
                mHaveParam = true;
            }

            if (!TextUtils.isEmpty(bean.isAmount)) {
                if (mHaveParam)
                    sb.append(",");
                sb.append("isAmount=?");
                list.add(bean.isAmount);
                mHaveParam = true;
            }
            if (!TextUtils.isEmpty(bean.tId)) {
                if (mHaveParam)
                    sb.append(",");
                sb.append("tId=?");
                list.add(bean.tId);
                mHaveParam = true;
            }
            if (!TextUtils.isEmpty(bean.sourceTid)) {
                if (mHaveParam)
                    sb.append(",");
                sb.append("sourceTid=?");
                list.add(bean.sourceTid);
                mHaveParam = true;
            }
            if (!TextUtils.isEmpty(bean.transType)) {
                if (mHaveParam)
                    sb.append(",");
                sb.append("transType=?");
                list.add(bean.transType);
                mHaveParam = true;
            }
            if (!TextUtils.isEmpty(bean.storeId)) {
                if (mHaveParam)
                    sb.append(",");
                sb.append("storeId=?");
                list.add(bean.storeId);
                mHaveParam = true;
            }
            if (!TextUtils.isEmpty(bean.kuanTaiId)) {
                if (mHaveParam)
                    sb.append(",");
                sb.append("kuanTaiId=?");
                list.add(bean.kuanTaiId);
                mHaveParam = true;
            }
            if (!TextUtils.isEmpty(bean.tidName)) {
                if (mHaveParam)
                    sb.append(",");
                sb.append("tidName=?");
                list.add(bean.tidName);
                mHaveParam = true;
            }

            if (!TextUtils.isEmpty(bean.iconUrl)) {
                if (mHaveParam)
                    sb.append(",");
                sb.append("iconUrl=?");
                list.add(bean.iconUrl);
                mHaveParam = true;
            }

            if (!TextUtils.isEmpty(bean.payableAmount)){   // 实际支付金额
                if (mHaveParam)
                    sb.append(",");
                sb.append("payableAmount=?");
                list.add(bean.payableAmount);
            }


            sb.append(" where orderNo =?");
            list.add(bean.orderNo);
            Log.d("sql333333", "更新的 sql 语句是：" + sb.toString());
            mDataBase.execSQL(sb.toString(), list.toArray());
        } catch (SQLException e) {
            e.printStackTrace();

        }

    }


    /**
     * 插入多条数据
     */
    public void insertDatas(List<QueryTransBean.DataBean> listData) {
        if (listData != null && listData.size() != 0) {
            for (int i = 0; i < listData.size(); i++) {
                insertData(listData.get(i), null);
            }
        }
    }

    /**
     * 列表页 查询 （收款码）
     *
     * @param page 页数。列表分页使用。 page=0 时，取前20条。 page>0 时，即上拉加载分页时，取前 20*(page+1) 条
     */
    public List<QueryTransBean.DataBean> queryDataByDate(String startLong, String endLong, int page) {
        List<QueryTransBean.DataBean> list = new ArrayList<>();
        if (mDataBase == null) return list;
        String query = "select * from " + mTableName + " where transTimeLong>=? and transTimeLong<=? and isHuiChuZhi=? order by transTimeLong desc limit "+(20*(page+1))+" offset 0";
        Log.d("sql333333", "列表页面 查询语句是：" + query);
        Cursor cursor = mDataBase.rawQuery(query, new String[]{startLong, endLong,"N"});
        if (cursor != null) {
            while (cursor.moveToNext()) {
                QueryTransBean.DataBean bean = new QueryTransBean().new DataBean();
                bean.UUID = cursor.getString(cursor.getColumnIndex("UUID"));
                bean.transAmount = cursor.getString(cursor.getColumnIndex("transAmount"));
                bean.transTime = cursor.getString(cursor.getColumnIndex("transTime"));
                bean.payType = cursor.getString(cursor.getColumnIndex("payType"));
                bean.payChannel = cursor.getString(cursor.getColumnIndex("payChannel"));
                bean.orderNo = cursor.getString(cursor.getColumnIndex("orderNo"));
                bean.iconUrl = cursor.getString(cursor.getColumnIndex("iconUrl"));
                bean.tidName = cursor.getString(cursor.getColumnIndex("tidName"));
                bean.payableAmount = cursor.getString(cursor.getColumnIndex("payableAmount"));
                list.add(bean);
            }
        }
        return list;
    }

    /**
     * 筛选 列表页 查询，数据库分页。
     * payChannel 0-微信  1-支付宝  3-银联
     * isAmount 值为 <300 或 >=300
     * transType 值为：“” 或者 3
     * 没有筛选的可以传 null
     * StringBuffer 是线程安全的。
     */
    private List<QueryTransBean.DataBean> mQueryDataList;
    private ArrayList<String> mQueryList;
    private QueryDataBean mDataBean;

    public List<QueryTransBean.DataBean> queryData(String startDateLong, String endDateLong, String payType,
                                   String isAmount, String tId, String sourceTid, String transType, String storeId, String kuanTaiId, int page) {
        if (mQueryDataList == null)
            mQueryDataList = new ArrayList<>();
        else
            mQueryDataList.clear();
        StringBuffer query = new StringBuffer("select * from " + mTableName + " where transTimeLong>=? and transTimeLong<=?");
        if (mQueryList == null)
            mQueryList = new ArrayList<>();
        else
            mQueryList.clear();
        mQueryList.add(startDateLong);
        mQueryList.add(endDateLong);
        if (!TextUtils.isEmpty(payType)) {
            query.append(" and payType=?");
            mQueryList.add(payType);
        }

        if (!TextUtils.isEmpty(isAmount)) {
            query.append(" and isAmount=?");
            mQueryList.add(isAmount);
        }
        if (!TextUtils.isEmpty(tId)) {
            query.append(" and tId=?");
            mQueryList.add(tId);
        }

        if (!TextUtils.isEmpty(sourceTid)) {
            query.append(" and sourceTid=?");
            mQueryList.add(sourceTid);
        }

        if (!TextUtils.isEmpty(transType)) {
            query.append(" and transType=?");
            mQueryList.add(transType);
        }
        if (!TextUtils.isEmpty(storeId)) {
            query.append(" and storeId=?");
            mQueryList.add(storeId);
        }
        if (!TextUtils.isEmpty(kuanTaiId)) {
            query.append(" and kuanTaiId=?");
            mQueryList.add(kuanTaiId);
        }

        query.append(" order by transTimeLong desc");
        query.append(" limit " + ScreeningListActivity.PAGE_SIZE + " offset " + (page*ScreeningListActivity.PAGE_SIZE)); // 分页

        Log.d("sql333333", "查询的 sql 语句是：" + query.toString());
        String[] strings = new String[mQueryList.size()];
        Cursor cursor = mDataBase.rawQuery(query.toString(), mQueryList.toArray(strings));

        if (cursor != null) {
            while (cursor.moveToNext()) {   // 筛选查询 ，要查询所有的 跟筛选有关的字段。
                QueryTransBean.DataBean bean = new QueryTransBean().new DataBean();
                bean.UUID = cursor.getString(cursor.getColumnIndex("UUID"));
                bean.transAmount = cursor.getString(cursor.getColumnIndex("transAmount"));
                bean.transTime = cursor.getString(cursor.getColumnIndex("transTime"));
                bean.payType = cursor.getString(cursor.getColumnIndex("payType"));
                bean.payChannel = cursor.getString(cursor.getColumnIndex("payChannel"));
                bean.orderNo = cursor.getString(cursor.getColumnIndex("orderNo"));
                bean.iconUrl = cursor.getString(cursor.getColumnIndex("iconUrl"));
                bean.tidName = cursor.getString(cursor.getColumnIndex("tidName"));
                bean.tId = cursor.getString(cursor.getColumnIndex("tId"));
                bean.sourceTid = cursor.getString(cursor.getColumnIndex("sourceTid"));
                bean.transType = cursor.getString(cursor.getColumnIndex("transType"));
                bean.payType = cursor.getString(cursor.getColumnIndex("payType"));
                bean.isAmount = cursor.getString(cursor.getColumnIndex("isAmount"));
                bean.storeId = cursor.getString(cursor.getColumnIndex("storeId"));
                bean.kuanTaiId = cursor.getString(cursor.getColumnIndex("kuanTaiId"));
                mQueryDataList.add(bean);
            }
        }


//        if (cursor != null) {
//            if (mDataBean == null)
//                mDataBean = new QueryDataBean();
//            else {
//                mDataBean.dataList = null;
//                mDataBean.transCount = 0;
//                mDataBean.transAmount = 0.00;
//            }
//            while (cursor.moveToNext()) {
//                QueryTransBean.DataBean bean = new QueryTransBean().new DataBean();
//                // 按照筛选条件查询数据的时候，如果没有 UUID，表示是推送过来的消息
//                // 计算推送过来的消息金额的总和 and 总笔数，返回。用于和网络请求的数据 金额和总比数 做和，计算筛选数据的总金额和总比数
//                bean.UUID = cursor.getString(cursor.getColumnIndex("UUID"));
//                bean.transAmount = cursor.getString(cursor.getColumnIndex("transAmount"));
//                if (TextUtils.isEmpty(bean.UUID)) {  // 推送过来的消息
//                    mDataBean.transCount++;
//                    if (!TextUtils.isEmpty(bean.transAmount) && (bean.transAmount.matches("[0-9]+") || bean.transAmount.matches("[0-9]+[.][0-9]+]"))) {
//                        mDataBean.transAmount += Double.parseDouble(bean.transAmount);
//                    }
//                }
//                bean.transTime = cursor.getString(cursor.getColumnIndex("transTime"));
//                bean.payType = cursor.getString(cursor.getColumnIndex("payType"));
//                bean.payChannel = cursor.getString(cursor.getColumnIndex("payChannel"));
//                bean.orderNo = cursor.getString(cursor.getColumnIndex("orderNo"));
//                bean.iconUrl = cursor.getString(cursor.getColumnIndex("iconUrl"));
//                mQueryDataList.add(bean);
//            }
//        } else {
//            mDataBean = null;
//        }
//        if (mDataBean != null) {
//            mDataBean.dataList = mQueryDataList;
//        }
        return mQueryDataList;
    }

    /**
     *  删除非当天的数据
     */
    public void deleteNotToday(){
        if (mDataBase == null) return;
        String timeLong = YrmUtils.getTodayTimeLong();
        String sql = "delete from "+mTableName+" where transTimeLong<?";
        mDataBase.execSQL(sql,new Object[]{timeLong});
    }


    /**
     *  删除所有数据（保留表结构）
     */
    public void deleteAll(){
        if (mDataBase == null) return;
        String sql = "delete from "+mTableName;
        mDataBase.execSQL(sql);
    }




}
