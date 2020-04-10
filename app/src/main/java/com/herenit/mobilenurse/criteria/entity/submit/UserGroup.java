package com.herenit.mobilenurse.criteria.entity.submit;

/**
 * author: HouBin
 * date: 2019/4/3 16:41
 * desc: 封装userId、groupCode字段
 */
public class UserGroup extends BaseSubmit {
    private String userId;
    private String groupCode;

    public UserGroup() {
    }

    public UserGroup(String userId, String groupCode) {
        this.userId = userId;
        this.groupCode = groupCode;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getGroupCode() {
        return groupCode;
    }

    public void setGroupCode(String groupCode) {
        this.groupCode = groupCode;
    }
}
