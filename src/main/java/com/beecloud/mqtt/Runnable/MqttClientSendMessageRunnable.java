package com.beecloud.mqtt.Runnable;

import com.beecloud.mqtt.entity.SendMessageObject;
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
	String clientId = UuidUtil.getUuid();// clientId不能重复
	public boolean status = true;
	private List<SendMessageObject> messages = new ArrayList<SendMessageObject>();
	

	
	public MqttClientSendMessageRunnable(){
	}

	public void addMessage(SendMessageObject sendMessageObject){
		messages.add(sendMessageObject);
	}

	public void disconnect(){
		try {
			client.disconnectForcibly(100);
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
			while(status) {
					Iterator<SendMessageObject> iterator = messages.iterator();
					while(iterator.hasNext()){
						SendMessageObject messageObject = iterator.next();
						String topic = messageObject.getTopic();
						String message = messageObject.getMessage();
						System.out.println(message);
						byte[] data = ProtocolUtil.formatBitStringToBytes(message);
						MqttMessage msg = new MqttMessage();
						msg.setPayload(data);
						client.publish(topic, msg);
						iterator.remove();
						System.out.println("SendMessage");
					}
				}
			}catch (MqttException e) {
				e.printStackTrace();
			}
	}



}