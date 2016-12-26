package com.beecloud.util;

import com.beecloud.platform.protocol.core.constants.ApplicationID;
import com.beecloud.platform.protocol.core.constants.ErrorCode;
import com.beecloud.platform.protocol.core.element.ErrorInfo;
import com.beecloud.platform.protocol.core.element.FunctionCommandStatus;
import com.beecloud.platform.protocol.core.element.Identity;
import com.beecloud.platform.protocol.core.element.TimeStamp;
import com.beecloud.platform.protocol.core.header.ApplicationHeader;
import com.beecloud.vehicle.spa.protocol.config.rawdata.remotediagnostics.Ecu;
import com.beecloud.vehicle.spa.protocol.config.rawdata.remotediagnostics.RemoteDiagnosticsResp;
import com.beecloud.vehicle.spa.protocol.constants.DtcType;
import com.beecloud.vehicle.spa.protocol.constants.EcuStatus;
import com.beecloud.vehicle.spa.protocol.message.RemoteDiagnoseNotify;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author hong.lin
 * @description
 * @date 2016/12/19.
 */
public class Test {
    public static void main(String ...args){
        RemoteDiagnoseNotify remoteDiagnoseNotify = new RemoteDiagnoseNotify();

        ApplicationHeader applicationHeader = new ApplicationHeader();
        applicationHeader.setProtocolVersion(10);
        applicationHeader.setSequenceId(888);
        applicationHeader.setApplicationID(ApplicationID.REMOTE_DIAGNOSIS);
        applicationHeader.setStepId(5);

        FunctionCommandStatus functionCommandStatus = new FunctionCommandStatus();
        functionCommandStatus.setStatus(FunctionCommandStatus.CommandStatus.COMPLETE);
        RemoteDiagnosticsResp remoteDiagnosticsResp = new RemoteDiagnosticsResp();
        remoteDiagnosticsResp.setDtcType(DtcType.TWO);
        List<Integer> dtcs = new ArrayList<Integer>();
        dtcs.add(1);
        EcuStatus ecuStatus = EcuStatus.getEcuStatus(EcuStatus.SUCCESS.getCode());
        Ecu ecu = new Ecu();
        ecu.setDtcType(DtcType.TWO);
        ecu.setDtcSize(dtcs.size());
        ecu.setCode(1);
        ecu.setEcuStatus(ecuStatus);
        ecu.setDtcs(dtcs);
        List<Ecu> ecuList = new ArrayList<Ecu>();
        ecuList.add(ecu);
        remoteDiagnosticsResp.setDtcType(DtcType.TWO);
        remoteDiagnosticsResp.setEcus(ecuList);
        functionCommandStatus.setRawData(remoteDiagnosticsResp.encode());

        TimeStamp timeStamp = new TimeStamp(new Date());
        ErrorInfo errorInfo = new ErrorInfo();
        errorInfo.setErrorCode(ErrorCode.OK);

        Identity identity = new Identity();
        identity.setIdentityCode(1618377851);

        remoteDiagnoseNotify.setApplicationHeader(applicationHeader);
        remoteDiagnoseNotify.setTimeStamp(timeStamp);
        remoteDiagnoseNotify.setErrorInfo(errorInfo);
        remoteDiagnoseNotify.setIdentity(identity);
        remoteDiagnoseNotify.setFunctionCommandStatus(functionCommandStatus);
        Gson gson = new Gson();
        System.out.println(gson.toJson(remoteDiagnoseNotify));
    }
}
