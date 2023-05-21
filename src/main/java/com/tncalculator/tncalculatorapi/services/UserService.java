package com.tncalculator.tncalculatorapi.services;

import com.tncalculator.tncalculatorapi.model.User;

import java.util.List;

public interface UserService {

    List<User> getAllUsers();
    User createUser(User user);
    User updateUser(User user);
}
