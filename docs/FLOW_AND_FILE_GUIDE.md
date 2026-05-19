# FLOW GUIDE

Login: POST /login -> AuthService -> session -> /app/dashboard
Create Project: POST /app/projects -> ProjectService -> ActivityLog
PUT Task: POST /app/tasks/put-update with _method=PUT
Upload: POST /upload -> disk + uploaded_files table

Key files: web.xml, hibernate.cfg.xml, spring-mvc.xml, AuthenticationFilter.java