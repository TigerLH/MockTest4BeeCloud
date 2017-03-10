package com.beecloud.service;

/**
 * Created by dell on 2016/11/9.
 */
public interface MqttService {
    void disconnect(String threadName,String type);
    void connect(String threadName,String type);
    void sendMessaage(String threadName,String message,String type);
    void sendFunctionMessage(String json,String vin);
    void subscribeTopic(String threadName,String topic,String type);
    void unSubscribeTopic(String threadName,String topic,String type);
    String getAllMessage4Function(String threadName);
    void clean(String threadName,String type);
    String getMessage(String threadName,String key,int timeOut,String type);
}
