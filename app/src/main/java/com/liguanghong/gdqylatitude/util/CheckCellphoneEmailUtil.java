package com.liguanghong.gdqylatitude.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CheckCellphoneEmailUtil {

    /**
      * 验证手机号码
      *
      * 移动号码段:139、138、137、136、135、134、147、150、151、152、157、158、159、178、182、183、184、187、188
      * 联通号码段:130、131、132、156、185、186、145、176
      * 电信号码段:133、153、177、180、181、189
      *
      * @param cellphone
      * @return
      */
    public static boolean checkCellphone(String cellphone) {
        String regex = "^((13[0-9])|(14[5|7])|(15([0-3]|[5-9]))|(17[6-8])|(18[0-9]))\\d{8}$";
        Pattern pattern=Pattern.compile(regex);
        Matcher matcher=pattern.matcher(cellphone);
        return matcher.matches();
    }

    /**
     * 验证邮箱
     *
     * @param email
     * @return
     */
    public static boolean checkEmail(String email) {
        String regex = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
        Pattern pattern=Pattern.compile(regex);
        Matcher matcher=pattern.matcher(email);
        return matcher.matches();
    }
}
