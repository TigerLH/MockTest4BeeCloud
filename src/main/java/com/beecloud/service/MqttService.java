package com.beecloud.service;

/**
 * Created by dell on 2016/11/9.
 */
public interface MqttService {
    void runMqttSendMessageServer();
    void disconnectSendMessageServer();
    void runMqttReceiverMessageServer();
    void disconnectReceiveMessageServer();
    void subscribeTopic(String topic);
    void sendMessaage(String message);
}
