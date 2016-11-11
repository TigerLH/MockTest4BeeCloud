package com.beecloud.service;

import com.beecloud.mqtt.Runnable.MqttClientReceiveMessageRunnable;
import com.beecloud.mqtt.Runnable.MqttClientSendMessageRunnable;
import com.beecloud.mqtt.entity.SendMessageObject;
import com.google.gson.Gson;
import org.springframework.stereotype.Service;

/**
 * Created by dell on 2016/11/9.
 *
 */
@Service
public class MqttServiceImpl implements MqttService{
    MqttClientReceiveMessageRunnable MRMR = new MqttClientReceiveMessageRunnable();
    MqttClientSendMessageRunnable MSMR = new MqttClientSendMessageRunnable();


    @Override
    public void runMqttSendMessageServer() {
        Thread sendThread = new Thread(MSMR);
        sendThread.start();
    }

    @Override
    public void sendMessaage(String message) {
        Gson gson = new Gson();
        SendMessageObject sendMessageObject = gson.fromJson(message,SendMessageObject.class);
        MSMR.addMessage(sendMessageObject);
    }

    @Override
    public void disconnectSendMessageServer() {
        MSMR.disconnect();
    }

    @Override
    public void runMqttReceiverMessageServer() {
        Thread receiveThread = new Thread(MRMR);
        receiveThread.start();
    }

    @Override
    public void subscribeTopic(String topic) {
        MRMR.addTopic(topic);
    }

    @Override
    public void disconnectReceiveMessageServer() {
        MRMR.disconnetc();
    }

}