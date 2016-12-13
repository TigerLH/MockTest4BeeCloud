package com.beecloud.mqtt.Runnable;

import com.beecloud.mqtt.constansts.MessageMapper;
import com.beecloud.mqtt.listenser.MqttObserver;
import com.beecloud.mqtt.listenser.MqttSubject;
import com.beecloud.platform.protocol.core.datagram.BaseDataGram;
import com.beecloud.platform.protocol.core.header.ApplicationHeader;
import com.beecloud.platform.protocol.core.message.AbstractMessage;
import com.beecloud.platform.protocol.core.message.BaseMessage;
import com.beecloud.util.UuidUtil;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.jayway.jsonpath.JsonPath;
import org.eclipse.paho.client.mqttv3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by dell on 2016/11/9.
 */
public class MqttClientReceiveMessageRunnable implements Runnable,MqttObserver {
	private MqttClient client = null;
	private Logger logger = LoggerFactory.getLogger(this.getClientId());
	private Queue<String> topics = new LinkedBlockingQueue<String>();
	private static Map<String,String> cache = new ConcurrentHashMap<String,String>();
	private String host;
	public MqttClientReceiveMessageRunnable(String host){
		this.host = host;
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

	public String getAllMessagebyVin(String  topic){
		JsonArray jsonArray = new JsonArray();
		Set<String> sets = cache.keySet();
		Iterator<String> iterator = sets.iterator();
		while (iterator.hasNext()){
			String key = iterator.next();
			if(key.contains(topic)){
				jsonArray.add(cache.get(key));
			}
		}
		return jsonArray.toString();
	}


	public String getClientId(){
		return UuidUtil.getUuid();
	}

	/**
	 * 退订topic
	 * @param topic
     */
	public void unsubscribe(String topic){
		try {
			if(null!=client){
				client.unsubscribe(topic);
				Set<String> sets = cache.keySet();
				Iterator<String> iterator = sets.iterator();
				while (iterator.hasNext()){
					String key = iterator.next();
					if(key.contains(topic)){
						cache.remove(key);
						logger.info("移除缓存数据:"+key);
					}
				}
			}
		} catch (Exception e) {
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
			while(true){
				if(topics.isEmpty()){
					continue;
				}
				String topic = topics.poll();
				PushCallback pushCallback = new PushCallback();
				pushCallback.registerMqttObserver(this);
				client.setCallback(pushCallback);
				client.subscribe(topic, 2);
				logger.info("订阅消息:"+topic);
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
			Object object = con.newInstance(data);
			logger.info(object.toString());
			String keyword = String.valueOf(topic) + String.valueOf(applicationID) + String.valueOf(stepId) + String.valueOf(sequenceId);
			if (null != object) {
				Gson gson = new Gson();
				this.notifyMqttObservers(keyword, gson.toJson(object));
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
			try{
				key = ((ArrayList<String>)JsonPath.parse(msg).read("$..msgId")).get(0);
			}catch(Exception e){
				String applicationId = ((ArrayList<String>)JsonPath.parse(msg).read("$..applicationId")).get(0);
				String stepId = ((ArrayList<String>)JsonPath.parse(msg).read("$..stepId")).get(0);
				long tboxEventTime =  ((ArrayList<Long>)JsonPath.parse(msg).read("$..tboxEventTime")).get(0);
				key = String.valueOf(applicationId)+String.valueOf(stepId)+String.valueOf(tboxEventTime);
			}
			logger.info("映射的Key为:");
			logger.info(key);
		    this.notifyMqttObservers(key,msg);
	}

	public static void main(String ...args){
		String msg = " {\"serviceHeader\":{\"applicationId\":\"1\",\"stepId\":\"7\",\"msgId\":\"1480488965807|VIN99999901\"},\"data\":{\"state\":false,\"tboxEventTime\":1480488966000,\"notiyfyMsg\":\"浠诲姟鎵ц\uE511鏉′欢涓嶆弧瓒砡杞﹁締姝ｅ湪琛岄┒\",\"plate\":\"宸滱99901\"}}";
		String key = "";
//		try{
//			key = ((ArrayList<String>)JsonPath.parse(msg).read("$..msgId")).get(0);
//		}catch(Exception e){
//			String applicationId = ((ArrayList<String>)JsonPath.parse(msg).read("$..applicationId")).get(0);
//			String stepId = ((ArrayList<String>)JsonPath.parse(msg).read("$..stepId")).get(0);
//			long tboxEventTime =  ((ArrayList<Long>)JsonPath.parse(msg).read("$..tboxEventTime")).get(0);
//			key = String.valueOf(applicationId)+String.valueOf(stepId)+String.valueOf(tboxEventTime);
//		}
		System.out.println(key);
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
