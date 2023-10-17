package com.example.remotehomeelectricalcontrolsystem.Model;

public class User {
  private String name, email, tel, houseKey, password, role, houseId;

  public User(String name, String email, String tel, String houseKey, String password, String role, String houseId) {
    this.name = name;
    this.email = email;
    this.tel = tel;
    this.houseKey = houseKey;
    this.password = password;
    this.role = role;
    this.houseId = houseId;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getTel() {
    return tel;
  }

  public void setTel(String tel) {
    this.tel = tel;
  }

  public String getHouseKey() {
    return houseKey;
  }

  public void setHouseKey(String houseKey) {
    this.houseKey = houseKey;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getRole() {
    return role;
  }

  public void setRole(String role) {
    this.role = role;
  }

  public String getHouseId() {
    return houseId;
  }

  public void setHouseId(String houseId) {
    this.houseId = houseId;
  }
  @Override
  public String toString() {
    return "User{name='" + name + '\'' +
        ", email='" + email + '\'' +
        ", role='" + role + '\'' +
        ", houseId='" + houseId + '\'' +
        // Add other fields you want to include
        '}';
  }

}
