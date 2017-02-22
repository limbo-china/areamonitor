package cn.ac.iie.hy.areamonitor.task;

import cn.ac.iie.hy.areamonitor.data.GlobalArea;
import cn.ac.iie.hy.areamonitor.data.SMetaData;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CountDownLatch;

public class StaffMonitoringTask implements Runnable {

	static Logger logger = null;
	CountDownLatch latch = null;
	List<SMetaData> cacheList  = null;

	static {
		PropertyConfigurator.configure("log4j.properties");
		logger = Logger.getLogger(StaffMonitoringTask.class.getName());
	}
	
	public StaffMonitoringTask(CountDownLatch latch, List<SMetaData> cacheList) {
		super();
		this.latch = latch;
		this.cacheList = cacheList;
	}

	@Override
	public void run() {
		try {
			Set<String> importantPerson = GlobalArea.importantPersion;
			for(SMetaData smd : cacheList){
				if(importantPerson.contains(smd.getC_UserNum())){
					logger.info("User " + smd.getC_UserNum() + " is now in circle");
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
		}finally{
			latch.countDown();
		}

	}

}
