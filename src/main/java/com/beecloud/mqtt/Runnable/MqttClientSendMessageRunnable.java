package com.beecloud.mqtt.Runnable;

import com.beecloud.mqtt.entity.SendMessageObject;
import com.beecloud.platform.protocol.core.datagram.BaseDataGram;
import com.beecloud.platform.protocol.core.message.BaseMessage;
import com.beecloud.platform.protocol.util.binary.ProtocolUtil;
import com.beecloud.util.UuidUtil;
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
			while (true) {
				Iterator<SendMessageObject> iterator = messages.iterator();
				while (iterator.hasNext()) {
					SendMessageObject messageObject = iterator.next();
					String topic = messageObject.getTopic();
					String message = messageObject.getMessage();
					//System.out.println(message);
					byte[] data = ProtocolUtil.formatBitStringToBytes(message);
					BaseDataGram baseDataGram = new BaseDataGram();
					BaseMessage baseMessage = new BaseMessage(data);
					baseDataGram.addMessage(baseMessage);
					MqttMessage msg = new MqttMessage();
					msg.setPayload(baseDataGram.encode());
					client.publish(topic, msg);
					iterator.remove();
					System.out.println("SendMessage to:" + topic);
				}
			}
		}catch (MqttException e) {
			e.printStackTrace();
		}
	}



}
