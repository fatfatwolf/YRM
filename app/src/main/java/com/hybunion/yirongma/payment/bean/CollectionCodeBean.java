package com.hybunion.yirongma.payment.bean;

import com.hybunion.yirongma.payment.bean.base.BaseBean;

import java.io.Serializable;
import java.util.List;

/**
 * 收款码实体类
 */

public class CollectionCodeBean extends BaseBean<List<CollectionCodeBean.DataBean>> {

    /**
     * msg : 查询成功
     * numberUnits :
     * obj : [{"APPROVEDATE":"2017-05-20 18:37:23","APPROVEUID":544924,"BAPID":22,"MAINTAINDATE":"2017-05-19 17:48:01","MAINTAINUSERID":1,"MID":"864000253114633","QRPWD":"5780","QRTID":"1000000005","QRURL":"http://10.51.130.148:9080/xpay/aggpayment.do?qrtid=1000000004","SCANRATE":0.0039,"SHOPNAME":"我","STATUS":2,"UNNO":"111000","USEDCONFIRMDATE":"2017-05-19 19:29:07"}]
     * sessionExpire : false
     * success : true
     */
    public class DataBean{
        public String cloudId, hornStatus, tidName, tid,tidUrl,type,source;
        public String secretKey,sn,snName,fjStatus; // 款台使用
    }

    private String msg;
    public String count;
    private String numberUnits;
    private boolean sessionExpire;
    private boolean success;
    private List<ObjBean> obj;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getNumberUnits() {
        return numberUnits;
    }

    public void setNumberUnits(String numberUnits) {
        this.numberUnits = numberUnits;
    }

    public boolean isSessionExpire() {
        return sessionExpire;
    }

