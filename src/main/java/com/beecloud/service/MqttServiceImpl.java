package com.beecloud.service;

import com.beecloud.mqtt.Entity.AuthObject;
import com.beecloud.mqtt.Entity.SendMessageObject;
import com.beecloud.mqtt.Runnable.MqttClientHandleMessageThread;
import com.beecloud.mqtt.Utils.Util;
import com.beecloud.mqtt.constansts.Type;
import com.beecloud.platform.protocol.core.message.AuthReqMessage;
import com.beecloud.platform.protocol.util.binary.ProtocolUtil;
import com.google.gson.Gson;
import com.jayway.jsonpath.JsonPath;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dell on 2016/11/9.
 *
 */
@Service
public class MqttServiceImpl implements MqttService{
    private Logger logger = org.slf4j.LoggerFactory.getLogger(this.getClass());
    private String host = "tcp://10.28.4.34:1883";  //功能测试环境
    private String auto_test_host = "tcp://10.28.4.76:1883";//自动化测试环境
    private List<MqttClientHandleMessageThread> thread_Group_function = new ArrayList<MqttClientHandleMessageThread>();
    private List<MqttClientHandleMessageThread> thread_Group_auto = new ArrayList<MqttClientHandleMessageThread>();
    private final String Tbox_Send_Topic = "mqtt/server";


    @Override
    public void disconnect(String vin,String type) {
        if(Type.FUNCTION.getCode().equals(type)){
            thread_Group_function = Util.stopThreadByVin(vin,thread_Group_function);
        }else{
            thread_Group_auto = Util.stopThreadByVin(vin,thread_Group_auto);
        }
    }



    @Override
    public synchronized void startAndAutoAuth(String authMessage,String type) {
        Gson gson = new Gson();
        AuthObject authObject = gson.fromJson(authMessage,AuthObject.class);
        SendMessageObject sendMessageObject = new SendMessageObject();
        AuthReqMessage authReqMessage = Util.getAuthReqMessage(authObject);
        sendMessageObject.setMessage(ProtocolUtil.bytesToFormatBitString(authReqMessage.encode()));
        sendMessageObject.setTopic(Tbox_Send_Topic);
        String vin = authObject.getVin();
        if(Type.FUNCTION.toString().equals(type)){
            if(!isClientExist(vin,thread_Group_function)){
                MqttClientHandleMessageThread MCHMR_FUNCTION = new MqttClientHandleMessageThread(host,vin);
                MCHMR_FUNCTION.start();
                thread_Group_function.add(MCHMR_FUNCTION);
                MCHMR_FUNCTION.sendMessage(sendMessageObject);
            }else{
                sendMessage4ExistThread(vin,thread_Group_function,sendMessageObject);
            }
        }else{
            if(!isClientExist(vin,thread_Group_auto)){
                MqttClientHandleMessageThread MCHMR_AUTOTEST = new MqttClientHandleMessageThread(auto_test_host,vin);
                MCHMR_AUTOTEST.start();
                thread_Group_auto.add(MCHMR_AUTOTEST);
                MCHMR_AUTOTEST.sendMessage(sendMessageObject);
            }else{
                sendMessage4ExistThread(vin,thread_Group_auto,sendMessageObject);
            }
        }
    }



    protected boolean isClientExist(String vin,List<MqttClientHandleMessageThread> list){
        boolean isExist = false;
        for(MqttClientHandleMessageThread thread : list) {
            if(thread.getName().equals(vin)&&thread.isAlive()){
                isExist = true;
                break;
            }
        }
        return isExist;
    }

    protected void sendMessage4ExistThread(String vin,List<MqttClientHandleMessageThread> list,SendMessageObject sendMessageObject){
        for(MqttClientHandleMessageThread thread : list) {
            if(thread.getName().equals(vin)){
                thread.sendMessage(sendMessageObject);
                break;
            }
        }
    }


    /**
     * 自动化测试发送消息
     * @param message
     * @param type
     */
    @Override
    public void sendMessaage(String vin,String message,String type) {
        SendMessageObject sendMessageObject = new SendMessageObject();
        sendMessageObject.setTopic(Tbox_Send_Topic);
        sendMessageObject.setMessage(message);
        if(Type.FUNCTION.getCode().equals(type)){
            Util.sendMessageByVin(vin,thread_Group_function,sendMessageObject);
        }else{
            Util.sendMessageByVin(vin,thread_Group_auto,sendMessageObject);
        }
    }

    /**
     * 功能测试发送消息
     * @param message
     * @param vin
     */
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
        SendMessageObject sendMessageObject = Util.transJsonToAbstractMessage(message);
        Util.sendMessageByVin(vin,thread_Group_function,sendMessageObject);
    }

    @Override
    public void subscribeTopic(String vin, String topic, String type) {
        if(Type.FUNCTION.getCode().equals(type)){
            Util.subscribeByVin(vin,thread_Group_function,topic);
        }else{
            Util.subscribeByVin(vin,thread_Group_auto,topic);
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
      //  new MqttServiceImpl().sendUnencryptedMessage(json,"VIN99999901");
        AuthObject authObject = new AuthObject();
        authObject.setVin("VIN99999901");
        authObject.setPid("BEECLOUD");
//      AuthReqMessage authReqMessage = new MqttServiceImpl().getAuthReqMessage(authObject);

        Gson gson = new Gson();
        System.out.println(gson.toJson(authObject));
    }


    @Override
    public String getMessage(String vin,String key,int timeOut,String type) {
        logger.info("GetMessage for key:"+key);
        logger.info("ReturnMessage:");
        long start = System.currentTimeMillis();
        String message = "";
        while((System.currentTimeMillis()-start)<timeOut*1000){  //设置超时时间
            String value = "";
            if(Type.FUNCTION.getCode().equals(type)){
                value = Util.getMessageByVin(vin,thread_Group_function,key);
            }else{
                value = Util.getMessageByVin(vin,thread_Group_auto,key);
            }
            if(!("").equals(value)){
                message = value;
            }else{
                message = "nothing to be found";
            }
        }
        logger.info(message);
        return message;
    }

    @Override
    public String getAllMessage4Function(String vin) {
        String receiveMessage = "nothing to be found";
        for (MqttClientHandleMessageThread thread : thread_Group_function) {
            if (vin.equals(thread.getName())&&thread.isAlive()) {
                receiveMessage = thread.getAllMessage();
                break;
            }
        }
        return receiveMessage;
    }

}
