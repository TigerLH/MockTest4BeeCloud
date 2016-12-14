package com.beecloud.service;

/**
 * Created by dell on 2016/11/9.
 */
public interface MqttService {
    void disconnect(String vin,String type);
    void startAndAutoAuth(String authMessage,String type);
    void sendMessaage(String vin,String message,String type);
    void sendFunctionMessage(String json,String vin);
    void subscribeTopic(String vin,String topic,String type);
    String getAllMessage4Function(String vin);
    String getMessage(String vin,String key,int timeOut,String type);
}
