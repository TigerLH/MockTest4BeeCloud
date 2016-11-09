package com.beecloud.mqtt.Runnable;

import com.beecloud.mqtt.entity.SendMessageObject;
import com.beecloud.platform.protocol.core.message.AbstractMessage;
import com.beecloud.platform.protocol.util.binary.ProtocolUtil;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class MqttClientSendMessageRunnable implements Runnable{
	private AbstractMessage sendMessage;

	private MqttClient mqttClient;
	
	private SendMessageObject requestObject;
	
	public MqttClientSendMessageRunnable(SendMessageObject requestObject){
		this.requestObject = requestObject;
	}
	
	@Override
	public void run() {
		String clientId = requestObject.getClientId();
		String host = requestObject.getHost();
		String topic = requestObject.getTopic();
        int times = requestObject.getTimes();
        int interval = requestObject.getInterval();
		Object message = requestObject.getMessage();
        sendMessage = (AbstractMessage)message;
		byte[] data = sendMessage.encode();
		MqttConnectOptions options = new MqttConnectOptions();
		options.setCleanSession(true);
		try {
			mqttClient = new MqttClient(host, clientId);
			mqttClient.connect(options);
			MqttMessage msg = new MqttMessage();
			msg.setPayload(data);
            if(times>1){
                for (int i=0;i<times;i++){
                    mqttClient.publish(topic, msg);
                    System.out.println("发送成功");
                    Thread.sleep(interval*1000);
                }
            }else{
                System.out.println("发送成功");
                mqttClient.publish(topic, msg);
            }
		} catch (MqttException e) {
			System.err.println("发送失败");
			e.printStackTrace();
		} catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
