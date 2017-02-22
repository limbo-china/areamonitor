package cn.ac.iie.hy.areamonitor.cronjob;

import cn.ac.iie.hy.areamonitor.data.CountryQuery;
import cn.ac.iie.hy.areamonitor.data.GlobalArea;
import cn.ac.iie.hy.areamonitor.data.PersonInfo;
import cn.ac.iie.hy.areamonitor.data.ProvinceQuery;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Set;

/**
 * Created by Nemo on 2016/12/21 0021.
 */
public class ScheduledAreaNumberJobMinite implements Job {

    static Logger logger = null;

    static {
        PropertyConfigurator.configure("log4j.properties");
        logger = Logger.getLogger(ScheduledAreaNumberJobMinite.class.getName());
    }

    private String getCurrentTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(new Date(System.currentTimeMillis()));
    }

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {

        //ShardedJedis LBSJedis = null;
        try{
            //LBSJedis = ShardedJedisUtil.getSource();
            Map<String, Map<String, PersonInfo>> mapLocal = GlobalArea.mapLocal;
            //ShardedJedisPipeline LBSPipe = null;
            Map<String, Set<String>> configMap = GlobalArea.configMap;
            for(Map.Entry<String, Set<String>> entry : configMap.entrySet()){
                String areaID = entry.getKey();
                Set<String> areaSet = entry.getValue();
                int allCount = 0;
                int leaveCount = 0;
                int otherProvinceCount = 0;
                int otherCountryCount = 0;
                for(String uli : areaSet){
                    Map<String, PersonInfo> baseStation = mapLocal.get(uli);
                    if(baseStation == null){
                        continue;
                    }
                    for(String key : baseStation.keySet()){
                        PersonInfo p = baseStation.get(key);
                        if(System.currentTimeMillis()/1000 - p.getUpdateTime() > 60*60*2){
                            baseStation.remove(key);
                            leaveCount ++;
                        }
                        if(p.getImsi().length() > 3){
                            String country = CountryQuery.getCountryByImsi(key);
                            if(country != null && !country.equals("中国")) {
                                otherCountryCount++;
                                continue;
                            }
                        }
                        else{
                            System.out.println(p.getImsi());
                        }
                        if(p.getHomecode().length() >=3){
                            String province = ProvinceQuery.getProviceByCode(p.getHomecode());
                            if(province != null && !province.equals("新疆")){
                                otherProvinceCount++;
                            }
                        }

                    }
//                    LBSPipe = LBSJedis.pipelined();
//                    for (String key : baseStation.keySet()) {
//                        LBSPipe.get(key);
//                    }
//                    List<Object> resp = LBSPipe.syncAndReturnAll();
//                    for(Object obj : resp){
//                        if(obj != null){
//                            String[] records = obj.toString().split(";");
//                            if(records.length < 7){
//                                continue;
//                            }
//                            String curUli = records[6];
//                            if(!curUli.equals(uli)){
//                                baseStation.remove(records[0]);
//                                if(!areaSet.contains(curUli)){
//                                    leaveCount++;
//                                }
//
//                            }
//                        }
//                    }
                    //logger.info("Base Station " + uli + " Current User " + baseStation.size() + " Left User " + leaveCount);
                    allCount += baseStation.size();
                }
                //统计各项数值加载
                logger.info(areaID + " : " + allCount + "," + leaveCount + " 国外人数： " + otherCountryCount + " 外省人数 " +otherProvinceCount);
            }
            //ShardedJedisUtil.returnResource(LBSJedis);
        }
        catch (Exception e) {
            logger.error(e.getMessage());
            //ShardedJedisUtil.returnBrokenResource(LBSJedis);
        }

    }
}
