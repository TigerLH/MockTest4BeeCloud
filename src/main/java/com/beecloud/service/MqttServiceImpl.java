package com.beecloud.service;

import com.beecloud.mqtt.Runnable.MqttClientReceiveMessageRunnable;
import com.beecloud.mqtt.entity.ReceiveMessageObject;
import com.beecloud.mqtt.entity.SendMessageObject;
import com.beecloud.mqtt.listenser.MqttObserver;
import org.springframework.stereotype.Service;

/**
 * Created by dell on 2016/11/9.
 *
 */
@Service
public class MqttServiceImpl implements MqttService{
    @Override
    public String runMqttSendMessageClinet(SendMessageObject sendMessageObject) {

        return null;
    }

    @Override
    public String runMqttReceiverMessageClient(ReceiveMessageObject receiveMessageObject) {
        MqttClientReceiveMessageRunnable MRMR = new MqttClientReceiveMessageRunnable(receiveMessageObject);
        Thread receiveThread = new Thread(MRMR);
        receiveThread.start();
        while(true){     //TODO 设置timeout
            if(MRMR.getResponse()!=null){
                return  MRMR.getResponse();
            }
        }
    }
}
