package com.example.finalbackend.Repository;
import com.example.finalbackend.DTO.RoomNameAndPriceDTO;
import com.example.finalbackend.Model.Room;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface RoomRepository extends JpaRepository<Room,Integer> {
    List<Room> findByRoomTypeIgnoreCase(String roomType);
    Room findByRoomNumberIgnoreCase(String roomNumber);
    Room findByRoomNameIgnoreCase(String roomName);
    @Transactional
    @Modifying
    @Query("UPDATE Room r SET r.roomAvailabilityStatus = :roomAvailabilityStatus WHERE r.roomId = :roomId")
    int updateAvailabilityStatus(@Param("roomId") Integer roomId, @Param("roomAvailabilityStatus") String roomAvailabilityStatus);

    @Query(value = """
        SELECT * FROM Rooms r
        WHERE r.room_id NOT IN (
            SELECT b.room_id FROM Bookings b
            WHERE b.check_in_date <= CURDATE() + INTERVAL 7 DAY
              AND b.check_out_date >= CURDATE()
        )
        """, nativeQuery = true)
    List<Room> getAvailableRoomsThisWeek();

    @Query(value = """
        SELECT r.room_name AS roomName, r.price_per_night AS pricePerNight
        FROM Rooms r
        WHERE r.price_per_night > (
            SELECT AVG(r2.price_per_night)
            FROM Rooms r2
            WHERE r2.room_type = r.room_type
            GROUP BY r2.room_type
        )
        """, nativeQuery = true)
    List<RoomNameAndPriceDTO> findRoomsAboveAveragePrice();
}
