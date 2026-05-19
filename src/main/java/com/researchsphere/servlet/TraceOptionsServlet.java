package com.researchsphere.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collections;

/**
 * Demonstrates TRACE and OPTIONS HTTP methods for academic requirements.
 */
public class TraceOptionsServlet extends HttpServlet {

    @Override
    protected void doOptions(HttpServletRequest req, HttpServletResponse resp) {
        resp.setHeader("Allow", "GET, POST, OPTIONS, TRACE");
        resp.setHeader("Access-Control-Allow-Methods", "GET, POST, OPTIONS, TRACE");
        resp.setStatus(HttpServletResponse.SC_OK);
    }

    @Override
    protected void doTrace(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("message/http");
        resp.setStatus(HttpServletResponse.SC_OK);
        PrintWriter out = resp.getWriter();
        out.println(req.getMethod() + " " + req.getRequestURI() + " HTTP/1.1");
        Collections.list(req.getHeaderNames()).forEach(name ->
                Collections.list(req.getHeaders(name)).forEach(value ->
                        out.println(name + ": " + value)));
        out.flush();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("info", "Use OPTIONS or TRACE on /http-methods for method demo.");
        req.getRequestDispatcher("/jsp/http-methods.jsp").forward(req, resp);
    }
}
