<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Login | ResearchSphere</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css" rel="stylesheet">
    <link href="<%= request.getContextPath() %>/css/style.css" rel="stylesheet">
</head>
<body class="rs-auth-page">
<div class="container px-3">
    <div class="rs-auth-card p-4 p-md-5 mx-auto">
        <div class="text-center mb-4">
            <h1 class="rs-brand h3"><i class="bi bi-globe2"></i> ResearchSphere</h1>
            <p class="text-muted small">Digital Research Collaboration Hub</p>
        </div>

        <c:if test="${not empty error}">
            <div class="alert alert-danger py-2">${error}</div>
        </c:if>
        <c:if test="${not empty success}">
            <div class="alert alert-success py-2">${success}</div>
        </c:if>

        <%-- JSP directive & form with client validation --%>
        <form action="<%= request.getContextPath() %>/login" method="post" data-validate="true" onsubmit="return validateLogin(this);">
            <div class="mb-3">
                <label class="form-label">Email</label>
                <input type="email" name="email" class="form-control" required
                       value="${not empty email ? email : rememberEmail}" placeholder="admin@researchsphere.edu">
            </div>
            <div class="mb-3">
                <label class="form-label">Password</label>
                <input type="password" name="password" id="password" class="form-control" required placeholder="password123">
            </div>
            <div class="mb-3 form-check">
                <input type="checkbox" class="form-check-input" name="rememberMe" id="rememberMe">
                <label class="form-check-label" for="rememberMe">Remember me (Cookie)</label>
            </div>
            <button type="submit" class="btn btn-primary w-100">Sign In</button>
        </form>

        <p class="text-center mt-3 mb-0 small">
            New user? <a href="<%= request.getContextPath() %>/register">Register</a>
        </p>
        <p class="text-center mt-2 mb-0 small text-muted">
            Demo: admin@researchsphere.edu / password123
        </p>
    </div>
</div>
<script>
    var contextPath = '<%= request.getContextPath() %>';
    function validateLogin(form) {
        var email = form.email.value.trim();
        var pass = form.password.value;
        if (!email || !pass) { alert('Please enter email and password.'); return false; }
        return true;
    }
</script>
</body>
</html>
