package com.example.login.service;

import com.example.login.dto.UserDto;
import com.example.login.entity.User;

import java.util.List;

public interface UserService {
    void saveUser(UserDto userDto);

    User findUserByEmail(String email);

    List<UserDto> findAllUsers();
}
