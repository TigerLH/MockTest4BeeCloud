package com.beecloud.mqtt.Runnable;

import com.beecloud.mqtt.Entity.SendMessageObject;
import com.beecloud.platform.protocol.core.datagram.BaseDataGram;
import com.beecloud.platform.protocol.core.message.BaseMessage;
import com.beecloud.platform.protocol.util.binary.ProtocolUtil;
import com.beecloud.util.UuidUtil;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.slf4j.Logger;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

public class MqttClientSendMessageRunnable implements Runnable{
	private MqttClient client = null;
	private boolean status = true;
	private Logger logger = org.slf4j.LoggerFactory.getLogger(this.getClass());
	private Queue<SendMessageObject> messages = new LinkedBlockingQueue<SendMessageObject>();
	private String host;

	
	public MqttClientSendMessageRunnable(String host){
		this.host = host;
	}

	public String getClientId(){
		return UuidUtil.getUuid();
	}


	public void addMessage(SendMessageObject sendMessageObject){
		messages.add(sendMessageObject);
	}


	@Override
	public void run() {
		try {
			status = true;
			client = new MqttClient(host, getClientId());
			MqttConnectOptions options = new MqttConnectOptions();
			options.setCleanSession(true);
			client.connect(options);
			while (status) {
				if(messages.isEmpty()){
					continue;
				}
				SendMessageObject messageObject = messages.poll();
				String topic = messageObject.getTopic();
				String message = messageObject.getMessage();
				byte[] data = ProtocolUtil.formatBitStringToBytes(message);
				BaseDataGram baseDataGram = new BaseDataGram();
				BaseMessage baseMessage = new BaseMessage(data);
				baseDataGram.addMessage(baseMessage);
				MqttMessage msg = new MqttMessage();
				msg.setPayload(baseDataGram.encode());
				logger.info("消息体:");
				logger.info(baseMessage.toString());
				client.publish(topic, msg);
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
	}



}
