package com.beecloud.mqtt.Runnable;

import com.beecloud.mqtt.entity.ReceiveMessageObject;

/**
 * Created by dell on 2016/11/9.
 */
public class ReceiveTest {
    public static  void main(String ...args){
        String host = "tcp://10.28.4.34:1883";
        String topic = "hello";
        String clientId = "12345";// clientId不能重复
        ReceiveMessageObject receiveMessageObject = new ReceiveMessageObject();
        receiveMessageObject.setHost(host);
        receiveMessageObject.setClientId(clientId);
        receiveMessageObject.setTopic(topic);
        MqttClientReceiveMessageRunnable receiveMessageRunnable = new MqttClientReceiveMessageRunnable(receiveMessageObject);
        Thread receive = new Thread(receiveMessageRunnable);
        receive.start();
    }
}
