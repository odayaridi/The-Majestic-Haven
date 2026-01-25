package com.example.finalbackend.Model;
import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "Rooms")
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "room_id")
    private Integer roomId;

    @Column(name = "room_name", nullable = false)
    private String roomName;

    @Column(name = "room_type", nullable = false)
    private String roomType;

    @Column(name = "room_number", nullable = false)
    private String roomNumber;

    @Column(name = "price_per_night", nullable = false)
    private Double roomPricePerNight;

    @Column(name = "availability_status")
    private String roomAvailabilityStatus;

    @Column(name = "image")
    private String roomImage;

    public Room() {
    }

    public Room(Integer roomId, Booking booking, List<Review> reviewList, String roomName, String roomType, String roomNumber, Double roomPricePerNight, String roomAvailabilityStatus, String roomImage) {
        this.roomId = roomId;
        this.roomName = roomName;
        this.roomType = roomType;
        this.roomNumber = roomNumber;
        this.roomPricePerNight = roomPricePerNight;
        this.roomAvailabilityStatus = roomAvailabilityStatus;
        this.roomImage = roomImage;
    }


    public Integer getRoomId() {
        return roomId;
    }

    public void setRoomId(Integer roomId) {
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

    public Double getRoomPricePerNight() {
        return roomPricePerNight;
    }

    public void setRoomPricePerNight(Double roomPricePerNight) {
        this.roomPricePerNight = roomPricePerNight;
    }

    public String getRoomAvailabilityStatus() {
        return roomAvailabilityStatus;
    }

    public void setRoomAvailabilityStatus(String roomAvailabilityStatus) {
        this.roomAvailabilityStatus = roomAvailabilityStatus;
    }

    public String getRoomImage() {
        return roomImage;
    }

    public void setRoomImage(String roomImage) {
        this.roomImage = roomImage;
    }

    @Override
    public String toString() {
        return "Room{" +
                "roomId=" + roomId +
                ", roomName='" + roomName + '\'' +
                ", roomType='" + roomType + '\'' +
                ", roomNumber='" + roomNumber + '\'' +
                ", roomPricePerNight=" + roomPricePerNight +
                ", roomAvailabilityStatus='" + roomAvailabilityStatus + '\'' +
                ", roomImage='" + roomImage + '\'' +
                '}';
    }
}
