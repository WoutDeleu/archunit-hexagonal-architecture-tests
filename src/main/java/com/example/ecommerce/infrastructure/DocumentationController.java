package com.example.ecommerce.infrastructure;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/docs")
public class DocumentationController {

    @GetMapping
    public String documentation() {
        return "documentation";
    }

    @GetMapping("/api")
    public String apiDocumentation() {
        return "api-docs";
    }
}