package com.herenit.mobilenurse.criteria.common;

import android.text.TextUtils;

import com.herenit.mobilenurse.app.utils.StringUtils;

/**
 * author: HouBin
 * date: 2019/5/24 17:07
 * desc: 扫描结果，对扫描结果进行封装
 */
public class ScanResult {
    private final String SEPARATOR = "|";//字符串分隔符
    private final String SEPARATOR_REGEX = "\\|";//分隔符的正则表达式，用于字符串切割

    private final String PREFIX_PATIENT = "01";//患者腕带前缀
    private final String[] PREFIX_ORDER = {"02", "03", "04", "05"};//医嘱瓶贴前缀
    private final String PREFIX_MONITOR = "07";//监护仪条码前缀
    private final String PREFIX_EMP_CARD = "LH_CONF:";//员工工牌，浙江省人民员工工牌二维码 如：LH_CONF:{MAIN_DISP:省人民;S_DISP:林青青;S_STR:3474}

    public static final int CODE_TYPE_EMP_CARD = 205;//条码类型--员工牌
    public static final int CODE_TYPE_PATIENT = 206;//条码类型--患者腕带
    public static final int CODE_TYPE_ORDER = 207;//条码类型--医嘱
    public static final int CODE_TYPE_MONITOR = 208;//条码类型--监护仪
    public static final int CODE_TYPE_UNKNOWN = 400;//条码类型--未知条码

    private int codeType = CODE_TYPE_UNKNOWN;

    /**
     * 扫描到的条码（二维码）内容
     */
    private String content;

    /**
     * 前缀
     */
    private String prefix;
    /**
     * 后缀
     */
    private String suffix;

    private String patientId;
    private String visitId;
    /**
     * 医嘱编号
     */
    private String orderNo;
    /**
     * 登录名，省人民工牌具有
     */
    private String loginName;
    /**
     * 组织名称，浙江省人民工牌内容
     */
    private String mainDisp;
    /**
     * 姓名，浙江省人民工牌内容
     */
    private String sDisp;

    /**
     * 监护仪码
     */
    private String monitorCode;

    public ScanResult(String content) {
        content = StringUtils.specialUnicode(content);
        this.content = content;
        if (!TextUtils.isEmpty(content) && content.startsWith(PREFIX_EMP_CARD) && content.length() > PREFIX_EMP_CARD.length()) {//员工工牌
            String cardInfo = content.substring(PREFIX_EMP_CARD.length() + 1, content.length() - 1);
            if (TextUtils.isEmpty(cardInfo) || !cardInfo.contains(";")) {
                codeType = CODE_TYPE_UNKNOWN;
                return;
            }
            String[] cardArr = cardInfo.split(";");
            if (cardArr == null || cardArr.length != 3) {
                codeType = CODE_TYPE_UNKNOWN;
                return;
            }
            codeType = CODE_TYPE_EMP_CARD;
            for (int x = 0; x < cardArr.length; x++) {
                String cardItem = cardArr[x];
                if (TextUtils.isEmpty(cardItem) || !cardItem.contains(":")) {
                    codeType = CODE_TYPE_UNKNOWN;
                    return;
                }
                String[] cardItemArr = cardItem.split("\\:");
                if (cardItemArr == null || cardItemArr.length != 2) {
                    codeType = CODE_TYPE_UNKNOWN;
                    return;
                }
                String cardItemValue = cardItemArr[1];
                if (x == 0)
                    mainDisp = cardItemValue;
                else if (x == 1)
                    sDisp = cardItemValue;
                else
                    loginName = cardItemValue;
            }
        } else if (!TextUtils.isEmpty(content) && content.contains(SEPARATOR)) {//规则的医嘱、腕带码
            String[] contentArr = content.split(SEPARATOR_REGEX);
            if (contentArr == null || contentArr.length != 2) {
                codeType = CODE_TYPE_UNKNOWN;
                return;
            }

            prefix = contentArr[0];//根据前缀判断是腕带还是医嘱
            suffix = contentArr[1];//二维码内容后缀
            if (TextUtils.isEmpty(prefix)) {
                codeType = CODE_TYPE_UNKNOWN;
                return;
            }
            codeType = CODE_TYPE_UNKNOWN;
            if (prefix.equals(PREFIX_PATIENT)) {//患者腕带二维码
                codeType = CODE_TYPE_PATIENT;
                patientId = contentArr[1];
            } else if (StringUtils.contains(PREFIX_ORDER, prefix)) {//来自护士站的医嘱二维码
                codeType = CODE_TYPE_ORDER;
                orderNo = contentArr[1];
            } else if (prefix.equals(PREFIX_MONITOR)) {//监护仪码
                codeType = CODE_TYPE_MONITOR;
                monitorCode = suffix;
            }
        } else if (!TextUtils.isEmpty(content) && StringUtils.isNumeric(content)) {//省人民的检验医嘱没有规则，就是一串数字
            prefix = "";
            suffix = content;
            codeType = CODE_TYPE_ORDER;
            orderNo = content;
        } else {
            codeType = CODE_TYPE_UNKNOWN;
        }
    }

    public int getCodeType() {
        return codeType;
    }

    public String getPatientId() {
        return patientId;
    }

    public String getVisitId() {
        return visitId;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public String getLoginName() {
        return loginName;
    }

    public String getMainDisp() {
        return mainDisp;
    }

    public String getsDisp() {
        return sDisp;
    }

    public String getContent() {
        return content;
    }

    public String getPrefix() {
        return prefix;
    }

    public String getSuffix() {
        return suffix;
    }

    public String getMonitorCode() {
        return monitorCode;
    }
}
