package com.herenit.mobilenurse.criteria.entity.submit;

import com.herenit.mobilenurse.criteria.common.NameCode;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;

import java.util.Arrays;

import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.Transient;

/**
 * author: HouBin
 * date: 2019/1/4 11:04
 * desc: 登录提交的数据
 */
@Entity(indexes = {
        @Index(value = "userId DESC", unique = true)
})
public class Login extends BaseSubmit implements NameCode {
    private String userId;
    private String password;
    @Transient
    private String groupCode;


    @Generated(hash = 1976220968)
    public Login(String userId, String password) {
        this.userId = userId;
        this.password = password;
    }

    @Generated(hash = 1827378950)
    public Login() {
    }


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getGroupCode() {
        return groupCode;
    }

    public void setGroupCode(String groupCode) {
        this.groupCode = groupCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Login login = (Login) o;
        return userId != null && userId.equals(login.getUserId());
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(new String[]{userId});
    }

    @Override
    public String getName() {
        return userId;
    }

    @Override
    public String getCode() {
        return null;
    }
}
