package com.herenit.mobilenurse.criteria.constant;

import android.os.Environment;

/**
 * author: HouBin
 * date: 2019/1/23 16:35
 * desc: 常用的、通用的一些常量，键值对的除外
 */
public class CommonConstant {

    public static final String PATH_SEPARATOR = "/";

    /************************************MediaType***********************************************/

    public static final String MEDIA_TYPE_URL = "url";
    public static final String MEDIA_TYPE_TEXT_HTML = "text/html";


    /************************************常用数据类型***********************************************/
    public static final String DATA_TYPE_NAME_BOOLEAN = "Boolean";
    public static final String DATA_TYPE_NAME_INTEGER = "Integer";
    public static final String DATA_TYPE_NAME_NULL = "Null";//空
    public static final String DATA_TYPE_NAME_NUMBER = "Number";//数字
    public static final String DATA_TYPE_NAME_DATE = "Date";//日期
    public static final String DATA_TYPE_NAME_CHARACTER = "Character";//字符
    public static final String DATA_TYPE_NAME_PERCENT = "Percent";//百分数
    /*********************************************************文件类型********************************/
    public static final String FILE_TYPE_NAME_PDF = "PDF";//PDF文件
    /*****************************常用控件类型**************************************************/
    public static final String VIEW_TYPE_CALENDAR = "Calendar";//日历
    public static final String VIEW_TYPE_TEXT_VIEW = "TextView";//文本控件
    public static final String VIEW_TYPE_CHECK_BOX = "CheckBox";//单选框
    public static final String VIEW_TYPE_SINGLE_INPUT_DIALOG = "SingleInputDialog";//单输入框的弹窗
    public static final String VIEW_TYPE_DOUBLE_INPUT_DIALOG = "DoubleInputDialog";//双输入框的弹窗
    public static final String VIEW_TYPE_LIST_DIALOG = "ListDialog";//弹窗列表
    public static final String VIEW_TYPE_SEEK_BAR_DIALOG = "SeekBarDialog";//普通的可拖动的进度条弹窗
    public static final String VIEW_TYPE_MN_SINGLE_INPUT_VIEW = "MNSingleInputView";//单输入框，MobileNurse特有的
    public static final String VIEW_TYPE_MN_DOUBLE_INPUT_VIEW = "MNDoubleInputView";//双输入框，MobileNurse特有的
    public static final String VIEW_TYPE_MN_LIST_POPUP_WINDOW = "MNListPopupWindow";//下拉列表（带有前后缀的）
    public static final String VIEW_TYPE_MN_SINGLE_INPUT_DIALOG = "MNSingleInputDialog";//下拉列表（带有前后缀的）
    public static final String VIEW_TYPE_LIST_POPUP_WINDOW = "ListPopupWindow";//下拉列表
    public static final String VIEW_TYPE_BP_VIEW_FOR_LX = "BPViewForLX";//临夏州的血压录入控件

    /**************************************网络连接类型***********************************************************/
    public static final int NETWORK_CONNECT_TYPE_WIFI = 8001;//Wifi网络
    public static final int NETWORK_CONNECT_TYPE_MOBILE = 8002;//移动网络
    public static final int NETWORK_CONNECT_TYPE_2G = 8003;//移动网络
    public static final int NETWORK_CONNECT_TYPE_3G = 8004;//移动网络
    public static final int NETWORK_CONNECT_TYPE_4G = 8005;//移动网络
    public static final int NETWORK_CONNECT_TYPE_5G = 8006;//移动网络
    public static final int NETWORK_CONNECT_TYPE_NONE = 8100;//无网络连接
    //时间误差范围
    public static final int TIME_ERROR_RANGE = 3;
    public static final int REFRESH_FAIL_TIMEOUT_MILLISECOND = 6 * 1000;//刷新失败毫秒数，当刷新时间大于等于6秒，则视为刷新失败，停止刷新

