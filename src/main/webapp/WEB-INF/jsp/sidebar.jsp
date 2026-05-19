<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<nav class="rs-sidebar d-flex flex-column">
    <div class="p-3 border-bottom border-secondary">
        <h5 class="text-white mb-0"><i class="bi bi-globe2"></i> ResearchSphere</h5>
        <small class="text-secondary">Collaboration Hub</small>
    </div>
    <ul class="nav flex-column py-3 flex-grow-1">
        <li class="nav-item">
            <a class="nav-link<c:if test="${activeNav == 'dashboard'}"> active</c:if>" href="${pageContext.request.contextPath}/app/dashboard">
                <i class="bi bi-speedometer2 me-2"></i>Dashboard
            </a>
        </li>
        <li class="nav-item">
            <a class="nav-link<c:if test="${activeNav == 'projects'}"> active</c:if>" href="${pageContext.request.contextPath}/app/projects">
                <i class="bi bi-folder2-open me-2"></i>Projects
            </a>
        </li>
        <li class="nav-item">
            <a class="nav-link<c:if test="${activeNav == 'tasks'}"> active</c:if>" href="${pageContext.request.contextPath}/app/tasks">
                <i class="bi bi-check2-square me-2"></i>
                <c:choose><c:when test="${isStudent}">My Tasks</c:when><c:otherwise>Tasks</c:otherwise></c:choose>
            </a>
        </li>
        <c:if test="${canManage}">
        <li class="nav-item">
            <a class="nav-link<c:if test="${activeNav == 'teams'}"> active</c:if>" href="${pageContext.request.contextPath}/app/teams">
                <i class="bi bi-people me-2"></i>Teams
            </a>
        </li>
        <li class="nav-item">
            <a class="nav-link<c:if test="${activeNav == 'milestones'}"> active</c:if>" href="${pageContext.request.contextPath}/app/milestones">
                <i class="bi bi-flag me-2"></i>Milestones
            </a>
        </li>
        </c:if>
        <li class="nav-item">
            <a class="nav-link<c:if test="${activeNav == 'discussions'}"> active</c:if>" href="${pageContext.request.contextPath}/app/discussions">
                <i class="bi bi-chat-dots me-2"></i>Discussions
            </a>
        </li>
        <c:if test="${isAdmin}">
        <li class="nav-item">
            <a class="nav-link<c:if test="${activeNav == 'admin'}"> active</c:if>" href="${pageContext.request.contextPath}/app/admin">
                <i class="bi bi-shield-lock me-2"></i>Admin
            </a>
        </li>
        </c:if>
    </ul>
    <div class="p-3 border-top border-secondary small text-secondary">
        <c:if test="${isAdmin}">Admin: full control</c:if>
        <c:if test="${isFaculty}">Faculty: manage projects and tasks</c:if>
        <c:if test="${isStudent}">Student: view projects, update assigned tasks</c:if>
    </div>
    <div class="p-3 border-top border-secondary small">
        <a class="nav-link" href="${pageContext.request.contextPath}/http-methods">
            <i class="bi bi-broadcast me-2"></i>HTTP Methods Demo
        </a>
    </div>
</nav>
