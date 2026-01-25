package org.example.the_majestic_haven_midterm.Models;

public class Room {
    private int roomId;
    private String roomName;
    private String roomType;
    private String roomNumber;
    private double roomPrice;
    private String availability_status;
    private String image;


    public Room(int roomId, String roomName, String roomType, String roomNumber, double roomPrice, String availability_status,String image) {
        setRoomInfo(roomId,roomName,roomType,roomNumber,roomPrice,availability_status,image);
    }

    public void setRoomInfo(int roomId, String roomName, String roomType, String roomNumber, double roomPrice, String availability_status,String image){
        setRoomId(roomId);
        setRoomName(roomName);
        setRoomType(roomType);
        setRoomNumber(roomNumber);
        setRoomPrice(roomPrice);
        setAvailability_status(availability_status);
        setImage(image);
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public String getRoomType() {
        return roomType;
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public double getRoomPrice() {
        return roomPrice;
    }

    public void setRoomPrice(double roomPrice) {
        this.roomPrice = roomPrice;
    }

    public String getAvailability_status() {
        return availability_status;
    }

    public void setAvailability_status(String availability_status) {
        this.availability_status = availability_status;
    }
}