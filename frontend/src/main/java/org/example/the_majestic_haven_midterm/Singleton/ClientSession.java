package org.example.the_majestic_haven_midterm.Singleton;

import org.example.the_majestic_haven_midterm.Models.Client;

public class ClientSession {

    private static int c = 0;
    private static ClientSession instance;
    private Client currentUser;

    private ClientSession() {}

    public static ClientSession getInstance() {
        if (c == 0) {
            c++;
            instance = new ClientSession();
        }
        return instance;
    }

    public void setCurrentUser(Client client) {
        this.currentUser = client;
    }

    public Client getCurrentUser() {
        return currentUser;
    }

    public void clearSession() {
        this.currentUser = null;
    }
}
