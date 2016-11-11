package com.beecloud.service;

/**
 * Created by dell on 2016/11/9.
 */
public interface MqttService {
    void run();
    void stop();
    void subscribeTopic(String topic);
    void sendAuthReqMessage(String authMessage);
    void sendMessaage(String message);
    String getMessageByKey(String key);
}
