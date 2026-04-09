package com.usermicro.userservicemicroservices.Services;

import com.usermicro.userservicemicroservices.entity.Dto.UserDto;
import com.usermicro.userservicemicroservices.entity.User;

import java.util.List;

public interface userServices {

    UserDto saveUser(UserDto userDto);

    List<UserDto> getAllUsers();

    UserDto getUserById(String id);

    UserDto getUserByEmail(String email);

    void deleteUserById(String id);

    UserDto updateUserById(String id, String newName, String newEmail);
}