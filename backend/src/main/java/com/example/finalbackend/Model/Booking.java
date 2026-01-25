package com.example.finalbackend.Model;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name="Bookings")
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "booking_id")
    private Integer bookingId;

    @ManyToOne
    @JoinColumn(name = "client_id",nullable = false)
    @JsonIgnore
    private Client client;

    @OneToOne
    @JoinColumn(name = "room_id",nullable = false)
    @JsonIgnore
    private Room room;

    @Column(name = "check_in_date",nullable = false)
    private LocalDate bookingCheckInDate;

    @Column(name = "check_out_date",nullable = false)
    private LocalDate  bookingCheckOutDate;

    @Positive(message = "Error, price must be positive!")
    @Column(name = "total_price", nullable = false, precision = 10, scale = 2)
    private BigDecimal bookingTotalPrice;

    public Booking() {
    }

    public Booking(Integer bookingId, Client client, Room room, LocalDate bookingCheckInDate, LocalDate bookingCheckOutDate, BigDecimal bookingTotalPrice) {
        this.bookingId = bookingId;
        this.client = client;
        this.room = room;
        this.bookingCheckInDate = bookingCheckInDate;
        this.bookingCheckOutDate = bookingCheckOutDate;
        this.bookingTotalPrice = bookingTotalPrice;
    }

    public Integer getBookingId() {
        return bookingId;
    }

    public void setBookingId(Integer bookingId) {
        this.bookingId = bookingId;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public LocalDate getBookingCheckInDate() {
        return bookingCheckInDate;
    }

    public void setBookingCheckInDate(LocalDate bookingCheckInDate) {
        this.bookingCheckInDate = bookingCheckInDate;
    }

    public LocalDate getBookingCheckOutDate() {
        return bookingCheckOutDate;
    }

    public void setBookingCheckOutDate(LocalDate bookingCheckOutDate) {
        this.bookingCheckOutDate = bookingCheckOutDate;
    }

    public BigDecimal getBookingTotalPrice() {
        return bookingTotalPrice;
    }

    public void setBookingTotalPrice(BigDecimal bookingTotalPrice) {
        this.bookingTotalPrice = bookingTotalPrice;
    }

    @Override
    public String toString() {
        return "Booking{" +
                "bookingId=" + bookingId +
                ", client=" + client +
                ", room=" + room +
                ", bookingCheckInDate=" + bookingCheckInDate +
                ", bookingCheckOutDate=" + bookingCheckOutDate +
                ", bookingTotalPrice=" + bookingTotalPrice +
                '}';
    }
}
