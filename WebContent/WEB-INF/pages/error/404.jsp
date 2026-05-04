<%@ page contentType="text/html;charset=UTF-8" language="java" isErrorPage="true" %>
<%
    String ctx = request.getContextPath();
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Page Not Found | CarePulse</title>
    <link rel="stylesheet" href="<%= ctx %>/css/style.css">
</head>
<body>
<div class="error-page">
    <div class="error-card">
        <div class="error-illustration">
            <svg viewBox="0 0 200 200" width="180" height="180" aria-hidden="true">
                <!-- soft outer ring -->
                <circle cx="100" cy="100" r="92" fill="#dbeafe"/>
                <circle cx="100" cy="100" r="92" fill="none" stroke="#bfdbfe" stroke-width="2" stroke-dasharray="4 6"/>
                <!-- inner card-like rectangle -->
                <rect x="55" y="60" width="90" height="80" rx="8" fill="#ffffff" stroke="#93c5fd" stroke-width="2"/>
                <!-- heartbeat line - the brand element, but interrupted -->
                <path d="M60 100 L75 100 L82 84 L92 116 L100 92 L108 108 L114 100 L130 100"
                      fill="none" stroke="#2563eb" stroke-width="3"
                      stroke-linejoin="round" stroke-linecap="round"/>
                <!-- floating question marks (small + big) -->
                <text x="148" y="58" font-family="Inter, Arial" font-size="32" font-weight="700" fill="#0d9488">?</text>
                <text x="38" y="160" font-family="Inter, Arial" font-size="22" font-weight="700" fill="#9333ea">?</text>
                <!-- magnifying glass overlay -->
                <circle cx="140" cy="140" r="14" fill="#ffffff" stroke="#0f172a" stroke-width="3"/>
                <line x1="150" y1="150" x2="160" y2="160" stroke="#0f172a" stroke-width="3" stroke-linecap="round"/>
            </svg>
        </div>

        <div class="error-code">404</div>
        <h1 class="error-title">Page Not Found</h1>
        <p class="error-desc">
            The page you're looking for doesn't exist or has been moved.
            Let's get you back to something useful.
        </p>

        <div class="error-actions">
            <a href="<%= ctx %>/login" class="btn btn-primary">Go to Sign In</a>
            <a href="<%= ctx %>/contact" class="btn btn-secondary">Contact Support</a>
        </div>

        <div class="error-hint">
            Try one of these instead:
            <a href="<%= ctx %>/about">About</a>
            <span class="error-hint-sep">·</span>
            <a href="<%= ctx %>/contact">Contact</a>
            <span class="error-hint-sep">·</span>
            <a href="<%= ctx %>/register">Create an account</a>
        </div>
    </div>
</div>
</body>
</html>
