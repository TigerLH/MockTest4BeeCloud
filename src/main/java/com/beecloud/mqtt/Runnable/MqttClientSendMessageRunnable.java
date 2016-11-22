package com.beecloud.mqtt.Runnable;

import com.beecloud.mqtt.entity.SendMessageObject;
import com.beecloud.platform.protocol.core.datagram.BaseDataGram;
import com.beecloud.platform.protocol.core.header.ApplicationHeader;
import com.beecloud.platform.protocol.core.message.BaseMessage;
import com.beecloud.platform.protocol.util.binary.ProtocolUtil;
import com.beecloud.util.UuidUtil;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MqttClientSendMessageRunnable implements Runnable{
	String host = "tcp://10.28.4.34:1883";
	private MqttClient client = null;
	private boolean status = true;
	private Logger logger = org.slf4j.LoggerFactory.getLogger(this.getClass());
	private List<SendMessageObject> messages = new ArrayList<SendMessageObject>();
	

	
	public MqttClientSendMessageRunnable(){
	}

	public String getClientId(){
		return UuidUtil.getUuid();
	}


	public void addMessage(SendMessageObject sendMessageObject){
		messages.add(sendMessageObject);
	}

	public void disconnect(){
		try {
			client.disconnect(10*1000);
			status = false;
			client = null;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		try {
			client = new MqttClient(host, getClientId());
			MqttConnectOptions options = new MqttConnectOptions();
			options.setCleanSession(true);
			client.connect(options);
			while (status) {
				List<SendMessageObject> current = messages;
				Iterator<SendMessageObject> iterator = current.iterator();
				while (iterator.hasNext()) {
					SendMessageObject messageObject = iterator.next();
					String topic = messageObject.getTopic();
					String message = messageObject.getMessage();
					byte[] data = ProtocolUtil.formatBitStringToBytes(message);
					BaseDataGram baseDataGram = new BaseDataGram();
					BaseMessage baseMessage = new BaseMessage(data);
					baseDataGram.addMessage(baseMessage);
					ApplicationHeader applicationHeader = baseMessage.getApplicationHeader();
					int stepId = applicationHeader.getStepId();
					if(stepId==3){
						logger.info("=====================发送AckMessage=================================");
					}else if(stepId==5){
						logger.info("=====================发送响应消息===================================");
					}else if(stepId==0){
						logger.info("=====================发送认证消息===================================");
					}
					logger.info(baseMessage.toString());
					MqttMessage msg = new MqttMessage();
					msg.setPayload(baseDataGram.encode());
					client.publish(topic, msg);
					iterator.remove();
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
	}



}
