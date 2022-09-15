package com.magictool.web.util.redis;

import org.springframework.data.redis.connection.MessageListener;

/**
 * 监听消息（实现此接口配置监听实例）
 * @author qxc
 * @date 2020/10/19 10:32
 */

public interface RedisMsgPubSubListener extends MessageListener {

    /**
     * 类型
     * @return str
     */
    default String getType() {
        return this.getClass().getSimpleName();
    }

    /**
     * 通道名称
     * @return str
     */
    String getTopic();

}
