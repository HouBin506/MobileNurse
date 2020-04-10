package com.herenit.mobilenurse.demo;

import com.herenit.mobilenurse.app.utils.StringUtils;

/**
 * author: HouBin
 * date: 2019/3/14 14:22
 * desc:
 */
public class CommonTest {

    public static void main(String[] args) {
        String str1 = "339.996327386%";
        String str2 = "330.526327386%";
        String str3 = "330.626327386%";
        String str4 = "330.496327386%";
        System.out.println(str1+" ---> "+ StringUtils.getIntegerNumFromPercent(str1));
        System.out.println(str2+" ---> "+ StringUtils.getIntegerNumFromPercent(str2));
        System.out.println(str3+" ---> "+ StringUtils.getIntegerNumFromPercent(str3));
        System.out.println(str4+" ---> "+ StringUtils.getIntegerNumFromPercent(str4));
    }
}
