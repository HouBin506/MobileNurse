package com.herenit.mobilenurse.criteria.entity.submit;

/**
 * author: HouBin
 * date: 2019/5/23 9:40
 * desc: 手术安排查询条件
 */
public class OperationScheduledQuery extends TimeIntervalQuery {
    private String ackIndicator;//手术确认状态标识
    private String emergencyIndicator;//手术紧急状态标识

    public String getAckIndicator() {
        return ackIndicator;
    }

    public void setAckIndicator(String ackIndicator) {
        this.ackIndicator = ackIndicator;
    }

    public String getEmergencyIndicator() {
        return emergencyIndicator;
    }

    public void setEmergencyIndicator(String emergencyIndicator) {
        this.emergencyIndicator = emergencyIndicator;
    }
}
