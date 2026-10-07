package com.example.expensetracker.service;

import com.example.expensetracker.dto.UserRequest;
import com.example.expensetracker.dto.UserResponse;
import com.example.expensetracker.entity.User;
import com.example.expensetracker.exception.DuplicateResourceException;
import com.example.expensetracker.exception.ResourceNotFoundException;
import com.example.expensetracker.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    private final UserRepository userRepository;

    // Constructor injection: Spring supplies the dependency (dependency injection).
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public UserResponse create(UserRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new DuplicateResourceException("A user with this email already exists");
        }
        User saved = userRepository.save(new User(request.name(), request.email()));
        return UserResponse.from(saved);
    }

    @Transactional(readOnly = true)
    public UserResponse getById(Long id) {
        return userRepository.findById(id)
                .map(UserResponse::from)
                .orElseThrow(() -> new ResourceNotFoundException("User", id));
    }
}
