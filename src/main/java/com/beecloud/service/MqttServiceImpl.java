package com.beecloud.service;

import com.beecloud.mqtt.Runnable.MqttClientReceiveMessageRunnable;
import com.beecloud.mqtt.Runnable.MqttClientSendMessageRunnable;
import com.beecloud.mqtt.constansts.MessageMapper;
import com.beecloud.mqtt.entity.AuthObject;
import com.beecloud.mqtt.entity.SendMessageObject;
import com.beecloud.platform.protocol.core.constants.ApplicationID;
import com.beecloud.platform.protocol.core.datagram.BaseDataGram;
import com.beecloud.platform.protocol.core.element.Authentication;
import com.beecloud.platform.protocol.core.element.TimeStamp;
import com.beecloud.platform.protocol.core.element.VehicleDescriptor;
import com.beecloud.platform.protocol.core.header.ApplicationHeader;
import com.beecloud.platform.protocol.core.message.AbstractMessage;
import com.beecloud.platform.protocol.core.message.AckMessage;
import com.beecloud.platform.protocol.core.message.AuthReqMessage;
import com.beecloud.platform.protocol.core.message.BaseMessage;
import com.beecloud.platform.protocol.util.binary.ProtocolUtil;
import com.beecloud.util.UuidUtil;
import com.google.gson.Gson;
import com.jayway.jsonpath.JsonPath;
import org.apache.log4j.spi.LoggerFactory;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
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
    private String host = "tcp://10.28.4.34:1883";
    private MqttClientReceiveMessageRunnable MRMR = null;
    private MqttClientSendMessageRunnable MSMR = null;
    private String Tbox_Send_Topic = "mqtt/server";
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
        sendMessageObject.setTopic(Tbox_Send_Topic);
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
    public void sendUnencryptedMessage(String unEncryptedMessage) {
        Gson gson = new Gson();
        String applicationId = JsonPath.parse(unEncryptedMessage).read("$.applicationHeader.applicationID");
        int stepId = JsonPath.parse(unEncryptedMessage).read("$.applicationHeader.stepId");
        String key = applicationId+stepId;
        AbstractMessage abstractMessage = (AbstractMessage)gson.fromJson(unEncryptedMessage,MessageMapper.getMessage(key));
        BaseDataGram baseDataGram = new BaseDataGram();
        BaseMessage baseMessage = (BaseMessage)abstractMessage;
        baseDataGram.addMessage(baseMessage);
        try {
            MqttClient client = new MqttClient(host, UuidUtil.getUuid());
            MqttConnectOptions options = new MqttConnectOptions();
            options.setCleanSession(true);
            client.connect(options);
            MqttMessage msg = new MqttMessage();
            msg.setPayload(baseDataGram.encode());
            client.publish(Tbox_Send_Topic, msg);
            client.disconnect(3*1000);
            logger.info("功能测试:发送消息");
            logger.info(baseDataGram.toString());
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public static void main(String...args){
        String json = "{\n" +
                "    \"identity\": {\n" +
                "        \"identityCode\": 1061653173,\n" +
                "        \"totalLength\": 0,\n" +
                "        \"messageLength\": 0\n" +
                "    },\n" +
                "    \"timeStamp\": {\n" +
                "        \"date\": \"Nov 24, 2016 2:22:33 PM\",\n" +
                "        \"totalLength\": 0,\n" +
                "        \"messageLength\": 0\n" +
                "    },\n" +
                "    \"errorInfo\": {\n" +
                "        \"errorCode\": \"OK\",\n" +
                "        \"totalLength\": 0,\n" +
                "        \"messageLength\": 0\n" +
                "    },\n" +
                "    \"applicationHeader\": {\n" +
                "        \"applicationID\": \"START_CAR\",\n" +
                "        \"protocolVersion\": 0,\n" +
                "        \"stepId\": 3,\n" +
                "        \"sequenceId\": 123123,\n" +
                "        \"remainingLength\": 0\n" +
                "    }\n" +
                "}";
        new MqttServiceImpl().sendUnencryptedMessage(json);
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
