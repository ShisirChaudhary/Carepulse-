package com.carepulse.util;

// Application-level exception used to surface user-visible business errors
// (such as locked accounts or invalid reset tokens) without exposing
// lower-level database or server exceptions.
public class CarePulseException extends Exception {
    public CarePulseException(String message) {
        super(message);
    }
}
