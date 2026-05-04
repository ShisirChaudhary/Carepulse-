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
    <link rel="stylesheet" href="<%= ctx %>/css/style.css">
</head>
<body>
<div class="error-page">
    <div class="error-card">
        <div class="error-illustration">
            <svg viewBox="0 0 200 200" width="180" height="180" aria-hidden="true">
                <!-- soft red outer ring -->
                <circle cx="100" cy="100" r="92" fill="#fee2e2"/>
                <circle cx="100" cy="100" r="92" fill="none" stroke="#fca5a5" stroke-width="2" stroke-dasharray="4 6"/>
                <!-- monitor body -->
                <rect x="48" y="60" width="104" height="68" rx="8" fill="#ffffff" stroke="#f87171" stroke-width="2"/>
                <!-- monitor stand -->
                <rect x="86" y="128" width="28" height="6" rx="2" fill="#fca5a5"/>
                <rect x="74" y="134" width="52" height="6" rx="2" fill="#f87171"/>
                <!-- heartbeat that flatlines: pulse, then drop, then flat -->
                <path d="M55 96 L66 96 L72 80 L80 110 L86 88 L92 96 L106 96 L110 96 L145 96"
                      fill="none" stroke="#dc2626" stroke-width="3"
                      stroke-linejoin="round" stroke-linecap="round"/>
                <!-- warning triangle -->
                <path d="M148 56 L162 84 L134 84 Z" fill="#f59e0b" stroke="#b45309" stroke-width="2" stroke-linejoin="round"/>
                <line x1="148" y1="68" x2="148" y2="76" stroke="#ffffff" stroke-width="2.5" stroke-linecap="round"/>
                <circle cx="148" cy="80" r="1.6" fill="#ffffff"/>
                <!-- flicker dots near the flat line -->
                <circle cx="120" cy="96" r="2" fill="#dc2626" opacity="0.6"/>
                <circle cx="130" cy="96" r="2" fill="#dc2626" opacity="0.4"/>
                <circle cx="140" cy="96" r="2" fill="#dc2626" opacity="0.25"/>
            </svg>
        </div>

        <div class="error-code error-code-danger">500</div>
        <h1 class="error-title">Something Went Wrong</h1>
        <p class="error-desc">
            We hit an unexpected error on our end. Our team has been notified.
            Please try again in a moment.
        </p>

        <% if (exception != null) { %>
            <button type="button"
                    class="btn btn-secondary btn-sm mb-2"
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

        <div class="error-actions">
            <a href="<%= ctx %>/login" class="btn btn-primary">Back to Sign In</a>
            <a href="<%= ctx %>/contact" class="btn btn-secondary">Report this Issue</a>
        </div>

        <div class="error-hint">
            If the problem keeps happening,
            <a href="<%= ctx %>/contact">let us know</a>
            and we'll investigate.
        </div>
    </div>
</div>
</body>
</html>
