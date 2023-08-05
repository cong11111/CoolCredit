package com.tiny.cash.loan.card.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class RegexUtils {

    /**
     * 截止 24/12/19，三大网络运营商在大陆号段分配：
     * 移动：134,135,136,137,138,139,144,147,148,150,151,152,157,158,159,172,178,182,183,184,187,188,198；
     * 联通：130,131,132,145,146,155,156,166,167,175,176,185,186；
     * 电信：133,149,153,173,174,177,180,181,189,191,199;
     * 5G：190、197、196、192
     * 虚拟运营商：170,171;
     *
     * 数据来源工信部网址：http://www.miit.gov.cn/Searchweb/news.jsp (网页中搜索：《电信网码号资源使用证书》颁发结果)
     *
     * 大陆手机号码11位数，再结合以上运营商支持号段，得出匹配格式：前三位固定格式 + 后8位任意数，
     * 此方法中前三位格式有：
     * 13 + (0-9之间任意数)
     * 14 + (4/5/6/7/8/9)
     * 15 + (0-9之间除4之外任意数)
     * 16 + (6/7)
     * 17 + (0-9之间除9之外任意数)
     * 18 + (0-9之间任意数)
     * 19 + (0/1/2/6/7/8/9)
     */
    public static boolean isValidPhone(String str) {
//        String regExp = "^((13[0-9])|(14[4-9])|(15[^4])|(16[6-7])|(17[^9])|(18[0-9])|(19[0|1|2|6|7|8|9]))\\d{8}$";
        String regExp = "^2340\\d{10}$";//（^254\d{9}$)
        String bregExp = "^234\\d{10}$";//（^254\d{9}$)
        Pattern p = Pattern.compile(regExp);
        Matcher m = p.matcher(str.replaceAll(" ",""));
        Pattern p1 = Pattern.compile(bregExp);
        Matcher m1 = p1.matcher(str.replaceAll(" ",""));
        return m.matches()||m1.matches();
    }

    public static boolean isEmail(String email){
        if (null==email || "".equals(email)){
            return false;
        }
        String regEx1 = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
        Pattern p = Pattern.compile(regEx1);
        Matcher m = p.matcher(email);
        if(m.matches()){
            return true;
        }else{
            return false;
        }
    }
    /**
     * 验证码
     * @param str
     * @return
     */
    public static boolean isValidNum(String str) {
        String regExp = "\\d{6}";
        Pattern p = Pattern.compile(regExp);
        Matcher m = p.matcher(str.replaceAll(" ",""));
        return m.matches();
    }
    /**
     * 密码长度
     * @param str
     * @return
     */
    public static boolean isPwd(String str) {
        String regExp = "\\d{4}";
        Pattern p = Pattern.compile(regExp);
        Matcher m = p.matcher(str.replaceAll(" ",""));
        return m.matches();
    }

    public static boolean isValidPhoneThree(String str) {
        String regExp ="^(([7-8]))\\d{9}$";;
        Pattern p = Pattern.compile(regExp);
        Matcher m = p.matcher(str);
        return m.matches();
    }


    public static boolean isValidPwd(String str) {
        String regExp = "(?!^\\d+$)(?!^[a-zA-Z]+$)(?!^[`~!@#$%^&*()\\-_+=\\[{\\]}\\\\|;:'\",<.>/?]+$)[0-9A-Za-z`~!@#$%^&*()\\-_+=\\[{\\]}\\\\|;:'\",<.>/?]{8,16}";
        Pattern p = Pattern.compile(regExp);
        Matcher m = p.matcher(str.replaceAll(" ",""));
        return m.matches();
    }

    /**
     * 以1开头的11位数字
     * @param str
     * @return
     */
    public static boolean isSimplePhone(String str) {
        String regExp = "^(1)\\d{10}$";
        Pattern p = Pattern.compile(regExp);
        Matcher m = p.matcher(str.replaceAll(" ",""));
        return m.matches();
    }

}
