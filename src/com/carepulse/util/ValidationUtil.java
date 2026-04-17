package com.carepulse.util;

import java.util.regex.Pattern;

/**
 * Validation utility for form input validation.
 */
public class ValidationUtil {

    private static final Pattern EMAIL_PATTERN = Pattern.compile(
        "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
    );

    private static final Pattern NAME_PATTERN = Pattern.compile(
        "^[A-Za-z\\s]{2,100}$"
    );

    private static final Pattern PHONE_PATTERN = Pattern.compile(
        "^\\d{10}$"
    );

    /**
     * Checks if a string is null or blank.
     */
    public static boolean isEmpty(String value) {
        return value == null || value.trim().isEmpty();
    }

    /**
     * Validates name: letters and spaces only, 2-100 characters.
     */
    public static boolean isValidName(String name) {
        return !isEmpty(name) && NAME_PATTERN.matcher(name.trim()).matches();
    }

    /**
     * Validates email format using regex.
     */
    public static boolean isValidEmail(String email) {
        return !isEmpty(email) && EMAIL_PATTERN.matcher(email.trim()).matches();
    }

    /**
     * Validates phone: exactly 10 digits.
     */
    public static boolean isValidPhone(String phone) {
        return !isEmpty(phone) && PHONE_PATTERN.matcher(phone.trim()).matches();
    }

    /**
     * Validates password: minimum 6 characters.
     */
    public static boolean isValidPassword(String password) {
        return !isEmpty(password) && password.trim().length() >= 6;
    }
}
