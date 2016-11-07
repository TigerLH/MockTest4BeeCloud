package com.beecloud.mqtt.Action;

import com.beecloud.platform.protocol.core.constants.ApplicationID;
import com.beecloud.platform.protocol.core.constants.ErrorCode;
import com.beecloud.platform.protocol.core.element.ErrorInfo;
import com.beecloud.platform.protocol.core.element.FunctionCommandStatus;
import com.beecloud.platform.protocol.core.element.Identity;
import com.beecloud.platform.protocol.core.element.TimeStamp;
import com.beecloud.platform.protocol.core.header.ApplicationHeader;
import com.beecloud.vehicle.spa.protocol.config.rawdata.unlock.UnlockNotificationRawData;
import com.beecloud.vehicle.spa.protocol.message.ResponseMessage;

import java.util.Date;

/**
 * Created by dell on 2016/11/7.
 */
public class UN_LOCK_COMMAND implements Command{
    @Override
    public byte[] mock() {
        return null;
    }

    @Override
    public byte[] give() {
        ResponseMessage notifyMsg = new ResponseMessage();
        ApplicationHeader applicationHeader = new ApplicationHeader();
        applicationHeader.setApplicationID(ApplicationID.UNLOCK);
        applicationHeader.setProtocolVersion(10);
        applicationHeader.setStepId(13);
        applicationHeader.setSequenceId(1);

        Identity authToken = new Identity(123);
        TimeStamp timeStamp = new TimeStamp(new Date());
        ErrorInfo errorInfo = new ErrorInfo(ErrorCode.OK);

        UnlockNotificationRawData rawData = new UnlockNotificationRawData();
        rawData.setStatus(UnlockNotificationRawData.UnlockStatus.MANUAL_LOCKED);

        FunctionCommandStatus commandStatus = new FunctionCommandStatus();
        commandStatus.setStatus(FunctionCommandStatus.CommandStatus.COMPLETE);
        commandStatus.setRawData(rawData.encode());

        notifyMsg.setApplicationHeader(applicationHeader);
        notifyMsg.setIdentity(authToken);
        notifyMsg.setTime(timeStamp);
        notifyMsg.setError(errorInfo);
        notifyMsg.setStatus(commandStatus);
        byte[] data = notifyMsg.encode();
        return data;
    }
}
