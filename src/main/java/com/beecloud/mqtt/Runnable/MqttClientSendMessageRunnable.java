package com.beecloud.mqtt.Runnable;

import com.beecloud.mqtt.entity.SendMessageObject;
import com.beecloud.platform.protocol.core.datagram.BaseDataGram;
import com.beecloud.platform.protocol.core.header.ApplicationHeader;
import com.beecloud.platform.protocol.core.message.AbstractMessage;
import com.beecloud.platform.protocol.core.message.AckMessage;
import com.beecloud.platform.protocol.core.message.AuthReqMessage;
import com.beecloud.platform.protocol.core.message.BaseMessage;
import com.beecloud.platform.protocol.util.binary.ProtocolUtil;
import com.beecloud.util.UuidUtil;
import com.beecloud.vehicle.spa.protocol.message.ResponseMessage;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MqttClientSendMessageRunnable implements Runnable{
	String host = "tcp://10.28.4.34:1883";
	private MqttClient client = null;
	private boolean status = true;
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
			client.disconnectForcibly(1000);
			status = false;
			client = null;
		} catch (MqttException e) {
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
				Iterator<SendMessageObject> iterator = messages.iterator();
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
						System.out.println("StepId=3,发送AckMessage");
						AckMessage ackMessage = new AckMessage(data);
						System.out.println(ackMessage);
					}else if(stepId==5){
						System.out.println("StepId=5,发送ResponseMessage");
					}else if(stepId==0){
						System.out.println("StepId=0,发送AuthReqMessage");
					}
					MqttMessage msg = new MqttMessage();
					msg.setPayload(baseDataGram.encode());
					client.publish(topic, msg);
					iterator.remove();
					System.out.println("SendMessage to:" + topic);
					System.out.println("SendMessage:" + baseMessage);
				}
			}
		}catch (MqttException e) {
			e.printStackTrace();
		}
	}



}
