/**
 * 
 */
package com.beecloud.mqtt.Listenser;


import com.beecloud.platform.protocol.core.constants.ApplicationID;
import com.beecloud.vehicle.spa.protocol.message.RequestMessage;

public interface MqttSubject {
	 void registerMqttObserver(MqttObserver o);

	 void removeMqttObserver(MqttObserver o);

	 void notifyMqttObservers(ApplicationID applicationID);

}
