package com.example.remotehomeelectricalcontrolsystem.Model;

public class CheckDevice {
    public int endTime;
    public String nameDevice;
    public int startTime;
    public int state;

    public CheckDevice(int endTime, String nameDevice, int startTime, int state) {
        this.endTime = endTime;
        this.nameDevice = nameDevice;
        this.startTime = startTime;
        this.state = state;
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
