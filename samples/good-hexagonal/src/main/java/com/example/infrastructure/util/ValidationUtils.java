package com.example.infrastructure.util;

/**
 * Utility class - correctly placed in infrastructure.
 * ✅ Passes: infrastructure_should_only_contain_config_and_utils
 * ✅ Passes: infrastructure_should_not_contain_business_logic
 */
public class ValidationUtils {

    public static boolean isValidIsbn(String isbn) {
        if (isbn == null || isbn.trim().isEmpty()) {
            return false;
        }

        // Remove hyphens and spaces
        String cleanIsbn = isbn.replaceAll("[-\\s]", "");

        // Check if it's ISBN-10 or ISBN-13
        return isValidIsbn10(cleanIsbn) || isValidIsbn13(cleanIsbn);
    }

    private static boolean isValidIsbn10(String isbn) {
        if (isbn.length() != 10) {
            return false;
        }

        // ISBN-10 validation logic
        int sum = 0;
        for (int i = 0; i < 9; i++) {
            if (!Character.isDigit(isbn.charAt(i))) {
                return false;
            }
            sum += (isbn.charAt(i) - '0') * (10 - i);
        }

        char lastChar = isbn.charAt(9);
        if (lastChar == 'X') {
            sum += 10;
        } else if (Character.isDigit(lastChar)) {
            sum += (lastChar - '0');
        } else {
            return false;
        }

        return sum % 11 == 0;
    }

    private static boolean isValidIsbn13(String isbn) {
        if (isbn.length() != 13) {
            return false;
        }

        // ISBN-13 validation logic
        int sum = 0;
        for (int i = 0; i < 12; i++) {
            if (!Character.isDigit(isbn.charAt(i))) {
                return false;
            }
            int digit = isbn.charAt(i) - '0';
            sum += (i % 2 == 0) ? digit : digit * 3;
        }

        int checkDigit = isbn.charAt(12) - '0';
        int calculatedCheckDigit = (10 - (sum % 10)) % 10;

        return checkDigit == calculatedCheckDigit;
    }
}