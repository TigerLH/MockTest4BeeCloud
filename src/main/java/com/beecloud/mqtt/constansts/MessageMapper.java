package com.beecloud.mqtt.constansts;

import com.beecloud.platform.protocol.core.constants.ApplicationID;
import com.beecloud.platform.protocol.core.message.AckMessage;
import com.beecloud.platform.protocol.core.message.AuthReqMessage;
import com.beecloud.vehicle.spa.protocol.message.*;

import java.util.HashMap;
import java.util.Map;

/**
 * @author hong.lin
 * @description
 * @date 2016/11/24.
 */
public class MessageMapper {
    private static Map<String,Class> map = new HashMap<String,Class>();
    static {
        /**身份认证**/
        map.put(ApplicationID.ID_AUTH.name()+0, AuthReqMessage.class);

        /**开锁**/
        map.put(ApplicationID.UNLOCK.name()+3,AckMessage.class);
        map.put(ApplicationID.UNLOCK.name()+5,ResponseMessage.class);

        /**锁车**/
        map.put(ApplicationID.LOCK.name()+3,AckMessage.class);
        map.put(ApplicationID.LOCK.name()+5,ResponseMessage.class);

        /**启动汽车**/
        map.put(ApplicationID.START_CAR.name()+3,AckMessage.class);
        map.put(ApplicationID.START_CAR.name()+5,ResponseMessage.class);

        /**车辆状态被动上传**/
        map.put(ApplicationID.PASSIVE_VEHICLE_STATUS_UPLOAD.name()+3,AckMessage.class);
        map.put(ApplicationID.PASSIVE_VEHICLE_STATUS_UPLOAD.name()+5,ResponseAutoState.class);

        /**熄火状态上传**/
        map.put(ApplicationID.STOPPED_VEHICLE_STATUS_UPLOAD.name()+5,NotifyAutoState.class);
       map.put(ApplicationID.DELAY_VEHICLE_STATUS_UPLOAD.name()+6,NotifyAutoState.class);

        /**获取车辆配置**/
        map.put(ApplicationID.REQUEST_VEHICLE_CONFIG.name()+2,NotifyAutoRemind.class);
        map.put(ApplicationID.REQUEST_VEHICLE_CONFIG.name()+8,AckMessage.class);

        /**启动状态上传**/
        map.put(ApplicationID.START_CAR_NOTIFY.name()+2,NotifyAutoRemind.class);

        /**运行时地理位置上传**/
        map.put(ApplicationID.POSITION_UPLOAD_RUNNING.name()+5,AutoLocationMessage.class);

        /**停止时地理位置上传**/
        map.put(ApplicationID.POSITION_UPLOAD_STOPPING.name()+5,AutoLocationMessage.class);

        /**远程定位**/
        map.put(ApplicationID.REMOTE_LOCATION.name()+3,AckMessage.class);
        map.put(ApplicationID.REMOTE_LOCATION.name()+5,RemoteLocationMessage.class);

        /**车辆异常移动报警**/
        map.put(ApplicationID.ABNORMAL_MOVEMENT_ALARM.name()+1,NotifyAutoAlarm.class);
        map.put(ApplicationID.ABNORMAL_MOVEMENT_ALARM.name()+3,AutoLocationMessage.class);
        map.put(ApplicationID.ABNORMAL_MOVEMENT_ALARM.name()+5,NotifyAutoAlarm.class);

        /**远程控制**/
        map.put(ApplicationID.REMOTE_CONTROL.name()+3,AckMessage.class);
        map.put(ApplicationID.REMOTE_CONTROL.name()+5,RemoteNotifyMessage.class);

         /**碰撞记录上传**/
        map.put(ApplicationID.COLLISION_RECORD_UPLOAD.name()+2,NotifyCollisionMessage.class);
    }
    public static Class getMessage(String key){
        return map.get(key);
    }
}
