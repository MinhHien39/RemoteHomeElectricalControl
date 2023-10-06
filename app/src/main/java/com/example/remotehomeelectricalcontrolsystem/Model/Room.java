package com.example.remotehomeelectricalcontrolsystem.Model;

public class Room {
    public int idRoom;
    public String nameRoom;
    public int idFloor;

    public Room(int idRoom, String nameRoom, int idFloor) {
        this.idRoom = idRoom;
        this.nameRoom = nameRoom;
        this.idFloor = idFloor;
    }

    public int getIdRoom() {
        return idRoom;
    }

    public void setIdRoom(int idRoom) {
        this.idRoom = idRoom;
    }

    public String getNameRoom() {
        return nameRoom;
    }

    public void setNameRoom(String nameRoom) {
        this.nameRoom = nameRoom;
    }

    public int getIdFloor() {
        return idFloor;
    }

    public void setIdFloor(int idFloor) {
        this.idFloor = idFloor;
    }
}
