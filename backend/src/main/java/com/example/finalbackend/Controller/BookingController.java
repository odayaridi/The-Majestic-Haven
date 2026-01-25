package com.example.finalbackend.Controller;
import com.example.finalbackend.DTO.ClientBookingDTO;
import com.example.finalbackend.DTO.RoomTypeBookingsDTO;
import com.example.finalbackend.Exception.ResourceNotFoundException;
import com.example.finalbackend.Model.Booking;
import com.example.finalbackend.Service.BookingService;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "*", allowCredentials = "false")
@RestController
@RequestMapping("/api/v1/bookings")
public class BookingController {
    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @GetMapping
    public List<ClientBookingDTO> getAllClientBookings(@RequestParam Integer cId){
        return bookingService.getAllClientBookings(cId);
    }

    @PostMapping("/clientId/{clientId}/roomId/{roomId}")
    public ResponseEntity<Object> insertBooking(@PathVariable Integer clientId,
                                                @PathVariable Integer roomId, @RequestBody Booking booking){
        try{
        Booking newBooking = bookingService.insertBookingService(clientId,roomId,booking);
        return new ResponseEntity<>(newBooking, HttpStatus.CREATED);
        }
        catch (ResourceNotFoundException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping
    public ResponseEntity<Object> deleteBooking(@RequestParam Integer clientId, @RequestParam Integer roomId) {
        try{
            Integer bookingId = bookingService.getBookingId(roomId,clientId);
            bookingService.deleteBookingService(bookingId);
            return ResponseEntity.noContent().build();
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @Transactional
    @DeleteMapping("/room/{roomId}")
    public ResponseEntity<Object> deleteOutdatedBookings(@PathVariable Integer roomId) {
        try{
            bookingService.deleteOutdatedBookingByRoomIdService(roomId);
            return ResponseEntity.noContent().build();
        }
        catch (ResourceNotFoundException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/clientId/{clientId}/roomId/{roomId}")
    public ResponseEntity<Map<String, Boolean>> isRoomRentedByClient(@PathVariable Integer clientId,
                                                                     @PathVariable Integer roomId) {
        boolean isRented = bookingService.isRoomRentedByClient(clientId, roomId);
        Map<String, Boolean> response = new HashMap<>();
        response.put("rented", isRented);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/checkoutDate")
    public ResponseEntity<Map<String, String>> getLatestCheckoutDate(@RequestParam Integer roomId) {
        String checkOutDate = bookingService.getCheckOutDateForRoom(roomId);
        Map<String, String> response = new HashMap<>();
        if (checkOutDate != null) {
            response.put("checkOutDate", checkOutDate);
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/rTypeBookings")
    public List<RoomTypeBookingsDTO> getRoomTypeBookings(){
        return bookingService.getRoomTypeBookings();
    }

    @PutMapping("/cId/{cId}/rId/{rId}")
    public ResponseEntity<Object> extendsBookingRent(@PathVariable Integer cId, @PathVariable Integer rId,
                                                     @RequestParam LocalDate updatedCheckOutDate){
        try{
            Booking booking = bookingService.extendBookingClient(cId,rId,updatedCheckOutDate);
            return ResponseEntity.ok(booking);
        }
        catch (ResourceNotFoundException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

    }
}
