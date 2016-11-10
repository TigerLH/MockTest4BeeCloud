package com.beecloud.mqtt.Runnable;

import com.beecloud.mqtt.listenser.MqttObserver;
import com.beecloud.mqtt.listenser.MqttSubject;
import com.beecloud.platform.protocol.core.header.ApplicationHeader;
import com.beecloud.platform.protocol.core.message.AbstractMessage;
import com.beecloud.platform.protocol.core.message.AckMessage;
import com.beecloud.platform.protocol.core.message.BaseMessage;
import com.beecloud.vehicle.spa.protocol.message.RequestMessage;
import com.google.gson.Gson;
import org.eclipse.paho.client.mqttv3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dell on 2016/11/10.
 */
public class ReturnTest {
     public static void main(String ...args){
         String host = "tcp://10.28.4.34:1883";
         String topic = "hello_MOCK";
         MqttConnectOptions options = new MqttConnectOptions();
         options.setCleanSession(true);
         try {
         MqttClient client = new MqttClient(host, "12312312");
         client.connect(options);
         PushCallback pushCallback = new PushCallback();
         client.setCallback(pushCallback);
         while (true){
                 client.subscribe(topic, 2);
             }
         } catch (MqttException e) {
             e.printStackTrace();
         }
     }


    static class PushCallback implements MqttCallback {
        private final Logger logger = LoggerFactory.getLogger(this.getClass());

        public void connectionLost(Throwable cause) {
            System.out.println("连接断开");
        }

        public void deliveryComplete(IMqttDeliveryToken token) {
            System.out.println("deliveryComplete---------" + token.isComplete());
        }


        public void messageArrived(String topic, MqttMessage message) throws Exception {
            System.out.println("Response:");
            byte[] data = message.getPayload();
            System.out.println(new String(data));
        }
    }

}
