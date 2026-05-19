<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="header.jsp"/><jsp:include page="sidebar.jsp"/>
<main class="rs-main"><jsp:include page="topbar.jsp"/>
<div class="rs-content">
<div class="row g-4">
<section class="col-lg-4"><article class="card p-3">
<h6>Create Milestone</h6>
<form method="post" action="${pageContext.request.contextPath}/app/milestones">
<select name="projectId" class="form-select mb-2" required><c:forEach var="p" items="${projects}"><option value="${p.id}">${p.title}</option></c:forEach></select>
<input name="title" class="form-control mb-2" required placeholder="Title">
<textarea name="description" class="form-control mb-2"></textarea>
<input type="date" name="targetDate" class="form-control mb-2">
<input type="number" name="completionPercent" class="form-control mb-2" min="0" max="100" value="0">
<button class="btn btn-primary w-100">Add</button>
</form></article></section>
<section class="col-lg-8">
<c:forEach var="m" items="${milestones}">
<article class="card mb-3 p-3">
<div class="d-flex justify-content-between"><h6>${m.title}</h6><span class="badge bg-primary">${m.status}</span></div>
<p class="small text-muted">${m.description}</p>
<div class="progress rs-progress mb-2"><div class="progress-bar" style="width:${m.completionPercent}%"></div></div>
<form method="post" action="${pageContext.request.contextPath}/app/milestones/progress" class="row g-2">
<input type="hidden" name="_method" value="PUT"><input type="hidden" name="id" value="${m.id}">
<div class="col"><input type="number" name="completionPercent" class="form-control form-control-sm" value="${m.completionPercent}" min="0" max="100"></div>
<div class="col-auto"><button class="btn btn-sm btn-success">Update Progress</button></div>
</form>
<form method="post" action="${pageContext.request.contextPath}/app/milestones/delete" class="mt-2" onsubmit="return confirm('Delete milestone?');">
<input type="hidden" name="id" value="${m.id}">
<button class="btn btn-sm btn-outline-danger">Delete</button>
</form>
</article>
</c:forEach>
</section></div></div>
<jsp:include page="footer.jsp"/>
