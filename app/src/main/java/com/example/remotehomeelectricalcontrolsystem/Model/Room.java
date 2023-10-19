package com.example.remotehomeelectricalcontrolsystem.Model;

import java.util.List;

public class Room {

    private String idRoom;
    private String imgUrl;

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public Room(String idRoom, String nameRoom , String imgUrl , List<Devices> list) {
        this.idRoom = idRoom;
        this.nameRoom = nameRoom;
        this.imgUrl = imgUrl;
        this.list = list;

    }

    public String getIdRoom() {
        return idRoom;
    }

    public void setIdRoom(String idRoom) {
        this.idRoom = idRoom;
    }

    public String nameRoom;


    public String getNameRoom() {
        return nameRoom;
    }

    public List<Devices> getList() {
        return list;
    }

    public void setList(List<Devices> list) {
        this.list = list;
    }

    public List<Devices> list;

    public void setNameRoom(String nameRoom) {
        this.nameRoom = nameRoom;
    }


}
