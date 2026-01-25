package org.example.the_majestic_haven_midterm.Models;

public class Client {
    private int clientId;
    private String firstName;
    private String lastName;
    private String email;
    private String password;


    public Client(int clientId,String firstName,String lastName,String email,String password){
        setClientInformation(clientId,firstName,lastName,email,password);
    }

    public void setClientInformation(int clientId,String firstName,String lastName,String email,String password){
        setClientId(clientId);
        setFirstName(firstName);
        setLastName(lastName);
        setEmail(email);
        setPassword(password);
    }


    public int getClientId() {
        return clientId;
    }

    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
