package com.beecloud.mqtt.Runnable;

import com.beecloud.mqtt.listenser.MqttObserver;
import com.beecloud.mqtt.listenser.MqttSubject;
import com.beecloud.platform.protocol.core.datagram.BaseDataGram;
import com.beecloud.platform.protocol.core.header.ApplicationHeader;
import com.beecloud.platform.protocol.core.message.AbstractMessage;
import com.beecloud.platform.protocol.core.message.AckMessage;
import com.beecloud.platform.protocol.core.message.BaseMessage;
import com.beecloud.util.UuidUtil;
import com.beecloud.vehicle.spa.protocol.message.RequestMessage;
import com.google.gson.Gson;
import com.jayway.jsonpath.JsonPath;
import org.eclipse.paho.client.mqttv3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * Created by dell on 2016/11/9.
 */
public class MqttClientReceiveMessageRunnable implements Runnable,MqttObserver {
	private MqttClient client = null;
	private Logger logger = LoggerFactory.getLogger(this.getClientId());
	private List<String> topics = new ArrayList<String>(); //需要订阅的Topic列表
	private String host = "tcp://10.28.4.34:1883";
	private static Map<String,String> cache = new HashMap<String,String>();
	private boolean status = true;
	public MqttClientReceiveMessageRunnable(){
	}

	public void addTopic(String topic){
		topics.add(topic);
	}

	public void setMessage(String key,String message){
		cache.put(key,message);
	}

	public String getMessageBykey(String key){
		return cache.get(key);
	}

	public String getClientId(){
		return UuidUtil.getUuid();
	}


	public void disconnetc(){
		try {
			while(status){
				if(topics.size()==0){
					client.disconnectForcibly(1000);
					client = null;
					status = false;
				}
			}

		} catch (MqttException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		MqttConnectOptions options = new MqttConnectOptions();
		options.setCleanSession(true);
		try {
			client = new MqttClient(host, getClientId());
			client.connect(options);
			while(status){
					if(topics.size()==0){
						return;
					}
					Iterator<String> iterator = topics.iterator();
					while(iterator.hasNext()){
						String topic = iterator.next();
						PushCallback pushCallback = new PushCallback();
						pushCallback.registerMqttObserver(this);
						client.setCallback(pushCallback);
						client.subscribe(topic, 2);
						logger.info("订阅消息:"+topic);
						iterator.remove();
					}
			}
		} catch (MqttException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onMessageReceive(String keyword,String message) {
		    System.out.println(keyword+":"+message);
			setMessage(keyword,message);
	}
}


/**
 * MQTT消息回调
 * @author dell
 *
 */
class PushCallback implements MqttCallback,MqttSubject {
	private String prefix = "mqtt/vehicle";
	private List<MqttObserver> observers = new ArrayList<MqttObserver>();
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	public void connectionLost(Throwable cause) {
		logger.error("Receive Client连接断开");
	}

	public void deliveryComplete(IMqttDeliveryToken token) {
		logger.info("deliveryComplete---------" + token.isComplete());
	}


	/**
	 * 接收Tbox消息,并已topic+applicationID+stepId+sequenceId做为key进行存储
	 * @param topic
	 * @param message
     */
	protected   void receiveTboxMessaage(String topic,MqttMessage message){
		try {
			AbstractMessage abstractMessage = null;
			BaseDataGram baseDataGram = new BaseDataGram(message.getPayload());
			List<BaseMessage> baseMessages = baseDataGram.getMessages();
			BaseMessage baseMessage = baseMessages.get(0);
			byte[] data = baseMessage.encode();
			//需要根据StepId和ApplicationId判断对应的业务
			ApplicationHeader applicationHeader = baseMessage.getApplicationHeader();
			int applicationID = applicationHeader.getApplicationID().getApplicationID();
			int stepId = applicationHeader.getStepId();
			long sequenceId = applicationHeader.getSequenceId();
			if (stepId == 2) {
				logger.info("接收Tbox消息:");
				logger.info("消息类型:RequestMessage");
				abstractMessage = new RequestMessage(data);
			} else if (stepId == 8||stepId ==1) {
				logger.info("接收Tbox消息:");
				logger.info("消息类型:AckMessage");
				abstractMessage = new AckMessage(data);
			}
			logger.info(abstractMessage.toString());
			String keyword = String.valueOf(topic) + String.valueOf(applicationID) + String.valueOf(stepId) + String.valueOf(sequenceId);
			if (null != abstractMessage) {
				Gson gson = new Gson();
				this.notifyMqttObservers(keyword, gson.toJson(abstractMessage));
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	/**
	 * 接收App消息,并以MsgId做为key进行存储
	 * @param topic
	 * @param message
     */
	protected void receiveAppMessage(String topic, MqttMessage message){
			byte[] data = message.getPayload();
			String msg = new String(data);
			String key = JsonPath.parse(msg).read("$.mqttHeader.msgId");
		    logger.info("收到App推送消息:");
			logger.info(msg);
		    this.notifyMqttObservers(key,msg);
	}


	public void messageArrived(String topic, MqttMessage message) throws Exception {
			if(topic.startsWith(prefix)){		//Tbox消息
				receiveTboxMessaage(topic,message);
			}else{								//APP消息
				receiveAppMessage(topic,message);
			}
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
