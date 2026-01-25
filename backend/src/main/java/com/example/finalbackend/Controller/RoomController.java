package com.example.finalbackend.Controller;
import com.example.finalbackend.DTO.RoomNameAndPriceDTO;
import com.example.finalbackend.Exception.ResourceNotFoundException;
import com.example.finalbackend.Model.Room;
import com.example.finalbackend.Service.RoomService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "*", allowCredentials = "false")
@RestController
@RequestMapping("/api/v1/rooms")
public class RoomController {

    private final RoomService roomService;

    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    @GetMapping
    public List<Room> getAllRooms(){
        return roomService.getAllRooms();
    }

    @GetMapping("/roomType/{type}")
    public ResponseEntity<Object> getFilteredRoomsByType(@PathVariable String type) {
        try {
            List<Room> rooms = roomService.getFilteredRoomsByRoomType(type);
            return ResponseEntity.ok(rooms);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/roomNumber/{number}")
    public ResponseEntity<Object> getFilteredRoomByNumber(@PathVariable String number){
        try{
            Room filteredRoom = roomService.getFilteredRoomByNumber(number);
            return ResponseEntity.ok(filteredRoom);
        }
        catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/roomName/{name}")
    public ResponseEntity<Object> getFilteredRoomByName(@PathVariable String name){
        try {
            Room filteredRoom = roomService.getFilteredRoomByName(name);
            return ResponseEntity.ok(filteredRoom);
        }
        catch (ResourceNotFoundException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/availability")
    public ResponseEntity<Object> checkRoomAvailability(@RequestParam Integer roomId) {
        try {
            String availability = roomService.checkRoomAvailable(roomId);
            Map<String, String> response = new HashMap<>();
            response.put("Availability", availability);
            return ResponseEntity.ok(response);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/{roomId}")
    public ResponseEntity<Map<String, Boolean>> updateRoomAvailabilityStatus(
            @PathVariable Integer roomId,
            @RequestParam String availabilityStatus) {
        boolean success = roomService.updateAvailabilityStatus(roomId, availabilityStatus);
        Map<String, Boolean> response = new HashMap<>();
        response.put("statusUpdated", success);
        if (success) {
            return ResponseEntity.ok(response);
        } else {
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/availableRoomsWeek")
    public List<Room> getAvailableRoomsThisWeek(){
        return roomService.getAvailableRoomsThisWeek();
    }

    @GetMapping("/highQualityRooms")
    public List<RoomNameAndPriceDTO> getHighQualityRooms(){
        return roomService.getHighQualityRooms();
    }

}
