package com.example;

/**
 * Main Spring Boot application class.
 * This should be in the root package (com.example) and outside of hexagonal architecture layers.
 *
 * Note: In a real project, this would be annotated with @SpringBootApplication
 * For testing purposes without full Spring dependencies, we simulate this with a comment.
 */
// @SpringBootApplication  // This annotation would be present in a real Spring Boot project
public class Application {

    public static void main(String[] args) {
        // SpringApplication.run(Application.class, args);
        System.out.println("Application started");
    }
}