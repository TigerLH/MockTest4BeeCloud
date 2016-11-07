package com.beecloud.mqtt.Listenser;


import com.beecloud.platform.protocol.core.constants.ApplicationID;

public interface MqttObserver {
	void onMessageReceive(ApplicationID applicationID);
}
