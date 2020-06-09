package com.hybunion.yirongma.payment.view;

/**
 * Created by admin on 2017/11/21.
 */

import android.os.Build;
import android.text.InputFilter;
import android.text.Spanned;

/**
 * EditText名字过滤
 * Created by vendor on 2017/5/22.
 */
public class NameInputFilter implements InputFilter {
    private CharSequence replace(CharSequence source, int position) {
        int lenght = source.length();
        for (int i = position; i < lenght; i++) {
            char c = source.charAt(i);
            int index =(int)c;
            if(c == '(' || c == ')'|| c=='（' || c=='）' || c=='.' || c=='*'){
                continue;
            } else if((index >= 48 && index <= 57) || (index >= 65 && index <= 90) || (index >= 97 && index <= 122)){ //数字 字母 大写字母
                continue;
            } else if (index > 126) {  //部分机型无法兼容后续字符判断 所以直接强制限死(小于等于126的允许字符上面两个判断已经兼容)
                Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);  //判断中文字符
                if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
                        || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                        || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
                        || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_B) {
                    continue;
                }

                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    if(ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_C
                            || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_D) {
                        continue;
                    }
                }

//            if (ub == Character.UnicodeBlock.GENERAL_PUNCTUATION  //判断中文符号
//                    || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
//                    || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS
//                    || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_FORMS
//                    || ub == Character.UnicodeBlock.VERTICAL_FORMS) {//jdk1.7
//                return replace(source.subSequence(i, i + 1), i);  //递归找寻
//            }
            }

            //以下代码为不符合条件的字符的处理
            if(lenght == 1){
                return source.subSequence(0, 0);  //返回一个空的回去
            }

            StringBuilder sb = new StringBuilder(source);
            sb.delete(i, i + 1);
            return replace(sb, i);  //递归找寻
        }

        return source;
    }

    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        return replace(source, 0);
    }
}