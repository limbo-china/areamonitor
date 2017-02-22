package cn.ac.iie.hy.areamonitor.task;

import cn.ac.iie.hy.areamonitor.data.GlobalArea;
import cn.ac.iie.hy.areamonitor.data.SMetaData;
import cn.ac.iie.hy.areamonitor.dbutils.RedisFactory;
import cn.ac.iie.hy.areamonitor.tokenbucket.TokenBucket;
import cn.ac.iie.hy.areamonitor.tokenbucket.TokenBuckets;
import com.google.gson.Gson;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;

import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class GetDataLoop implements Runnable {

	static Logger logger = null;
	private Jedis redis = null;

	private static String key = "area_monitor_queue";
	static {
		PropertyConfigurator.configure("log4j.properties");
		logger = Logger.getLogger(GetDataLoop.class.getName());
	}

	private List<SMetaData> cacheList = new ArrayList<SMetaData>();

	private ThreadPoolManager threadPool = ThreadPoolManager.newInstance();

	private Gson gson = new Gson();

	private void fetchData() {

		long len = redis.llen(key);
		if (len <= 0L) {
			return;
		}
		Pipeline pipeline = redis.pipelined();
		Long interval = len > 10000L ? 10000L : len;
		for (long i = 0; i < interval; i++) {
			pipeline.rpop(key);
		}
		List<Object> rawCDR = pipeline.syncAndReturnAll();
		for (Object data : rawCDR) {
			String record = data.toString();
			SMetaData smd = gson.fromJson(record, SMetaData.class);
			cacheList.add(smd);
		}
	}

	private void dispatchTask() {
		CountDownLatch latch = new CountDownLatch(3);
		threadPool.addExecuteTask(new MergeCDRTask(latch, cacheList));
		threadPool.addExecuteTask(new AreaMonitoringTask(latch, cacheList));
		threadPool.addExecuteTask(new StaffMonitoringTask(latch, cacheList));
		try {
			latch.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private void loadConfig(){
		Jedis jedis = RedisFactory.getJedis("redisConfigServer");
//		Configuration conf = Configuration.getConfiguration("sysconfig.properties");
//		String host = conf.getString("redisConfigServer", "");
//		String ip = host.split(":")[0];
//		int port = Integer.parseInt(host.split(":")[1]);
		Map<String, Set<String>> configMap= GlobalArea.configMap;
//		Set<HostAndPort> jedisClusterNodes = new HashSet<HostAndPort>();
//		jedisClusterNodes.add(new HostAndPort(ip, port));
//		JedisCluster jc = new JedisCluster(jedisClusterNodes);
		/* 读取重点区域配置 */
//		Map<String, String> rules = jc.hgetAll("ImportantAreaRule");
        Map<String, String> rules = jedis.hgetAll("ImportentAreaRule");
        Long start = System.currentTimeMillis();
		for (Map.Entry<String, String> entry : rules.entrySet()) {
			String key = entry.getKey();
			String value = entry.getValue();
			JSONObject jsonObject = JSONObject.fromObject(value);
			if (jsonObject != null) {
				JSONObject ruleInfos = jsonObject.getJSONObject("ruleInfos");
				if (ruleInfos != null) {
					JSONArray personInfos = ruleInfos.getJSONArray("areaInfos");
					Set<String> ulis = new HashSet<>();
					for (int i = 0; i < personInfos.size(); i++) {
						JSONObject bs = (JSONObject) personInfos.get(i);
						if (bs != null) {
							String uli = bs.getString("uli").trim();
							ulis.add(uli);
							//jedis.sadd("ImportAreaUliSet", uli);
						} else {
							System.out.println("[Warning]-Ruleid : " + key + " phoneno is null!");
						}
					}
					configMap.put(key, ulis);
				}
			}
		}
        /* 读取重点人群配置 */
		Map<String, String> personRules = jedis.hgetAll("ImportentPersonRule");
		Set<String> importantPersonSet = GlobalArea.importantPersion;
		for (Map.Entry<String, String> entry : personRules.entrySet()) {
			String key = entry.getKey();
			String value = entry.getValue();
            JSONObject jsonObject = JSONObject.fromObject(value);
			if (jsonObject != null) {
                JSONArray ruleInfoArray = jsonObject.getJSONArray("ruleInfos");
				JSONObject ruleInfos = (JSONObject)ruleInfoArray.get(0);
				if (ruleInfos != null) {
					JSONArray personInfos = ruleInfos.getJSONArray("personInfos");

					for (int i = 0; i < personInfos.size(); i++) {
						JSONObject person = (JSONObject) personInfos.get(i);
						if (person != null) {
							String number = person.getString("number").trim();
							if (number.length() == 13) {
								importantPersonSet.add(number);
							}
						} else {
							System.out.println("[Warning]-Ruleid : " + key + " phoneno is null!");
						}
					}

				}
			}
		}

        RedisFactory.returnResource("redisConfigServer", jedis);
		//jc.close();
	}

	public static void main(String[] argv){
		new GetDataLoop().loadConfig();
	}
	@Override
	public void run() {

        loadConfig();
		TokenBucket bucket = TokenBuckets.builder().withCapacity(1 * 2)
				.withFixedIntervalRefillStrategy(1, 1, TimeUnit.SECONDS).build();

		while (true) {
			try {
				bucket.consume(1);
				redis = RedisFactory.getJedis("redisQueueServer");
				fetchData();
				if (cacheList.size() > 0) {
					dispatchTask();

				} else {

				}
                RedisFactory.returnResource("redisQueueServer", redis);
			} catch (Exception e) {
				e.printStackTrace();
				RedisFactory.returnBrokenResource("redisQueueServer", redis);
			} finally {
				cacheList.clear();
			}
		}
	}

}
