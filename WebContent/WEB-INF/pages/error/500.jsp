<%@ page contentType="text/html;charset=UTF-8" language="java" isErrorPage="true" %>
<%
    String ctx = request.getContextPath();
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Internal Server Error | CarePulse</title>
    <link rel="stylesheet" href="<%= ctx %>/css/style.css?v=20260514">
</head>
<body class="error-body">
    <main class="error-screen">
        <article class="error-panel error-panel-danger">
            <figure class="error-figure">
                <svg viewBox="0 0 240 160" width="240" height="160" aria-hidden="true">
                    <defs>
                        <linearGradient id="dangerGrad" x1="0%" y1="0%" x2="100%" y2="0%">
                            <stop offset="0%" stop-color="#dc2626"/>
                            <stop offset="100%" stop-color="#f97316"/>
                        </linearGradient>
                    </defs>
                    <!-- Baseline -->
                    <line x1="10" y1="80" x2="230" y2="80"
                          stroke="#fecaca" stroke-width="1.5" stroke-dasharray="3 5"/>
                    <!-- Pulse, then flatlines -->
                    <path d="M10 80 L40 80 L52 50 L70 110 L86 60 L100 95 L112 80 L230 80"
                          fill="none" stroke="url(#dangerGrad)" stroke-width="3.5"
                          stroke-linejoin="round" stroke-linecap="round"/>
                </svg>
            </figure>

            <p class="error-eyebrow error-eyebrow-danger">Error 500</p>
            <h1 class="error-headline">Something Went Wrong</h1>
            <p class="error-lead">
                We hit an unexpected error on our end. Our team has been notified.
                Please try again in a moment.
            </p>

            <% if (exception != null) { %>
                <button type="button" class="error-trace-toggle"
                        onclick="document.getElementById('trace').classList.add('is-visible'); this.style.display='none';">
                    Show Technical Details
                </button>
                <div id="trace" class="exception-trace">
                    <strong><%= exception.getClass().getSimpleName() %>:</strong> <%= exception.getMessage() %><br><br>
                    <%
                        for (StackTraceElement el : exception.getStackTrace()) {
                            out.println(el.toString() + "<br>");
                        }
                    %>
                </div>
            <% } %>

            <div class="error-cta">
                <a href="<%= ctx %>/login" class="btn btn-primary">Return to Sign In</a>
                <a href="<%= ctx %>/contact" class="btn btn-secondary">Report this Issue</a>
            </div>

            <nav class="error-quicklinks" aria-label="Quick links">
                <a href="<%= ctx %>/about">About</a>
                <a href="<%= ctx %>/contact">Contact</a>
            </nav>
        </article>
    </main>
</body>
</html>
