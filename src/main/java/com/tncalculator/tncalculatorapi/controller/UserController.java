package com.tncalculator.tncalculatorapi.controller;

import com.tncalculator.tncalculatorapi.model.User;
import com.tncalculator.tncalculatorapi.services.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/user")
@Validated
public class UserController {

    private final UserServiceImpl userServiceImpl;

    @Autowired
    public UserController(UserServiceImpl userServiceImpl) {
        this.userServiceImpl = userServiceImpl;
    }

    @GetMapping("/all")
    @ResponseBody
    public List<User> getAll() {
        return userServiceImpl.getAllUsers();
    }

}
