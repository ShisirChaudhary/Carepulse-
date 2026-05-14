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
    <link rel="stylesheet" href="<%= ctx %>/css/style.css?v=20260514">
</head>
<body class="error-body">
    <main class="error-screen">
        <article class="error-panel">
            <figure class="error-figure">
                <svg viewBox="0 0 240 160" width="240" height="160" aria-hidden="true">
                    <defs>
                        <linearGradient id="pulseGrad" x1="0%" y1="0%" x2="100%" y2="0%">
                            <stop offset="0%" stop-color="#2563eb"/>
                            <stop offset="100%" stop-color="#0d9488"/>
                        </linearGradient>
                    </defs>
                    <!-- Baseline -->
                    <line x1="10" y1="80" x2="230" y2="80"
                          stroke="#e2e8f0" stroke-width="1.5" stroke-dasharray="3 5"/>
                    <!-- Strong pulse on the left, then breaks into faded dots -->
                    <path d="M10 80 L40 80 L52 50 L70 110 L86 60 L100 95 L112 80 L140 80"
                          fill="none" stroke="url(#pulseGrad)" stroke-width="3.5"
                          stroke-linejoin="round" stroke-linecap="round"/>
                    <circle cx="150" cy="80" r="2.5" fill="#2563eb" opacity="0.55"/>
                    <circle cx="162" cy="80" r="2.2" fill="#2563eb" opacity="0.4"/>
                    <circle cx="173" cy="80" r="1.9" fill="#2563eb" opacity="0.28"/>
                    <circle cx="183" cy="80" r="1.6" fill="#2563eb" opacity="0.18"/>
                </svg>
            </figure>

            <p class="error-eyebrow">Error 404</p>
            <h1 class="error-headline">Page Not Found</h1>
            <p class="error-lead">
                We searched our records and couldn&rsquo;t locate the page you&rsquo;re looking for.
                It may have moved or no longer exists.
            </p>

            <div class="error-cta">
                <a href="<%= ctx %>/login" class="btn btn-primary">Return to Sign In</a>
                <a href="<%= ctx %>/contact" class="btn btn-secondary">Contact Support</a>
            </div>

            <nav class="error-quicklinks" aria-label="Quick links">
                <a href="<%= ctx %>/about">About</a>
                <a href="<%= ctx %>/contact">Contact</a>
                <a href="<%= ctx %>/register">Create account</a>
            </nav>
        </article>
    </main>
</body>
</html>
