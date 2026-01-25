package org.example.the_majestic_haven_midterm.Models;

public class Booking {
    private String roomNumber;
    private String roomName;
    private double totalPrice;
    private String checkInDate;
    private String checkOutDate;

    public Booking( String roomNumber, String roomName,  String checkInDate, String checkOutDate,double totalPrice) {
        setRoomNumber(roomNumber);
        setRoomName(roomName);
        setTotalPrice(totalPrice);
        setCheckInDate(checkInDate);
        setCheckOutDate(checkOutDate);
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getCheckInDate() {
        return checkInDate;
    }

    public void setCheckInDate(String checkInDate) {
        this.checkInDate = checkInDate;
    }

    public String getCheckOutDate() {
        return checkOutDate;
    }

    public void setCheckOutDate(String checkOutDate) {
        this.checkOutDate = checkOutDate;
    }
}