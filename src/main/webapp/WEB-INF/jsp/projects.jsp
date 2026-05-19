<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="header.jsp"/>
<jsp:include page="sidebar.jsp"/>
<main class="rs-main">
<jsp:include page="topbar.jsp"/>
<div class="rs-content">
    <c:if test="${isStudent}"><div class="alert alert-info">Students can view projects. Faculty/Admin create and upload.</div></c:if>
    <c:if test="${not empty success}"><div class="alert alert-success">${success}</div></c:if>
    <c:if test="${not empty error}"><div class="alert alert-danger">${error}</div></c:if>
    <div class="row g-4">
        <c:if test="${canManage}">
        <div class="col-lg-4">
            <div class="card p-3">
                <h6>Create Project</h6>
                <form action="${pageContext.request.contextPath}/app/projects" method="post">
                    <input type="text" name="title" class="form-control mb-2" placeholder="Title" required>
                    <textarea name="description" class="form-control mb-2"></textarea>
                    <input type="text" name="category" class="form-control mb-2" placeholder="Category">
                    <select name="status" class="form-select mb-2"><option value="PLANNING">Planning</option><option value="IN_PROGRESS">In Progress</option><option value="COMPLETED">Completed</option></select>
                    <select name="teamId" class="form-select mb-2"><option value="">-- Team --</option><c:forEach var="tm" items="${teams}"><option value="${tm.id}">${tm.name}</option></c:forEach></select>
                    <input type="date" name="startDate" class="form-control mb-2"><input type="date" name="endDate" class="form-control mb-2">
                    <button class="btn btn-primary w-100">Create</button></form>
                <hr><h6>Upload File</h6>
                <form action="${pageContext.request.contextPath}/upload" method="post" enctype="multipart/form-data">
                    <select name="projectId" class="form-select mb-2" required><c:forEach var="pr" items="${projects}"><option value="${pr.id}">${pr.title}</option></c:forEach></select>
                    <input type="file" name="file" class="form-control mb-2" required><button class="btn btn-outline-primary w-100">Upload</button></form>
            </div></div></div>
        </c:if>
        <c:choose><c:when test="${canManage}"><c:set var="listCol" value="col-lg-8"/></c:when><c:otherwise><c:set var="listCol" value="col-lg-12"/></c:otherwise></c:choose>
        <div class="${listCol}">
            <div class="rs-table-card"><table class="table table-hover mb-0">
                <thead class="table-dark"><tr><th>Title</th><th>Team</th><th>Category</th><th>Status</th><th>Actions</th></tr></thead><tbody>
                <c:forEach var="p" items="${projects}"><tr>
                    <td><strong>${p.title}</strong><br><small>${p.description}</small></td>
                    <td><c:forEach var="tm" items="${teams}"><c:if test="${p.teamId == tm.id}">${tm.name}</c:if></c:forEach></td>
                    <td>${p.category}</td><td><span class="badge bg-info">${p.status}</span></td>
                    <td class="text-nowrap">
                        <a class="btn btn-sm btn-outline-info" href="${pageContext.request.contextPath}/app/projects/files?projectId=${p.id}">Files</a>
                        <c:if test="${canManage}">
                        <button type="button" class="btn btn-sm btn-outline-secondary" data-bs-toggle="modal" data-bs-target="#edit${p.id}">Edit</button>
                        <form class="d-inline" method="post" action="${pageContext.request.contextPath}/app/projects/delete" onsubmit="return confirm('Delete?');">
                            <input type="hidden" name="id" value="${p.id}"><button class="btn btn-sm btn-outline-danger">Delete</button></form>
                        </c:if></td></tr>
                    <c:if test="${canManage}"><div class="modal fade" id="edit${p.id}" tabindex="-1"><div class="modal-dialog">
                    <form method="post" action="${pageContext.request.contextPath}/app/projects/update" class="modal-content">
                    <input type="hidden" name="_method" value="PUT"><input type="hidden" name="id" value="${p.id}">
                    <div class="modal-header"><h5>Edit Project</h5><button type="button" class="btn-close" data-bs-dismiss="modal"></button></div>
                    <div class="modal-body">
                    <input name="title" class="form-control mb-2" value="${p.title}" required>
                    <textarea name="description" class="form-control mb-2">${p.description}</textarea>
                    <input name="category" class="form-control mb-2" value="${p.category}">
                    <select name="status" class="form-select mb-2">
                    <option value="PLANNING" <c:if test="${p.status == 'PLANNING'}">selected</c:if>>PLANNING</option>
                    <option value="IN_PROGRESS" <c:if test="${p.status == 'IN_PROGRESS'}">selected</c:if>>IN_PROGRESS</option>
                    <option value="COMPLETED" <c:if test="${p.status == 'COMPLETED'}">selected</c:if>>COMPLETED</option></select>
                    <select name="teamId" class="form-select mb-2"><option value="">-- Team --</option>
                    <c:forEach var="tm" items="${teams}"><option value="${tm.id}" <c:if test="${p.teamId == tm.id}">selected</c:if>>${tm.name}</option></c:forEach></select>
                    <input type="date" name="startDate" class="form-control mb-2" value="${p.startDate}">
                    <input type="date" name="endDate" class="form-control mb-2" value="${p.endDate}"></div>
                    <div class="modal-footer"><button class="btn btn-primary">Save</button></div></form></div></div></c:if>
                </c:forEach></tbody></table></div></div></div></div>
<jsp:include page="footer.jsp"/>
