package com.beecloud.service;

import com.beecloud.mqtt.entity.ReceiveMessageObject;
import com.beecloud.mqtt.entity.SendMessageObject;

/**
 * Created by dell on 2016/11/9.
 */
public interface MqttService {
    String runMqttSendMessageClinet(SendMessageObject sendMessageObject);
    String runMqttReceiverMessageClient(ReceiveMessageObject receiveMessageObject);
}
