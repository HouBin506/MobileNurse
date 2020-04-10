package com.herenit.mobilenurse.criteria.enums;

import com.herenit.mobilenurse.criteria.constant.CommonConstant;
import com.herenit.mobilenurse.criteria.entity.Order;

/**
 * author: HouBin
 * date: 2019/3/8 15:08
 * desc: 执行结果枚举
 */
public enum ExecuteResultEnum {
    NONEXECUTION(0, "未执行"),
    EXECUTED(1, "执行"),
    PART_OF_EXECUTED(2, "部分执行"),
    EXECUTE_PAUSE(3, "执行暂停"),
    EXECUTE_STOP(4, "执行中断"),
    NOT_VERIFY(2, "未核对"),

    SKIN_TEST_POSITIVE(1, CommonConstant.COMMON_VALUE_POSITIVE),//阳性
    SKIN_TEST_NEGATIVE(2, CommonConstant.COMMON_VALUE_NEGATIVE);//阴性

    private int code;

    private String name;

    ExecuteResultEnum(int code, String name) {
        this.code = code;
        this.name = name;
    }

//    public static String getName(int code) {
//        String name = "";
//        switch (code) {
//            case 0:
//                name = NONEXECUTION.getName();
//                break;
//            case 1:
//                name = EXECUTING.getName();
//                break;
//            case 2:
//                name = EXECUTED.getName();
//                break;
//            case 3:
//                name = EXECUTE_PAUSE.getName();
//                break;
//            case 4:
//                name = EXECUTE_STOP.getName();
//                break;
//        }
//        return name;
//    }
//
//    /**
//     * 获取下一步执行结果
//     *
//     * @param order
//     * @return
//     */
//    public static Integer getNextResult(Order order) {
//        int executeResult = order.getExecuteResult();
//        if (executeResult == NONEXECUTION.getCode()) {
//            return order.isContinue() ? EXECUTING.getCode() : EXECUTED.getCode();
//        } else {
//            return EXECUTED.getCode();
//        }
//    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * 判断某条医嘱是否已执行
     *
     * @param executeResult
     * @return
     */
    public static boolean isExecuted(int executeResult) {
        return executeResult > NONEXECUTION.getCode();
    }

    /**
     * 判断某执行状态是否可以被执行
     *
     * @param executeResult
     * @return
     */
    public static boolean canExecute(int executeResult) {
        return !isExecuted(executeResult);
    }
}
