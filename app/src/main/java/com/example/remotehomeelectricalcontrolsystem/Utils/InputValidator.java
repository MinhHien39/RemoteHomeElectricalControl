package com.example.remotehomeelectricalcontrolsystem.Utils;

public class InputValidator {
  public static String isValidFullName(String fullName) {
    if (!fullName.matches("^[a-zA-Z\\s]+")) {
      return "Invalid full name";
    }
    return null;
  }

  public static String isValidPhoneNumber(String phoneNumber) {
    if (!phoneNumber.matches("^\\d{10}$")) {
      return "Invalid phone number";
    }
    return null;
  }

  public static String isValidEmail(String email) {
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    if (!email.matches(emailPattern)) {
      return "Invalid email address";
    }
    return null;
  }

  public static String isValidPassword(String password) {
    if (password.length() < 8) {
      return "Password must be at least 8 characters long";
    }
    return null;
  }

  public static boolean[] areAllFieldsNotEmpty(String email, String password) {
    boolean[] isValidForm = {false, false};
    isValidForm[0] = !email.isEmpty();
    isValidForm[1] = !password.isEmpty();
    return isValidForm;
  }

  public static boolean[] areAllFieldsNotEmpty(String fullName, String phoneNumber, String email, String houseKey, String password) {
    boolean[] isValidForm = {false, false, false, false, false};
    isValidForm[0] = !fullName.isEmpty();
    isValidForm[1] = !phoneNumber.isEmpty();
    isValidForm[2] = !email.isEmpty();
    isValidForm[3] = !houseKey.isEmpty();
    isValidForm[4] = !password.isEmpty();
    return isValidForm;
  }
}