    public void setSessionExpire(boolean sessionExpire) {
        this.sessionExpire = sessionExpire;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public List<ObjBean> getObj() {
        return obj;
    }


    public void setObj(List<ObjBean> obj) {
        this.obj = obj;
    }

    public static class ObjBean implements Serializable{
        /**
         * APPROVEDATE : 2017-05-20 18:37:23
         * APPROVEUID : 544924
         * BAPID : 22
         * MAINTAINDATE : 2017-05-19 17:48:01
         * MAINTAINUSERID : 1
         * MID : 864000253114633
         * QRPWD : 5780
         * QRTID : 1000000005
         * QRURL : http://10.51.130.148:9080/xpay/aggpayment.do?qrtid=1000000004
         * SCANRATE : 0.0039
         * SHOPNAME : 我
         * STATUS : 2
         * UNNO : 111000
         * USEDCONFIRMDATE : 2017-05-19 19:29:07
         */

        private String APPROVEDATE;
        private int APPROVEUID;
        private int BAPID;
        private String MAINTAINDATE;
        private int MAINTAINUSERID;
        private String MID;
        private String RNAME;
        private String bindDate;//绑定时间
        private String QRPWD;
        private String deviceCom;//设备厂商
        private String deviceModel;//设备型号
        private String deviceNo;//设备编号
        private String isDeposit;//是否是押金设备(Y：是；N或其他：否；)
        private String isReach;//是否达标（Y-达标； N-未达标）
        private String isSettle;//是否结算（Y-已结算； N-未结算）
        private String payDeposit;//是否激活（（Y-是； N-否））
        private String quickpayFlg;//是否支持非接支付：0-不支持，1-已开通 2 -待审核
        private String QRTID;
        private String deviceUrl;//设备图片
        private String QRURL;//二维码连接
        private String SCANRATE;
        private String SHOPNAME;
        private int STATUS;
        private String UNNO;
        private String USEDCONFIRMDATE;
        private String timeDiff;//时间差
        private String rebateDays;//活动时长
        private String deviceText;//文字说明
        private String reachTransAmount;//进度条显示的达标金额20000

        public String getReachTransAmount() {
            return reachTransAmount;
        }

        public void setReachTransAmount(String reachTransAmount) {
            this.reachTransAmount = reachTransAmount;
        }

        public String getDeviceText() {
            return deviceText;
        }

        public void setDeviceText(String deviceText) {
            this.deviceText = deviceText;
        }

        public String getTransAmount() {
            return transAmount;
        }

        public void setTransAmount(String transAmount) {
            this.transAmount = transAmount;
        }

        public String getIsFailure() {
            return isFailure;
        }

        public void setIsFailure(String isFailure) {
            this.isFailure = isFailure;
        }

        private String transAmount;//交易金额
        private String isFailure;//Y:失效


        public String getRebateDays() {
            return rebateDays;
        }

        public void setRebateDays(String rebateDays) {
            this.rebateDays = rebateDays;
        }

        public String getTimeDiff() {
            return timeDiff;
        }

        public void setTimeDiff(String timeDiff) {
            this.timeDiff = timeDiff;
        }

        public String getAPPROVEDATE() {
            return APPROVEDATE;
        }

        public void setAPPROVEDATE(String APPROVEDATE) {
            this.APPROVEDATE = APPROVEDATE;
        }

        public int getAPPROVEUID() {
            return APPROVEUID;
        }

        public void setAPPROVEUID(int APPROVEUID) {
            this.APPROVEUID = APPROVEUID;
        }

        public int getBAPID() {
            return BAPID;
        }

        public String getDeviceUrl() {
            return deviceUrl;
        }

        public void setDeviceUrl(String deviceUrl) {
            this.deviceUrl = deviceUrl;
        }

        public void setBAPID(int BAPID) {
            this.BAPID = BAPID;
        }

        public String getMAINTAINDATE() {
            return MAINTAINDATE;
        }

        public void setMAINTAINDATE(String MAINTAINDATE) {
            this.MAINTAINDATE = MAINTAINDATE;
        }

        public int getMAINTAINUSERID() {
            return MAINTAINUSERID;
        }

        public void setMAINTAINUSERID(int MAINTAINUSERID) {
            this.MAINTAINUSERID = MAINTAINUSERID;
        }

        public String getMID() {
            return MID;
        }

        public void setMID(String MID) {
            this.MID = MID;
        }

        public String getQRPWD() {
            return QRPWD;
        }

        public void setQRPWD(String QRPWD) {
            this.QRPWD = QRPWD;
        }

        public String getQRTID() {
            return QRTID;
        }

        public void setQRTID(String QRTID) {
            this.QRTID = QRTID;
        }

        public String getQRURL() {
            return QRURL;
        }

        public void setQRURL(String QRURL) {
            this.QRURL = QRURL;
        }

        public String getSCANRATE() {
            return SCANRATE;
        }

        public void setSCANRATE(String SCANRATE) {
            this.SCANRATE = SCANRATE;
        }

        public String getSHOPNAME() {
            return SHOPNAME;
        }

        public void setSHOPNAME(String SHOPNAME) {
            this.SHOPNAME = SHOPNAME;
        }

        public int getSTATUS() {
            return STATUS;
        }

        public void setSTATUS(int STATUS) {
            this.STATUS = STATUS;
        }

        public String getUNNO() {
            return UNNO;
        }

        public void setUNNO(String UNNO) {
            this.UNNO = UNNO;
        }

        public String getUSEDCONFIRMDATE() {
            return USEDCONFIRMDATE;
        }

        public void setUSEDCONFIRMDATE(String USEDCONFIRMDATE) {
            this.USEDCONFIRMDATE = USEDCONFIRMDATE;
        }

        public String getRNAME() {
            return RNAME;
        }

        public void setRNAME(String RNAME) {
            this.RNAME = RNAME;
        }

        public String getBindDate() {
            return bindDate;
        }

        public void setBindDate(String bindDate) {
            this.bindDate = bindDate;
        }

        public String getDeviceCom() {
            return deviceCom;
        }

        public void setDeviceCom(String deviceCom) {
            this.deviceCom = deviceCom;
        }

        public String getDeviceModel() {
            return deviceModel;
        }

        public void setDeviceModel(String deviceModel) {
            this.deviceModel = deviceModel;
        }

        public String getDeviceNo() {
            return deviceNo;
        }

        public void setDeviceNo(String deviceNo) {
            this.deviceNo = deviceNo;
        }

        public String getIsDeposit() {
            return isDeposit;
        }

        public void setIsDeposit(String isDeposit) {
            this.isDeposit = isDeposit;
        }

        public String getIsReach() {
            return isReach;
        }

        public void setIsReach(String isReach) {
            this.isReach = isReach;
        }

        public String getIsSettle() {
            return isSettle;
        }

        public void setIsSettle(String isSettle) {
            this.isSettle = isSettle;
        }

        public String getPayDeposit() {
            return payDeposit;
        }

        public void setPayDeposit(String payDeposit) {
            this.payDeposit = payDeposit;
        }

        public String getQuickpayFlg() {
            return quickpayFlg;
        }

        public void setQuickpayFlg(String quickpayFlg) {
            this.quickpayFlg = quickpayFlg;
        }
    }
}
