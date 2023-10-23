package com.example.remotehomeelectricalcontrolsystem.Utils;

import android.util.Log;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class EncryptionUtils {
  public static String encrypt(String text) {
    String key = "e96722373830bccd2e4c499a826b3abd";
    try {
      byte[] decodedKey = Base64.getDecoder().decode(key);
      SecretKeySpec secretKey = new SecretKeySpec(decodedKey, "AES");


      Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
      IvParameterSpec iv = new IvParameterSpec(key.substring(0, 16).getBytes());

      cipher.init(Cipher.ENCRYPT_MODE, secretKey, iv);
      Log.d("aaa", secretKey + "---" + iv);
      byte[] encryptedBytes = cipher.doFinal(text.getBytes());
      return Base64.getEncoder().encodeToString(encryptedBytes);
    } catch (Exception e) {
      e.printStackTrace();
      return null; // Handle encryption errors appropriately in your code
    }
  }

  public static String decrypt(String encryptedText) {
    String key = "e96722373830bccd2e4c499a826b3abd";
    try {
      byte[] decodedKey = Base64.getDecoder().decode(key);
      SecretKeySpec secretKey = new SecretKeySpec(decodedKey, "AES");

      Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
      IvParameterSpec iv = new IvParameterSpec(key.substring(0, 16).getBytes());

      cipher.init(Cipher.DECRYPT_MODE, secretKey, iv);

      byte[] encryptedBytes = Base64.getDecoder().decode(encryptedText);
      byte[] decryptedBytes = cipher.doFinal(encryptedBytes);

      return new String(decryptedBytes);
    } catch (Exception e) {
      e.printStackTrace();
      return null; // Handle decryption errors appropriately in your code
    }
  }
}
