package com.beecloud.service;

/**
 * Created by dell on 2016/11/9.
 */
public interface MqttService {
    void run();
    void stop();
    void sendAuthReqMessage(String authMessage);
    void subscribeTopic(String topic);
    void sendMessaage(String message);
    void sendUnencryptedMessage(String json);
    String getMessageByKey(String key,int timeOut);
}
