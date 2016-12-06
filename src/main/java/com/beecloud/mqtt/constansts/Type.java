package com.beecloud.mqtt.constansts;

/**
 * @author hong.lin
 * @description
 * @date 2016/12/5.
 */
public enum Type {
    FUNCTION("FUNCTION"),   //功能测试
    AUTOTEST("AUTOTEST");   //自动化测试
    private String code;
    Type(String code){
        this.code = code;
    }
    public String getCode() {
        return code;
    }
    public void setCode(String code) {
        this.code = code;
    }

}
