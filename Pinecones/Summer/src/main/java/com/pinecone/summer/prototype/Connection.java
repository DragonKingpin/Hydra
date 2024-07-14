package com.pinecone.summer.prototype;

import com.pinecone.summer.http.HttpMethod;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public interface Connection {
    PrintWriter writer() throws IOException;

    ServletOutputStream out() throws IOException;

    HttpServletRequest getRequest();

    HttpServletRequest getMultipartRequest();

    boolean isMultipartRequest();

    HttpServletResponse getResponse();

    HttpServlet getServlet();

    HttpMethod currentHttpMethod() ;

    ConnectDispatcher getDispatcher();

    HostSystem getHostSystem();
}
