package com.herenit.mobilenurse.criteria.entity;

import java.io.Serializable;

/**
 * author: HouBin
 * date: 2019/1/8 16:35
 * desc: 版本信息
 */
public class VersionInfo implements Serializable {
    private Integer versionCode;
    private String versionName;
    private String fileName;
    private Long fileSize;
    private Boolean updateNow;

    public VersionInfo() {
    }

    public Integer getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(Integer versionCode) {
        this.versionCode = versionCode;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    public Boolean getUpdateNow() {
        return updateNow;
    }

    public void setUpdateNow(Boolean updateNow) {
        this.updateNow = updateNow;
    }
}
