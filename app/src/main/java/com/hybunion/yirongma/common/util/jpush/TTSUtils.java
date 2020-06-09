package com.hybunion.yirongma.common.util.jpush;

import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author pengyunlong
 * @Description 解析TTS音频资源并播放 线程安全
 * @Date 2017/10/25 11:59
 */
public class TTSUtils {
    private static ThreadLocal<Looper> looperThreadLocal;
    private volatile static TTSUtils ttsUtils;
    //数字位
    public static String[] chnNumChar = {"零","一","二","三","四","五","六","七","八","九"};
    //节权位
    public static String[] chnUnitSection = {"","万","亿","万亿"};
    //权位
    public static String[] chnUnitChar = {"","十","百","千"};

    private TTSUtils(){
    }
    public static TTSUtils getInstance(){
        if (ttsUtils ==null){
            synchronized (TTSUtils.class){
                if (ttsUtils==null){
                    ttsUtils = new TTSUtils();
                    ttsUtils.init();
                }
            }
        }
        return ttsUtils;
    }
    private void init(){
        looperThreadLocal = new ThreadLocal<>();
    }
    /**
     * @Description 返回声音的播放顺序Id
     * @Date 2017/10/25 21:13
     */
    public  List<String> getChs(String num) {
        char[] chArray = formatDecimal(num).toCharArray();
        List<String> result = new ArrayList<>();
        boolean hasLooper = getLooper()!=null;
        if (hasLooper)getLooper().onPreLoop();
        for (char ch:chArray) {
            result.add(String.valueOf(ch));
            if (hasLooper) getLooper().onLoop(String.valueOf(ch));
        }
        if (hasLooper)getLooper().onPostLoop();
        return result;
    }

    /**
     * @Description 将数字转换成汉字数字
     * @Date 2017/10/25 21:13
     */
    public String formatDecimal(String decimals) {
        String intPart = decimals;
        int decIndex = decimals.indexOf(".");
        String result = "";
        if (decIndex!=-1){
            intPart = decimals.substring(0, decIndex);
            result = formatInteger(intPart);
            String fractionPart = decimals.substring(decIndex + 1);
            result +=formatFractionalPart(fractionPart);
        }else {
            result = formatInteger(intPart);
        }
        return result;
    }

    /**
     * @Description 格式化小数部分的数字
     * @Date 2017/10/25 21:13
     */
    private  String formatFractionalPart(String fractPart) {
        if (!TextUtils.isEmpty(fractPart)){
            int index;
            for (index=fractPart.length()-1;index>0;index--){

            }


        }

        if ("00".equals(fractPart) || "0".equals(fractPart)){  // 如果小数点后面是 0 ，就舍去，不读
            return "";
        }

        StringBuilder sb = new StringBuilder();
        char[] val = fractPart.toCharArray();
        int len = val.length;
        for (int i = 0; i < len; i++) {
            int n = Integer.valueOf(val[i] + "");
            sb.append(chnNumChar[n]);
        }
        return "."+sb.toString();
    }

    /**
     * @Description 格式化数字部分
     * @Date 2017/10/25 21:13
     */
    private  String formatInteger(String intPart){
        int num = Integer.valueOf(intPart);
        if(num == 0){
            return "零";
        }
        int unitPos = 0;//节权位标识
        String All = new String();
        String chineseNum = new String();//中文数字字符串
        boolean needZero = false;//下一小结是否需要补零
        String strIns = new String();
        while(num>0){
            int section = num%10000;//取最后面的那一个小节
            if(needZero){//判断上一小节千位是否为零，为零就要加上零
                All = chnNumChar[0] + All;
            }
            chineseNum = sectionTOChinese(section,chineseNum);//处理当前小节的数字,然后用chineseNum记录当前小节数字
            if( section!=0 ){//此处用if else 选择语句来执行加节权位
                strIns = chnUnitSection[unitPos];//当小节不为0，就加上节权位
                chineseNum = chineseNum + strIns;
            }else{
                strIns = chnUnitSection[0];//否则不用加
                chineseNum = strIns + chineseNum;
            }
            All = chineseNum+All;
            chineseNum = "";
            needZero = (section<1000) && (section>0);
            num = num/10000;
            unitPos++;
        }
        return All;
    }

    private  String sectionTOChinese(int section, String chineseNum){
        String setionChinese = new String();//小节部分用独立函数操作
        int unitPos = 0;//小节内部的权值计数器
        boolean zero = true;//小节内部的制零判断，每个小节内只能出现一个零
        while(section>0){
            int v = section%10;//取当前最末位的值
            if(v == 0){
                if( !zero ){
                    zero = true;//需要补零的操作，确保对连续多个零只是输出一个
                    chineseNum = chnNumChar[0] + chineseNum;
                }
            }else{
                zero = false;//有非零的数字，就把制零开关打开
                setionChinese = chnNumChar[v];//对应中文数字位
                setionChinese = setionChinese + chnUnitChar[unitPos];//对应中文权位
                chineseNum = setionChinese + chineseNum;
            }
            unitPos++;
            section = section/10;
        }
        return chineseNum;
    }
    /**
     * @Description 监听器,可以增加额外的监听方法
     * @Date 2017/10/26 9:17
     */
    public interface Looper{
        void onLoop(String ch);
        void onPreLoop();
        void onPostLoop();
    }
    public TTSUtils setLooper(Looper looper){
        if (looperThreadLocal == null)
            init();
        looperThreadLocal.set(looper);
        return this;
    }
    public Looper getLooper(){
        if (looperThreadLocal == null)
            init();
        return looperThreadLocal.get();
    }

    public static void main(String[] args) {
        TTSUtils.getInstance().setLooper(new Looper() {
            @Override
            public void onLoop(String ch) {
                System.out.println(ch);
            }

            @Override
            public void onPreLoop() {

            }

            @Override
            public void onPostLoop() {
            }
        }).getChs("1212.121");

    }

}
