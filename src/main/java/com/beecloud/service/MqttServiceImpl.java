package com.beecloud.service;

import com.beecloud.mqtt.Entity.AuthObject;
import com.beecloud.mqtt.Entity.SendMessageObject;
import com.beecloud.mqtt.Runnable.MqttClientHandleMessageThread;
import com.beecloud.mqtt.Utils.Util;
import com.beecloud.mqtt.constansts.Type;
import com.google.gson.Gson;
import com.jayway.jsonpath.JsonPath;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
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
    @Value("${function_test_host}") //功能测试环境
    private String host;
    @Value("${auto_test_host}")  //自动化测试环境
    private String auto_test_host;
    private List<MqttClientHandleMessageThread> thread_Group_function = new ArrayList<MqttClientHandleMessageThread>();
    private List<MqttClientHandleMessageThread> thread_Group_auto = new ArrayList<MqttClientHandleMessageThread>();
    private final String Tbox_Send_Topic = "mqtt/server";


    @Override
    public void disconnect(String threadName,String type) {
        if(Type.FUNCTION.getCode().equals(type)){
            thread_Group_function = Util.stopThreadByThreadName(threadName,thread_Group_function);
        }else if(Type.AUTOTEST.getCode().equals(type)){
            thread_Group_auto = Util.stopThreadByThreadName(threadName,thread_Group_auto);
        }
    }


    @Override
    public void connect(String threadName, String type) {
        if (Type.FUNCTION.toString().equals(type)) {
            if (!isClientExist(threadName, thread_Group_function)) {
                MqttClientHandleMessageThread MCHMR_FUNCTION = new MqttClientHandleMessageThread(host, threadName);
                MCHMR_FUNCTION.start();
                thread_Group_function.add(MCHMR_FUNCTION);
            }
        } else {
            if (!isClientExist(threadName, thread_Group_auto)) {
                MqttClientHandleMessageThread MCHMR_AUTOTEST = new MqttClientHandleMessageThread(auto_test_host, threadName);
                MCHMR_AUTOTEST.start();
                thread_Group_auto.add(MCHMR_AUTOTEST);
            }
        }
    }


    protected boolean isClientExist(String threadName,List<MqttClientHandleMessageThread> list){
        for(MqttClientHandleMessageThread thread : list) {
            if(thread.getName().equals(threadName)&&thread.isUseFul()){
                return true;
            }
        }
        return false;
    }



    /**
     * 自动化测试发送消息
     * @param message
     * @param type
     */
    @Override
    public void sendMessaage(String threadName,String message,String type) {
        SendMessageObject sendMessageObject = new SendMessageObject();
        sendMessageObject.setTopic(Tbox_Send_Topic);
        sendMessageObject.setMessage(message);
        if(Type.FUNCTION.getCode().equals(type)){
            Util.sendMessageByThreadName(threadName,thread_Group_function,sendMessageObject);
        }else{
            Util.sendMessageByThreadName(threadName,thread_Group_auto,sendMessageObject);
        }
    }

    /**
     * 功能测试发送消息
     * @param message
     * @param identity
     */
    @Override
    public void sendFunctionMessage(String message,String identity) {     //功能测试发送//替换掉Message中的identityCode
        Gson gson = new Gson();
        AuthObject authObject = gson.fromJson(identity,AuthObject.class);
        long identityCode = authObject.getIdentityCode();
        Object tobeReplace = JsonPath.parse(message).read("$.identity.identityCode");
        message = message.replace(String.valueOf(tobeReplace),String.valueOf(identityCode));
        SendMessageObject sendMessageObject = Util.transJsonToAbstractMessage(message);
        Util.sendMessageByThreadName(authObject.getVin().trim(),thread_Group_function,sendMessageObject);
    }

    @Override
    public void subscribeTopic(String threadName, String topic, String type) {
        if(Type.FUNCTION.getCode().equals(type)){
            Util.subscribeByThreadName(threadName,thread_Group_function,topic);
        }else{
            Util.subscribeByThreadName(threadName,thread_Group_auto,topic);
        }
    }

    @Override
    public void unSubscribeTopic(String threadName, String topic, String type) {
        if(Type.FUNCTION.getCode().equals(type)){
            Util.unSubscribeByThreadName(threadName,thread_Group_function,topic);
        }else{
            Util.unSubscribeByThreadName(threadName,thread_Group_auto,topic);
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
    public String getMessage(String threadName,String key,int timeOut,String type) {
        logger.info("GetMessage for key:"+key);
        logger.info("ReturnMessage:");
        long start = System.currentTimeMillis();
        String message = "";
        while((System.currentTimeMillis()-start)<timeOut*1000){  //设置超时时间
            if(Type.FUNCTION.getCode().equals(type)){
                message = Util.getMessageByThreadName(threadName,thread_Group_function,key);
            }else{
                message = Util.getMessageByThreadName(threadName,thread_Group_auto,key);
            }
            if(!"".equals(message)){
              return message;
            }
        }
        return "nothing to be found";
    }

    @Override
    public String getAllMessage4Function(String threadName) {
        String receiveMessage = "nothing to be found";
        for (MqttClientHandleMessageThread thread : thread_Group_function) {
            if (threadName.equals(thread.getName())&&thread.isAlive()) {
                receiveMessage = thread.getAllMessage();
                break;
            }
        }
        return receiveMessage;
    }

    @Override
    public void clean(String vin,String type) {
        if(Type.FUNCTION.getCode().equals(type)){
            Util.cleanCache(thread_Group_function,vin);
        }else{
            Util.cleanCache(thread_Group_auto,vin);
        }
    }


}
