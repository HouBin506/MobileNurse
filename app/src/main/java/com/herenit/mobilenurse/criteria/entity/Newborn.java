package com.herenit.mobilenurse.criteria.entity;

import com.herenit.mobilenurse.app.utils.StringUtils;
import com.herenit.mobilenurse.criteria.common.NameCode;

import java.io.Serializable;
import java.util.Objects;

public class Newborn implements Serializable, NameCode {

    private static final long serialVersionUID = 6724793831628856544L;

    /**
     * 婴儿标识号
     */
    private String babyId;

    /**
     * 妈妈住院流水号
     */
    private String mumVisitNo;

    /**
     * 母亲标识号
     */
    private String motherId;

    /**
     * 名字
     */
    private String name;

    /**
     * 婴儿编号
     */
    private Integer babyNo;

    /**
     * 出生日期
     */
    private Long dateOfBirth;

    /**
     * 性别
     */
    private String sex;


    public String getBabyId() {
        return babyId;
    }

    public void setBabyId(String babyId) {
        this.babyId = babyId;
    }

    public String getMumVisitNo() {
        return mumVisitNo;
    }

    public void setMumVisitNo(String mumVisitNo) {
        this.mumVisitNo = mumVisitNo;
    }

    public String getMotherId() {
        return motherId;
    }

    public void setMotherId(String motherId) {
        this.motherId = motherId;
    }

    public String getName() {
        return name;
    }

    @Override
    public String getCode() {
        return String.valueOf(babyNo);
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getBabyNo() {
        return babyNo;
    }

    public void setBabyNo(Integer babyNo) {
        this.babyNo = babyNo;
    }

    public Long getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Long dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Newborn newborn = (Newborn) o;
        return babyId.equals(newborn.babyId) &&
                motherId.equals(newborn.motherId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(babyId, motherId);
    }
}
