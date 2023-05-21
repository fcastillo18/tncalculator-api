package com.tncalculator.tncalculatorapi.controller;

import com.tncalculator.tncalculatorapi.constant.Constants;
import com.tncalculator.tncalculatorapi.model.User;
import com.tncalculator.tncalculatorapi.services.impl.UserServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
public class UserControllerTests {

    private UserController userController;

    @Mock
    private UserServiceImpl userServiceImpl;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userController = new UserController(userServiceImpl);
    }

    @Test
    void testGetAllUsers() {
        // Prepare
        List<User> users = new ArrayList<>();
        users.add(User.builder()
                .id(1L)
                .username("franklin@mail.com")
                .password("password")
                .status(Constants.UserStatus.ACTIVE.toString())
                .balance(BigDecimal.valueOf(100.0))
                .build());
        users.add(User.builder()
                .id(2L)
                .username("jose@mail.com")
                .password("password")
                .status(Constants.UserStatus.ACTIVE.toString())
                .balance(BigDecimal.valueOf(200.0))
                .build());
        when(userServiceImpl.getAllUsers()).thenReturn(users);

        // Execute
        List<User> result = userController.getAll();

        // Verify
        Assertions.assertEquals(2, result.size());
        Assertions.assertEquals("franklin@mail.com", result.get(0).getUsername());
        Assertions.assertEquals(Constants.UserStatus.ACTIVE.toString(), result.get(1).getStatus());
    }


    @Test
    @DisplayName("Test Create User")
    public void testCreateUser() {
        // Mock data
        User user = User.builder()
                .id(1L)
                .username("franklin@mail.com")
                .password("password1")
                .status("active")
                .balance(BigDecimal.valueOf(100.0))
                .build();

        // Mock service method
        when(userServiceImpl.createUser(any(User.class))).thenReturn(user);

        // Call the controller method
        User result = userController.createUser(user);

        // Verify the result
        Assertions.assertNotNull(result);
        Assertions.assertEquals(user.getId(), result.getId());
        Assertions.assertEquals(user.getUsername(), result.getUsername());
        Assertions.assertEquals(user.getPassword(), result.getPassword());
        Assertions.assertEquals(user.getStatus(), result.getStatus());
        Assertions.assertEquals(user.getBalance(), result.getBalance());
    }

    @Test
    @DisplayName("Test Update User")
    public void testUpdateUser() {
        // Mock data
        User user = User.builder()
                .id(1L)
                .username("jose@mail.com")
                .password("password1")
                .status("active")
                .balance(BigDecimal.valueOf(100.0))
                .build();

        // Mock service method
        when(userServiceImpl.updateUser(any(User.class))).thenReturn(user);

        // Call the controller method
        User result = userController.updateUser(user);

        // Verify the result
        Assertions.assertNotNull(result);
        Assertions.assertEquals(user.getId(), result.getId());
        Assertions.assertEquals(user.getUsername(), result.getUsername());
        Assertions.assertEquals(user.getPassword(), result.getPassword());
        Assertions.assertEquals(user.getStatus(), result.getStatus());
        Assertions.assertEquals(user.getBalance(), result.getBalance());
    }
}
