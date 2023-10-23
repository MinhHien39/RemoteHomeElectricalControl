package com.example.remotehomeelectricalcontrolsystem.Model;

public class User {
  private String userId, name, email, tel, password;

  public User(String userId, String name, String email, String tel, String password) {
    this.userId = userId;
    this.name = name;
    this.email = email;
    this.tel = tel;
    this.password = password;
  }

  public String getUserId() {
    return userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
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

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }
}
