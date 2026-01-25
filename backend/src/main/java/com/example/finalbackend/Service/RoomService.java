package com.example.finalbackend.Service;

import com.example.finalbackend.DTO.RoomNameAndPriceDTO;
import com.example.finalbackend.Exception.ResourceNotFoundException;
import com.example.finalbackend.Model.Room;
import com.example.finalbackend.Repository.RoomRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoomService {
    private final RoomRepository roomRepository;

    public RoomService(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    public List<Room> getAllRooms(){
        return roomRepository.findAll();
    }

    public List<Room> getFilteredRoomsByRoomType(String roomType) {
        List<Room> rooms = roomRepository.findByRoomTypeIgnoreCase(roomType);
        if (rooms.isEmpty()) {
            throw new ResourceNotFoundException("Invalid entry of room type : " + roomType + "!");
        }
        return rooms;
    }


    public Room getFilteredRoomByNumber(String roomNumber){
        Room r = roomRepository.findByRoomNumberIgnoreCase(roomNumber);
        if(r==null){
            throw new ResourceNotFoundException("Invalid entry of room number : " + roomNumber + "!");
        }
        return r;
    }

    public Room getFilteredRoomByName(String roomName){
        Room r = roomRepository.findByRoomNameIgnoreCase(roomName);
        if(r==null){
            throw new ResourceNotFoundException("Invalid entry of room name : " + roomName + "!");
        }
        return r;
    }

    public String checkRoomAvailable(Integer roomId){
        Room r = roomRepository.findById(roomId).orElseThrow(()->
                new ResourceNotFoundException("Invalid roomId, cannot be fetched!"));
        return r.getRoomAvailabilityStatus();
    }

    public boolean updateAvailabilityStatus(int roomId, String availabilityStatus) {
        int updatedRows = roomRepository.updateAvailabilityStatus(roomId, availabilityStatus);
        return updatedRows > 0;
    }

    public List<Room> getAvailableRoomsThisWeek(){
        return roomRepository.getAvailableRoomsThisWeek();
    }

    public List<RoomNameAndPriceDTO> getHighQualityRooms() {
        return roomRepository.findRoomsAboveAveragePrice();
    }
}
