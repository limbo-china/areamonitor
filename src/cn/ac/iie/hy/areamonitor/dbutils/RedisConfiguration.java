package cn.ac.iie.hy.areamonitor.dbutils;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import java.io.FileInputStream;
import java.util.*;

/**
 * Created by NemoCH on 2016/12/21 0021.
 */
public class RedisConfiguration {

    static Logger logger = null;
    Map<String, String> param2Value = new HashMap<String, String>();
    private static RedisConfiguration conf = null;
    {
        PropertyConfigurator.configure("log4j.properties");
        logger = Logger.getLogger(RedisConfiguration.class.getName());
    }

    private RedisConfiguration() {

    }
    public static RedisConfiguration getConfiguration(String pConfFileName) {
        if (conf != null) {
            return conf;
        } else {
            conf = new RedisConfiguration();
            try {
                conf.loadConfiguration(pConfFileName);
                conf.dumpConfiguration();
                return conf;
            } catch (Exception ex) {
                logger.warn("loading configuration from file " + pConfFileName + " is failed for " + ex.getMessage(), ex);
                return null;
            }
        }
    }

    private void loadConfiguration(String pConfFileName) throws Exception {
        FileInputStream fis = null;

        try {
            fis = new FileInputStream(pConfFileName);
            Properties pros = new Properties();
            pros.load(fis);
            Set params = pros.keySet();
            Iterator itr = params.iterator();
            String key = null;
            while (itr.hasNext()) {
                key = (String) itr.next();
                param2Value.put(key, pros.getProperty(key));
            }
        } finally {
            try {
                fis.close();
            } catch (Exception ex) {
                //do noting
            }
        }
    }

    public int getInt(String pParamName, int pDefaultValue) throws RuntimeException {
        String valueStr = param2Value.get(pParamName);
        int value = pDefaultValue;
        if (valueStr != null && !valueStr.equals("")) {
            try {
                value = Integer.parseInt(valueStr);
            } catch (Exception ex) {
                throw new RuntimeException("can not cast value " + valueStr + " correctly into a integer for parameter " + pParamName);
            }
        }
        return value;
    }

    public boolean getBoolean(String pParamName, boolean pDefaultValue) throws RuntimeException {
        String valueStr = param2Value.get(pParamName);
        boolean value = pDefaultValue;
        if (valueStr != null && !valueStr.equals("")) {
            try {
                value = Boolean.parseBoolean(valueStr);
            } catch (Exception ex) {
                throw new RuntimeException("can not cast value " + valueStr + " correctly into a boolean value for parameter " + pParamName);
            }
        }
        return value;
    }

    public String getString(String pParamName, String pDefaultValue) {
        String valueStr = param2Value.get(pParamName);
        String value = pDefaultValue;
        if (valueStr != null) {
            value = valueStr;
        }
        return value;
    }

    //for debug
    public void dumpConfiguration() {
        Set params = param2Value.keySet();
        Iterator itr = params.iterator();
        String key = null;
        while (itr.hasNext()) {
            key = (String) itr.next();
            logger.debug(key + "=" + param2Value.get(key));
        }
    }
}
