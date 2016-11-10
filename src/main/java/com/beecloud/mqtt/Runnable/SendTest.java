//package com.beecloud.mqtt.Runnable;
//
//import com.beecloud.mqtt.entity.SendMessageObject;
//import com.beecloud.platform.protocol.core.constants.ApplicationID;
//import com.beecloud.platform.protocol.core.constants.ErrorCode;
//import com.beecloud.platform.protocol.core.element.ErrorInfo;
//import com.beecloud.platform.protocol.core.element.Identity;
//import com.beecloud.platform.protocol.core.element.TimeStamp;
//import com.beecloud.platform.protocol.core.header.ApplicationHeader;
//import com.beecloud.platform.protocol.core.message.AckMessage;
//import com.beecloud.vehicle.spa.protocol.message.RequestMessage;
//import com.google.gson.Gson;
//
//import java.util.Date;
//
//public class SendTest {
//	public static void main(String[] args) {
//		sendAckMessage();
//	}
//
//
//	public static void sendRequestMessage(){
//		String host = "tcp://10.28.4.34:1883";
//		String topic = "hello";
//		String clientId = "Server";// clientId不能重复
//		SendMessageObject requestObject = new SendMessageObject();
//		requestObject.setTopic(topic);
//		RequestMessage requestMsg = new RequestMessage();
//		ApplicationHeader applicationHeader = new ApplicationHeader();
//		applicationHeader.setApplicationID(ApplicationID.ID_AUTH);
//		applicationHeader.setProtocolVersion(10);
//		applicationHeader.setStepId(2);
//		applicationHeader.setSequenceId(1);
//		Identity identity = new Identity();
//		identity.setIdentityCode(123);
//		TimeStamp timeStamp = new TimeStamp(new Date());
//		requestMsg.setApplicationHeader(applicationHeader);
//		requestMsg.setIdentity(identity);
//		requestMsg.setTimeStamp(timeStamp);
//
//		requestObject.setMessage(new Gson().toJson(requestMsg));
//
//		MqttClientSendMessageRunnable mr = new MqttClientSendMessageRunnable();
//		Thread thread = new Thread(mr);
//		thread.start();
//	}
//
//
//	public  static void sendAckMessage(){
//		String host = "tcp://10.28.4.34:1883";
//		String topic = "hello";
//		String clientId = "Server";// clientId不能重复
//		SendMessageObject requestObject = new SendMessageObject();
//		requestObject.setTopic(topic);
//		AckMessage ackMessage = new AckMessage();
//		Identity identity = new Identity();
//		identity.setIdentityCode(123);
//		ApplicationHeader applicationHeader = new ApplicationHeader();
//		applicationHeader.setApplicationID(ApplicationID.ID_AUTH);
//		applicationHeader.setProtocolVersion(10);
//		applicationHeader.setStepId(8);
//		applicationHeader.setSequenceId(1);
//		ErrorInfo errorInfo = new ErrorInfo(ErrorCode.AUTH_ERR);
//		ackMessage.setApplicationHeader(applicationHeader);
//		ackMessage.setIdentity(identity);
//		ackMessage.setErrorInfo(errorInfo);
//		TimeStamp timeStamp = new TimeStamp(new Date());
//		ackMessage.setTimeStamp(timeStamp);
//		requestObject.setMessage(new Gson().toJson(ackMessage));
//		MqttClientSendMessageRunnable mr = new MqttClientSendMessageRunnable();
//		Thread thread = new Thread(mr);
//		thread.start();
//        mr.addMessage(requestObject);
//	}
//}
