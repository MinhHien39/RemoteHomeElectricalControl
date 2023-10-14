package com.example.remotehomeelectricalcontrolsystem.Model;

import java.util.List;
import java.util.Map;

public class House {
    private String houseKey;
    private String name;
    private String telHost;
    private String emailHost;
    private List<Floor> floorList;

    // Getters and setters
    public String getHouseKey() {
        return houseKey;
    }

    public void setHouseKey(String houseKey) {
        this.houseKey = houseKey;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTelHost() {
        return telHost;
    }

    public void setTelHost(String telHost) {
        this.telHost = telHost;
    }

    public String getEmailHost() {
        return emailHost;
    }

    public void setEmailHost(String emailHost) {
        this.emailHost = emailHost;
    }


}





class Device {
    private int endTime;
    private String name;
    private int startTime;
    private int state;

    // Getters and setters
    public int getEndTime() {
        return endTime;
    }

    public void setEndTime(int endTime) {
        this.endTime = endTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

class DHT {
    private double humidity;
    private double temperature;


    public double getHumidity() {
        return humidity;
    }

    public void setHumidity(double humidity) {
        this.humidity = humidity;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }
}

class OtherSensor {
    private String name;
    private int state;

    // Getters and setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
}




