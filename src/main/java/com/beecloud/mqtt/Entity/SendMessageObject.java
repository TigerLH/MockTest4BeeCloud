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
	 * MQTT地址
     */
	private String host;
	/**
	 * MQTT topic
     */
	private String topic;

	/**
	 * MQTT Clitent ID
     */
	private String clientId;
	/**
	 * 发送次数
     */
	private int times;
	/**
	 * 发送间隔
     */
	private int interval;
	/**
	 * 发送的消息
     */
	private Object message;

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public int getTimes() {
		return times;
	}

	public void setTimes(int times) {
		this.times = times;
	}

	public int getInterval() {
		return interval;
	}

	public void setInterval(int interval) {
		this.interval = interval;
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
				"host='" + host + '\'' +
				", topic='" + topic + '\'' +
				", clientId='" + clientId + '\'' +
				", times=" + times +
				", interval=" + interval +
				", message=" + message +
				'}';
	}
}
