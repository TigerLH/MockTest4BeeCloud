package com.beecloud.service;

import com.beecloud.mqtt.Entity.AuthObject;
import com.beecloud.mqtt.Entity.SendMessageObject;
import com.beecloud.mqtt.Runnable.MqttClientReceiveMessageRunnable;
import com.beecloud.mqtt.Runnable.MqttClientSendMessageRunnable;
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
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * Created by dell on 2016/11/9.
 *
 */
@Service
public class MqttServiceImpl implements MqttService{
    private Logger logger = org.slf4j.LoggerFactory.getLogger(this.getClass());
    private String host = "tcp://10.28.4.34:1883";  //功能测试环境
    private String auto_test_host = "tcp://10.28.4.76:1883";//自动化测试环境
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
        MSMR = new MqttClientSendMessageRunnable(host);
        Thread sendThread = new Thread(MSMR);
        sendThread.start();
        MRMR = new MqttClientReceiveMessageRunnable(host);
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
        MRMR.addTopic(String.format(Tbox_Channel_Topic,authObject.getVin().trim()));//订阅认证ack topic

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
    public void sendUnencryptedMessage(String message,String vin) {
        if(vin!=null&&!"".equals(vin)){  //如果VIN码不为空,则替换掉Message中的identityCode
            AuthObject authObject = new AuthObject();
            authObject.setVin(vin);
            authObject.setPid("BEECLOUD");
            long identityCode = authObject.getIdentityCode();
            int tobeReplace = JsonPath.parse(message).read("$.identity.identityCode");
            message = message.replace(String.valueOf(tobeReplace),String.valueOf(identityCode));
        }
        Gson gson = new Gson();
        String applicationId = JsonPath.parse(message).read("$.applicationHeader.applicationID");
        int stepId = JsonPath.parse(message).read("$.applicationHeader.stepId");
        String key = applicationId+stepId;
        AbstractMessage abstractMessage = (AbstractMessage)gson.fromJson(message, MessageMapper.getMessage(key));
        SendMessageObject sendMessageObject = new SendMessageObject();
        sendMessageObject.setTopic(Tbox_Send_Topic);
        sendMessageObject.setMessage(ProtocolUtil.bytesToFormatBitString(abstractMessage.encode()));
        MSMR.addMessage(sendMessageObject);
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
      //  new MqttServiceImpl().sendUnencryptedMessage(json,"VIN99999901");
        AuthObject authObject = new AuthObject();
        authObject.setVin("VIN99999901");
        authObject.setPid("BEECLOUD");
//        AuthReqMessage authReqMessage = new MqttServiceImpl().getAuthReqMessage(authObject);
        Gson gson = new Gson();
        System.out.println(gson.toJson(authObject));
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
