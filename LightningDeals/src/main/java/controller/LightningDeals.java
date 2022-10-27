package controller;

import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.concurrent.TimeUnit;

/**
 * 秒杀
 *
 * @author YuMengMeng
 * @date 2022/10/25
 */

@Controller
public class LightningDeals {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private Redisson redisson;

    /**
     * 只能解决一个tomcat并发，synchronized锁的是一个进程下的多线程并发，
     * 分布式式环境，多个进程并发，这种方案失效
     *
     * @return
     */
    @RequestMapping("/kill")
    public @ResponseBody
    synchronized String kill() {
        // 定义商品id
        String productKey = "apple4s";

        // 通过redisson获取锁，底层源码是集成setnx，过期时间等操作
        RLock lock = redisson.getLock(productKey);
        // 上过期时间为30s的锁
        lock.lock(30, TimeUnit.SECONDS);

        try {
            int phoneCount = Integer.parseInt(stringRedisTemplate.opsForValue().get("phone"));
            if (phoneCount > 0) {
                phoneCount--;
                stringRedisTemplate.opsForValue().set("phone", phoneCount + "");
                System.out.println("库存-1，剩余" + phoneCount + "台手机");
            } else {
                System.out.println("库存不足");
            }
        } catch (NumberFormatException e) {
            throw new RuntimeException(e);
        } finally {
            lock.unlock();
        }
        return "over";
    }

    @Bean
    public Redisson redisson() {
        Config config = new Config();
        // 使用单个Redis服务器
        config.useSingleServer().setAddress("redis://ip-address:6379").setDatabase(0);
        // 集群
        // config.useClusterServers().setScanInterval(2000).addNodeAddress("address1","address2");
        RedissonClient redissonClient = Redisson.create(config);

        return (Redisson) redissonClient;
    }

}
