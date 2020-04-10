package com.herenit.mobilenurse.criteria.message.eventbus;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * author: HouBin
 * date: 2019/1/22 17:00
 * desc: Eventbus发送的消息，id为接收方的标识，message为消息内容
 */
public class Event {
    /**
     * 接收方的唯一标识，id规定了该条消息要发送给谁,我们将Activity的Eventbus的Id定位类名
     */
    @NonNull
    private String id;
    /**
     * 发送的消息内容
     */
    private Object message;

    public Event(@NonNull String id, @Nullable Object message) {
        this.id = id;
        this.message = message;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Object getMessage() {
        return message;
    }

    public void setMessage(Object message) {
        this.message = message;
    }
}
