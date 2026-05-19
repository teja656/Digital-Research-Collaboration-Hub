<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="header.jsp"/>
<jsp:include page="sidebar.jsp"/>
<main class="rs-main">
<jsp:include page="topbar.jsp"/>
<div class="rs-content">
<c:if test="${not empty error}"><div class="alert alert-danger">${error}</div></c:if>
<c:if test="${isStudent}"><div class="alert alert-info">Showing tasks assigned to you.</div></c:if>
<div class="row g-4">
<c:if test="${canManage}">
<div class="col-lg-4"><div class="card p-3"><h6>Create Task</h6>
<form method="post" action="${pageContext.request.contextPath}/app/tasks">
<select name="projectId" class="form-select mb-2" required><c:forEach var="p" items="${projects}"><option value="${p.id}">${p.title}</option></c:forEach></select>
<input name="title" class="form-control mb-2" required placeholder="Title">
<textarea name="description" class="form-control mb-2"></textarea>
<select name="assignedTo" class="form-select mb-2"><option value="">Unassigned</option><c:forEach var="u" items="${users}"><option value="${u.id}">${u.fullName}</option></c:forEach></select>
<select name="priority" class="form-select mb-2"><option value="HIGH">HIGH</option><option value="MEDIUM" selected>MEDIUM</option><option value="LOW">LOW</option></select>
<select name="status" class="form-select mb-2"><option value="TODO">TODO</option><option value="IN_PROGRESS">IN_PROGRESS</option><option value="DONE">DONE</option></select>
<input type="date" name="dueDate" class="form-control mb-2"><button class="btn btn-primary w-100">Add Task</button>
</form></div></div>
</c:if>
<div class="col-lg-8"><div class="rs-table-card"><table class="table table-hover mb-0">
<thead class="table-dark"><tr><th>Task</th><th>Project</th><th>Status</th><th>Actions</th></tr></thead>
<tbody><c:forEach var="t" items="${tasks}"><tr>
<td>${t.title}</td><td><c:forEach var="p" items="${projects}"><c:if test="${p.id==t.projectId}">${p.title}</c:if></c:forEach></td>
<td>${t.status}</td><td>
<form method="post" action="${pageContext.request.contextPath}/app/tasks/put-update" class="d-flex gap-1">
<input type="hidden" name="_method" value="PUT"><input type="hidden" name="id" value="${t.id}">
<select name="status" class="form-select form-select-sm">
<option value="TODO" ${t.status=='TODO'?'selected':''}>TODO</option>
<option value="IN_PROGRESS" ${t.status=='IN_PROGRESS'?'selected':''}>IN_PROGRESS</option>
<option value="DONE" ${t.status=='DONE'?'selected':''}>DONE</option></select>
<button class="btn btn-sm btn-primary">Update</button></form>
<c:if test="${canManage}"><form method="post" action="${pageContext.request.contextPath}/app/tasks/delete" class="d-inline"><input type="hidden" name="id" value="${t.id}"><button class="btn btn-sm btn-outline-danger">Delete</button></form></c:if>
</td></tr></c:forEach></tbody></table></div></div></div></div>
<jsp:include page="footer.jsp"/>