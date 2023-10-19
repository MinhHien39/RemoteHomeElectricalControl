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




