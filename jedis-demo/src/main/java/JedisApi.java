import org.junit.Before;
import org.junit.Test;
import redis.clients.jedis.Jedis;

import java.util.Set;

/**
 * @author YuMengMeng
 * @date 2022/10/25
 */

public class JedisApi {

    private Jedis jedis;

    @Before
    public void connect() {
        jedis = new Jedis("ip-address", 6379);
        String pong = jedis.ping();
        // PONG
        System.out.println(pong);
    }

    @Test
    public void set() {
        jedis.set("k1", "v1");
        jedis.set("k2", "v2");
        jedis.set("k3", "v3");

        Set<String> keys = jedis.keys("*");

        for (String key : keys) {
            System.out.println(key + " = " + jedis.get(key));
        }

        Boolean exists = jedis.exists("k2");
        System.out.println("k2 is exists : " + exists);
        System.out.println(jedis.ttl("k2"));
    }


    public static void main(String[] args) {



    }
}
