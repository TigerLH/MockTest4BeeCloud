package com.beecloud.service;

import com.beecloud.mqtt.constansts.Type;

/**
 * Created by dell on 2016/11/9.
 */
public interface MqttService {
    void run(Type type);
    void stop(Type type);
    void sendAuthReqMessage(String authMessage,Type type);
    void subscribeTopic(String topic,Type type);
    void sendMessaage(String message);
    void sendFunctionMessage(String json,String vin);
    String getMessageByKey(String key,int timeOut,Type type);
}
