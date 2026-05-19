<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="header.jsp"/><jsp:include page="sidebar.jsp"/>
<main class="rs-main"><jsp:include page="topbar.jsp"/>
<div class="rs-content">
<div class="row g-4">
<section class="col-lg-6">
<div class="rs-table-card"><table class="table mb-0">
<thead class="table-dark"><tr><th>Users</th><th>Email</th><th>Role</th></tr></thead>
<tbody><c:forEach var="u" items="${users}">
<tr><td>${u.fullName}</td><td>${u.email}</td><td><span class="badge bg-info">${u.role}</span></td></tr>
</c:forEach></tbody></table></div>
</section>
<section class="col-lg-6">
<div class="card p-3"><h6>Activity Logs (last 50)</h6>
<ul class="list-group list-group-flush">
<c:forEach var="a" items="${activities}">
<li class="list-group-item small"><strong>${a.action}</strong> – ${a.details}<br><span class="text-muted">${a.createdAt}</span></li>
</c:forEach>
</ul></div>
</section>
</div></div>
<jsp:include page="footer.jsp"/>
