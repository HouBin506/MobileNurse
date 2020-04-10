package com.herenit.mobilenurse.datastore.tempcache;

/**
 * author: HouBin
 * date: 2019/1/24 15:58
 * desc:临时存储的当前登录用户，保存该用户一些常用的信息，供全局使用
 */
public class UserTemp {

    private static UserTemp mInstance;

    private UserTemp() {

    }

    public static UserTemp getInstance() {
        if (mInstance == null) {
            mInstance = new UserTemp();
        }
        return mInstance;
    }

    /**
     * 账号
     */
    private String userId;
    /**
     * 病区代码或者科室代码（要看医院护士根据什么来区分）
     */
    private String groupCode;
    /**
     * 病区名称或者科室名称（要看医院护士根据什么来区分）
     */
    private String groupName;
    /**
     * 名字
     */
    private String userName;

    private boolean isOperation;


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

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public boolean isOperation() {
        return isOperation;
    }

    public void setOperation(boolean operation) {
        isOperation = operation;
    }

    /**
     * 清除内存中当前登录账号信息
     */
    public void clear() {
        setUserId("");
        setGroupName("");
        setGroupCode("");
        setUserName("");
        setOperation(false);
    }
}
