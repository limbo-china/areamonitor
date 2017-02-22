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

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by Nemo on 2016/12/21 0021.
 */
public class ScheduledAreaInfoJobHour implements Job {

    static Logger logger = null;

    static {
        PropertyConfigurator.configure("log4j.properties");
        logger = Logger.getLogger(ScheduledAreaInfoJobHour.class.getName());
    }

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        Map<String, Set<String>> configMap = GlobalArea.configMap;
        Map<String, Map<String, PersonInfo>> mapLocal = GlobalArea.mapLocal;
        for (Map.Entry<String, Set<String>> entry : configMap.entrySet()) {
            String areaID = entry.getKey();
            Set<String> areaSet = entry.getValue();
            Map<String, Integer> otherCountryMap = new HashMap<>();
            Map<String, Integer> otherProvinceMap = new HashMap<>();
            for (String uli : areaSet) {
                Map<String, PersonInfo> baseStation = mapLocal.get(uli);
                if(baseStation == null){
                    continue;
                }
                for (String imsi : baseStation.keySet()) {
                    //统计人员构成
                    PersonInfo p = baseStation.get(imsi);
                    String country = CountryQuery.getCountryByImsi(imsi);
                    if(country != null && !country.equals("中国")){
                        otherCountryMap.put(country, otherCountryMap.get(country)==null ? 1 : otherCountryMap.get(country) + 1);
                        continue;
                    }
                    if(p.getHomecode()!=null && p.getHomecode().length() >= 3){
                        String province = ProvinceQuery.getProviceByCode(p.getHomecode());
                        if(province != null && !province.equals("新疆")){
                            otherProvinceMap.put(province, otherProvinceMap.get(province)== null? 1: otherProvinceMap.get(province)+ 1);
                        }
                    }
                }
            }
            for(String country : otherCountryMap.keySet()){
                logger.info(country + " : " + otherCountryMap.get(country));
            }
            for(String province : otherProvinceMap.keySet()){
                logger.info(province + " : " + otherProvinceMap.get(province));
            }
            //汇总统计结果
        }
    }
}
