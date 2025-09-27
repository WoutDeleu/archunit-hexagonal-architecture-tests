package com.example.core.user;

import com.example.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * ❌ VIOLATION 1: Core using Spring annotations
 * ❌ Fails: core_should_not_use_spring_annotations
 *
 * ❌ VIOLATION 2: Core depending on adapters (repositories package)
 * ❌ Fails: core_should_not_depend_on_adapters
 *
 * ❌ VIOLATION 3: Core using field injection
 * ❌ Fails: autowired_should_not_be_used_in_core
 *
 * ❌ VIOLATION 4: Core depending on Spring framework
 * ❌ Fails: core_should_not_depend_on_spring_framework
 */
@Service  // ❌ Spring annotation in core!
public class UserService {

    @Autowired  // ❌ Field injection in core!
    private UserRepository userRepository;  // ❌ Core depending on adapter!

    public User createUser(String name, String email) {
        User user = new User(null, name, email);
        return userRepository.save(user);  // ❌ Core calling adapter directly!
    }

    public User findUser(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}