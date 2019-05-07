package endgame.data.dreamcorporation;

import com.google.firebase.auth.FirebaseAuth;

import java.util.Random;

public class Encryption {

  private static FirebaseAuth mAuth;
  private static int key;
  private static String customername;
  private static String encry = "";
  private static String dencry = "";
  private static String uid;

  public static String encode(String name) {
    key = new Random().nextInt(99) + 2;
    String temp = name;
    char[] c = new char[temp.length()];
    for (int shu = 1; shu <= key; shu++) {
      int j = 0;
      int k = 0;
      for (int i = 0; i < temp.length(); i++) {
        if (i % 2 == 0 && j < temp.length() / 2) {
          c[i] = (char) (temp.charAt(j));
          j++;
        } else {
          c[i] = (char) (temp.charAt(k + temp.length() / 2));
          k++;
        }
      }
      temp = "";
      for (int count = 0; count < (j + k); count++) {
        temp += c[count];
      }
    }
    for (int i = 0; i < c.length; i++)
      encry += c[i];
    return encry;
  }

  public static String decode(String cipher, String check) {
    mAuth = FirebaseAuth.getInstance(); // Initialize Firebase Auth
    uid = mAuth.getUid();

    if (check.equals(uid)) {
      String temp = cipher;
      char[] c = new char[temp.length()];
      for (int shu = 1; shu <= key; shu++) {
        int j = 0;
        int k = temp.length() / 2;
        for (int i = 0; i < temp.length(); i++) {
          if (i % 2 == 0 && i != temp.length() - 1) {
            c[j] = (char) (temp.charAt(i));
            j++;
          } else {
            c[k] = (char) (temp.charAt(i));
            k++;
          }
        }

        int total = temp.length();
        temp = "";
        for (int count = 0; count < total; count++) {
          temp += c[count];
        }
      }
      for (int i = 0; i < c.length; i++)
        dencry += c[i];
      return dencry;
    }
    return "";
  }
}
