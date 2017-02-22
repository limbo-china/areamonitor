package cn.ac.iie.hy.areamonitor.cronjob;

import cn.ac.iie.hy.areamonitor.data.GlobalArea;
import cn.ac.iie.hy.areamonitor.data.PersonInfo;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.util.Map;
import java.util.Set;

/**
 * Created by Nemo on 2016/12/21 0021.
 */
public class ScheduledBsInfoJobHour implements Job {
    static Logger logger = null;

    static {
        PropertyConfigurator.configure("log4j.properties");
        logger = Logger.getLogger(ScheduledBsInfoJobHour.class.getName());
    }

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        Map<String, Set<String>> configMap = GlobalArea.configMap;
        Map<String, Map<String, PersonInfo>> mapLocal = GlobalArea.mapLocal;
        for (Map.Entry<String, Set<String>> entry : configMap.entrySet()) {
            String areaID = entry.getKey();
            Set<String> areaSet = entry.getValue();
            for (String uli : areaSet) {
                Map<String, PersonInfo> baseStation = mapLocal.get(uli);
                if(baseStation == null){
                    continue;
                }

                //统计基站信息
            }

        }
    }
}
