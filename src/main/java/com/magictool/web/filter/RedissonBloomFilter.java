package com.magictool.web.filter;
import org.redisson.Redisson;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

/**
 * 布隆过滤器配置
 * @author lijf
 */
public class RedissonBloomFilter {

    private static RBloomFilter<String> bloomFilter;

    static {
        Config config = new Config();
        config.useSingleServer().setAddress("redis://127.0.0.1:6379");
        RedissonClient client = Redisson.create(config);

        bloomFilter = client.getBloomFilter("bloomFilter");
        bloomFilter.tryInit(1000_000, 0.01);
    }

    public static void add(String data) {
        bloomFilter.add(data);
    }

    public static boolean check(String data) {
        return bloomFilter.contains(data);
    }

}