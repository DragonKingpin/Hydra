package com.pinecone.summer;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Connectiom {
    protected ArchConnection   connection;
    public HttpServletRequest  request;
    public HttpServletResponse response;
    public HttpServlet         servlet;

    public Connectiom( HttpServletRequest request, HttpServletResponse response, HttpServlet servlet ) {
        this.request  = request;
        this.response = response;
        this.servlet  = servlet;
    }

    protected void afterConnectionRipe( ArchConnection connection ) {
        this.connection = connection;
    }

    public ArchConnection getConnection() {
        return this.connection;
    }
}
