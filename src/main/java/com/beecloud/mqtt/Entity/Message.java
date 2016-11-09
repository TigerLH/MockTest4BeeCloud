package com.beecloud.mqtt.entity;

public class Message {
	private int protocolVersion;
	private int stepId;
	private int sequenceId;
	private String vin;
	private String serial;
	private String imei;
	private String iccid;
	private String pid;
	public int getProtocolVersion() {
		return protocolVersion;
	}
	public void setProtocolVersion(int protocolVersion) {
		this.protocolVersion = protocolVersion;
	}
	public int getStepId() {
		return stepId;
	}
	public void setStepId(int stepId) {
		this.stepId = stepId;
	}
	public int getSequenceId() {
		return sequenceId;
	}
	public void setSequenceId(int sequenceId) {
		this.sequenceId = sequenceId;
	}
	public String getVin() {
		return vin;
	}
	public void setVin(String vin) {
		this.vin = vin;
	}
	public String getSerial() {
		return serial;
	}
	public void setSerial(String serial) {
		this.serial = serial;
	}
	public String getImei() {
		return imei;
	}
	public void setImei(String imei) {
		this.imei = imei;
	}
	public String getIccid() {
		return iccid;
	}
	public void setIccid(String iccid) {
		this.iccid = iccid;
	}
	public String getPid() {
		return pid;
	}
	public void setPid(String pid) {
		this.pid = pid;
	}
	@Override
	public String toString() {
		return "Message [protocolVersion=" + protocolVersion + ", stepId="
				+ stepId + ", sequenceId=" + sequenceId + ", vin=" + vin
				+ ", serial=" + serial + ", imei=" + imei + ", iccid=" + iccid
				+ ", pid=" + pid + "]";
	}
	
}
