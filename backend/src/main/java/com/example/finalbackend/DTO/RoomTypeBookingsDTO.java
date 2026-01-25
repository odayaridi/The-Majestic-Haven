package com.example.finalbackend.DTO;


public class RoomTypeBookingsDTO {
    private String roomType;
    private Long totalBookings;

    public RoomTypeBookingsDTO(String roomType, Long totalBookings) {
        this.roomType = roomType;
        this.totalBookings = totalBookings;
    }

    public String getRoomType() {
        return roomType;
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }

    public Long getTotalBookings() {
        return totalBookings;
    }

    public void setTotalBookings(Long totalBookings) {
        this.totalBookings = totalBookings;
    }

    @Override
    public String toString() {
        return "RoomTypeBookingsDTO{" +
                "roomType='" + roomType + '\'' +
                ", totalBookings=" + totalBookings +
                '}';
    }
}
