<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%-- JSP useBean demo for page helper --%>
<jsp:useBean id="now" class="java.util.Date" scope="page"/>
<jsp:include page="header.jsp"/><jsp:include page="sidebar.jsp"/>
<main class="rs-main"><jsp:include page="topbar.jsp"/>
<div class="rs-content">
<div class="row g-4">
<section class="col-lg-5">
<article class="card p-3 mb-3">
<h6>Start Thread (POST)</h6>
<form method="post" action="${pageContext.request.contextPath}/app/discussions" data-validate="true">
<select name="projectId" class="form-select mb-2" required><c:forEach var="p" items="${projects}"><option value="${p.id}">${p.title}</option></c:forEach></select>
<input name="title" class="form-control mb-2" required>
<textarea name="content" class="form-control mb-2" required></textarea>
<button class="btn btn-primary">Post Discussion</button>
</form></article>
<ul class="list-group">
<c:forEach var="d" items="${discussions}">
<li class="list-group-item">
<a href="${pageContext.request.contextPath}/app/discussions?view=${d.id}"><strong>${d.title}</strong></a>
<p class="small mb-0 text-muted">${d.content}</p>
<form method="post" action="${pageContext.request.contextPath}/app/discussions/delete" class="mt-1" onsubmit="return confirm('Delete thread?');">
<input type="hidden" name="id" value="${d.id}"><button class="btn btn-sm btn-outline-danger">Delete</button></form>
</li>
</c:forEach>
</ul>
</section>
<section class="col-lg-7">
<c:if test="${not empty selectedDiscussion}">
<article class="card p-3">
<h5>${selectedDiscussion.title}</h5>
<form method="post" action="${pageContext.request.contextPath}/app/discussions/delete" class="float-end" onsubmit="return confirm('Delete thread?');">
<input type="hidden" name="id" value="${selectedDiscussion.id}"><button class="btn btn-sm btn-outline-danger">Delete Thread</button></form>
<p>${selectedDiscussion.content}</p>
<hr>
<h6>Comments & Replies</h6>
<c:forEach var="c" items="${comments}">
<div class="border rounded p-2 mb-2 small">
${c.content}<br><span class="text-muted">User #${c.createdBy} at ${c.createdAt}</span>
</div>
</c:forEach>
<form method="post" action="${pageContext.request.contextPath}/app/discussions/comment" class="mt-3">
<input type="hidden" name="discussionId" value="${selectedDiscussion.id}">
<textarea name="content" class="form-control mb-2" required placeholder="Add comment"></textarea>
<button class="btn btn-outline-primary btn-sm">Reply</button>
</form>
</article>
</c:if>
<p class="small text-muted mt-2">Server time (useBean): <%= now %></p>
</section>
</div></div>
<jsp:include page="footer.jsp"/>
