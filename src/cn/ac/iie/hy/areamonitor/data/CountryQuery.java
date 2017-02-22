package cn.ac.iie.hy.areamonitor.data;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;


public class CountryQuery {
    private static final Map<String, String> CountryMccMap = new HashMap<String, String>();

    static {
        String fileName = "countrylist.txt";

        try {
            String encoding = "UTF-8";
            File file = new File(fileName);
            InputStreamReader read = new InputStreamReader(
                    new FileInputStream(file), encoding);
            BufferedReader bufferedReader = new BufferedReader(read);
            String lineTxt = null;
            while ((lineTxt = bufferedReader.readLine()) != null) {
                String[] sArray = lineTxt.split(":");
                CountryMccMap.put(sArray[0], sArray[1]);
            }
            read.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getCountryByImsi(String imsi) {
        return CountryMccMap.get(imsi.substring(0, 3));
    }

}
