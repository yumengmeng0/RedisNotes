import redis.clients.jedis.Jedis;

import java.awt.image.Kernel;

/**
 * @author YuMengMeng
 * @date 2022/10/25
 */

public class JedisPoolUtilsTest {
    public static void main(String[] args) {
        Jedis jedis = JedisPoolUtils.getJedis();
        Jedis jedis1 = JedisPoolUtils.getJedis();
        System.out.println(jedis1 == jedis);
    }
}
