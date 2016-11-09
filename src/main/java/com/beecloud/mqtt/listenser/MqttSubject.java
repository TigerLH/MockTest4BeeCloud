/**
 * 
 */
package com.beecloud.mqtt.listenser;



public interface MqttSubject {
	 void registerMqttObserver(MqttObserver o);

	 void removeMqttObserver(MqttObserver o);

	 void notifyMqttObservers(String message);

}
