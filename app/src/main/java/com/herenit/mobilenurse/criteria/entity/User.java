package com.herenit.mobilenurse.criteria.entity;

import android.support.annotation.DrawableRes;
import android.text.TextUtils;
import android.util.JsonReader;

import com.alibaba.fastjson.JSON;

import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.converter.PropertyConverter;

import java.util.List;

import org.greenrobot.greendao.annotation.Generated;

/**
 * author: HouBin
 * date: 2019/1/4 11:14
 * desc:
 */
@Entity(indexes = {
        @Index(value = "userId DESC", unique = true)
})
public class User {


    private String userId;


    /**
     * mnUserVsDeptVPOJOList : [{"application":"产科护理单元","userId":"000033","userName":"唐文森","deptCode":"7001"},{"application":"妇科护理单元","userId":"000033","userName":"唐文森","deptCode":"7002"},{"application":"肛肠科护理单元","userId":"000033","userName":"唐文森","deptCode":"7003"},{"application":"普外二科护理单元","userId":"000033","userName":"唐文森","deptCode":"7004"},{"application":"普外一科护理单元","userId":"000033","userName":"唐文森","deptCode":"7005"},{"application":"耳鼻喉科护理单元","userId":"000033","userName":"唐文森","deptCode":"7006"},{"application":"神经外科护理单元","userId":"000033","userName":"唐文森","deptCode":"7007"},{"application":"眼科护理单元","userId":"000033","userName":"唐文森","deptCode":"7008"},{"application":"泌尿外科护理单元","userId":"000033","userName":"唐文森","deptCode":"7009"},{"application":"创伤骨科护理单元","userId":"000033","userName":"唐文森","deptCode":"7010"},{"application":"骨病科护理单元","userId":"000033","userName":"唐文森","deptCode":"7011"},{"application":"呼吸科护理单元","userId":"000033","userName":"唐文森","deptCode":"7012"},{"application":"心内科护理单元","userId":"000033","userName":"唐文森","deptCode":"7013"},{"application":"消化科护理单元","userId":"000033","userName":"唐文森","deptCode":"7014"},{"application":"肾病科护理单元","userId":"000033","userName":"唐文森","deptCode":"7015"},{"application":"神经内科护理单元","userId":"000033","userName":"唐文森","deptCode":"7016"},{"application":"内分泌科护理单元","userId":"000033","userName":"唐文森","deptCode":"7017"},{"application":"儿科护理单元","userId":"000033","userName":"唐文森","deptCode":"7018"},{"application":"新生儿科护理单元","userId":"000033","userName":"唐文森","deptCode":"7019"},{"application":"急诊科护理单元","userId":"000033","userName":"唐文森","deptCode":"7023"},{"application":"ICU护理单元","userId":"000033","userName":"唐文森","deptCode":"7024"},{"application":"血液透析护理单元","userId":"000033","userName":"唐文森","deptCode":"7025"}]
     * mnUserVPOJO : {"userId":"000033","userName":"唐文森","deptCode":"1004","deptName":"医务科","userRoles":"医生","userPassword":"C4CA4238A0B923820DCC509A6F75849B"}
     * mnModuleDictTOList : [{"moduleId":"000001","moduleName":"床位一览","visibility":null,"roles":null},{"moduleId":"000002","moduleName":"测试模块2","visibility":null,"roles":null},{"moduleId":"000006","moduleName":"测试模块6","visibility":null,"roles":null}]
     */
    @Convert(converter = UserStringConvert.class, columnType = String.class)
    private MnUserVPOJOBean mnUserVPOJO;
    @Convert(converter = GroupListStringConvert.class, columnType = String.class)
    private List<MnUserVsGroupVPOJOListBean> mnUserVsGroupVPOJOList;


    @Generated(hash = 1878060168)
    public User(String userId, MnUserVPOJOBean mnUserVPOJO, List<MnUserVsGroupVPOJOListBean> mnUserVsGroupVPOJOList) {
        this.userId = userId;
        this.mnUserVPOJO = mnUserVPOJO;
        this.mnUserVsGroupVPOJOList = mnUserVsGroupVPOJOList;
    }


    @Generated(hash = 586692638)
    public User() {
    }


