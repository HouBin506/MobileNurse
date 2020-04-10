package com.herenit.mobilenurse.criteria.entity.submit;

/**
 * author: HouBin
 * date: 2019/1/24 14:52
 * desc:
 */
public class ChangePassword extends BaseSubmit {
    private String userId;
    private String oldPassword;
    private String newPassword;

    public ChangePassword(String userId, String oldPassword, String newPassword) {
        this.userId = userId;
        this.oldPassword = oldPassword;
        this.newPassword = newPassword;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
