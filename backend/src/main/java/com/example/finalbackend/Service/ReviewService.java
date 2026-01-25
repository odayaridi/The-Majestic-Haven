package com.example.finalbackend.Service;

import com.example.finalbackend.Exception.ResourceNotFoundException;
import com.example.finalbackend.Model.Client;
import com.example.finalbackend.Model.Review;
import com.example.finalbackend.Model.Room;
import com.example.finalbackend.Repository.ClientRepository;
import com.example.finalbackend.Repository.ReviewRepository;
import com.example.finalbackend.Repository.RoomRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

@Service
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final ClientRepository clientRepository;
    private final RoomRepository roomRepository;


    public ReviewService(ReviewRepository reviewRepository, ClientRepository clientRepository, RoomRepository roomRepository) {
        this.reviewRepository = reviewRepository;
        this.clientRepository = clientRepository;
        this.roomRepository = roomRepository;
    }

    public Review sendClientReview(Integer clientId,Integer roomId,Review review){
            Client client = clientRepository.findById(clientId).orElseThrow(() ->
                    new ResourceNotFoundException("Error in retrieving client!"));
            Room room = roomRepository.findById(roomId).orElseThrow(() ->
                    new ResourceNotFoundException("Error in retrieving room!"));
            review.setClient(client);
            review.setRoom(room);
            review.setCreatedAt(LocalDateTime.now());
        return reviewRepository.save(review);
    }
}

