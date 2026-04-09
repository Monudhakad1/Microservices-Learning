package com.usermicro.userservicemicroservices.Services;

import com.usermicro.userservicemicroservices.entity.User;

import java.util.List;

public interface userServices {

    User saveUser(User user);

    List<User> getAllUsers();

    User getUserById(String id);

    User getUserByEmail(String email);

    void deleteUserById(String id);

    User updateUserById(String id,String newName,String newEmail );

}
