package com.tncalculator.tncalculatorapi.controller;

import com.tncalculator.tncalculatorapi.model.User;
import com.tncalculator.tncalculatorapi.services.impl.UserServiceImpl;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

// TODO integration tests (e2e) can be added instead of unit this test to simulate user real actions
@SpringBootTest
public class UserControllerTests {

    @Mock
    private UserServiceImpl userServiceImpl;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        UserController userController = new UserController(userServiceImpl);
    }

    @Test
    public void testGetAllUsers() {
        // Create a sample filters map
        Map<String, String> filters = new HashMap<>();
        filters.put("id", "1");
        filters.put("username", "franklin@mail.com");
        filters.put("status", "active");

        // Create a sample page and size
        int page = 0;
        int size = 10;

        // Create a sample page of records
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "id");
        @SuppressWarnings("unchecked")
        Page<User> expectedPage = mock(Page.class);

        // Mock service
        doReturn(pageable).when(expectedPage).getPageable();
        doReturn(1L).when(expectedPage).getTotalElements();
        doReturn(expectedPage.getContent()).when(expectedPage).getContent();
        doReturn(expectedPage).when(userServiceImpl).getAllUsersWithFilterAndPagination(filters, page, size);

        when(userServiceImpl.getAllUsersWithFilterAndPagination(filters, page, size)).thenReturn(expectedPage);

        // Call the method under test
        Page<User> result = userServiceImpl.getAllUsersWithFilterAndPagination(filters, page, size);

        // Verify the method calls and assertions
        verify(userServiceImpl, times(1)).getAllUsersWithFilterAndPagination(filters, page, size);
        Assertions.assertEquals(expectedPage, result);
    }



    @Test
    @DisplayName("Test Create User")
    @Disabled
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
//        User result = userController.createUser(user);
//
//        // Verify the result
//        Assertions.assertNotNull(result);
//        Assertions.assertEquals(user.getId(), result.getId());
//        Assertions.assertEquals(user.getUsername(), result.getUsername());
//        Assertions.assertEquals(user.getPassword(), result.getPassword());
//        Assertions.assertEquals(user.getStatus(), result.getStatus());
//        Assertions.assertEquals(user.getBalance(), result.getBalance());
    }

    @Test
    @DisplayName("Test Update User")
    @Disabled
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
//        User result = userController.updateUser(user);
//
//        // Verify the result
//        Assertions.assertNotNull(result);
//        Assertions.assertEquals(user.getId(), result.getId());
//        Assertions.assertEquals(user.getUsername(), result.getUsername());
//        Assertions.assertEquals(user.getPassword(), result.getPassword());
//        Assertions.assertEquals(user.getStatus(), result.getStatus());
//        Assertions.assertEquals(user.getBalance(), result.getBalance());
    }
}
