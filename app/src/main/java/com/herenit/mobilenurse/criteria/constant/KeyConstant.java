package com.herenit.mobilenurse.criteria.constant;

/**
 * author: HouBin
 * date: 2019/1/15 13:57
 * desc: 用来存放一些参数名或者key的字段名,或者是Extra的name等
 */
public class KeyConstant {
    /*****************常用的*******************/
    public static final String NAME_EXTRA_URL = "url";
    public static final String NAME_EXTRA_HTML = "html";
    public static final String NAME_EXTRA_COMMON_BOOLEAN = "common_boolean";
    //页面类型
    public static final String NAME_EXTRA_PAGE_TYPE = "pageType";
    //媒体类型
    public static final String NAME_EXTRA_MEDIA_TYPE = "mediaType";
    //操作类型
    public static final String NAME_EXTRA_OPERATION_TYPE = "operationType";
    //已选择的项目列表
    public static final String NAME_EXTRA_SELECTED_ITEM_LIST = "selected_item_list";
    //是否登录
    public static final String NAME_EXTRA_IS_LOGIN = "isLogin";
    //模块Id
    public static final String NAME_EXTRA_MODULE_ID = "moduleId";
    //文档Id
    public static final String NAME_EXTRA_DOC_ID = "docId";
    //病床
    public static final String NAME_EXTRA_SICKBED = "sickbed";
    public static final String NAME_EXTRA_EXAM_REPORT = "exam_report";
    public static final String NAME_EXTRA_LAB_REPORT = "lab_report";
    public static final String NAME_EXTRA_ORDER_LIST = "orderList";
    public static final String NAME_EXTRA_EXECUTE_LIST = "executeList";
    public static final String NAME_EXTRA_OPERATION_SCHEDULED = "operationScheduled";
    /**
     * 体征项目列表
     */
    public static final String NAME_EXTRA_VITAL_ITEM_LIST = "vitalItemList";
    //表示界面UI的体征项
    public static final String NAME_EXTRA_VITAL_SIGNS_VIEW = "vitalSignsView";
    //表示体征数据的体征项
    public static final String NAME_EXTRA_VITAL_SIGHS_DATA = "vitalSignsData";
    //体征项目ID
    public static final String NAME_EXTRA_VITAL_ITEM_ID = "VitalItemID";

    /*************************************DataStore****************************************************************/
    /* ---------------------------SharePreferences----------------------------*/
    //SharePreferences存储名 config
    public static final String NAME_SP_CONFIG = "config";
    public static final String NAME_SP_USER = "user";
    //SharePreferences存储config下的是否记住密码
    public static final String KEY_SP_CONFIG_REMEMBER_PASSWORD = "rememberPassword";
    public static final String KEY_SP_CONFIG_COOKIE = "Cookie";
    public static final String KEY_SP_CONFIG_CAMERA_SCAN = "cameraScan";
    public static final String KEY_SP_CONFIG_SERVICE_ADDRESS = "serviceAddress";

    public static final String KEY_SP_SUFFIX_USER_GROUP_CODE = "_groupCode";//存储User当前groupCode的key的后缀，前面拼接对应的userId
    /* ---------------------------cache----------------------------*/
    public static final String KEY_PROVIDER_USER = "USER";
    public static final String KEY_PROVIDER_LOGIN = "LOGIN";
    public static final String KEY_PROVIDER_SICKBED = "SICKBED";
    public static final String KEY_PROVIDER_PATIENT = "PATIENT";
    public static final String KEY_PROVIDER_ORDERS = "ORDERS";
    /******************************JS Interface***********************************/

    public static final String NAME_JS_INTERFACE_COMMON = "common_js_interface";

}
