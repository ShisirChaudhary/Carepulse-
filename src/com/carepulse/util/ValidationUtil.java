package com.carepulse.util;

import java.util.regex.Pattern;

// Static helper methods used by controllers to validate user input.
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

    // Returns true when the supplied string is null or contains only whitespace.
    public static boolean isEmpty(String value) {
        return value == null || value.trim().isEmpty();
    }

    // Returns true when the name contains only letters and spaces (2-100 characters).
    public static boolean isValidName(String name) {
        return !isEmpty(name) && NAME_PATTERN.matcher(name.trim()).matches();
    }

    // Returns true when the email matches the basic email pattern.
    public static boolean isValidEmail(String email) {
        return !isEmpty(email) && EMAIL_PATTERN.matcher(email.trim()).matches();
    }

    // Returns true when the phone consists of exactly ten digits.
    public static boolean isValidPhone(String phone) {
        return !isEmpty(phone) && PHONE_PATTERN.matcher(phone.trim()).matches();
    }

    // Returns true when the password has at least six characters.
    public static boolean isValidPassword(String password) {
        return !isEmpty(password) && password.trim().length() >= 6;
    }
}
