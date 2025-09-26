package com.example.ecommerce.violations;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;

// VIOLATION: Controller in wrong package (should be in adapters.api)
@RestController
public class BadControllerInCore {

    @GetMapping("/bad")
    public String badEndpoint() {
        return "This controller should not be here!";
    }
}