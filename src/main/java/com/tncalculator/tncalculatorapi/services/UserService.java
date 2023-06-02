package com.tncalculator.tncalculatorapi.services;

import com.tncalculator.tncalculatorapi.model.User;
import org.springframework.data.domain.Page;

import java.util.Map;

public interface UserService {

    User createUser(User user);
    User updateUser(User user);

    Page<User> getAllUsersWithFilterAndPagination(Map<String, String> filters, int page, int size);

    User getUserById(Long id);
}
