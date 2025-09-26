package com.x.y.z.team.nameapp;

/**
 * Main Spring Boot application class.
 * This demonstrates a deeper package structure: com.x.y.z.team.nameapp
 * The rule should accept any root package as long as it's outside hexagonal layers.
 *
 * Note: In a real project, this would be annotated with @SpringBootApplication
 */
// @SpringBootApplication  // This annotation would be present in a real Spring Boot project
public class NameappApplication {

    public static void main(String[] args) {
        // SpringApplication.run(NameappApplication.class, args);
        System.out.println("Nameapp Application started");
    }
}