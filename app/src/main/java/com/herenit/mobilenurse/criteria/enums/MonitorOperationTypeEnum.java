package com.herenit.mobilenurse.criteria.enums;


/**
 * author: HouBin
 * date: 2019/2/14 11:17
 * desc: 监护仪操作类型枚举
 */
public enum MonitorOperationTypeEnum {
    //小工具
    BINDING("将当前患者与当前监护仪绑定", 1),
    UNBIND("将当患者与当前监护仪解绑", 2),
    PATIENT_UNBIND_BINDING("将当前患者与另一监护仪解绑，再与当前监护仪绑定", 3),
    MONITOR_UNBIND_BINDING("将当前监护仪与另一患者解绑，再与当前患者绑定", 4),
    PATIENT_MONITOR_UNBIND_BINDING("将当前患者与当前监护仪，各自分别解绑，然后再相互绑定", 5);

    private int operationType;
    private String operationDesc;

    MonitorOperationTypeEnum(String desc, int type) {
        this.operationDesc = desc;
        this.operationType = type;
    }

    public int getOperationType() {
        return operationType;
    }

    public String getOperationDesc() {
        return operationDesc;
    }

    /**
     * 根据操作类型Code，匹配对应的枚举
     *
     * @param operationType
     * @return
     */
    public static MonitorOperationTypeEnum matchMonitorOperationType(int operationType) {
        for (MonitorOperationTypeEnum operationTypeEnum : MonitorOperationTypeEnum.values()) {
            if (operationType == operationTypeEnum.getOperationType())
                return operationTypeEnum;
        }
        return null;
    }
}
