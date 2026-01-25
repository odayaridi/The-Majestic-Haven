package com.example.finalbackend.Repository;
import com.example.finalbackend.DTO.ClientBookingDTO;
import com.example.finalbackend.DTO.RoomTypeBookingsDTO;
import com.example.finalbackend.Model.Booking;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.sql.Date;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking,Integer> {
    @Query("select b.bookingId from Booking b where b.client.clientId= :cId and b.room.roomId =:rId")
    Integer getBookingIdByCidAndRid(@Param("cId") Integer cId, @Param("rId") Integer rId);

    @Query("""
        SELECT new  com.example.finalbackend.DTO.ClientBookingDTO(
            r.roomNumber, r.roomName,
            b.bookingCheckInDate, b.bookingCheckOutDate,
            b.bookingTotalPrice
        )
        FROM Booking b
        JOIN b.room r
        WHERE b.client.clientId = :clientId
    """)
    List<ClientBookingDTO> findClientBookings(@Param("clientId") Integer clientId);

    @Transactional
    @Modifying
    @Query("DELETE FROM Booking b WHERE b.room.roomId = :roomId AND b.bookingCheckOutDate <= CURRENT_DATE")
    void deleteOutdatedBookingsByRoomId(@Param("roomId") Integer roomId);
    @Query("SELECT b.bookingCheckOutDate FROM Booking b WHERE b.room.roomId = :roomId ORDER BY b.bookingCheckOutDate DESC")
    Date findLatestCheckoutDateByRoomId(@Param("roomId") Integer roomId);
    boolean existsByClient_ClientIdAndRoom_RoomId(Integer clientId, Integer roomId);
    @Query("SELECT new com.example.finalbackend.DTO.RoomTypeBookingsDTO(r.roomType, COUNT(b)) " +
            "FROM Room r JOIN Booking b ON r.roomId = b.room.roomId " +
            "GROUP BY r.roomType " +
            "ORDER BY COUNT(b) DESC")
    List<RoomTypeBookingsDTO> getRoomTypeBookings();
}
