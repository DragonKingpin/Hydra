package com.pinecone.summer;

import com.pinecone.summer.prototype.Servletson;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 *  Pinecone System Router Servlet
 *  Like spring framework for map and dispatch all path based router.
 */
public class SystemRoutlet extends HttpServlet implements Servletson {
    private ArchHostSystem mSystem     = null             ;

    public ArchHostSystem getHostSystem(){
        return this.mSystem;
    }


    @Override
    public void init() throws ServletException {
        this.mSystem = ArchHostSystem.G_SystemServlet.getHostSystem();
    }

    @Override
    protected void doGet( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException {
        this.mSystem.handleByDispatcher( RouterType.PathRouter ).handleGet( new Connectiom( request, response, this ) );
    }

    @Override
    protected void doPost( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException {
        this.mSystem.handleByDispatcher( RouterType.PathRouter ).handlePost( new Connectiom( request, response, this ) );
    }

}