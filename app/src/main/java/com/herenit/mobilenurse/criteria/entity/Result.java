package com.herenit.mobilenurse.criteria.entity;

import com.herenit.mobilenurse.api.Api;

/**
 * author: HouBin
 * date: 2019/1/4 10:52
 * desc:
 */
public class Result<T> {
    private Integer code;
    private String message;
    private T data;

    public Result() {
    }

    public Result(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    /**
     * 判断请求是否成功
     *
     * @return
     */
    public boolean isSuccessful() {
        return code == null ? false : code.equals(Api.CODE_SUCCESS);
    }
}
