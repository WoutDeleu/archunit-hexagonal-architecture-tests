package com.x.y.z.team.nameapp.core;

/**
 * This is an INCORRECT placement - SpringBoot application should NOT be in core layer.
 * This would violate the hexagonal architecture rule.
 */
// @SpringBootApplication  // This would violate the rule if present
public class WrongApplication {

    public static void main(String[] args) {
        System.out.println("Wrong placement - should not be in core!");
    }
}