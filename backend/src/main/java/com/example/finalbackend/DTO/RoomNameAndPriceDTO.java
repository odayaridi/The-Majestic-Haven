package com.example.finalbackend.DTO;

public class RoomNameAndPriceDTO {
    private String roomName;
    private Double pricePerNight;

    public RoomNameAndPriceDTO(String roomName, Double pricePerNight) {
       setRoomName(roomName);
       setPricePerNight(pricePerNight);
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public Double getPricePerNight() {
        return pricePerNight;
    }

    public void setPricePerNight(Double pricePerNight) {
        this.pricePerNight = pricePerNight;
    }

    @Override
    public String toString() {
        return "RoomNameAndPriceDTO{" +
                "roomName='" + roomName + '\'' +
                ", pricePerNight=" + pricePerNight +
                '}';
    }
}
