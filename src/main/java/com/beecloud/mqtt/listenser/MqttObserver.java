package com.beecloud.mqtt.listenser;

public interface MqttObserver {
	void onMessageReceive(String topic,String messaage);
}
