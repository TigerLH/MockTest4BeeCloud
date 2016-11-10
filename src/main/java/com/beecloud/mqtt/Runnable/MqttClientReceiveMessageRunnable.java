package com.beecloud.mqtt.Runnable;

import com.beecloud.mqtt.listenser.MqttObserver;
import com.beecloud.mqtt.listenser.MqttSubject;
import com.beecloud.platform.protocol.core.header.ApplicationHeader;
import com.beecloud.platform.protocol.core.message.AbstractMessage;
import com.beecloud.platform.protocol.core.message.AckMessage;
import com.beecloud.platform.protocol.core.message.BaseMessage;
import com.beecloud.util.UuidUtil;
import com.beecloud.vehicle.spa.protocol.message.RequestMessage;
import com.google.gson.Gson;
import org.eclipse.paho.client.mqttv3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dell on 2016/11/9.
 */
public class MqttClientReceiveMessageRunnable implements Runnable,MqttObserver {
	private MqttClient client = null;
	private List<String> topics = new ArrayList<String>(); //需要订阅的Topic列表
	String host = "tcp://10.28.4.34:1883";
	String clientId = UuidUtil.getUuid();// clientId不能重复
	public boolean status = true;
	public MqttClientReceiveMessageRunnable( ){
	}

	public void addTopic(String topic){
		topics.add(topic);
	}

	public List<String> getTopics(){
		return topics;
	}

	public void removeTopic(String topic){
	    topics.remove(topic);
    }
	public void cleanTopics(){
		topics.clear();
	}

	public void disconnetc(){
		try {
			client.disconnectForcibly(1000);
			status = false;
		} catch (MqttException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		MqttConnectOptions options = new MqttConnectOptions();
		options.setCleanSession(true);
		try {
			client = new MqttClient(host, clientId);
			client.connect(options);
			while(status){
				if(topics.size()>0){
					for(String topic : topics){
						PushCallback pushCallback = new PushCallback();
						pushCallback.registerMqttObserver(this);
						client.setCallback(pushCallback);
						client.subscribe(topic, 2);
					}
				}
			}
		} catch (MqttException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onMessageReceive(String topic,String message) {
		System.out.println(message);
		byte[] response = message.getBytes();
		MqttMessage msg = new MqttMessage();
		msg.setPayload(response);
		String response_topic = topic+"_MOCK";
		System.out.println("Publish:"+response_topic);
		try {
			client.publish(response_topic,msg);
		} catch (MqttException e) {
			e.printStackTrace();
		}
	}
}


/**
 * MQTT消息回调
 * @author dell
 *
 */
class PushCallback implements MqttCallback,MqttSubject {
	private List<MqttObserver> observers = new ArrayList<MqttObserver>();
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	public void connectionLost(Throwable cause) {
		System.out.println("连接断开");
	}

	public void deliveryComplete(IMqttDeliveryToken token) {
		System.out.println("deliveryComplete---------" + token.isComplete());
	}


	public void messageArrived(String topic, MqttMessage message) throws Exception {
		AbstractMessage arriveMessage = null;
		byte[] data = message.getPayload();
		//需要根据StepId和ApplicationId判断对应的业务
		BaseMessage baseMessage = new BaseMessage(data);
		ApplicationHeader applicationHeader = baseMessage.getApplicationHeader();
		int stepId= applicationHeader.getStepId();
		if(stepId==2){
			System.out.println("收到RequestMessage");
			arriveMessage = new RequestMessage(data);
		}else if(stepId==8){
			System.out.println("收到AckMessage");
			arriveMessage = new AckMessage(data);
		}
		Gson gson = new Gson();
		this.notifyMqttObservers(topic,gson.toJson(arriveMessage));
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
	public void notifyMqttObservers(String topic,String messaage) {
		for (MqttObserver observer : observers) {
			observer.onMessageReceive(topic,messaage);
		}
	}

}
