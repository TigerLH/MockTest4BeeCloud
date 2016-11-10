package com.beecloud.service;

import com.beecloud.mqtt.entity.ReceiveMessageObject;
import com.beecloud.mqtt.entity.SendMessageObject;

/**
 * Created by dell on 2016/11/9.
 */
public interface MqttService {
    void runMqttSendMessageServer();
    void disconnectSendMessageServer();
    void runMqttReceiverMessageServer();
    void disconnectReceiveMessageServer();
    void subscribeTopic(String topic);
    void sendMessaage(SendMessageObject sendMessageObject);
}
