<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Register | ResearchSphere</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="<%= request.getContextPath() %>/css/style.css" rel="stylesheet">
</head>
<body class="rs-auth-page">
<div class="container px-3">
    <div class="rs-auth-card p-4 p-md-5 mx-auto">
        <h2 class="rs-brand h4 text-center mb-3">Create Account</h2>
        <c:if test="${not empty error}"><div class="alert alert-danger">${error}</div></c:if>
        <form action="<%= request.getContextPath() %>/register" method="post" data-validate="true">
            <div class="mb-3">
                <label class="form-label">Full Name</label>
                <input type="text" name="fullName" class="form-control" required minlength="2" value="${fullName}">
            </div>
            <div class="mb-3">
                <label class="form-label">Email</label>
                <input type="email" name="email" class="form-control" required value="${email}">
            </div>
            <div class="mb-3">
                <label class="form-label">Role</label>
                <select name="role" class="form-select">
                    <option value="STUDENT">Student</option>
                    <option value="FACULTY">Faculty</option>
                </select>
            </div>
            <div class="mb-3">
                <label class="form-label">Password</label>
                <input type="password" name="password" id="password" class="form-control" required minlength="6">
            </div>
            <div class="mb-3">
                <label class="form-label">Confirm Password</label>
                <input type="password" name="confirmPassword" id="confirmPassword" class="form-control" required>
            </div>
            <button type="submit" class="btn btn-primary w-100">Register</button>
        </form>
        <p class="text-center mt-3 small"><a href="<%= request.getContextPath() %>/login">Back to Login</a></p>
    </div>
</div>
<script src="<%= request.getContextPath() %>/js/app.js"></script>
</body>
</html>
