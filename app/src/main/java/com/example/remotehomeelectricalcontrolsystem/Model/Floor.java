package com.example.remotehomeelectricalcontrolsystem.Model;

public class Floor {
    public int idFloor;
    public String nameFloor;

    public Floor(int idFloor, String nameFloor) {
        this.idFloor = idFloor;
        this.nameFloor = nameFloor;
    }

    public int getIdFloor() {
        return idFloor;
    }

    public void setIdFloor(int idFloor) {
        this.idFloor = idFloor;
    }

    public String getNameFloor() {
        return nameFloor;
    }

    public void setNameFloor(String nameFloor) {
        this.nameFloor = nameFloor;
    }
}
