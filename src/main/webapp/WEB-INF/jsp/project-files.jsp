<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="header.jsp"/>
<jsp:include page="sidebar.jsp"/>
<main class="rs-main">
<jsp:include page="topbar.jsp"/>
<div class="rs-content">
    <div class="d-flex justify-content-between align-items-center mb-3">
        <h5 class="mb-0">Files: ${projectTitle}</h5>
        <a href="${pageContext.request.contextPath}/app/projects" class="btn btn-outline-secondary btn-sm">Back to Projects</a>
    </div>
    <div class="rs-table-card">
        <table class="table table-hover mb-0">
            <thead class="table-dark">
            <tr><th>File</th><th>Type</th><th>Size</th><th>Actions</th></tr>
            </thead>
            <tbody>
            <c:forEach var="f" items="${files}">
                <tr>
                    <td>${f.fileName}</td>
                    <td>${f.fileType}</td>
                    <td>${f.fileSize} bytes</td>
                    <td>
                        <a class="btn btn-sm btn-primary"
                           href="${pageContext.request.contextPath}/download?id=${f.id}">Download</a>
                        <form class="d-inline" method="post"
                              action="${pageContext.request.contextPath}/app/projects/files/delete"
                              onsubmit="return confirm('Remove file record?');">
                            <input type="hidden" name="id" value="${f.id}">
                            <input type="hidden" name="projectId" value="${projectId}">
                            <button class="btn btn-sm btn-outline-danger">Delete</button>
                        </form>
                    </td>
                </tr>
            </c:forEach>
            <c:if test="${empty files}">
                <tr><td colspan="4" class="text-muted text-center py-4">No files uploaded for this project yet.</td></tr>
            </c:if>
            </tbody>
        </table>
    </div>
</div>
<jsp:include page="footer.jsp"/>
