package cn.ac.iie.hy.areamonitor.data;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class ProvinceQuery {
    private static final Map<String, String> ProvinceRegionMap = new HashMap<String, String>();
    private static final Map<String, String> RegionCodeMap = new HashMap<String, String>();

    static {
        String fileName1 = "homelist.txt";
        String fileName2 = "provlist.txt";

        try {
            String encoding = "UTF-8";
            File file1 = new File(fileName1);
            {
                InputStreamReader read = new InputStreamReader(
                        new FileInputStream(file1), encoding);
                BufferedReader bufferedReader = new BufferedReader(read);
                String lineTxt = null;
                while ((lineTxt = bufferedReader.readLine()) != null) {
                    String[] sArray = lineTxt.split(":");
                    RegionCodeMap.put(sArray[0], sArray[1]);
                }
                read.close();
            }

            File file2 = new File(fileName2);
            {
                InputStreamReader read = new InputStreamReader(
                        new FileInputStream(file2), encoding);
                BufferedReader bufferedReader = new BufferedReader(read);
                String lineTxt = null;
                while ((lineTxt = bufferedReader.readLine()) != null) {
                    String[] sArray = lineTxt.split(":");
                    ProvinceRegionMap.put(sArray[0], sArray[1]);
                }
                read.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String getRegionByCode(String code) {
        return RegionCodeMap.get(code);
    }

    private static String getProvinceByRegion(String region) {
        return ProvinceRegionMap.get(region);
    }

    public static String getProviceByCode(String code) {
        return ProvinceQuery.getRegionByCode(code) == null?null:ProvinceQuery.getProvinceByRegion(ProvinceQuery.getRegionByCode(code));
    }
}

