# VIVA AND CONCEPTS - ResearchSphere

## Servlet
Java classes handling HTTP. LoginServlet (session+cookie), RegisterServlet, FileUploadServlet, TraceOptionsServlet (OPTIONS/TRACE).

## JSP
Dynamic HTML with JSTL and EL. jsp:include for layout. jsp:useBean on discussions page.

## Hibernate
ORM maps entities to tables. HibernateUtil SessionFactory, DAO layer runs HQL.

## Spring MVC
Controllers under /app/* return view names. ChartController returns JSON.

## Session
loggedInUser in HttpSession. AuthenticationFilter protects /app/*.

## Cookie
rs_remember_email stores email for 7 days when Remember Me checked.

## HTTP Methods
GET pages, POST forms, PUT simulated via _method=PUT, OPTIONS and TRACE on /http-methods.

## Roles
ADMIN, FACULTY, STUDENT - admin panel restricted to ADMIN.

## Layers
JSP -> Servlet/Spring -> Service -> DAO -> MySQL