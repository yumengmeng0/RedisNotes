import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

/**
 * @author YuMengMeng
 * @date 2022/10/25
 */

public class JedisTransaction {

    public static void main(String[] args) throws InterruptedException {
        Jedis jedis = new Jedis("ip-address", 6379);
        int balance = Integer.parseInt(jedis.get("balance"));
        System.out.println("balance = " + balance);
        int pay = 10000;

        jedis.watch("balance");
        Thread.sleep(5000);

        if (pay > balance) {
            jedis.unwatch();
            System.out.println("Insufficient balance");
        } else {
            // open transaction
            Transaction transaction = jedis.multi();
            transaction.decrBy("balance", pay);
            transaction.incrBy("consumption", pay);
            System.out.println("consumption = " + pay);
            transaction.exec();
            transaction.close();
        }
    }

}
