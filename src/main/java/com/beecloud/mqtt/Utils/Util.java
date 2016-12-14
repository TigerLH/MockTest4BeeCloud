package com.beecloud.mqtt.Utils;

import com.beecloud.mqtt.Entity.AuthObject;
import com.beecloud.mqtt.Entity.SendMessageObject;
import com.beecloud.mqtt.Runnable.MqttClientHandleMessageThread;
import com.beecloud.mqtt.constansts.MessageMapper;
import com.beecloud.platform.protocol.core.constants.ApplicationID;
import com.beecloud.platform.protocol.core.element.Authentication;
import com.beecloud.platform.protocol.core.element.TimeStamp;
import com.beecloud.platform.protocol.core.element.VehicleDescriptor;
import com.beecloud.platform.protocol.core.header.ApplicationHeader;
import com.beecloud.platform.protocol.core.message.AbstractMessage;
import com.beecloud.platform.protocol.core.message.AuthReqMessage;
import com.beecloud.platform.protocol.util.binary.ProtocolUtil;
import com.google.gson.Gson;
import com.jayway.jsonpath.JsonPath;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * @author hong.lin
 * @description
 * @date 2016/12/14.
 */
public class Util {
    private static final String Tbox_Send_Topic = "mqtt/server";
    private static Logger logger = LoggerFactory.getLogger("com.beecloud.mqtt.Utils.Util");
    /**
     * 生成认证消息
     * @return
     */
    public static AuthReqMessage getAuthReqMessage(AuthObject authObject){
        AuthReqMessage authReqMessage = new AuthReqMessage();
        ApplicationHeader applicationHeader = new ApplicationHeader();
        applicationHeader.setStepId(0);
        applicationHeader.setApplicationID(ApplicationID.ID_AUTH);
        applicationHeader.setSequenceId(0);
        applicationHeader.setProtocolVersion(0);
        Authentication authentication = new Authentication();
        authentication.setPid("BEECLOUD");

        String iccid = authObject.getIccid();
        String imei = authObject.getImei();
        String tboxSerial = authObject.getTboxSerial();
        VehicleDescriptor vehicleDescriptor = new VehicleDescriptor();
        vehicleDescriptor.setTboxSerial(tboxSerial);
        vehicleDescriptor.setIccid(iccid);
        vehicleDescriptor.setImei(imei);
        vehicleDescriptor.setVin(authObject.getVin());
        TimeStamp timeStamp = new TimeStamp(new Date());

        authReqMessage.setTimeStamp(timeStamp);
        authReqMessage.setApplicationHeader(applicationHeader);
        authReqMessage.setAuthentication(authentication);
        authReqMessage.setVehicleDescriptor(vehicleDescriptor);
        return authReqMessage;
    }



    /**
     * 转换接口参数中的json转换为AbstractMessage实体
     * @param message
     * @return
     */
    public static SendMessageObject transJsonToAbstractMessage(String message){
        Gson gson = new Gson();
        String applicationId = JsonPath.parse(message).read("$.applicationHeader.applicationID");
        int stepId = JsonPath.parse(message).read("$.applicationHeader.stepId");
        String key = applicationId+stepId;
        logger.info("发送消息:");
        logger.info("消息类型:"+ MessageMapper.getMessage(key).getName());
        AbstractMessage abstractMessage = (AbstractMessage)gson.fromJson(message, MessageMapper.getMessage(key));
        SendMessageObject sendMessageObject = new SendMessageObject();
        sendMessageObject.setTopic(Tbox_Send_Topic);
        sendMessageObject.setMessage(ProtocolUtil.bytesToFormatBitString(abstractMessage.encode()));
        return sendMessageObject;
    }


    /**
     * 停止对应的线程，断开client
     * @param vin
     * @param list
     */
    public static List<MqttClientHandleMessageThread> stopThreadByVin(String vin,List<MqttClientHandleMessageThread> list){
        List<MqttClientHandleMessageThread> updateThreadList = new ArrayList<MqttClientHandleMessageThread>();
        Iterator<MqttClientHandleMessageThread> iterator = list.iterator();
        while(iterator.hasNext()){
            MqttClientHandleMessageThread thread = iterator.next();
            if(thread.getName().equals(vin)){
                thread.forceDisconnect();
            }else {
                updateThreadList.add(thread);
            }
        }
        return updateThreadList;
    }

    /**
     * 发送消息到对应的线程中
     * @param vin
     * @param list
     * @param sendMessageObject
     */
    public static void sendMessageByVin(String vin,List<MqttClientHandleMessageThread> list,SendMessageObject sendMessageObject){
        for(MqttClientHandleMessageThread thread : list){
            if(thread.getName().equals(vin)){
                thread.sendMessage(sendMessageObject);
            }
        }
    }

    /**
     * 获取对应线程下mesaage
     * @param vin
     * @param list
     * @param key
     * @return
     */
    public static String getMessageByVin(String vin,List<MqttClientHandleMessageThread> list,String key){
        String message = "";
        for(MqttClientHandleMessageThread thread : list){
            if(thread.getName().equals(vin)&&thread.isAlive()){
                message = thread.getMessageBykey(key);
                break;
            }
        }
        return message;
    }

    /**
     * 查询对应的线程订阅消息
     * @param vin
     * @param list
     * @param topic
     */
    public static void subscribeByVin(String vin,List<MqttClientHandleMessageThread> list,String topic){
        for(MqttClientHandleMessageThread thread : list){
            if(thread.getName().equals(vin)){
                thread.subscirbe(topic);
                break;
            }
        }
    }
}