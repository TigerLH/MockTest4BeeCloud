/**
 * 
 */
package com.beecloud.mqtt.Client;

import com.beecloud.mqtt.Action.Command;
import com.beecloud.mqtt.Action.UN_LOCK_COMMAND;
import com.beecloud.mqtt.Listenser.MqttObserver;
import com.beecloud.platform.protocol.core.constants.ApplicationID;
import com.beecloud.vehicle.spa.protocol.message.ResponseMessage;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;

import java.util.HashMap;
import java.util.Map;

/**
 * @description //TODO
 * @author hong.lin@beecloud.com
 * @date 2016年10月11日 下午6:13:55
 * @version v1.0
 */
public class MockTbox implements MqttObserver{
	private static Map<ApplicationID,Command> map = new HashMap<ApplicationID,Command>();
	static {
		map.put(ApplicationID.UNLOCK,new UN_LOCK_COMMAND());
	}

	 public static void main(String[] args) throws Exception {
				new MockTbox().mockTest();
	    }

	    public void mockTest() throws Exception{
			String host = "tcp://10.28.4.34:1883";
			String topic = "hello";
			String clientId = "12345";// clientId不能重复
			MqttConnectOptions options = new MqttConnectOptions();
			options.setCleanSession(true);
			MqttClient client = new MqttClient(host, clientId);
			client.connect(options);
			PushCallback pushCallback = new PushCallback();
			client.setCallback(pushCallback);
			pushCallback.registerMqttObserver(this);
			while (true) {
				client.subscribe(topic, 2);
			}
		}

	@Override
	public void onMessageReceive(ApplicationID applicationID) {
		System.out.print("收到新推送====================>\n");
		System.out.print(applicationID.getDescription());
		//TODO 添加查询接口，如果用户未添加Mock值，则返回默认调用give方法
		byte[] data = map.get(applicationID).give();
		System.out.println(new ResponseMessage(data));
	}
}
