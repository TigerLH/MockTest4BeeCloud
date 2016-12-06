package com.beecloud.service;

/**
 * Created by dell on 2016/11/9.
 */
public interface MqttService {
    void run(String type);
    void stop(String vin,String type);
    void sendAuthReqMessage(String authMessage,String type);
    void subscribeTopic(String topic,String type);
    void sendMessaage(String message,String type);
    void sendFunctionMessage(String json,String vin);
    String getMessageByKey(String key,int timeOut,String type);
}
