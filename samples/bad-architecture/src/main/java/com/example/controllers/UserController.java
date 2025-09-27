package com.example.controllers;

import com.example.core.user.User;
import com.example.core.user.UserService;
import com.example.repositories.UserRepository;
import org.springframework.web.bind.annotation.*;

/**
 * ❌ VIOLATION 1: Controller NOT in adapters.api package
 * ❌ Fails: controllers_should_be_in_api_adapters
 *
 * ❌ VIOLATION 2: Controller depending directly on repository (cross-adapter dependency)
 * ❌ Fails: api_adapters_should_not_use_database_adapters_directly
 * ❌ Fails: adapters_should_not_depend_on_other_adapter_types
 */
@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;
    private final UserRepository userRepository;  // ❌ Direct repository dependency!

    public UserController(UserService userService, UserRepository userRepository) {
        this.userService = userService;
        this.userRepository = userRepository;  // ❌ Cross-adapter dependency!
    }

    @PostMapping
    public User createUser(@RequestBody CreateUserRequest request) {
        return userService.createUser(request.getName(), request.getEmail());
    }

    @GetMapping("/{id}")
    public User getUser(@PathVariable Long id) {
        // ❌ Controller directly accessing repository instead of service!
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public static class CreateUserRequest {
        private String name;
        private String email;

        // Getters and setters
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
    }
}