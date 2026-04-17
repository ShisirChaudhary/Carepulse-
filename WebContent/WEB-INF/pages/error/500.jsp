<%@ page contentType="text/html;charset=UTF-8" language="java" isErrorPage="true" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Internal Server Error | CarePulse</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/style.css">
    <style>
        .error-page {
            display: flex;
            flex-direction: column;
            align-items: center;
            justify-content: center;
            min-height: 100vh;
            background-color: #f8fafc;
            text-align: center;
            padding: 2rem;
        }
        .error-code {
            font-size: 6rem;
            font-weight: 700;
            color: #ef4444;
            line-height: 1;
            margin-bottom: 1rem;
        }
        .error-title {
            font-size: 2rem;
            color: #1e293b;
            margin-bottom: 1rem;
        }
        .error-desc {
            color: #64748b;
            margin-bottom: 2rem;
            max-width: 500px;
        }
        .exception-trace {
            background: #fff;
            padding: 1rem;
            border-radius: 6px;
            border: 1px solid #e2e8f0;
            text-align: left;
            overflow-x: auto;
            max-width: 800px;
            width: 100%;
            margin-bottom: 2rem;
            font-family: monospace;
            font-size: 0.85rem;
            color: #ef4444;
            display: none; /* hidden by default, could be toggled */
        }
    </style>
</head>
<body>
    <div class="error-page">
        <div class="error-code">500</div>
        <h1 class="error-title">Internal Server Error</h1>
        <p class="error-desc">We're sorry, but something went wrong on our end. Please try again later.</p>
        
        <% if (exception != null) { %>
            <button class="btn btn-secondary btn-sm" onclick="document.getElementById('trace').style.display='block'" style="margin-bottom: 1rem;">Show Details (Admin only)</button>
            <div id="trace" class="exception-trace">
                <%= exception.getMessage() %><br><br>
                <%
                    for(StackTraceElement el : exception.getStackTrace()) {
                        out.println(el.toString() + "<br>");
                    }
                %>
            </div>
        <% } %>

        <a href="<%= request.getContextPath() %>/login" class="btn btn-primary">Return Home</a>
    </div>
</body>
</html>