    /**
     * 根据科室代码（病区代码），从列表中获取科室
     *
     * @param groupCode
     * @param groupList
     * @return
     */
    public static MnUserVsGroupVPOJOListBean getGroupByGroupCode(String groupCode, List<MnUserVsGroupVPOJOListBean> groupList) {
        if (groupList == null || groupList.isEmpty() || TextUtils.isEmpty(groupCode))
            return null;
        for (MnUserVsGroupVPOJOListBean group : groupList) {
            if (groupCode.equals(group.getGroupCode()))
                return group;
            else
                continue;
        }
        return null;
    }


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public MnUserVPOJOBean getMnUserVPOJO() {
        return mnUserVPOJO;
    }

    public void setMnUserVPOJO(MnUserVPOJOBean mnUserVPOJO) {
        this.mnUserVPOJO = mnUserVPOJO;
    }

    public List<MnUserVsGroupVPOJOListBean> getMnUserVsGroupVPOJOList() {
        return mnUserVsGroupVPOJOList;
    }

    public void setMnUserVsGroupVPOJOList(List<MnUserVsGroupVPOJOListBean> mnUserVsGroupVPOJOList) {
        this.mnUserVsGroupVPOJOList = mnUserVsGroupVPOJOList;
    }

    public static class MnUserVPOJOBean {
        /**
         * userId : 000033
         * userName : 唐文森
         * deptCode : 1004
         * deptName : 医务科
         * userRoles : 医生
         * userPassword : C4CA4238A0B923820DCC509A6F75849B
         */

        private String userId;
        private String userName;
        private String deptCode;
        private String deptName;
        private String userRoles;
        private String userPassword;
        //是否为手术室账号
        private boolean isOperation;

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getDeptCode() {
            return deptCode;
        }

        public void setDeptCode(String deptCode) {
            this.deptCode = deptCode;
        }

        public String getDeptName() {
            return deptName;
        }

        public void setDeptName(String deptName) {
            this.deptName = deptName;
        }

        public String getUserRoles() {
            return userRoles;
        }

        public void setUserRoles(String userRoles) {
            this.userRoles = userRoles;
        }

        public String getUserPassword() {
            return userPassword;
        }

        public void setUserPassword(String userPassword) {
            this.userPassword = userPassword;
        }

        public boolean getIsOperation() {
            return isOperation;
        }

        public void setIsOperation(boolean isOperation) {
            this.isOperation = isOperation;
        }
    }

    public static class MnUserVsGroupVPOJOListBean {
        /**
         * application : 产科护理单元
         * userId : 000033
         * userName : 唐文森
         * deptCode : 7001
         */

        private String groupName;
        private String userId;
        private String userName;
        /**
         * 病区代码或者科室代码（要看医院护士根据什么来区分）
         */
        private String groupCode;

        public String getGroupName() {
            return groupName;
        }

        public void setGroupName(String groupName) {
            this.groupName = groupName;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getGroupCode() {
            return groupCode;
        }

        public void setGroupCode(String groupCode) {
            this.groupCode = groupCode;
        }
    }

    /**
     * MnUserVPOJOBean对象与String的互转
     */
    public static class UserStringConvert implements PropertyConverter<MnUserVPOJOBean, String> {
        @Override
        public MnUserVPOJOBean convertToEntityProperty(String databaseValue) {
            return JSON.parseObject(databaseValue, MnUserVPOJOBean.class);
        }

        @Override
        public String convertToDatabaseValue(MnUserVPOJOBean entityProperty) {
            return JSON.toJSONString(entityProperty);
        }
    }

    /**
     * MnUserVsGroupVPOJOListBean对象与String的互转
     */
    public static class GroupListStringConvert implements PropertyConverter<List<MnUserVsGroupVPOJOListBean>, String> {
        @Override
        public List<MnUserVsGroupVPOJOListBean> convertToEntityProperty(String databaseValue) {
            return JSON.parseArray(databaseValue, MnUserVsGroupVPOJOListBean.class);
        }

        @Override
        public String convertToDatabaseValue(List<MnUserVsGroupVPOJOListBean> entityProperty) {
            return JSON.toJSONString(entityProperty);
        }
    }

}
