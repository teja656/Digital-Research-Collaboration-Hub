<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="header.jsp"/><jsp:include page="sidebar.jsp"/>
<main class="rs-main"><jsp:include page="topbar.jsp"/>
<div class="rs-content">
<div class="row g-4">
<section class="col-lg-4"><article class="card p-3">
<h6>Create Team</h6>
<form method="post" action="${pageContext.request.contextPath}/app/teams" data-validate="true">
<input name="name" class="form-control mb-2" placeholder="Team name" required>
<textarea name="description" class="form-control mb-2"></textarea>
<select name="leaderId" class="form-select mb-2" required>
<c:forEach var="u" items="${users}"><option value="${u.id}">${u.fullName} (${u.role})</option></c:forEach>
</select>
<button class="btn btn-primary w-100">Create Team</button>
</form>
</article></section>
<section class="col-lg-8">
<c:forEach var="team" items="${teams}">
<article class="card mb-3 p-3">
<h5>${team.name}</h5>
<p class="text-muted small">${team.description}</p>
<p class="small">Leader: ${userNames[team.leaderId]}</p>

<form method="post" action="${pageContext.request.contextPath}/app/teams/update" class="row g-2 mb-2">
<input type="hidden" name="_method" value="PUT"><input type="hidden" name="id" value="${team.id}">
<div class="col"><input name="name" class="form-control form-control-sm" value="${team.name}" required></div>
<div class="col"><input name="description" class="form-control form-control-sm" value="${team.description}"></div>
<div class="col-auto"><button class="btn btn-sm btn-outline-secondary">Update</button></div>
</form>
<form method="post" action="${pageContext.request.contextPath}/app/teams/delete" onsubmit="return confirm('Delete team?');">
<input type="hidden" name="id" value="${team.id}"><button class="btn btn-sm btn-outline-danger">Delete Team</button>
</form>
<ul class="list-group mb-2">
<c:forEach var="m" items="${membersMap[team.id]}">
<li class="list-group-item d-flex justify-content-between align-items-center">
${userNames[m.userId]} <span class="badge bg-secondary">${m.memberRole}</span>
<form method="post" action="${pageContext.request.contextPath}/app/teams/remove-member" class="m-0">
<input type="hidden" name="teamId" value="${team.id}"><input type="hidden" name="userId" value="${m.userId}">
<button class="btn btn-sm btn-outline-danger">Remove</button></form>
</li>
</c:forEach>
</ul>
<form method="post" action="${pageContext.request.contextPath}/app/teams/add-member" class="row g-2">
<input type="hidden" name="teamId" value="${team.id}">
<div class="col"><select name="userId" class="form-select form-select-sm"><c:forEach var="u" items="${users}"><option value="${u.id}">${u.fullName}</option></c:forEach></select></div>
<div class="col"><select name="memberRole" class="form-select form-select-sm"><option>MEMBER</option><option>REVIEWER</option></select></div>
<div class="col-auto"><button class="btn btn-sm btn-primary">Add</button></div>
</form>
</article>
</c:forEach>
</section>
</div></div>
<jsp:include page="footer.jsp"/>
