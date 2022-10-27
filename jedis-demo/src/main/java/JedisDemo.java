import redis.clients.jedis.Jedis;

import java.util.Set;

/**
 * 测试连接redis
 *
 * @author YuMengMeng
 * @date 2022/10/25
 */

public class JedisDemo {

    /**
     *  1.运行前关闭防火墙
     *  2.修改redis.conf：bind 0.0.0.0 （允许任何ip访问）
     *  redis-server redis.conf
     *  redis-cli -p 6379
     * @param args
     */
    public static void main(String[] args) {
        Jedis jedis = new Jedis("ip-address", 6379);
        String pong = jedis.ping();
        // PONG
        System.out.println(pong);

    }
}
