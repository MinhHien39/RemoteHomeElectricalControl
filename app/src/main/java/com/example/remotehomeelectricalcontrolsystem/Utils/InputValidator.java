package com.example.remotehomeelectricalcontrolsystem.Utils;

public class InputValidator {
  public static boolean isValidFullName(String fullName) {
    return fullName.matches("^[a-zA-Z\\s]+");
  }

  public static boolean isValidPhoneNumber(String phoneNumber) {
    return phoneNumber.matches("^\\d{10}$");
  }

  public static boolean isValidEmail(String email) {
    return email.matches("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+");
  }

  public static boolean isValidPassword(String password) {
    return password.length() >= 8;
  }
}
