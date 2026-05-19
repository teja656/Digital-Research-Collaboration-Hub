<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html><head><title>HTTP Methods | ResearchSphere</title>
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
</head><body class="p-4">
<div class="container">
<h2>TRACE & OPTIONS Demo</h2>
<p>${info}</p>
<p>Use browser dev tools or curl:</p>
<pre>curl -X OPTIONS <%= request.getRequestURL() %>
curl -X TRACE <%= request.getRequestURL() %></pre>
<a href="<%= request.getContextPath() %>/login" class="btn btn-primary">Back to Login</a>
</div></body></html>
