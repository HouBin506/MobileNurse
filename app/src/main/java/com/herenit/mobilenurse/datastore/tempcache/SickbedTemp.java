package com.herenit.mobilenurse.datastore.tempcache;

import android.os.IInterface;
import android.text.TextUtils;

import com.herenit.mobilenurse.criteria.entity.Newborn;
import com.herenit.mobilenurse.criteria.entity.Sickbed;

import java.util.ArrayList;
import java.util.List;

/**
 * author: HouBin
 * date: 2019/3/4 16:56
 * desc: 患者、床位的一些临时存储,比如全局提供床位列表、提供当前正在操作的患者
 * 省人民患者列表直接获取到住院患者，有些特殊的床位没有visitId，直接通过patientId锁定患者
 */
public class SickbedTemp {

    private static SickbedTemp mInstance;

    private List<Sickbed> mSickbedList = new ArrayList<>();

    private Sickbed mCurrentSickbed;

    private SickbedTemp() {
    }

    public static SickbedTemp getInstance() {
        if (mInstance == null)
            mInstance = new SickbedTemp();
        return mInstance;
    }

    /**
     * 获取当前科室下的床位列表
     *
     * @return
     */
    public List<Sickbed> getSickbedList() {
        return mSickbedList;
    }

    /**
     * 设置当前科室下的床位列表
     *
     * @return
     */
    public void setSickbedList(List<Sickbed> sickbedList) {
        if (mSickbedList == null)
            mSickbedList = new ArrayList<>();
        mSickbedList.clear();
        if (sickbedList == null)
            return;
        mSickbedList.addAll(sickbedList);
    }

    public Sickbed getCurrentSickbed() {
        return mCurrentSickbed;
    }

    public void setCurrentSickbed(Sickbed mCurrentSickbed) {
        this.mCurrentSickbed = mCurrentSickbed;
    }

    public int getSickbedPosition(Sickbed sickbed) {
        if (mSickbedList == null || mSickbedList.isEmpty() || sickbed == null)
            return -1;
        return mSickbedList.indexOf(sickbed);
    }

    /**
     * 根据患者Id和住院号在当前患者列表中查找患者
     *
     * @param patientId
     * @param visitId
     * @return
     */
    public Sickbed getSickbed(String patientId, String visitId) {
        if (mSickbedList == null || TextUtils.isEmpty(patientId))
            return null;
        for (Sickbed sickbed : mSickbedList) {
            if (!TextUtils.isEmpty(sickbed.getPatientId())
                    && patientId.equals(sickbed.getPatientId())) {
                return sickbed;
            }
        }
        return null;
    }

    public void clear() {
        mCurrentSickbed = null;
        if (mSickbedList != null)
            mSickbedList.clear();
        mSickbedList = null;
    }

    /**
     * 根据PatientId、VisitID判断是否是当前患者
     *
     * @param patientId
     * @param visitId
     * @return
     */
    public boolean isCurrentPatient(String patientId, String visitId) {
        if (mCurrentSickbed == null)
            return false;
        if (TextUtils.isEmpty(patientId))
            return false;
        return patientId.equals(mCurrentSickbed.getPatientId());
    }

    /**
     * 根据新生儿ID找到母亲
     *
     * @param babyId
     * @return
     */
    public Sickbed getMother(String babyId) {
        if (mSickbedList == null || TextUtils.isEmpty(babyId))
            return null;
        for (Sickbed sickbed : mSickbedList) {
            List<Newborn> babyList = sickbed.getBabyList();
            if (babyList == null || babyList.isEmpty())
                continue;
            for (int x = 0; x < babyList.size(); x++) {
                Newborn newborn = babyList.get(x);
                if (newborn.getBabyId().equals(babyId)) {
                    sickbed.setCurrentBabyIndex(x);
                    return sickbed;
                }
            }
        }
        return null;

    }
}
