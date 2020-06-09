package com.hybunion.yirongma.payment.view;

/**
 * Created by admin on 2017/11/24.
 */

import android.text.InputFilter;
import android.text.Spanned;

/**
 * EditText最大长度过滤
 * Created by vendor on 2017/5/22.
 */
public class MaxLenghtInputFilter implements InputFilter {

    private int mMaxLenght = 255;

    public MaxLenghtInputFilter(int maxLenght){
        mMaxLenght = maxLenght;
    }

    @Override


    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        int dindex = 0;
        int count = 0;
        //判断原有字符
        while (count <= mMaxLenght && dindex < dest.length()) {
            char c = dest.charAt(dindex++);
            if (c < 128) {
                count = count + 1;
            } else {
                count = count + 2;
            }
        }

        if (count > mMaxLenght) {
            return dest.subSequence(0, dindex - 1);
        }
       //判断插入字符
        int sindex = 0;
        while (count <= mMaxLenght && sindex < source.length()) {
            char c = source.charAt(sindex++);
            if (c < 128) {
                count = count + 1;
            } else {
                count = count + 2;
            }
        }

        if (count > mMaxLenght) {
            sindex--;
        }

        return source.subSequence(0, sindex);
    }
}
