<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="header.jsp"/>
<jsp:include page="sidebar.jsp"/>
<main class="rs-main">
<jsp:include page="topbar.jsp"/>
<div class="rs-content">
    <div class="row g-3 mb-4">
        <div class="col-md-3">
            <div class="card rs-stat-card p-3">
                <div class="d-flex justify-content-between">
                    <div><p class="text-muted mb-1 small">Projects</p><h3>${projectCount}</h3></div>
                    <div class="rs-stat-icon bg-indigo-soft"><i class="bi bi-folder2-open"></i></div>
                </div>
            </div>
        </div>
        <div class="col-md-3">
            <div class="card rs-stat-card p-3">
                <div class="d-flex justify-content-between">
                    <div><p class="text-muted mb-1 small">Tasks</p><h3>${taskCount}</h3></div>
                    <div class="rs-stat-icon bg-cyan-soft"><i class="bi bi-check2-square"></i></div>
                </div>
            </div>
        </div>
        <div class="col-md-3">
            <div class="card rs-stat-card p-3">
                <div class="d-flex justify-content-between">
                    <div><p class="text-muted mb-1 small">Teams</p><h3>${teamCount}</h3></div>
                    <div class="rs-stat-icon bg-green-soft"><i class="bi bi-people"></i></div>
                </div>
            </div>
        </div>
        <div class="col-md-3">
            <div class="card rs-stat-card p-3">
                <div class="d-flex justify-content-between">
                    <div><p class="text-muted mb-1 small">Unread Alerts</p><h3>${unreadNotifications}</h3></div>
                    <div class="rs-stat-icon bg-amber-soft"><i class="bi bi-bell"></i></div>
                </div>
            </div>
        </div>
    </div>
    <div class="row g-3">
        <div class="col-lg-4">
            <div class="card p-3 mb-3"><h6>Task Distribution</h6><canvas id="taskChart"></canvas></div>
            <div class="card p-3"><h6>Project Status</h6><canvas id="projectChart"></canvas></div>
        </div>
        <div class="col-lg-4">
            <div class="card p-3 h-100">
                <h6><i class="bi bi-activity"></i> Recent Activity</h6>
                <ul class="list-group list-group-flush">
                    <c:forEach var="log" items="${recentActivity}">
                        <li class="list-group-item px-0 small">
                            <strong>${log.action}</strong> – ${log.details}<br>
                            <span class="text-muted">${log.createdAt}</span>
                        </li>
                    </c:forEach>
                </ul>
            </div>
        </div>
        <div class="col-lg-4">
            <div class="card p-3 mb-3">
                <h6><i class="bi bi-bell"></i> Notifications</h6>
                <c:forEach var="n" items="${notifications}">
                    <div class="border-bottom py-2 small">
                        <strong>${n.title}</strong><br>${n.message}
                    </div>
                </c:forEach>
            </div>
            <div class="card p-3">
                <h6>Team Overview</h6>
                <c:forEach var="team" items="${teams}">
                    <div class="d-flex justify-content-between small py-1 border-bottom">
                        <span>${team.name}</span>
                        <span class="badge bg-secondary">#${team.id}</span>
                    </div>
                </c:forEach>
            </div>
        </div>
    </div>
</div>
<jsp:include page="footer.jsp"/>
