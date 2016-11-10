/**
 * 
 */
package com.beecloud.mqtt.entity;


import com.beecloud.mqtt.constansts.MessageType;

import java.util.Arrays;

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
	private String message;

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "SendMessageObject{" +
                "topic='" + topic + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
