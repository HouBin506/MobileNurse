package com.herenit.mobilenurse.criteria.entity.submit;

import java.io.Serializable;
import java.util.List;

/**
 * author: HouBin
 * date: 2019/5/16 14:26
 * desc:查询体征历史记录提交的条件
 */
public class VitalSignsHistoryQuery extends TimeIntervalQuery {
    private String patientId;
    private String visitId;
    private List<VitalItemID> vitalItemIdList;

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public String getVisitId() {
        return visitId;
    }

    public void setVisitId(String visitId) {
        this.visitId = visitId;
    }

    public List<VitalItemID> getVitalItemIdList() {
        return vitalItemIdList;
    }

    public void setVitalItemIdList(List<VitalItemID> vitalItemIdList) {
        this.vitalItemIdList = vitalItemIdList;
    }

    public static class VitalItemID implements Serializable {
        private static final long serialVersionUID = -8721914114367427161L;
        private String itemCode;
        private String classCode;
        private String itemName;

        public VitalItemID() {
        }

        public VitalItemID(String itemCode) {
            this.itemCode = itemCode;
        }

        public VitalItemID(String itemCode, String classCode) {
            this.itemCode = itemCode;
            this.classCode = classCode;
        }

        public VitalItemID(String itemCode, String classCode, String itemName) {
            this.itemCode = itemCode;
            this.classCode = classCode;
            this.itemName = itemName;
        }

        public String getItemCode() {
            return itemCode;
        }

        public void setItemCode(String itemCode) {
            this.itemCode = itemCode;
        }

        public String getClassCode() {
            return classCode;
        }

        public void setClassCode(String classCode) {
            this.classCode = classCode;
        }

        public String getItemName() {
            return itemName;
        }

        public void setItemName(String itemName) {
            this.itemName = itemName;
        }
    }
}
