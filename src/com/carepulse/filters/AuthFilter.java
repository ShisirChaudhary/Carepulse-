package com.carepulse.filters;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

// Servlet filter that protects the admin and patient URL trees.
// Anything outside those trees is passed through so unknown paths
// can return the custom 404 page defined in web.xml.
@WebFilter("/*")
public class AuthFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        String path = req.getRequestURI().substring(req.getContextPath().length());

        // Public pages are accessible without a session.
        if (isPublicPath(path)) {
            chain.doFilter(request, response);
            return;
        }

        // Only the admin and patient URL trees require authentication.
        // Other unknown paths fall through to the 404 mapping.
        boolean isProtected = path.startsWith("/admin") || path.startsWith("/patient");
        if (!isProtected) {
            chain.doFilter(request, response);
            return;
        }

        // Protected path: redirect to login when no session is active.
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            res.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        String role = (String) session.getAttribute("role");

        // Send the user to their own dashboard if they hit the wrong tree.
        if (path.startsWith("/admin") && !"admin".equals(role)) {
            res.sendRedirect(req.getContextPath() + "/patient");
            return;
        }
        if (path.startsWith("/patient") && !"patient".equals(role)) {
            res.sendRedirect(req.getContextPath() + "/admin");
            return;
        }

        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
    }

    // Returns true for paths that should always be reachable without a login.
    private boolean isPublicPath(String path) {
        return path.equals("/") ||
               path.equals("/login") ||
               path.equals("/register") ||
               path.equals("/logout") ||
               path.equals("/forgot-password") ||
               path.equals("/about") ||
               path.equals("/contact") ||
               path.equals("/error-test") ||
               path.startsWith("/css/") ||
               path.startsWith("/images/");
    }
}
