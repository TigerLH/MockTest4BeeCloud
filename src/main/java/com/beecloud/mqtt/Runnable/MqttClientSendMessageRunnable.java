package com.beecloud.mqtt.Runnable;

import com.beecloud.mqtt.Entity.SendMessageObject;
import com.beecloud.mqtt.constansts.MessageMapper;
import com.beecloud.platform.protocol.core.datagram.BaseDataGram;
import com.beecloud.platform.protocol.core.header.ApplicationHeader;
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
				ApplicationHeader applicationHeader = baseMessage.getApplicationHeader();
				int stepId = applicationHeader.getStepId();
				String applicatonName = applicationHeader.getApplicationID().name();
				String key = applicatonName+stepId;
				logger.info("功能测试:发送消息");
				logger.info("消息类型:"+ MessageMapper.getMessage(key).getName());
				logger.info(baseMessage.toString());
				MqttMessage msg = new MqttMessage();
				msg.setPayload(baseDataGram.encode());
				client.publish(topic, msg);
			}
		}catch (Exception e) {
			//this.disconnect();
			e.printStackTrace();
		}
	}



}
