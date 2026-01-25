package com.example.finalbackend.Model;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "Reviews")
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id")
    private Integer reviewId;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "client_id",nullable = false)
    private Client client;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "room_id",nullable = false)
    private Room room;

    @Lob
    @Column(name = "review_text", nullable = false)
    private String reviewText;

    @Column(name = "created_at", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt;

    public Review() {
    }

    public Review(Integer reviewId, Client client, Room room, String reviewText, LocalDateTime createdAt) {
        this.reviewId = reviewId;
        this.client = client;
        this.room = room;
        this.reviewText = reviewText;
        this.createdAt = createdAt;
    }


    public Integer getReviewId() {
        return reviewId;
    }

    public void setReviewId(Integer reviewId) {
        this.reviewId = reviewId;
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

    public String getReviewText() {
        return reviewText;
    }

    public void setReviewText(String reviewText) {
        this.reviewText = reviewText;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "Review{" +
                "reviewId=" + reviewId +
                ", client=" + client +
                ", room=" + room +
                ", reviewText='" + reviewText + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}

