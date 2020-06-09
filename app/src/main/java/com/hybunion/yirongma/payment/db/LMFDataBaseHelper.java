package com.hybunion.yirongma.payment.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.hybunion.yirongma.HRTApplication;


public class LMFDataBaseHelper extends SQLiteOpenHelper {


    public LMFDataBaseHelper(Context context, String name, int version) {
        super(context, name, null, version);
    }

    // 表的字段有：
    // _id
    // UUID 后台请求的数据都有值，推送的数据没有值，可以用来区分是否是推送数据
    // transAmount  交易金额，不能为null
    // transTime  交易时间，格式为：yyyy/MM/dd HH:mm:ss
    // transTimeLong  将 transTime 转成了 long 类型，用来比较时间前后
    // payChannel  交易状态 如：支付成功 退款中 等
    // orderNo  订单号，不能重复
    // tidName  收款码名称
    // isAmount 筛选 “<300” ，isAmount = "1" ; 筛选 “>=300” ，isAmount = "2"
    // tId  收款码 id
    // sourceTid 收银插件 id
    // transType  如果是退款 transType = “3”
    // storeId  门店 id
    // kuanTaiId  款台 id
    // iconUrl 列表图标的 url
    // payableAmount 实际支付金额

    // 惠储值列表用：
    // isHuiChuZhi  惠储值数据标识    isHuiChuZhi="Y" 表示是惠储值数据
    // orderType   订单类型 0：购卡；1：充值；2：消费

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sqlMsg = "create table billing_data (_id integer primary key autoincrement, UUID varchar," +
                "transAmount varchar not null, transTime varchar(20), transTimeLong varchar, " +
                "payType varchar, payChannel varchar, orderNo varchar(4) unique,tidName varchar, " +
                "isAmount varchar, tId varchar, sourceTid varchar, transType varchar, storeId varchar, " +
                "kuanTaiId varchar, iconUrl varchar, payableAmount varchar,isHuiChuZhi varchar(2), orderType varchar)";
        db.execSQL(sqlMsg);
    }

    boolean isColumnExists = false;

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.i("xjz", "oldVersion:" + oldVersion + "        newVersion" + newVersion);
        if (oldVersion<2){  // 增加了 payableAmount 字段，oldViersion=1 需要更新
            String querySql = "select payableAmount from billing_data";
            try {
                Cursor cursor = db.rawQuery(querySql, null);
                if (cursor != null) {
                    while (cursor.moveToNext()) {
                        isColumnExists = true;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (!isColumnExists) {
                    String insertSql = "alter table billing_data add payableAmount varchar(20)";
                    db.execSQL(insertSql);
                }
            }
        }
        if (oldVersion<3){  // 增加了 orderType、isHuiChuZhi 两个字段，oldVersion=2 需要更新
            String sql = "alter table billing_data add isHuiChuZhi varchar(2)";
            db.execSQL(sql);
            String sql2 = "alter table billing_data add orderType varchar(2)";
            db.execSQL(sql2);
        }


    }
}
