package cn.ac.iie.hy.areamonitor.data;

/**
 * Created by Nemo on 2016/12/21 0021.
 */
public class PersonInfo {
    String imsi;
    String imei;
    String msisdn;
    String homecode;
    Long updateTime;

    public PersonInfo(String imsi, String imei, String msisdn, String homecode, Long updateTime) {
        this.imsi = imsi;
        this.imei = imei;
        this.msisdn = msisdn;
        this.homecode = homecode;
        this.updateTime = updateTime;
    }

    public String getImsi() {
        return imsi;
    }

    public void setImsi(String imsi) {
        this.imsi = imsi;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public String getMsisdn() {
        return msisdn;
    }

    public void setMsisdn(String msisdn) {
        this.msisdn = msisdn;
    }

    public String getHomecode() {
        return homecode;
    }

    public void setHomecode(String homecode) {
        this.homecode = homecode;
    }

    public Long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Long updateTime) {
        this.updateTime = updateTime;
    }
}
