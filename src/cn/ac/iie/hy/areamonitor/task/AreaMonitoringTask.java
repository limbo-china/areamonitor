package cn.ac.iie.hy.areamonitor.task;

import cn.ac.iie.hy.areamonitor.data.GlobalArea;
import cn.ac.iie.hy.areamonitor.data.PersonInfo;
import cn.ac.iie.hy.areamonitor.data.SMetaData;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;

public class AreaMonitoringTask implements Runnable {

	CountDownLatch latch = null;
	List<SMetaData> cacheList  = null;

	public AreaMonitoringTask(CountDownLatch latch, List<SMetaData> cacheList) {
		super();
		this.latch = latch;
		this.cacheList = cacheList;
	}
	private boolean ifLeft(int cdrType){
		return cdrType==2;
	}
	
	@Override
	public void run() {
		Map<String, Map<String, PersonInfo>> mapLocal = GlobalArea.mapLocal;
		for(SMetaData smd : cacheList){
			if(smd.getC_uli() == null||smd.getC_uli().isEmpty()||smd.getC_Imsi().length() <= 10){
				continue;
			}
			
			Map<String, PersonInfo> baseStation = mapLocal.get(smd.getC_uli());
			if(baseStation == null){
				baseStation = new ConcurrentHashMap<String, PersonInfo>();
				mapLocal.put(smd.getC_uli(), baseStation);
			}
			if(ifLeft(smd.getC_CDRtype())){
				if(baseStation.containsKey(smd.getC_Imsi())){
					baseStation.remove(smd.getC_Imsi());
				}
			}
			else{
				baseStation.put(smd.getC_Imsi(), new PersonInfo(smd.getC_Imsi(), smd.getC_Imei(), smd.getC_UserNum(),smd.getC_HomeCode() ,smd.getC_TimeStamp()));
			}
		}

		latch.countDown();
	}
}
