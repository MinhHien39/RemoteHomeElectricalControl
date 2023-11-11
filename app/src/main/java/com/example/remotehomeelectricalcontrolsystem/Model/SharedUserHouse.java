package com.example.remotehomeelectricalcontrolsystem.Model;

public class SharedUserHouse {
  private static UserHouse userHouse;

  public static UserHouse getUserHouse() {
    return userHouse;
  }

  public static void setUserHouse(UserHouse userHouse) {
    SharedUserHouse.userHouse = userHouse;
  }
}
