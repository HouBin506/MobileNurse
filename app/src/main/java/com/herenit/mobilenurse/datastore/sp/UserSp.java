package com.herenit.mobilenurse.datastore.sp;

import com.herenit.arms.utils.Preconditions;
import com.herenit.mobilenurse.criteria.constant.KeyConstant;

/**
 * author: HouBin
 * date: 2019/2/14 16:11
 * desc:文件名为{@link KeyConstant#NAME_SP_USER}的{@link android.content.SharedPreferences}存储
 * 存储用户相关的一些状态
 */
public class UserSp extends CommonSp {

    private static UserSp mInstance;

    private UserSp() {
        super(KeyConstant.NAME_SP_USER);
    }

    public static UserSp getInstance() {
        if (mInstance == null)
            mInstance = new UserSp();
        return mInstance;
    }

    /**
     * 存储用户的groupCode（病区号或者科室代码）
     *
     * @param userId    用户Id
     * @param groupCode 病区号或者科室代码
     */
    public void setUserGroupCode(String userId, String groupCode) {
        userId = Preconditions.checkNotNull(userId);
        setValue(userId + KeyConstant.KEY_SP_SUFFIX_USER_GROUP_CODE, groupCode);
    }

    /**
     * 获取用户当前的病区号或者科室代码
     *
     * @param userId 用户Id
     */
    public String getUserGroupCode(String userId) {
        return getValue(userId + KeyConstant.KEY_SP_SUFFIX_USER_GROUP_CODE, "");
    }

    /**
     * 删除户当前的病区号或者科室代码
     *
     * @param userId 用户Id
     */
    public void removeUserGroupCode(String userId) {
        removeValue(userId + KeyConstant.KEY_SP_SUFFIX_USER_GROUP_CODE);
    }
}
