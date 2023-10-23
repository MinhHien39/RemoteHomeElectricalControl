package com.example.remotehomeelectricalcontrolsystem.Utils;

public class Format {
  public static String formatName(String fullName) {
    // Split the full name into individual words
    String[] words = fullName.split("\\s+");

    // Initialize a StringBuilder to construct the formatted name
    StringBuilder formattedName = new StringBuilder();

    for (String word : words) {
      // Capitalize the first letter of each word
      if (word.length() > 0) {
        formattedName.append(Character.toUpperCase(word.charAt(0)));
        if (word.length() > 1) {
          formattedName.append(word.substring(1).toLowerCase());
        }
        formattedName.append(" "); // Add a space between words
      }
    }

    // Remove the trailing space and return the formatted name
    return formattedName.toString().trim();
  }
}
