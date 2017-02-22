package cn.ac.iie.hy.areamonitor.dbutils;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.HashMap;
import java.util.Map;

/**
 * Redis 工厂类
 */
public class RedisFactory {
    static String configurationFileName = "sysconfig.properties";
    private static int MAX_IDLE = 4096;
    private static int MAX_WAIT = 1000;
    private static int TIMEOUT = 100000;
    private static boolean TEST_ON_BORROW = true;

    static RedisConfiguration conf = null;
    static Map<String, JedisPool> jedisMap= new HashMap();
    static {
        conf = RedisConfiguration.getConfiguration(configurationFileName);
        if (conf == null) {
            System.out.println("reading " + configurationFileName + " is failed.");
            System.exit(-1);
        }
    }

    public synchronized static Jedis getJedis(String key){
        JedisPool jedisPool = jedisMap.get(key);
        if(jedisPool!=null){
            Jedis resource = jedisPool.getResource();
            return resource;
        }else{
            String ADDR = conf.getString(key, "");
            if (ADDR.equals("")) {
                System.out.println("definition redisIP is not found in " + configurationFileName);
                System.exit(-1);
            }
            if(ADDR.split(":").length < 2){
                System.out.println("definition redisIP is not legal in " + configurationFileName);
                System.exit(-1);
            }
            System.out.println(ADDR);
            String ip = ADDR.split(":")[0];
            int port = Integer.parseInt(ADDR.split(":")[1]);
            JedisPoolConfig config = new JedisPoolConfig();
            config.setMaxIdle(MAX_IDLE);
            config.setTestOnBorrow(TEST_ON_BORROW);
            jedisPool = new JedisPool(config,ip,port,TIMEOUT);
            jedisMap.put(key, jedisPool);

            Jedis resource = jedisPool.getResource();
            return resource;
        }
    }

    public synchronized static void returnBrokenResource(String key, Jedis jedis){
        JedisPool jedisPool = jedisMap.get(key);
        if(jedis!=null){
            jedisPool.returnBrokenResource(jedis);
        }
        else {
            jedis.close();
        }
    }

    public synchronized static void returnResource(String key, Jedis jedis){
        JedisPool jedisPool = jedisMap.get(key);
        if(jedis!=null){
            jedisPool.returnResource(jedis);
        }
        else {
            jedis.close();
        }
    }

}
