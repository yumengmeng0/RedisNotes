import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * 单例模式优化jedis连接池
 *
 * @author YuMengMeng
 * @date 2022/10/25
 */

public class JedisPoolUtils {
    private volatile static JedisPool jedisPool = null;
    private volatile static Jedis jedis = null;

    private JedisPoolUtils() {

    }

    /**
     * @return
     */
    private static JedisPool getInstance() {
        // 双层检测锁
        if (jedisPool == null) {
            // 排队
            synchronized (JedisPoolUtils.class) {
                if (jedisPool == null) {
                    JedisPoolConfig config = new JedisPoolConfig();
                    config.setMaxTotal(1000);
                    config.setMaxIdle(30);
                    config.setMaxWaitMillis(60 * 1000);
                    config.setTestOnBorrow(true);
                    jedisPool = new JedisPool(config, "ip-address", 6379);
                }
            }
        }

        return jedisPool;
    }

    /**
     * @return
     */
    public static Jedis getJedis() {
        if (jedisPool == null) {
            jedis = getInstance().getResource();
        }
        return jedis;
    }


}
