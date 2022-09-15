package com.magictool.web.util.redis;


/**
 * 发布订阅消息接口
 * @author qxc
 * @date 2020/10/19 11:00
 */
public interface RedisMsgPubService {

    /**
     * 发布Redis主题消息
     * @param topic 通道
     * @param message 消息
     */
    void publishRedisTopicMessage(String topic,String message);
}
