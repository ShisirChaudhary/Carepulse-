<%@ page contentType="text/html;charset=UTF-8" language="java" isErrorPage="true" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Page Not Found | CarePulse</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/style.css">
    <style>
        .error-page {
            display: flex;
            flex-direction: column;
            align-items: center;
            justify-content: center;
            height: 100vh;
            background-color: #f8fafc;
            text-align: center;
            padding: 2rem;
        }
        .error-code {
            font-size: 6rem;
            font-weight: 700;
            color: #2563eb;
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
            max-width: 400px;
        }
    </style>
</head>
<body>
    <div class="error-page">
        <div class="error-code">404</div>
        <h1 class="error-title">Page Not Found</h1>
        <p class="error-desc">Oops! The page you are looking for does not exist or has been moved.</p>
        <a href="<%= request.getContextPath() %>/login" class="btn btn-primary">Return Home</a>
    </div>
</body>
</html>
