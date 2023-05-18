package Pinecone.Framework.Util.Summer;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

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