    /****************************************常用条件******************************************/
    public static final String CONDITION_TYPE_ALL_OR_NEW = "conditionType_allOrNew";//条件类型：所有/新的
    public static final String CONDITION_TYPE_OPERATION_TIME = "conditionType_operationTime";//手术时间
    public static final String CONDITION_TYPE_PATIENT_CONDITION = "conditionType_patientCondition";//病情
    public static final String CONDITION_TYPE_NURSING_CLASS = "conditionType_nursingClass";//护理等级

    public static final String CONDITION_TYPE_ALL = "conditionType_all";//所有
    public static final String CONDITION_TYPE_MATERNAL_AND_INFANTS = "conditionType_maternalAndInfants";//母婴，用来筛选是母亲的医嘱还是婴儿的医嘱
    public static final String CONDITION_TYPE_ORDER_CLASS = "conditionType_orderClass";//医嘱分类
    public static final String CONDITION_TYPE_EXECUTE_TIME = "conditionType_executeTime";//执行时间
    public static final String CONDITION_TYPE_EXECUTE_RESULT = "conditionType_executeResult";//执行结果
    public static final String CONDITION_TYPE_REPEAT_INDICATOR = "conditionType_repeatIndicator";//长期临时
    public static final String CONDITION_TYPE_ORDER_PERFORM_LABEL = "conditionType_orderPerformLabel";//执行单类别

    /******************************************常用时间描述************************************************/
    public static final String TIME_CODE_IN_A_MONTH = "InAMonth";//一个月内
    public static final String TIME_CODE_IN_A_WEEK = "InAWeek";//一周内
    public static final String TIME_CODE_IN_THREE_DAYS = "InThreeDays";//三天内
    public static final String TIME_CODE_TWO_DAYS_LATER = "TwoDaysLater";//后天
    public static final String TIME_CODE_TOMORROW = "Tomorrow";//明天
    public static final String TIME_CODE_TODAY = "Today";//今天
    public static final String TIME_CODE_YESTERDAY = "Yesterday";//昨天
    public static final String TIME_CODE_IN_PAST_THREE_DAYS = "InPastThreeDays";//过去的三天内
    public static final String TIME_CODE_IN_PAST_WEEK = "InPastWeek";//过去的一周内
    public static final String TIME_CODE_IN_PAST_MONTH = "InPastMonth";//过去的一个月内
    public static final String TIME_CODE_IN_PAST_THREE_MONTH = "InPastThreeMonth";//过去的三个月内

    /*******************************************EventBus常用意图*************************************************/
    public static final String EVENT_INTENTION_REMOVE_USER = "remove_user";
    public static final String EVENT_INTENTION_CONDITION_CHANGED = "condition_changed";
    public static final String EVENT_INTENTION_PATIENT_CHANGED = "patient_changed";
    public static final String EVENT_INTENTION_UPDATE_CONDITION = "update_condition";//更新条件
    public static final String EVENT_INTENTION_UPDATE_ORDERS = "update_orders";//更新医嘱
    public static final String EVENT_INTENTION_UPDATE_NURSE_ROUND = "update_nurse_round";//更新护理巡视
    public static final String EVENT_INTENTION_UPDATE_PATIENT_INFO = "update_patient_info";//更新患者详情
    public static final String EVENT_INTENTION_UPDATE_MONITOR = "update_monitor";//更新监护仪绑定信息
    public static final String EVENT_INTENTION_INFUSION_ROUND = "infusion_round";//输液巡视
    public static final String EVENT_INTENTION_COMMIT_NURSE_ROUND = "commit_nurse_round";//提交护理巡视
    public static final String EVENT_INTENTION_ON_TITLEBAR_RIGHT_CLICK = "on_titleBar_right_click";//标题栏右侧按钮点击
    public static final String EVENT_INTENTION_REFRESH = "refresh";//刷新
    public static final String EVENT_INTENTION_UPDATE_DATA = "update_data";//跟新数据
    public static final String EVENT_INTENTION_EDITABLE_CHANGED = "editable_changed";//编辑状态发生了改变
    public static final String EVENT_INTENTION_SELECT_ITEM_CHANGED = "select_item_changed";//选择条目发生改变
    public static final String EVENT_INTENTION_EXECUTE_ORDERS = "execute_orders";//执行医嘱
    public static final String EVENT_INTENTION_LOAD_DATA = "load_data";//加载数据
    public static final String EVENT_INTENTION_LOAD_ORDER_LIST = "load_orderList";//加载医嘱列表
    public static final String EVENT_INTENTION_COMMIT_ADMISSION_ASSESSMENT_DATA = "commit_admission_assessment_data";//加载入院评估数据

