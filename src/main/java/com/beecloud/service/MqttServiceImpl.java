package com.beecloud.service;

import com.beecloud.mqtt.Runnable.MqttClientReceiveMessageRunnable;
import com.beecloud.mqtt.Runnable.MqttClientSendMessageRunnable;
import com.beecloud.mqtt.entity.AuthObject;
import com.beecloud.mqtt.entity.SendMessageObject;
import com.beecloud.platform.protocol.core.constants.ApplicationID;
import com.beecloud.platform.protocol.core.element.Authentication;
import com.beecloud.platform.protocol.core.element.TimeStamp;
import com.beecloud.platform.protocol.core.element.VehicleDescriptor;
import com.beecloud.platform.protocol.core.header.ApplicationHeader;
import com.beecloud.platform.protocol.core.message.AuthReqMessage;
import com.beecloud.platform.protocol.util.binary.ProtocolUtil;
import com.google.gson.Gson;
import org.apache.log4j.spi.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * Created by dell on 2016/11/9.
 *
 */
@Service
public class MqttServiceImpl implements MqttService{
    private Logger logger = org.slf4j.LoggerFactory.getLogger(this.getClass());
    MqttClientReceiveMessageRunnable MRMR = null;
    MqttClientSendMessageRunnable MSMR = null;
    private String Tbox_Auth_Topic = "mqtt/server";
    private String Tbox_Channel_Topic = "mqtt/vehicle/%s";


    /**
     * 生成认证消息
     * @return
     */
    protected AuthReqMessage getAuthReqMessage(AuthObject authObject){
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

    @Override
    public void run() {
        MSMR = new MqttClientSendMessageRunnable();
        Thread sendThread = new Thread(MSMR);
        sendThread.start();
        MRMR = new MqttClientReceiveMessageRunnable();
        Thread receiveThread = new Thread(MRMR);
        receiveThread.start();
    }

    @Override
    public void stop() {
        if(null!=MRMR){
            MRMR.disconnetc();
            MRMR = null;
        }
        if(null!=MSMR){
            MSMR.disconnect();
            MSMR = null;
        }
    }

    @Override
    public void sendAuthReqMessage(String authMessage) {
        Gson gson = new Gson();
        AuthObject authObject = gson.fromJson(authMessage,AuthObject.class);
        MRMR.addTopic(String.format(Tbox_Channel_Topic,authObject.getVin()));//订阅认证ack topic

        SendMessageObject sendMessageObject = new SendMessageObject();
        AuthReqMessage authReqMessage = getAuthReqMessage(authObject);
        sendMessageObject.setMessage(ProtocolUtil.bytesToFormatBitString(authReqMessage.encode()));
        sendMessageObject.setTopic(Tbox_Auth_Topic);
        MSMR.addMessage(sendMessageObject);  //发布认证message
    }

    @Override
    public void subscribeTopic(String topic){
        MRMR.addTopic(topic);
    }

    @Override
    public void sendMessaage(String message) {
        Gson gson = new Gson();
        SendMessageObject sendMessageObject = gson.fromJson(message,SendMessageObject.class);
        MSMR.addMessage(sendMessageObject);
    }

    @Override
    public String getMessageByKey(String key,int timeOut) {
        long start = System.currentTimeMillis();
        while((System.currentTimeMillis()-start)<timeOut*1000){  //设置超时时间
            String message = MRMR.getMessageBykey(key);
            if(null!=message&&!"".equals(message)){
                logger.info("ReturnMessage for key:"+key);
                logger.info(message);
                return message;
            }
        }
        return "nothing to be found";
    }
}
