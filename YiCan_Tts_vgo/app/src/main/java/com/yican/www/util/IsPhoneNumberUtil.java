package com.yican.www.util;

import java.util.regex.Pattern;

/**监测是否是手机号
 * Created by Administrator on 2016/9/22.
 */
public class IsPhoneNumberUtil {
    static String PHONE_PATTERN = "^((13[0-9])|(14[5|7])|(15([0-3]|[5-9]))|(17([0,1,6,7,]))|(18[0-2,5-9]))\\d{8}$";

    public static boolean Isphone(String phone) {
        boolean isphone = Pattern.compile(PHONE_PATTERN).matcher(phone).matches();
        return isphone;
    }
}
