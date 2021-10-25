package com.magictool.web.util;

public class ViewConvertUtils {
    /**
     * 转换Email的格式，中间加*
     * 长度如果小于7，则保留前后1个字符
     * 如果长度大于等于7，则保留前后2个字符
     *
     * @param email
     * @return
     */
    public static String hideEmail(String email){
        String[] subs = email.split("@");
        //被隐藏的部分
        String target = subs[0];
        //保留字符串的长度
        int has = target.length() < 7 ? 1 : 2;
        String prefix = target.substring(0, has);
        String suffix = target.substring(target.length()-has);
        String hidden = target.substring(has, target.length()-has);
        return prefix + padStr("*", hidden.length()) + suffix + subs[1];
    }

    /**
     * 产生一个固定重复次数的字符串
     * @param str    目标字符串
     * @param times  重复的次数
     * @return
     */
    public static String padStr(String str, int times){
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < times; i++) {
            buffer.append(str);
        }
        return buffer.toString();
    }
}
