package com.example.remotehomeelectricalcontrolsystem.Model;

public class Devices {
    public String deviceId;
    public int endTime;
    public String nameDevice;
    public int startTime;
    public int state;
    public String imgUrl;

    public Devices(String deviceId, int endTime, String nameDevice, int startTime, int state, String imgUrl) {
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

    public int getEndTime() {
        return endTime;
    }

    public void setEndTime(int endTime) {
        this.endTime = endTime;
    }

    public String getNameDevice() {
        return nameDevice;
    }

    public void setNameDevice(String nameDevice) {
        this.nameDevice = nameDevice;
    }

    public int getStartTime() {
        return startTime;
    }

    public void setStartTime(int startTime) {
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
