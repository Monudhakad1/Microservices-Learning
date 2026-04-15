package com.usermicro.userservicemicroservices.Controllers;

import com.usermicro.userservicemicroservices.Services.userServices;
import com.usermicro.userservicemicroservices.entity.Dto.UserDto;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final userServices userService;
    Logger log = LoggerFactory.getLogger(UserController.class);
    //  CREATE
    @PostMapping
    public ResponseEntity<UserDto> createUser(@RequestBody UserDto userDto) {
        UserDto savedUser = userService.saveUser(userDto);
        return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
    }

    //  GET ALL
    int retryCount= 1;
//    @CircuitBreaker(name = "ratingHotelBreaker", fallbackMethod = "ratingHotelFallback")
    @GetMapping
    @Retry(name="ratingHotelService",fallbackMethod = "ratingHotelFallback")
    public ResponseEntity<List<UserDto>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    //  GET BY ID
    @GetMapping("/{id}")
//    @CircuitBreaker(name = "ratingHotelBreakerById", fallbackMethod = "ratingHotelFallbackById")
//    @Retry(name="ratingHotelServiceById",fallbackMethod = "ratingHotelFallbackById")
    @RateLimiter(name="userRateLimiter",fallbackMethod = "ratingHotelFallbackById")
    public ResponseEntity<UserDto> getUserById(@PathVariable String id) {
        log.info("Retry attempt: " + retryCount);
        retryCount++;
        return ResponseEntity.ok(userService.getUserById(id));
    }

    //  GET BY EMAIL
    @GetMapping("/email")
    public ResponseEntity<UserDto> getUserByEmail(@RequestParam String email) {
        return ResponseEntity.ok(userService.getUserByEmail(email));
    }

    // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<UserDto> updateUser(
            @PathVariable String id,
            @RequestParam String name,
            @RequestParam String email
    ) {
        UserDto updatedUser = userService.updateUserById(id, name, email);
        return ResponseEntity.ok(updatedUser);
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable String id) {
        userService.deleteUserById(id);
        return ResponseEntity.ok("User deleted successfully");
    }

    // fallback method for circuit breaker
    public ResponseEntity<List<UserDto>> ratingHotelFallback(Exception e) {
        // Log the exception (optional)
        System.err.println("Circuit breaker triggered: " + e.getMessage());

        // Return a fallback response, e.g., an empty list or a custom message
        return ResponseEntity.ok(List.of()); // returning an empty list as fallback
    }

    //fallback for fetching using user id


    public ResponseEntity<UserDto> ratingHotelFallbackById(String id, Exception e) {
        // Log the exception (optional)
        System.err.println("Circuit breaker triggered for user id: " + id + " - " + e.getMessage());

        // Return a fallback response, e.g., a default user or an error message
        UserDto user = UserDto.builder()
                .userId(id)
                .name("Unknown User")
                .email("dummy@gmail.com")
                .about("Dummy about services are down")
                .build();
        return new ResponseEntity<>(user, HttpStatus.BAD_REQUEST);
    }
}