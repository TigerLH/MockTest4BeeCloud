/**
 * 
 */
package com.beecloud.mqtt.mockserver;

import com.beecloud.mqtt.Listenser.MqttObserver;
import com.beecloud.platform.protocol.core.constants.ApplicationID;
import com.beecloud.platform.protocol.core.constants.ErrorCode;
import com.beecloud.platform.protocol.core.element.ErrorInfo;
import com.beecloud.platform.protocol.core.element.FunctionCommandStatus;
import com.beecloud.platform.protocol.core.element.Identity;
import com.beecloud.platform.protocol.core.element.TimeStamp;
import com.beecloud.platform.protocol.core.header.ApplicationHeader;
import com.beecloud.vehicle.spa.protocol.config.rawdata.unlock.UnlockNotificationRawData;
import com.beecloud.vehicle.spa.protocol.message.ResponseMessage;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.fusesource.mqtt.client.FutureConnection;
import org.fusesource.mqtt.client.MQTT;
import org.fusesource.mqtt.client.QoS;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @description //TODO
 * @author hong.lin@beecloud.com
 * @date 2016年10月11日 下午6:13:24
 * @version v1.0
 */
public class MockServer {
	public static void main(String[] args) throws Exception {

	        String host = "tcp://10.28.4.34:1883";
	        String topic = "hello";
	        String clientId = "server";// clientId不能重复
	        MqttConnectOptions options = new MqttConnectOptions();
	        options.setCleanSession(true);
	        MqttClient client = new MqttClient(host, clientId);
	        client.connect(options);
	        MqttMessage message = new MqttMessage();
            message.setPayload(productMessage());
            while(true){
            	client.publish(topic, message);
            	Thread.sleep(5000);
            }



//		 MQTT mqtt = new MQTT();
//		 mqtt.setHost("10.28.4.34",1883);
//		 mqtt.setClientId("server");
//		 //连接前清空会话信息
//		 mqtt.setCleanSession(true);
//		 FutureConnection connection= mqtt.futureConnection();
//		 connection.connect();
//		 while(true){
//			 connection.publish(topic,productMessage(), QoS.AT_LEAST_ONCE,false);
//			 Thread.sleep(5000);
//		 }


	    }
	 
	 
	 
	 public static byte[] productMessage(){
		 	ResponseMessage notifyMsg = new ResponseMessage();
	        ApplicationHeader applicationHeader = new ApplicationHeader();
	        applicationHeader.setApplicationID(ApplicationID.UNLOCK);
	        applicationHeader.setProtocolVersion(10);
	        applicationHeader.setStepId(13);
	        applicationHeader.setSequenceId(1);
	        
	        Identity authToken = new Identity();
	        authToken.setIdentityCode(123);

	        TimeStamp timeStamp = new TimeStamp();
	        timeStamp.setDate(new Date());

	        ErrorInfo errorInfo = new ErrorInfo();
	        errorInfo.setErrorCode(ErrorCode.OK);

	        UnlockNotificationRawData rawData = new UnlockNotificationRawData();
	        rawData.setStatus(UnlockNotificationRawData.UnlockStatus.MANUAL_LOCKED);

	        FunctionCommandStatus commandStatus = new FunctionCommandStatus();
	        commandStatus.setStatus(FunctionCommandStatus.CommandStatus.COMPLETE);
	        commandStatus.setRawData(rawData.encode());

	        notifyMsg.setApplicationHeader(applicationHeader);
	        notifyMsg.setIdentity(authToken);
	        notifyMsg.setTime(timeStamp);
	        notifyMsg.setError(errorInfo);
	        notifyMsg.setStatus(commandStatus);

	        byte[] data = notifyMsg.encode();
	        return data;
	 }
	 
	 
	 
	 
}
