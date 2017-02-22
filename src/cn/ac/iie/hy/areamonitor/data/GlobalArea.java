package cn.ac.iie.hy.areamonitor.data;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class GlobalArea {

	public static Map<String, Map<String, PersonInfo>> mapLocal = new ConcurrentHashMap<String, Map<String, PersonInfo>>();

	public static Map<String, Set<String>> configMap= new ConcurrentHashMap();

	public static Set<String> importantPersion = new HashSet<>();

}
