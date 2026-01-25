package com.example.finalbackend.Service;

import com.example.finalbackend.DTO.ClientBookingDTO;
import com.example.finalbackend.DTO.RoomTypeBookingsDTO;
import com.example.finalbackend.Exception.ResourceNotFoundException;
import com.example.finalbackend.Model.Booking;
import com.example.finalbackend.Model.Client;
import com.example.finalbackend.Model.Room;
import com.example.finalbackend.Repository.BookingRepository;
import com.example.finalbackend.Repository.ClientRepository;
import com.example.finalbackend.Repository.RoomRepository;
import org.springframework.stereotype.Service;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.List;

@Service
public class BookingService {
    private final BookingRepository bookingRepository;
    private final ClientRepository clientRepository;
    private final RoomRepository roomRepository;

    public BookingService(BookingRepository bookingRepository, ClientRepository clientRepository, RoomRepository roomRepository) {
        this.bookingRepository = bookingRepository;
        this.clientRepository = clientRepository;
        this.roomRepository = roomRepository;
    }

    public List<ClientBookingDTO> getAllClientBookings(Integer cId){
        return bookingRepository.findClientBookings(cId);
    }

    public Integer getBookingId(Integer roomId,Integer clientId) {
        Integer bookingId = bookingRepository.getBookingIdByCidAndRid(clientId,roomId);
        if (bookingId == null) {
            throw new ResourceNotFoundException("Booking not found!");
        }
        return bookingId;
    }

    public Booking insertBookingService(Integer clientId, Integer roomId, Booking booking){
        Client client = clientRepository.findById(clientId).orElseThrow(()->
                new ResourceNotFoundException("Error in retrieving client!"));
        Room room = roomRepository.findById(roomId).orElseThrow(()->
            new ResourceNotFoundException("Error in retrieving room!")
        );
        booking.setClient(client);
        booking.setRoom(room);

        return bookingRepository.save(booking);
    }

    public void deleteBookingService(Integer bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Invalid booking Id, Cannot fetch!"));
        bookingRepository.delete(booking);
    }


    public void deleteOutdatedBookingByRoomIdService(Integer roomId) {
        roomRepository.findById(roomId)
                .orElseThrow(() -> new ResourceNotFoundException("Invalid room Id!"));
        bookingRepository.deleteOutdatedBookingsByRoomId(roomId);
    }


    public boolean isRoomRentedByClient(Integer clientId, Integer roomId) {
        return bookingRepository.existsByClient_ClientIdAndRoom_RoomId(clientId, roomId);
    }


    public String getCheckOutDateForRoom(Integer roomId) {
        Date checkOutDate = bookingRepository.findLatestCheckoutDateByRoomId(roomId);
        if (checkOutDate != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            return sdf.format(checkOutDate);
        }
        return null;
    }

    public List<RoomTypeBookingsDTO> getRoomTypeBookings(){
        return bookingRepository.getRoomTypeBookings();
    }


    public Booking extendBookingClient(Integer cId, Integer rId, LocalDate extendedCheckOutDate) {
        Integer bookingId = bookingRepository.getBookingIdByCidAndRid(cId, rId);
        if (bookingId == null) {
            throw new ResourceNotFoundException("No booking found for the given client and room!");
        }
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found!"));
        Room updatedRoom = roomRepository.findById(rId)
                .orElseThrow(() -> new ResourceNotFoundException("Room not found!"));
        updatedRoom.setRoomAvailabilityStatus("Room Rented till " + extendedCheckOutDate);
        booking.setBookingCheckOutDate(extendedCheckOutDate);
        roomRepository.save(updatedRoom);
        return bookingRepository.save(booking);
    }
}