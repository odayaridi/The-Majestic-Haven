package com.example.finalbackend.Controller;

import com.example.finalbackend.Exception.ResourceNotFoundException;
import com.example.finalbackend.Model.Review;
import com.example.finalbackend.Service.ReviewService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", allowCredentials = "false")
@RestController
@RequestMapping("/api/v1/reviews")
public class ReviewController {
    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PostMapping()
    public ResponseEntity<Object> sendReview(@RequestParam Integer cId, @RequestParam Integer rId, @RequestBody Review review){
      try {
          Review newReview = reviewService.sendClientReview(cId, rId, review);
          return ResponseEntity.ok(newReview);
      }
      catch (ResourceNotFoundException e) {
          return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
      }
    }
}
