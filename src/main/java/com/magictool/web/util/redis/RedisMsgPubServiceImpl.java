package com.magictool.web.util.redis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/**
 * 消息订阅发布
 * @author qxc
 * @date 2020/10/19 11:04
 */
@Service("redisMsgPubService")
public class RedisMsgPubServiceImpl implements RedisMsgPubService {

    private final Logger logger = LoggerFactory.getLogger(RedisMsgPubServiceImpl.class);

    @Autowired
    private RedisTemplate<Object,Object> redisTemplate;

    @Override
    public void publishRedisTopicMessage(String topic, String message) {
        logger.info("发布消息：{}", message);
        redisTemplate.convertAndSend(topic,message);
    }

}
