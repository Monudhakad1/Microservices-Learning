package com.usermicro.userservicemicroservices.Services.impl;

import com.usermicro.userservicemicroservices.Exceptions.ResourceNotFoundException;
import com.usermicro.userservicemicroservices.Repository.UserRepo;
import com.usermicro.userservicemicroservices.Services.userServices;
import com.usermicro.userservicemicroservices.entity.Dto.UserDto;
import com.usermicro.userservicemicroservices.entity.User;
import com.usermicro.userservicemicroservices.Mapper.userMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServicesImpl implements userServices {

    private final UserRepo userRepo;
    private final userMapper userMapper;

    public UserServicesImpl(UserRepo userRepo, userMapper userMapper) {
        this.userRepo = userRepo;
        this.userMapper = userMapper;
    }

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
        return userRepo.findAll()
                .stream()
                .map(userMapper::toDto)
                .collect(Collectors.toList());
    }

    //  GET BY ID
    @Override
    public UserDto getUserById(String id) {
        User user = userRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return userMapper.toDto(user);
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