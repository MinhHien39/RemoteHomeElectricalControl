package com.example.remotehomeelectricalcontrolsystem.Model;

public class Devices {
    public int endTime;
    public String nameDevice;
    public int startTime;
    public int state;
    public String imgUrl;

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public Devices(int endTime, String nameDevice, int startTime, int state , String imgUrl) {
        this.endTime = endTime;
        this.nameDevice = nameDevice;
        this.startTime = startTime;
        this.state = state;
        this.imgUrl = imgUrl;
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
}