    /********************************************常用一些id、code、value******************************************************/

    public static final String COMMON_CODE_ALL = "all";
    public static final String COMMON_CODE_NEW = "New";
    public static final String COMMON_VALUE_POSITIVE = "阳性";
    public static final String COMMON_VALUE_NEGATIVE = "阴性";
    public static final String COMMON_FLAG_POSITIVE = "(+)";
    public static final String COMMON_FLAG_NEGATIVE = "(-)";

    public static final String FIELD_NAME_PATIENT_ID = "patientId";
    public static final String FIELD_NAME_VISIT_ID = "visitId";
    public static final String FIELD_NAME_VISIT_NO = "visitNo";
    public static final String FIELD_NAME_GROUP_CODE = "groupCode";
    public static final String FIELD_NAME_RECORD_GROUP_ID = "recordGroupId";
    public static final String FIELD_NAME_TITLE = "title";
    public static final String FIELD_NAME_MESSAGE = "message";
    public static final String FIELD_NAME_HINT = "hint";
    public static final String FIELD_NAME_DATA = "data";
    public static final String FIELD_NAME_DOC_ID = "docId";
    public static final String FIELD_NAME_LOGIN_NAME = "loginName";
    public static final String FIELD_NAME_BAR_CODE = "barCode";
    public static final String FIELD_NAME_TYPE = "type";

    /********************************************ItemType，条目类型******************************************************/
    public static final String ITEM_TYPE_GROUP = "Group";//组，该条Item只代表组的意义，是组标记，无实际数据操作意义
    public static final String ITEM_TYPE_VALUE = "Value";//数值，该条Item只代表数据的意义，是数值标记，只具备数据操作的意义
    public static final String ITEM_TYPE_GROUP_VALUE = "GroupValue";//即表示组，也表示数值，既有租的含义，也具备数据操作的意义
    public static final String ITEM_NAME_INFUSION = "输液";//输液条目
    /******************************************************操作类型**************************************************************/
    public static final int OPERATION_TYPE_PERSIST = 1011;//持久化、保存
    public static final int OPERATION_TYPE_UPDATE = 1012;//更新
    public static final int OPERATION_TYPE_SELECT = 1013;//选择
    public static final int OPERATION_TYPE_QUERY = 1014;//简单的查询
    public static final int OPERATION_TYPE_EXECUTE = 1015;//执行
    public static final int OPERATION_TYPE_VERIFY = 1016;//核对

    /******************************************************requestCode，ResultCode**************************************************************/
    //选择医嘱
    public static final int REQUEST_CODE_SELECT_ORDERS = 20001;
    public static final int RESULT_CODE_SELECT_ORDERS = 20002;
    //医嘱详情界面
    public static final int REQUEST_CODE_ORDER_INFO = 20003;
    public static final int RESULT_CODE_ORDER_INFO = 20004;
    //修改条件
    public static final int REQUEST_CODE_MODIFY_CONDITION = 20011;
    public static final int RESULT_CODE_MODIFY_CONDITION = 20012;

    /********************************************************************DocType，文档类型***********************************************************************/

    public static final String DOC_TYPE_HEALTH_EDU = "健康教育";

    /*********************************************************常用体征项名称******************************************************/

    public static final String VITAL_SIGNS_ADMISSION = "入院";
    public static final String VITAL_SIGNS_BP = "血压";

    /***********************************************************************************************************************/
    //患者切换的方式为左右侧滑、滑动切换
    public static final int SWITCH_TYPE_SCROLL = 0;
    //患者切换的方式为
    public static final int SWITCH_TYPE_SELECT = 1;
    //患者切换方式为扫码切换
    public static final int SWITCH_TYPE_SCAN = 2;
}
