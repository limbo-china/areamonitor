package cn.ac.iie.hy.areamonitor.task;

import cn.ac.iie.hy.areamonitor.data.SMetaData;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;


public class MergeCDRTask implements Runnable {

    public static Map<String, Long> msCache = new ConcurrentHashMap<String, Long>();

    static Logger logger = null;
    CountDownLatch latch = null;
    List<SMetaData> cacheList = null;

    static {
        PropertyConfigurator.configure("log4j.properties");
        logger = Logger.getLogger(MergeCDRTask.class.getName());
    }

    public MergeCDRTask(CountDownLatch latch, List<SMetaData> cacheList) {
        super();
        this.latch = latch;
        this.cacheList = cacheList;
    }

    public static String stampToDate(Long s){
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long lt = s;
        Date date = new Date(lt);
        res = simpleDateFormat.format(date);
        return res;
    }

    @Override
    public void run() {
        try {
            for (SMetaData smd : cacheList) {
                if (smd.getC_CDRtype() >= 3 && smd.getC_CDRtype() <= 8) {
                    if(smd.getC_CDRtype() == 7){
                        //System.out.println("发送短信： " + smd.getC_Content());
                    }
                    if(smd.getC_CDRtype() == 8){
                        //System.out.println("接收短信： " + smd.getC_Content());
                    }
                    if(!smd.getC_UserNum().isEmpty()&&!smd.getC_RelateNum().isEmpty()){
                        if(smd.getC_CDRtype() == 3){
                            String key = smd.getC_CDRtype() + "_" + smd.getC_UserNum()  + "_" + smd.getC_RelateNum();
                            msCache.put(key, smd.getC_TimeStamp());
                            //通话开始 记录
                            //System.out.println("主叫开始: " + smd);
                        }
                        else if(smd.getC_CDRtype() == 5){
                            String key = smd.getC_CDRtype() + "_" + smd.getC_UserNum()  + "_" + smd.getC_RelateNum();
                            msCache.put(key, smd.getC_TimeStamp());
                            //System.out.println("被叫开始: " + smd);
                        }
                        else if(smd.getC_CDRtype() == 4){
                            String key = "3_" + smd.getC_UserNum()  + "_" + smd.getC_RelateNum();
                            Long lastTime = msCache.get(key);
                            if(lastTime != null){
                                //通话结束，开始时间是lastTime
                                msCache.remove(key);
                                System.out.println("主叫结束: 通话开始于 " + lastTime + " " + smd);
                            }
                        }
                        else if(smd.getC_CDRtype() == 6){
                            String key = "5_" + smd.getC_UserNum()  + "_" + smd.getC_RelateNum();
                            Long lastTime = msCache.get(key);
                            if(lastTime != null){
                                msCache.remove(key);
                                //通话结束，开始时间是lastTime
                                System.out.println("被叫结束: 通话开始于 " + lastTime + " " + smd);
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        } finally {

            latch.countDown();
        }
    }

}
