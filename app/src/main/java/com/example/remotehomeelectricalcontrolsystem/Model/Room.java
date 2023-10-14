package com.example.remotehomeelectricalcontrolsystem.Model;

import java.util.List;

public class Room {

        private String idRoom;

    public Room(String idRoom, String name) {
        this.idRoom = idRoom;
        this.name = name;
    }

    public String getIdRoom() {
        return idRoom;
    }

    public void setIdRoom(String idRoom) {
        this.idRoom = idRoom;
    }

    private List<Device> deviceList;
        private String name;
        private List<DHT> dhtList;
        private List<OtherSensor> otherSensorList;



        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public List<Device> getDeviceList() {
            return deviceList;
        }

        public void setDeviceList(List<Device> deviceList) {
            this.deviceList = deviceList;
        }

        public List<DHT> getDhtList() {
            return dhtList;
        }

        public void setDhtList(List<DHT> dhtList) {
            this.dhtList = dhtList;
        }

        public List<OtherSensor> getOtherSensorList() {
            return otherSensorList;
        }

        public void setOtherSensorList(List<OtherSensor> otherSensorList) {
            this.otherSensorList = otherSensorList;
        }

}
