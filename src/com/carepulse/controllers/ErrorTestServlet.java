package com.carepulse.controllers;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

// Servlet used during development to verify the custom 500 error page.
// Throws a runtime exception on every request.
@WebServlet("/error-test")
public class ErrorTestServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        throw new RuntimeException(
            "This is a test exception to demonstrate the custom 500 error page.");
    }
}
