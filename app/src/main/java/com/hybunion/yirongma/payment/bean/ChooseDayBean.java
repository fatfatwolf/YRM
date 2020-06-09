package com.hybunion.yirongma.payment.bean;


public class ChooseDayBean{
    public String day;
    public boolean isChoose;//判断第一个是否被选中
    public boolean isChoose2;//判断第二个是否被选中
    public boolean isClickable;//判断第二个是否不可选 true 表示第一个被选了，第二个不能再选
    public boolean isClickable2;//判断第一个是否不可选
    public String daynum;
}
