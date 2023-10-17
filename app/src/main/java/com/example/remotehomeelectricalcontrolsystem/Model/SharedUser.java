package com.example.remotehomeelectricalcontrolsystem.Model;

public class SharedUser {
  private static User user;

  public static void setUser(User user) {
    SharedUser.user = user;
  }

  public static User getUser() {
    return user;
  }
}
