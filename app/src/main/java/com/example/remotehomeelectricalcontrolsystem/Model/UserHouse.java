package com.example.remotehomeelectricalcontrolsystem.Model;

public class UserHouse {
  private String userHouseId, userId, houseId, role;

  public UserHouse(String userHouseId, String userId, String houseId, String role) {
    this.userHouseId = userHouseId;
    this.userId = userId;
    this.houseId = houseId;
    this.role = role;
  }

  public String getUserHouseId() {
    return userHouseId;
  }

  public void setUserHouseId(String userHouseId) {
    this.userHouseId = userHouseId;
  }

  public String getUserId() {
    return userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }

  public String getHouseId() {
    return houseId;
  }

  public void setHouseId(String houseId) {
    this.houseId = houseId;
  }

  public String getRole() {
    return role;
  }

  public void setRole(String role) {
    this.role = role;
  }
}
