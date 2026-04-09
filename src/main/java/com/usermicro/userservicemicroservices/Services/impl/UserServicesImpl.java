package com.usermicro.userservicemicroservices.Services.impl;

import com.usermicro.userservicemicroservices.Repository.UserRepo;
import com.usermicro.userservicemicroservices.Services.userServices;
import com.usermicro.userservicemicroservices.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class UserServicesImpl implements userServices {

    @Autowired
    private UserRepo userRepo;

    @Override
    public User saveUser(User user) {
        return userRepo.save(user);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepo.findAll();
    }

    @Override
    public User getUserById(String id) {
        return userRepo.findById(id).orElse(null);
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepo.findAll().stream().filter(user -> user.getEmail().equals(email)).findFirst().orElse(null);
    }

    @Override
    public void deleteUserById(String id) {
        userRepo.deleteById(id);
    }

    @Override
    public User updateUserById(String id, String newName, String newEmail) {
        User user = userRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setName(newName);
        user.setEmail(newEmail);

        return userRepo.save(user);
    }
}
