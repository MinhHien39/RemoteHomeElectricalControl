package com.example.remotehomeelectricalcontrolsystem.Model;

public class UserHouse {
  private String userId, houseId, role;

  public UserHouse(String userId, String houseId, String role) {
    this.userId = userId;
    this.houseId = houseId;
    this.role = role;
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
