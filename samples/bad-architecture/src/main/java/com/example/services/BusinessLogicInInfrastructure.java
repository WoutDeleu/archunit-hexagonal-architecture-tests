package com.example.services;

import com.example.core.user.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * ❌ VIOLATION 1: Business logic in wrong package (should be in core)
 * ❌ Fails: infrastructure_should_not_contain_business_logic
 *
 * ❌ VIOLATION 2: @Service annotation outside adapters
 * ❌ Fails: services_should_be_in_adapters_only
 */
@Service
public class BusinessLogicInInfrastructure {

    /**
     * ❌ This is pure business logic but placed in infrastructure/services package!
     */
    public List<User> filterActiveUsers(List<User> users) {
        return users.stream()
                .filter(user -> user.getEmail() != null && !user.getEmail().isEmpty())
                .filter(user -> user.getName() != null && !user.getName().isEmpty())
                .collect(Collectors.toList());
    }

    /**
     * ❌ More business logic in wrong place!
     */
    public boolean isValidUser(User user) {
        return user != null
            && user.getName() != null
            && user.getEmail() != null
            && user.getEmail().contains("@");
    }
}