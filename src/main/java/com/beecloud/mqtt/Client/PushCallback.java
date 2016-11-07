/**
 * 
 */
package com.beecloud.mqtt.Client;

import com.beecloud.mqtt.Action.Command;
import com.beecloud.mqtt.Action.UN_LOCK_COMMAND;
import com.beecloud.mqtt.Listenser.MqttObserver;
import com.beecloud.mqtt.Listenser.MqttObserver;
import com.beecloud.mqtt.Listenser.MqttSubject;
import com.beecloud.platform.protocol.core.constants.ApplicationID;
import com.beecloud.vehicle.spa.protocol.message.ResponseMessage;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class PushCallback implements MqttCallback,MqttSubject{
	private List<MqttObserver> observers = new ArrayList<MqttObserver>();

	 private final Logger logger = LoggerFactory.getLogger(this.getClass());

	    public void connectionLost(Throwable cause) {
	    	//TODO 需要实现断线重连功能
	        System.out.println("连接断开");
	    }

	    public void deliveryComplete(IMqttDeliveryToken token) {
	        System.out.println("deliveryComplete---------" + token.isComplete());
	    }

	    //此处无法调用ack，只能返回ApplicationId给Tox处理
	    public void messageArrived(String topic, MqttMessage message) throws Exception {
	        byte[] response = message.getPayload();
	        ResponseMessage responseMessage = new ResponseMessage(response);
			ApplicationID applicationID = responseMessage.getApplicationHeader().getApplicationID();
            this.notifyMqttObservers(applicationID);
	    }

	@Override
	public void registerMqttObserver(MqttObserver o) {
		observers.add(o);
	}

	@Override
	public void removeMqttObserver(MqttObserver o) {
		int index = observers.indexOf(o);
		if (index != -1) {
			observers.remove(o);
		}
	}

    @Override
    public void notifyMqttObservers(ApplicationID applicationID) {
        for (MqttObserver observer : observers) {
            observer.onMessageReceive(applicationID);
        }
    }

}
