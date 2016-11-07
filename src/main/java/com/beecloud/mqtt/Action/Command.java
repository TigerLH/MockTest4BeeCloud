/**
 * 
 */
package com.beecloud.mqtt.Action;

/**
 * @description //MQTT Client回调函数中根据不同的command返回不同的mock值
 * @author hong.lin@beecloud.com
 * @date 2016-11-7 上午11:19:35
 * @version v1.0
 */
public interface Command {
	/**
	 * 根据用户指定返回
	 * @return
     */
	byte[] mock();

	/**
	 * 默认返回
	 * @return
     */
	byte[] give();
}
