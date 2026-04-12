package com.usermicro.userservicemicroservices.ExternalServices;

import com.usermicro.userservicemicroservices.entity.Dto.RatingDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name="RATINGSERVICE")
public interface RatingService {



     //GET RATINGS BY MULTIPLE USER IDS
    @GetMapping("/api/ratings/users")
    List<RatingDto> getRatingsByUserIds(@RequestParam("ids") List<String> ids);


    // CREATE RATING
    @PostMapping("/api/ratings")
    RatingDto createRating(@RequestBody RatingDto ratingDto);

    // GET ALL RATINGS
    @GetMapping("/api/ratings")
    List<RatingDto> getAllRatings();

    // GET RATING BY USER ID
    @GetMapping("/api/ratings/user/{userId}")
    List<RatingDto> getRatingsByUserId(@PathVariable("userId") String userId);

    // GET RATING BY HOTEL ID
    @GetMapping("/api/ratings/hotel/{hotelId}")
    List<RatingDto> getRatingsByHotelId(@PathVariable("hotelId") String hotelId);
}
