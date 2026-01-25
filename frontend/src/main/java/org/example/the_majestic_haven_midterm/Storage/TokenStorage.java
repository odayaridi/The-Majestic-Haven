package org.example.the_majestic_haven_midterm.Storage;

public class TokenStorage {
    private static String token;

    public static void setToken(String t) {
        token = t;
    }

    public static String getToken() {
        return token;
    }

    public static void clearToken() {
        token = null;
    }
}



