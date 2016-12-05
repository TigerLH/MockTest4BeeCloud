package com.beecloud.service;

import com.beecloud.mqtt.Entity.AuthObject;
import com.beecloud.mqtt.Entity.SendMessageObject;
import com.beecloud.mqtt.Runnable.MqttClientReceiveMessageRunnable;
import com.beecloud.mqtt.Runnable.MqttClientSendMessageRunnable;
import com.beecloud.mqtt.constansts.MessageMapper;
import com.beecloud.mqtt.constansts.Type;
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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by dell on 2016/11/9.
 *
 */
@Service
public class MqttServiceImpl implements MqttService{
    private Logger logger = org.slf4j.LoggerFactory.getLogger(this.getClass());
    private String host = "tcp://10.28.4.34:1883";  //功能测试环境
    private String auto_test_host = "tcp://10.28.4.76:1883";//自动化测试环境
    private Thread sendThread = null;
    private Thread receiveThread = null;
    private MqttClientReceiveMessageRunnable MRMR_FUNCTION = new MqttClientReceiveMessageRunnable(host);
    private MqttClientSendMessageRunnable MSMR_FUNCTION = new MqttClientSendMessageRunnable(host);
    private MqttClientReceiveMessageRunnable MRMR_AUTOTEST = new MqttClientReceiveMessageRunnable(auto_test_host);
    private MqttClientSendMessageRunnable MSMR_AUTOTEST = new MqttClientSendMessageRunnable(auto_test_host);
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
    public void run(Type type) {
        ExecutorService pool = Executors.newCachedThreadPool();

        if(Type.FUNCTION.equals(type)){
            sendThread = new Thread(MRMR_FUNCTION);
            sendThread.start();
            receiveThread = new Thread(MSMR_FUNCTION);
            receiveThread.start();
        }else{
            sendThread = new Thread(MRMR_AUTOTEST);
            sendThread.start();
            receiveThread = new Thread(MSMR_AUTOTEST);
            receiveThread.start();
        }
    }

    @Override
    public void stop(Type type) {
        if(Type.FUNCTION.equals(type)){
            MRMR_FUNCTION.disconnetc();
            MSMR_FUNCTION.disconnect();
        }else{
            MSMR_AUTOTEST.disconnect();
            MRMR_AUTOTEST.disconnetc();
        }
        if(null != sendThread){
            sendThread.stop();
            sendThread = null;
        }
        if(null != receiveThread){
            receiveThread.stop();
            receiveThread = null;
        }
    }

    @Override
    public void sendAuthReqMessage(String authMessage,Type type) {
        Gson gson = new Gson();
        AuthObject authObject = gson.fromJson(authMessage,AuthObject.class);
        SendMessageObject sendMessageObject = new SendMessageObject();
        AuthReqMessage authReqMessage = getAuthReqMessage(authObject);
        sendMessageObject.setMessage(ProtocolUtil.bytesToFormatBitString(authReqMessage.encode()));
        sendMessageObject.setTopic(Tbox_Send_Topic);
        if(Type.FUNCTION.equals(type)){
            MRMR_FUNCTION.addTopic(String.format(Tbox_Channel_Topic,authObject.getVin().trim()));//订阅认证ack topic
            MSMR_FUNCTION.addMessage(sendMessageObject);  //发布认证message
        }else{
            MRMR_AUTOTEST.addTopic(String.format(Tbox_Channel_Topic,authObject.getVin().trim()));//订阅认证ack topic
            MSMR_AUTOTEST.addMessage(sendMessageObject);  //发布认证message
        }
    }

    @Override
    public void subscribeTopic(String topic,Type type){
        if(Type.FUNCTION.equals(type)){
            MRMR_FUNCTION.addTopic(topic);
        }else{
            MRMR_AUTOTEST.addTopic(topic);
        }
    }

    @Override
    public void sendMessaage(String message) {              //自动化测试发送
        Gson gson = new Gson();
        SendMessageObject sendMessageObject = gson.fromJson(message,SendMessageObject.class);
        MSMR_AUTOTEST.addMessage(sendMessageObject);
    }

    @Override
    public void sendFunctionMessage(String message,String vin) {     //功能测试发送
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
        MSMR_FUNCTION.addMessage(sendMessageObject);
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
    public String getMessageByKey(String key,int timeOut,Type type) {
        long start = System.currentTimeMillis();
        while((System.currentTimeMillis()-start)<timeOut*1000){  //设置超时时间
            String message = "";
            if(Type.FUNCTION.equals(type)){
                 message = MRMR_FUNCTION.getMessageBykey(key);
            }else{
                message = MRMR_AUTOTEST.getMessageBykey(key);
            }
            if(null!=message&&!"".equals(message)){
                logger.info("ReturnMessage for key:"+key);
                logger.info(message);
                return message;
            }
        }
        return "nothing to be found";
    }
}
