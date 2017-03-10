package com.beecloud.mqtt.Runnable;

import com.beecloud.mqtt.Entity.SendMessageObject;
import com.beecloud.mqtt.Utils.Util;
import com.beecloud.mqtt.constansts.MessageMapper;
import com.beecloud.mqtt.listenser.MqttObserver;
import com.beecloud.mqtt.listenser.MqttSubject;
import com.beecloud.platform.protocol.core.datagram.BaseDataGram;
import com.beecloud.platform.protocol.core.header.ApplicationHeader;
import com.beecloud.platform.protocol.core.message.AbstractMessage;
import com.beecloud.platform.protocol.core.message.BaseMessage;
import com.beecloud.platform.protocol.util.binary.ProtocolUtil;
import com.beecloud.util.UuidUtil;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.jayway.jsonpath.JsonPath;
import org.eclipse.paho.client.mqttv3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by dell on 2016/11/9.
 */
public class MqttClientHandleMessageThread extends Thread implements MqttObserver {
	private MqttClient client = null;
	private Logger logger = LoggerFactory.getLogger(this.getClientId());
	private Queue<SendMessageObject> messages = new LinkedBlockingQueue<SendMessageObject>();
	private Map<String,String> cache = new HashMap<String, String>();
	private String host;
	private boolean status = true;
	public MqttClientHandleMessageThread(String host,String threadName){
		this.host = host;
		this.setName(threadName);	//给线程设置名称,根据名称释放线程
	}

	public void setMessage(String key,String message){
		cache.put(key,message);
	}

	public String getMessageBykey(String key){
		if(cache.containsKey(key)){
			return cache.get(key);
		}
		if(key.endsWith("*")){
			String matcher = key.substring(0,key.length()-1);
			Set<String> sets = cache.keySet();
			for(String set : sets){
				if(set.startsWith(matcher)){
					return cache.get(set);
				}
			}
		}
		return "";
	}


	public void subscirbe(String topic){
		if(null!=client){
			try {
				logger.info("订阅消息:"+topic);
				client.subscribe(topic, 2);
			} catch (MqttException e) {
				e.printStackTrace();
			}
		}
	}

	public void unSubscribe(String topic){
		if(null!=client){
			try {
				logger.info("退订消息:"+topic);
				client.unsubscribe(topic);
			} catch (MqttException e) {
				e.printStackTrace();
			}
		}
	}


	public void  sendMessage(SendMessageObject sendMessageObject){
		messages.add(sendMessageObject);
	}

	public String getAllMessage(){
		JsonArray jsonArray = new JsonArray();
		Set<String> sets = cache.keySet();
		Iterator<String> iterator = sets.iterator();
		while (iterator.hasNext()){
			String key = iterator.next();
				jsonArray.add(cache.get(key));
		}
		return jsonArray.toString();
	}


	public String getClientId(){
		return UuidUtil.getUuid();
	}

	public void cleanCache(){
		this.cache.clear();
	}

	/**
	 * 强制断开连接
     */
	public void forceDisconnect(){
		try {
			client.disconnectForcibly();
			cache.clear();
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
//			String topic = String.format(Tbox_Receive_Topic,vin);
			client = new MqttClient(host, getClientId());
			client.connect(options);
			PushCallback pushCallback = new PushCallback();
			pushCallback.registerMqttObserver(this);
			client.setCallback(pushCallback);
//			this.subscirbe(topic);
			while(status){
				if(messages.isEmpty()){
					continue;
				}
				SendMessageObject messageObject = messages.poll();
				String send_topic = messageObject.getTopic();
				String message = messageObject.getMessage();
				byte[] data = ProtocolUtil.formatBitStringToBytes(message);
				BaseDataGram baseDataGram = new BaseDataGram();
				BaseMessage baseMessage = new BaseMessage(data);
				baseDataGram.addMessage(baseMessage);
				MqttMessage msg = new MqttMessage();
				msg.setPayload(baseDataGram.encode());
				logger.info("消息体:");
				logger.info(baseMessage.toString());
				client.publish(send_topic, msg);
			}
		} catch (MqttException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onMessageReceive(String keyword,String message) {
		    logger.info(keyword+":"+message);
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
		logger.error("Mqtt Client连接断开");
	}

	public void deliveryComplete(IMqttDeliveryToken token) {
		logger.info("deliveryComplete:" + token.isComplete());
	}


	/**
	 * 接收Tbox消息,并已topic+applicationID+stepId+sequenceId做为key进行存储
	 * @param topic
	 * @param message
     */
	protected void receiveTboxMessaage(String topic,MqttMessage message){
		try {
			BaseDataGram baseDataGram = new BaseDataGram(message.getPayload());
			List<BaseMessage> baseMessages = baseDataGram.getMessages();
			BaseMessage baseMessage = baseMessages.get(0);
			byte[] data = baseMessage.encode();
			//需要根据StepId和ApplicationId判断对应的业务
			ApplicationHeader applicationHeader = baseMessage.getApplicationHeader();
			int applicationID = applicationHeader.getApplicationID().getApplicationID();
			String name = applicationHeader.getApplicationID().name();
			int stepId = applicationHeader.getStepId();
			long sequenceId = applicationHeader.getSequenceId();
			String key = name+stepId;
			logger.info("Tbox收到消息:");
			logger.info("消息类型:");
			logger.info(MessageMapper.getMessage(key).getName());
//			Constructor<?> cons[] = MessageMapper.getMessage(key).getConstructors();
			Constructor con = MessageMapper.getMessage(key).getConstructor(byte[].class);
			AbstractMessage abstractMessage = (AbstractMessage)con.newInstance(data);
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
		    String key;
			String msg = new String(data);
			logger.info("收到App推送消息:");
			logger.info(msg);
		    boolean isNum = Util.isNumeric(topic);
		    if(!isNum){				//CarRental
		    	key = topic;
			}else{
				try{
					key = ((ArrayList<String>)JsonPath.parse(msg).read("$..msgId")).get(0);
				}catch(Exception e){
					String applicationId = ((ArrayList<String>)JsonPath.parse(msg).read("$..applicationId")).get(0);
					String stepId = ((ArrayList<String>)JsonPath.parse(msg).read("$..stepId")).get(0);
					long tboxEventTime =  ((ArrayList<Long>)JsonPath.parse(msg).read("$..tboxEventTime")).get(0);
					key = String.valueOf(applicationId)+String.valueOf(stepId)+String.valueOf(tboxEventTime);
				}
			}
			logger.info("映射的Key为:");
			logger.info(key);
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
