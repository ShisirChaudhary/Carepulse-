package com.carepulse.util;

/**
 * Custom exception class for CarePulse business logic failures.
 * This ensures that standard application validation errors are safely displayed
 * to the user, while preventing deeper SQL or server exceptions from leaking.
 */
public class CarePulseException extends Exception {
    public CarePulseException(String message) {
        super(message);
    }
}
