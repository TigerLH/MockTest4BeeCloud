package com.beecloud.mqtt.Utils;

import com.alibaba.fastjson.JSON;
import com.beecloud.mqtt.Entity.SendMessageObject;
import com.beecloud.mqtt.Runnable.MqttClientHandleMessageThread;
import com.beecloud.mqtt.constansts.MessageMapper;
import com.beecloud.platform.protocol.core.message.AbstractMessage;
import com.beecloud.platform.protocol.util.binary.ProtocolUtil;
import com.jayway.jsonpath.JsonPath;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author hong.lin
 * @description
 * @date 2016/12/14.
 */
public class Util {
    private static final String Tbox_Send_Topic = "mqtt/server";
    private static Logger logger = LoggerFactory.getLogger("com.beecloud.mqtt.Utils.Util");


    /**
     * 功能测试
     * 转换接口参数中的json转换为AbstractMessage实体
     * @param message
     * @return
     */
    public static SendMessageObject transJsonToAbstractMessage(String message){
        String applicationId = JsonPath.parse(message).read("$.applicationHeader.applicationID");
        int stepId = JsonPath.parse(message).read("$.applicationHeader.stepId");
        String key = applicationId+stepId;
        logger.info("发送消息:");
        logger.info("消息类型:"+ MessageMapper.getMessage(key).getName());
        AbstractMessage abstractMessage = (AbstractMessage)JSON.parseObject(message, MessageMapper.getMessage(key));
        SendMessageObject sendMessageObject = new SendMessageObject();
        sendMessageObject.setTopic(Tbox_Send_Topic);
        sendMessageObject.setMessage(ProtocolUtil.bytesToFormatBitString(abstractMessage.encode()));
        return sendMessageObject;
    }


    /**
     * 停止对应的线程，断开client
     * @param ThreadName
     * @param list
     */
    public static List<MqttClientHandleMessageThread> stopThreadByThreadName(String ThreadName,List<MqttClientHandleMessageThread> list){
        List<MqttClientHandleMessageThread> updateThreadList = new ArrayList<MqttClientHandleMessageThread>();
        Iterator<MqttClientHandleMessageThread> iterator = list.iterator();
        while(iterator.hasNext()){
            MqttClientHandleMessageThread thread = iterator.next();
            if(thread.getName().equals(ThreadName)){
                thread.forceDisconnect();
            }else {
                updateThreadList.add(thread);
            }
        }
        return updateThreadList;
    }


    /**
     * 发送消息到对应的线程中
     * @param ThreadName
     * @param list
     * @param sendMessageObject
     */
    public static void sendMessageByThreadName(String ThreadName,List<MqttClientHandleMessageThread> list,SendMessageObject sendMessageObject){
        for(MqttClientHandleMessageThread thread : list){
            if(thread.getName().equals(ThreadName)){
                thread.sendMessage(sendMessageObject);
            }
        }
    }

    /**
     * 获取对应线程下mesaage
     * @param ThreadName
     * @param list
     * @param key
     * @return
     */
    public static String getMessageByThreadName(String ThreadName,List<MqttClientHandleMessageThread> list,String key){
        for(MqttClientHandleMessageThread thread : list){
            if(thread.getName().equals(ThreadName)&&thread.isAlive()){
                return thread.getMessageBykey(key);
            }
        }
        return "";
    }

    /**
     * 订阅消息
     * @param threadName
     * @param list
     * @param topic
     */
    public static void subscribeByThreadName(String threadName,List<MqttClientHandleMessageThread> list,String topic){
        for(MqttClientHandleMessageThread thread : list){
            if(thread.getName().equals(threadName)){
                thread.subscirbe(topic);
                break;
            }
        }
    }

    /**
     * 退订消息
     * @param ThreadName
     * @param list
     * @param topic
     */
    public static void unSubscribeByThreadName(String ThreadName,List<MqttClientHandleMessageThread> list,String topic){
        for(MqttClientHandleMessageThread thread : list){
            if(thread.getName().equals(ThreadName)){
                thread.unSubscribe(topic);
                break;
            }
        }
    }


    /**
     * 清除数据缓存
     * @param mqttClientHandleMessageThreads
     * @param ThreadName
     */
    public static void cleanCache(List<MqttClientHandleMessageThread> mqttClientHandleMessageThreads,String ThreadName){
        for (MqttClientHandleMessageThread thread : mqttClientHandleMessageThreads) {
            if (ThreadName.equals(thread.getName())&&thread.isAlive()) {
                thread.cleanCache();
            }
        }
    }

    /**
     * 判断字符串是否为数值
     * @param str
     * @return
     */
    public static boolean isNumeric(String str){
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if( !isNum.matches() ){
            return false;
        }
        return true;
    }
}
