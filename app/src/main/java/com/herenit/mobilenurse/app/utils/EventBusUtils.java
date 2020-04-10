package com.herenit.mobilenurse.app.utils;

import android.text.TextUtils;

import com.herenit.mobilenurse.criteria.enums.EventIntentionEnum;
import com.herenit.mobilenurse.criteria.message.eventbus.Event;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.UUID;

/**
 * author: HouBin
 * date: 2019/1/23 15:31
 * desc: EventBus工具类，提供了本项目对EventBus的基本操作
 * 因为Eventbus3.0虽然说具备更快、代码解耦、代码量小等，但是考虑到每一个事件发送后，只要接收方法的参数类型
 * 与发出方发送的类型相同，就可以接收到该事件，区分的办法，只能通过参数，定义太多的参数或者封装太多的类容易
 * 搞混，所以每一个事件都有一个Id，接收方与发送方通过Id来建立连接
 */
public class EventBusUtils {
    /**
     * 单线的、私有的EventBus事件的Id集合，该Id标记的Event只允许被一个地方接收消费
     * 外层的Key标识事件消费者的标识，当事件消费者销毁时，根据标识找到与之对应Id并删除，可节省内存;
     * 内层Key为事件Id，内层Value为事件意图。私有事件的消费意图，决定当前事件要表达的意义，消费者根据
     * 意图可作出具体的操作
     * 公共的事件，其Id可用消费意图来表示，因为全局公用、唯一，定义在{@link EventIntentionEnum}中
     */
    private static HashMap<String, Map<String, String>> privateEventIdMap = new HashMap<>();


    /**
     * 发送消息
     *
     * @param id
     * @param message
     */
    public static void post(String id, Object message) {
        EventBus.getDefault().post(new Event(id, message));
    }

    /**
     * 发送消息（延迟接收）
     *
     * @param id
     * @param message
     */
    public static void postSticky(String id, Object message) {
        EventBus.getDefault().postSticky(new Event(id, message));
    }

    /**
     * 获取一个私有的单线的Id，
     *
     * @param tag       事件消费方的标识
     * @param intention 事件意图（消费该事件时，要做什么操作，事件消费标识）
     * @return 返回事件的唯一标识
     */
    public static String obtainPrivateId(String tag, String intention) {
        Map<String, String> idMap = privateEventIdMap.get(tag);
        if (idMap == null)
            idMap = new HashMap<>();
        String id = UUID.randomUUID().toString();
        idMap.put(id, intention);
        privateEventIdMap.put(tag, idMap);
        return id;
    }

    /**
     * 获取某私有事件的事件意图。用来判断该事件该如何消费
     *
     * @param tag 消费者的标识
     * @param id  事件Id
     * @return
     */
    public static String getPrivateEventIntention(String tag, String id) {
        Map<String, String> idMap = privateEventIdMap.get(tag);
        if (idMap == null)
            return null;
        return idMap.get(id);
    }

    /**
     * 清除某事件消费方所有的私有Id---intention缓存
     *
     * @param tag 事件消费方，私有事件一般为当前对象的toString
     */
    public static void clearIdMap(String tag) {
        Map<String, String> idMap = privateEventIdMap.get(tag);
        if (idMap != null)
            idMap.clear();
        privateEventIdMap.remove(tag);
    }


}
