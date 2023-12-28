package com.example.remotehomeelectricalcontrolsystem.Model;

public class Devices {
    public String deviceId;
    public String endTime;
    public String nameDevice;
    public String startTime;
    public int state;
    public String imgUrl;

    public Devices(String deviceId, String endTime, String nameDevice, String startTime, int state, String imgUrl) {
        this.deviceId = deviceId;
        this.endTime = endTime;
        this.nameDevice = nameDevice;
        this.startTime = startTime;
        this.state = state;
        this.imgUrl = imgUrl;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getNameDevice() {
        return nameDevice;
    }

    public void setNameDevice(String nameDevice) {
        this.nameDevice = nameDevice;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
}
