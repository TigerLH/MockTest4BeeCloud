package com.beecloud.mqtt.entity;

/**
 * Created by dell on 2016/11/9.
 */
public class ReceiveMessageObject {
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

    @Override
    public String toString() {
        return "ReceiveMessageObject{" +
                "host='" + host + '\'' +
                ", topic='" + topic + '\'' +
                ", clientId='" + clientId + '\'' +
                '}';
    }
}
