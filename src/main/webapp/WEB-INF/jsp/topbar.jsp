<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<header class="rs-topbar d-flex justify-content-between align-items-center">
    <h4 class="mb-0">${pageTitle}</h4>
    <div class="d-flex align-items-center gap-3">
        <span class="badge bg-primary">${currentUser.role}</span>
        <span class="text-muted"><i class="bi bi-person-circle"></i> ${currentUser.fullName}</span>
        <a href="${pageContext.request.contextPath}/logout" class="btn btn-outline-secondary btn-sm">Logout</a>
    </div>
</header>
