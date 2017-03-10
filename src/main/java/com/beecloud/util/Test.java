package com.beecloud.util;

import com.alibaba.fastjson.JSON;
import com.beecloud.vehicle.spa.protocol.message.ResponseStateMessage;
import com.google.gson.Gson;

/**
 * @author hong.lin
 * @description
 * @date 2016/12/19.
 */
public class Test {
    public static void main(String ...args){
//        RemoteDiagnoseNotify remoteDiagnoseNotify = new RemoteDiagnoseNotify();
//
//        ApplicationHeader applicationHeader = new ApplicationHeader();
//        applicationHeader.setProtocolVersion(10);
//        applicationHeader.setSequenceId(888);
//        applicationHeader.setApplicationID(ApplicationID.REMOTE_DIAGNOSIS);
//        applicationHeader.setStepId(5);
//
//        FunctionCommandStatus functionCommandStatus = new FunctionCommandStatus();
//        functionCommandStatus.setStatus(FunctionCommandStatus.CommandStatus.COMPLETE);
//        RemoteDiagnosticsResp remoteDiagnosticsResp = new RemoteDiagnosticsResp();
//        remoteDiagnosticsResp.setDtcType(DtcType.TWO);
//        List<Integer> dtcs = new ArrayList<Integer>();
//        dtcs.add(1);
//        EcuStatus ecuStatus = EcuStatus.getEcuStatus(EcuStatus.SUCCESS.getCode());
//        Ecu ecu = new Ecu();
//        ecu.setDtcType(DtcType.TWO);
//        ecu.setDtcSize(dtcs.size());
//        ecu.setCode(1);
//        ecu.setEcuStatus(ecuStatus);
//        ecu.setDtcs(dtcs);
//        List<Ecu> ecuList = new ArrayList<Ecu>();
//        ecuList.add(ecu);
//        remoteDiagnosticsResp.setDtcType(DtcType.TWO);
//        remoteDiagnosticsResp.setEcus(ecuList);
//        functionCommandStatus.setRawData(remoteDiagnosticsResp.encode());
//
//        TimeStamp timeStamp = new TimeStamp(new Date());
//        ErrorInfo errorInfo = new ErrorInfo();
//        errorInfo.setErrorCode(ErrorCode.OK);
//
//        Identity identity = new Identity();
//        identity.setIdentityCode(1618377851);
//
//        remoteDiagnoseNotify.setApplicationHeader(applicationHeader);
//        remoteDiagnoseNotify.setTimeStamp(timeStamp);
//        remoteDiagnoseNotify.setErrorInfo(errorInfo);
//        remoteDiagnoseNotify.setIdentity(identity);
//        remoteDiagnoseNotify.setFunctionCommandStatus(functionCommandStatus);
//        Gson gson = new Gson();
//        System.out.println(gson.toJson(remoteDiagnoseNotify));
        String str = "{\n" +
                "    \"identity\": {\n" +
                "        \"identityCode\": 1618377851,\n" +
                "        \"totalLength\": 0,\n" +
                "        \"messageLength\": 0\n" +
                "    },\n" +
                "    \"timeStamp\": {\n" +
                "        \"date\": \"Dec 28, 2016 5:26:22 PM\",\n" +
                "        \"totalLength\": 0,\n" +
                "        \"messageLength\": 0\n" +
                "    },\n" +
                "    \"errorInfo\": {\n" +
                "        \"errorCode\": \"OK\",\n" +
                "        \"totalLength\": 0,\n" +
                "        \"messageLength\": 0\n" +
                "    },\n" +
                "    \"responseState\": {\n" +
                "        \"stateConfigurations\": [\n" +
                "            {\n" +
                "                \"stateConfig\": \"AIR\",\n" +
                "                \"data\": 25\n" +
                "            },\n" +
                "            {\n" +
                "                \"stateConfig\": \"DOOR_LQ\",\n" +
                "                \"data\": 2\n" +
                "            },\n" +
                "            {\n" +
                "                \"stateConfig\": \"DOOR_LH\",\n" +
                "                \"data\": 2\n" +
                "            },\n" +
                "            {\n" +
                "                \"stateConfig\": \"DOOR_RH\",\n" +
                "                \"data\": 2\n" +
                "            },\n" +
                "            {\n" +
                "                \"stateConfig\": \"DOOR_RQ\",\n" +
                "                \"data\": 2\n" +
                "            },\n" +
                "            {\n" +
                "                \"stateConfig\": \"THE_HOOD\",\n" +
                "                \"data\": 2\n" +
                "            },\n" +
                "            {\n" +
                "                \"stateConfig\": \"TRUNK\",\n" +
                "                \"data\": 2\n" +
                "            },\n" +
                "            {\n" +
                "                \"stateConfig\": \"CHARGING_PORT\",\n" +
                "                \"data\": 2\n" +
                "            },\n" +
                "            {\n" +
                "                \"stateConfig\": \"control_lock\",\n" +
                "                \"data\": 2\n" +
                "            },\n" +
                "            {\n" +
                "                \"stateConfig\": \"WINDOW_LQ\",\n" +
                "                \"data\": 2\n" +
                "            },\n" +
                "            {\n" +
                "                \"stateConfig\": \"WINDOW_LH\",\n" +
                "                \"data\": 3\n" +
                "            },\n" +
                "            {\n" +
                "                \"stateConfig\": \"WINDOW_RF\",\n" +
                "                \"data\": 3\n" +
                "            },\n" +
                "            {\n" +
                "                \"stateConfig\": \"WINDOW_RH\",\n" +
                "                \"data\": 3\n" +
                "            },\n" +
                "            {\n" +
                "                \"stateConfig\": \"WINDOW_RQ\",\n" +
                "                \"data\": 1\n" +
                "            },\n" +
                "            {\n" +
                "                \"stateConfig\": \"BRAKE\",\n" +
                "                \"data\": 1\n" +
                "            },\n" +
                "            {\n" +
                "                \"stateConfig\": \"THROTTLE\",\n" +
                "                \"data\": 1\n" +
                "            },\n" +
                "            {\n" +
                "                \"stateConfig\": \"GEAR_LEVER\",\n" +
                "                \"data\": 9\n" +
                "            },\n" +
                "            {\n" +
                "                \"stateConfig\": \"HANDBRAKE\",\n" +
                "                \"data\": 2\n" +
                "            },\n" +
                "            {\n" +
                "                \"stateConfig\": \"IGNITION_STATE\",\n" +
                "                \"data\": 2\n" +
                "            },\n" +
                "            {\n" +
                "                \"stateConfig\": \"TOTAL_MILEAGE\",\n" +
                "                \"data\": 200\n" +
                "            },\n" +
                "            {\n" +
                "                \"stateConfig\": \"RESIDUAL_OIL_VOLUME\",\n" +
                "                \"data\": 2\n" +
                "            }\n" +
                "        ],\n" +
                "        \"totalLength\": 0,\n" +
                "        \"messageLength\": 0\n" +
                "    },\n" +
                "    \"applicationHeader\": {\n" +
                "        \"applicationID\": \"VEHICLE_COMPONENT_ACQUISITION\",\n" +
                "        \"protocolVersion\": 0,\n" +
                "        \"stepId\": 5,\n" +
                "        \"sequenceId\": 1256,\n" +
                "        \"remainingLength\": 0\n" +
                "    }\n" +
                "}";
        Gson gson = new Gson();
        ResponseStateMessage responseStateMessage =  gson.fromJson(str,ResponseStateMessage.class);
        ResponseStateMessage responseStateMessage1 = JSON.parseObject(JSON.toJSONString(responseStateMessage),ResponseStateMessage.class);
        System.out.println(JSON.toJSONString(responseStateMessage1));
    }
}
