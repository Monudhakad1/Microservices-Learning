package com.usermicro.userservicemicroservices.Services.impl;

import com.usermicro.userservicemicroservices.Exceptions.ResourceNotFoundException;
import com.usermicro.userservicemicroservices.Repository.UserRepo;
import com.usermicro.userservicemicroservices.Services.userServices;
import com.usermicro.userservicemicroservices.entity.Dto.HotelDto;
import com.usermicro.userservicemicroservices.entity.Dto.RatingDto;
import com.usermicro.userservicemicroservices.entity.Dto.UserDto;
import com.usermicro.userservicemicroservices.entity.User;
import com.usermicro.userservicemicroservices.Mapper.userMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import static java.util.Arrays.stream;

@Service
public class UserServicesImpl implements userServices {

    private final UserRepo userRepo;
    private final userMapper userMapper;



    public UserServicesImpl(UserRepo userRepo, userMapper userMapper) {
        this.userRepo = userRepo;
        this.userMapper = userMapper;
    }

    @Autowired
    private RestTemplate restTemplate ;

    private Logger logger= LoggerFactory.getLogger(UserServicesImpl.class);


    //  SAVE
    @Override
    public UserDto saveUser(UserDto userDto) {

        User user = userMapper.toEntity(userDto);
        User saved = userRepo.save(user);
        return userMapper.toDto(saved);
    }

    //  GET ALL
    @Override
    public List<UserDto> getAllUsers() {

        // STEP 1: get all users
        List<User> users = userRepo.findAll();

        // STEP 2: collect userIds
        List<String> userIds = users.stream()
                .map(User::getUserId)
                .toList();

        // STEP 3: call rating service ONCE
        ResponseEntity<List<RatingDto>> response =
                restTemplate.exchange(
                        "http://localhost:8081/api/ratings/users?ids=" + String.join(",", userIds),
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<List<RatingDto>>() {}
                );

        List<RatingDto> allRatings = response.getBody();

        // STEP 4: group ratings by userId
        Map<String, List<RatingDto>> ratingMap =
                allRatings.stream()
                        .collect(Collectors.groupingBy(RatingDto::getUserId));

        // STEP 5: map users + attach ratings
        return users.stream().map(user -> {

            UserDto dto = userMapper.toDto(user);

            dto.setRatings(
                    ratingMap.getOrDefault(user.getUserId(), new ArrayList<>())
            );

            return dto;

        }).toList();
    }

    //  GET BY ID
    @Override
    public UserDto getUserById(String id) {

        // STEP 1: Get user
        User user = userRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        // STEP 2: Fetch ratings (FIXED URL ✅)
        ResponseEntity<List<RatingDto>> response =
                restTemplate.exchange(
                        "http://localhost:8081/api/ratings/user/" + id,
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<List<RatingDto>>() {}
                );

        List<RatingDto> ratings = response.getBody();

        // STEP 3: Fetch hotel for each rating
        if (ratings != null) {
            ratings.forEach(rating -> {
                try {
                    HotelDto hotel = restTemplate.getForObject(
                            "http://localhost:8082/api/hotels/" + rating.getHotelId(),
                            HotelDto.class
                    );

                    rating.setHotel(hotel); // ✅ FIXED

                } catch (Exception e) {
                    System.out.println("Hotel not found for id: " + rating.getHotelId());
                }
            });
        }

        // STEP 4: Map user
        UserDto userDto = userMapper.toDto(user);

        // STEP 5: Attach ratings
        userDto.setRatings(ratings);

        return userDto;
    }

    //  GET BY EMAIL
    @Override
    public UserDto getUserByEmail(String email) {
        User user = userRepo.findAll()
                .stream()
                .filter(u -> u.getEmail().equals(email))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        return userMapper.toDto(user);
    }

    //  DELETE
    @Override
    public void deleteUserById(String id) {
        userRepo.deleteById(id);
    }

    //  UPDATE
    @Override
    public UserDto updateUserById(String id, String newName, String newEmail) {
        User user = userRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        user.setName(newName);
        user.setEmail(newEmail);

        User updated = userRepo.save(user);

        return userMapper.toDto(updated);
    }
}