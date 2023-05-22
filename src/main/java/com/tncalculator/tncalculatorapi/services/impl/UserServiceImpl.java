package com.tncalculator.tncalculatorapi.services.impl;

import com.tncalculator.tncalculatorapi.model.Operation;
import com.tncalculator.tncalculatorapi.model.User;
import com.tncalculator.tncalculatorapi.repository.UserRepository;
import com.tncalculator.tncalculatorapi.services.UserService;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User createUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public User updateUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public Page<User> getAllUsersWithFilterAndPagination(Map<String, String> filters, int page, int size) {
        Sort sort = Sort.by(Sort.Direction.DESC, "id");
        Pageable pageable = PageRequest.of(page, size, sort);

        // Create a new HashMap to store the modified filters
        Map<String, String> modifiedFilters = new HashMap<>(filters);

        // Remove the "page" and "size" keys from the modified filters
        modifiedFilters.remove("page");
        modifiedFilters.remove("size");

        if (!modifiedFilters.isEmpty()) {
            Specification<Operation> specification = (root, query, criteriaBuilder) -> {
                List<Predicate> predicates = new ArrayList<>();
                for (Map.Entry<String, String> entry : modifiedFilters.entrySet()) {
                    String fieldName = entry.getKey();
                    String fieldValue = entry.getValue();
                    if (!StringUtils.isEmpty(fieldName) && !StringUtils.isEmpty(fieldValue)) {
                        Path<String> path = root.get(fieldName);
                        predicates.add(criteriaBuilder.equal(path, fieldValue));
                    }
                }
                return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
            };

            return userRepository.findAll(specification, pageable);
        }

        return userRepository.findAll(pageable);
    }
}
