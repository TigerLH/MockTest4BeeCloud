/**
 * 
 */
package com.beecloud.mqtt.entity;


/**
 * @description //TODO
 * @author hong.lin@beecloud.com
 * @date 2016-11-9 上午11:20:48
 * @version v1.0
 */
public class SendMessageObject {
	/**
	 * MQTT topic
     */
	private String topic;

	/**
	 * 发送的消息体
     */
	private Object message;

	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

	public Object getMessage() {
		return message;
	}

	public void setMessage(Object message) {
		this.message = message;
	}

	@Override
	public String toString() {
		return "SendMessageObject{" +
				"topic='" + topic + '\'' +
				", message=" + message +
				'}';
	}
}
