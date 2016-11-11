package com.beecloud.mqtt.entity;

/**
 * Created by dell on 2016/11/11.
 */
public class AuthObject {
    private String vin;
    private String iccid;
    private String tboxSerial;
    private String imei;

    public String getVin() {
        return vin;
    }

    public void setVin(String vin) {
        this.vin = vin;
    }

    public String getIccid() {
        return iccid;
    }

    public void setIccid(String iccid) {
        this.iccid = iccid;
    }

    public String getTboxSerial() {
        return tboxSerial;
    }

    public void setTboxSerial(String tboxSerial) {
        this.tboxSerial = tboxSerial;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    @Override
    public String toString() {
        return "AuthObject{" +
                "vin='" + vin + '\'' +
                ", iccid='" + iccid + '\'' +
                ", tboxSerial='" + tboxSerial + '\'' +
                ", imei='" + imei + '\'' +
                '}';
    }
}
