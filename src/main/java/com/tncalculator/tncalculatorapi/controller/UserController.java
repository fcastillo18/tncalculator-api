package com.tncalculator.tncalculatorapi.controller;

import com.tncalculator.tncalculatorapi.model.User;
import com.tncalculator.tncalculatorapi.services.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

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
    @PreAuthorize("hasRole('ADMIN')")
    public Page<User> getAllRecords(@RequestParam(required = false) Map<String, String> filters,
                                      @RequestParam(name = "page", required = false, defaultValue = "0") Integer page,
                                      @RequestParam(name = "size", required = false, defaultValue = "10") Integer size) {
        return userServiceImpl.getAllUsersWithFilterAndPagination(filters, page, size);
    }

    // TODO this will now be partially covered by the AuthController. We might need to remove this controller or create future iterations
//    @PostMapping("/create")
//    @ResponseBody
//    public User createUser(@RequestBody User user) {
//        return userServiceImpl.createUser(user);
//    }
//
//    @PutMapping("/update")
//    @ResponseBody
//    public User updateUser(@RequestBody User user) {
//        return userServiceImpl.updateUser(user);
//    }

}
